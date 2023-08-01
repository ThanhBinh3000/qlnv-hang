package com.tcdt.qlnvhang.service.xuathang.thanhlytieuhuy.thanhly;

import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.UserInfoRepository;
import com.tcdt.qlnvhang.repository.xuathang.thanhlytieuhuy.thanhly.XhTlBangKeRepository;
import com.tcdt.qlnvhang.repository.xuathang.thanhlytieuhuy.thanhly.XhTlXuatKhoRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.xuathang.thanhlytieuhuy.thanhly.XhTlBangKeHdrReq;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.thanhly.XhTlBangKeHdr;
import com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.thanhly.XhTlXuatKhoHdr;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.DataUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class XhTlBangKeService extends BaseServiceImpl {

    @Autowired
    private XhTlBangKeRepository xhTlBangKeRepository;
    @Autowired
    private XhTlXuatKhoRepository xhTlXuatKhoRepository;
    @Autowired
    private UserInfoRepository userInfoRepository;

    public Page<XhTlBangKeHdr> searchPage(CustomUserDetails currentUser, XhTlBangKeHdrReq req) throws Exception {
        String dvql = currentUser.getDvql();
        if (currentUser.getUser().getCapDvi().equals(Contains.CAP_CHI_CUC)) {
            req.setDvql(dvql.substring(0, 6));
        } else if (currentUser.getUser().getCapDvi().equals(Contains.CAP_CUC)) {
            req.setDvql(dvql);
            req.setTrangThai(Contains.DADUYET_LDCC);
        }
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        Page<XhTlBangKeHdr> search = xhTlBangKeRepository.search(req, pageable);
        Map<String, String> mapDmucVthh = getListDanhMucHangHoa();
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        search.getContent().forEach(data -> {
            data.setMapDmucDvi(mapDmucDvi);
            data.setMapVthh(mapDmucVthh);
            data.setTrangThai(data.getTrangThai());
        });
        return search;
    }

    @Transactional
    public XhTlBangKeHdr create(CustomUserDetails currentUser, XhTlBangKeHdrReq req) throws Exception {
        if (currentUser == null) throw new Exception("Bad requesr.");
        XhTlBangKeHdr data = new XhTlBangKeHdr();
        BeanUtils.copyProperties(req, data);
        data.setMaDvi(currentUser.getUser().getDepartment());
        data.setTrangThai(Contains.DU_THAO);
        XhTlBangKeHdr created = xhTlBangKeRepository.save(data);
        return created;
    }

    @Transactional
    public XhTlBangKeHdr update(CustomUserDetails currentUser, XhTlBangKeHdrReq req) throws Exception {
        if (currentUser == null) throw new Exception("Bad requesr.");
        Optional<XhTlBangKeHdr> optional = xhTlBangKeRepository.findById(req.getId());
        if (!optional.isPresent()) throw new Exception("Không tìm thấy dữ liệu cần sửa");
        XhTlBangKeHdr data = optional.get();
        BeanUtils.copyProperties(req, data, "id", "maDvi");
        XhTlBangKeHdr updated = xhTlBangKeRepository.save(data);
        return updated;
    }

    public List<XhTlBangKeHdr> detail(List<Long> ids) throws Exception {
        if (DataUtils.isNullOrEmpty(ids)) throw new Exception("Tham số không hợp lệ.");
        List<XhTlBangKeHdr> list = xhTlBangKeRepository.findByIdIn(ids);
        if (DataUtils.isNullOrEmpty(list)) throw new Exception("Không tìm thấy dữ liệu");
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        Map<String, String> mapVthh = getListDanhMucHangHoa();
        List<XhTlBangKeHdr> allById = xhTlBangKeRepository.findAllById(ids);
        allById.forEach(data -> {
            data.setMapDmucDvi(mapDmucDvi);
            data.setMapVthh(mapVthh);
            data.setTrangThai(data.getTrangThai());
            data.setTenNguoiPduyet(ObjectUtils.isEmpty(data.getNguoiPduyetId()) ? null : userInfoRepository.findById(data.getNguoiPduyetId()).get().getFullName());
        });
        return allById;
    }

    @Transactional
    public void delete(IdSearchReq idSearchReq) throws Exception {
        Optional<XhTlBangKeHdr> optional = xhTlBangKeRepository.findById(idSearchReq.getId());
        if (!optional.isPresent()) throw new Exception("Banr ghi không tồn tại");
        xhTlBangKeRepository.delete(optional.get());
    }

    @Transactional
    public void deleteMulti(IdSearchReq idSearchReq) throws Exception {
        List<XhTlBangKeHdr> list = xhTlBangKeRepository.findAllByIdIn(idSearchReq.getIdList());
        if (list.isEmpty()) throw new Exception("Bản ghi không tồn tại");
        xhTlBangKeRepository.deleteAll(list);
    }

    public XhTlBangKeHdr approve(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {
        if (StringUtils.isEmpty(statusReq.getId())) throw new Exception("Không tìm thấy dữ liệu");
        Optional<XhTlBangKeHdr> optional = xhTlBangKeRepository.findById(Long.valueOf(statusReq.getId()));
        if (!optional.isPresent()) throw new Exception("Không tìm thấy dữ liệu");
        XhTlBangKeHdr data = optional.get();
        String status = statusReq.getTrangThai() + data.getTrangThai();
        switch (status) {
            case Contains.CHODUYET_LDCC + Contains.DUTHAO:
            case Contains.CHODUYET_LDCC + Contains.TUCHOI_LDCC:
                data.setNguoiGduyetId(currentUser.getUser().getId());
                data.setNgayGduyet(LocalDate.now());
                break;
            case Contains.TUCHOI_LDCC + Contains.CHODUYET_LDCC:
                data.setNguoiPduyetId(currentUser.getUser().getId());
                data.setNgayPduyet(LocalDate.now());
                data.setLyDoTuChoi(statusReq.getLyDoTuChoi());
            case Contains.DADUYET_LDCC + Contains.CHODUYET_LDCC:
                data.setNguoiPduyetId(currentUser.getUser().getId());
                data.setNgayPduyet(LocalDate.now());
                break;
            default:
                throw new Exception("Phê duyệt không thành công");
        }
        data.setTrangThai(statusReq.getTrangThai());
        if (statusReq.getTrangThai().equals(Contains.DADUYET_LDCC)) {
            Optional<XhTlXuatKhoHdr> optional1 = xhTlXuatKhoRepository.findById(data.getIdPhieuXuatKho());
            if (optional1.isPresent()) {
                optional1.get().setIdBangCanKeHang(data.getId());
                optional1.get().setSoBangCanKeHang(data.getSoBangKe());
                xhTlXuatKhoRepository.save(optional1.get());
            }
        }
        XhTlBangKeHdr created = xhTlBangKeRepository.save(data);
        return created;
    }
}