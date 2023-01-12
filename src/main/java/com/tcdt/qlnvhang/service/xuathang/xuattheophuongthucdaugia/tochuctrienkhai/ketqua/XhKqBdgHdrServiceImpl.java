package com.tcdt.qlnvhang.service.xuathang.xuattheophuongthucdaugia.tochuctrienkhai.ketqua;

import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.repository.xuathang.xuattheophuongthucdaugia.tochuctrienkhai.ketqua.XhKqBdgHdrRepository;
import com.tcdt.qlnvhang.request.xuathang.xuattheophuongthucdaugia.tochuctrienkhai.ketqua.XhKqBdgHdrReq;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.table.xuathang.xuattheophuongthucdaugia.tochuctrienkhai.ketqua.XhKqBdgHdr;
import com.tcdt.qlnvhang.util.Contains;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class XhKqBdgHdrServiceImpl extends BaseServiceImpl implements XhKqBdgHdrService {

    @Autowired
    private XhKqBdgHdrRepository xhKqBdgHdrRepository;

    @Override
    public Page<XhKqBdgHdr> searchPage(XhKqBdgHdrReq req) throws Exception {
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(),req.getPaggingReq().getLimit(), Sort.by("id").descending());
        Page<XhKqBdgHdr> page = xhKqBdgHdrRepository.search(req,pageable);
        Map<String, String> listDanhMucHangHoa = getListDanhMucHangHoa();
        page.getContent().forEach(f -> {
            f.setTenLoaiVthh(listDanhMucHangHoa.get(f.getLoaiVthh()));
        });
        return page;
    }

    @Override
    public List<XhKqBdgHdr> searchAll(XhKqBdgHdrReq req) {
        return null;
    }

    @Override
    public XhKqBdgHdr create(XhKqBdgHdrReq req) throws Exception {
        XhKqBdgHdr data = new XhKqBdgHdr();
        BeanUtils.copyProperties(req, data, "id");
        data.setNam(LocalDate.now().getYear());
        data.setNguoiTaoId(getUser().getId());
        data.setNgayTao(new Date());
        data.setMaDvi(getUser().getDvql());

        XhKqBdgHdr byMaThongBao = xhKqBdgHdrRepository.findByMaThongBao(req.getMaThongBao());
        if(!ObjectUtils.isEmpty(byMaThongBao)){
            throw new Exception("Mã thông báo này đã được quyết định kết quả bán đấu giá, xin vui lòng chọn mã thông báo khác");
        }
        xhKqBdgHdrRepository.save(data);
        return data;
    }

    @Override
    public XhKqBdgHdr update(XhKqBdgHdrReq req) throws Exception {
        if(ObjectUtils.isEmpty(req.getId())){
            throw new Exception("Không tìm thấy dữ liệu");
        }
        Optional<XhKqBdgHdr> byId = xhKqBdgHdrRepository.findById(req.getId());
        if (!byId.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        XhKqBdgHdr data = byId.get();
        BeanUtils.copyProperties(req, data, "id");
        data.setNgaySua(new Date());
        data.setNguoiSuaId(getUser().getId());
        xhKqBdgHdrRepository.save(data);
        return data;
    }

    @Override
    public XhKqBdgHdr detail(Long id) throws Exception {
        if (ObjectUtils.isEmpty(id)) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        Optional<XhKqBdgHdr> byId = xhKqBdgHdrRepository.findById(id);
        if (!byId.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        XhKqBdgHdr data = byId.get();
        return data;
    }

    @Override
    public XhKqBdgHdr approve(XhKqBdgHdrReq req) throws Exception {
        UserInfo userInfo= SecurityContextService.getUser();
        if (userInfo == null){
            throw new Exception("Bad request.");
        }

        if (StringUtils.isEmpty(req.getId())){
            throw new Exception("Không tìm thấy dữ liệu");
        }

        Optional<XhKqBdgHdr> optional = xhKqBdgHdrRepository.findById(req.getId());
        if(!optional.isPresent()){
            throw new Exception("Không tìm thấy dữ liệu");
        }

        XhKqBdgHdr data = optional.get();
        String status = req.getTrangThai() + data.getTrangThai();
        switch (status) {
            case Contains.CHODUYET_TP + Contains.DUTHAO:
            case Contains.CHODUYET_TP + Contains.TUCHOI_TP:
            case Contains.CHODUYET_TP + Contains.TUCHOI_LDC:
                data.setNguoiGuiDuyetId(userInfo.getId());
                data.setNgayGuiDuyet(getDateTimeNow());
            case Contains.TUCHOI_TP + Contains.CHODUYET_TP:
            case Contains.TUCHOI_LDC + Contains.CHODUYET_LDC:
                data.setNguoiPduyetId(userInfo.getId());
                data.setNgayPduyet(getDateTimeNow());
                data.setLyDoTuChoi(req.getLyDoTuChoi());
                break;
            case Contains.CHODUYET_LDC + Contains.CHODUYET_TP:
            case Contains.DADUYET_LDC + Contains.CHODUYET_LDC:
            case Contains.BAN_HANH + Contains.DADUYET_LDC:
                data.setNguoiPduyetId(userInfo.getId());
                data.setNgayPduyet(getDateTimeNow());
                break;
            default:
                throw new Exception("Phê duyệt không thành công");
        }
        data.setTrangThai(req.getTrangThai());
        return xhKqBdgHdrRepository.save(data);
    }

    @Override
    public void delete(Long id) throws Exception {
        if (ObjectUtils.isEmpty(id)) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        Optional<XhKqBdgHdr> byId = xhKqBdgHdrRepository.findById(id);
        if (!byId.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        if(!byId.get().getTrangThai().equals(NhapXuatHangTrangThaiEnum.DUTHAO.getId())){
            throw new Exception("Chỉ được xóa bản ghi với trang thái là dự thảo");
        }
        xhKqBdgHdrRepository.delete(byId.get());
    }

    @Override
    public void deleteMulti(List<Long> listMulti) throws Exception {
        if (ObjectUtils.isEmpty(listMulti)) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        listMulti.forEach(item -> {
            try {
                this.delete(item);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public void export(XhKqBdgHdrReq req, HttpServletResponse response) throws Exception {

    }

}

