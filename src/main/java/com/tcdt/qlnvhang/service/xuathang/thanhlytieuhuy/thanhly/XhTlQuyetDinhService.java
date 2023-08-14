package com.tcdt.qlnvhang.service.xuathang.thanhlytieuhuy.thanhly;

import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.xuathang.thanhlytieuhuy.thanhly.XhTlHoSoRepository;
import com.tcdt.qlnvhang.repository.xuathang.thanhlytieuhuy.thanhly.XhTlQuyetDinhDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.thanhlytieuhuy.thanhly.XhTlQuyetDinhRepository;
import com.tcdt.qlnvhang.repository.xuathang.thanhlytieuhuy.thanhly.XhTlToChucRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.xuathang.thanhlytieuhuy.thanhly.SearchXhTlQuyetDinh;
import com.tcdt.qlnvhang.request.xuathang.thanhlytieuhuy.thanhly.XhTlQuyetDinhHdrReq;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.thanhly.XhTlHoSoHdr;
import com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.thanhly.XhTlQuyetDinhDtl;
import com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.thanhly.XhTlQuyetDinhHdr;
import com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.thanhly.XhTlToChucHdr;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.DataUtils;
import com.tcdt.qlnvhang.util.ExportExcel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.Transient;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class XhTlQuyetDinhService extends BaseServiceImpl {

    @Autowired
    private XhTlQuyetDinhRepository xhTlQuyetDinhRepository;
    @Autowired
    private XhTlQuyetDinhDtlRepository xhTlQuyetDinhDtlRepository;
    @Autowired
    private XhTlHoSoRepository xhTlHoSoRepository;
    @Autowired
    private XhTlToChucRepository xhTlToChucRepository;

    public Page<XhTlQuyetDinhHdr> searchPage(CustomUserDetails currentUser, SearchXhTlQuyetDinh req) throws Exception {
        String dvql = currentUser.getDvql();
        if (currentUser.getUser().getCapDvi().equals(Contains.CAP_CUC)) {
            req.setDvql(dvql.substring(0, 4));
        } else if (currentUser.getUser().getCapDvi().equals(Contains.CAP_TONG_CUC)) {
            req.setDvql(dvql);
        }
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        Page<XhTlQuyetDinhHdr> search = xhTlQuyetDinhRepository.search(req, pageable);
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        Map<String, String> mapVthh = getListDanhMucHangHoa();
        search.getContent().forEach(s -> {
            s.getQuyetDinhDtl().forEach(f -> {
                f.setMapDmucDvi(mapDmucDvi);
                f.setMapVthh(mapVthh);
                f.setTrangThaiThucHien(f.getTrangThaiThucHien());
            });
            s.setMapDmucDvi(mapDmucDvi);
            s.setTrangThai(s.getTrangThai());
        });
        return search;
    }

    @Transactional
    public XhTlQuyetDinhHdr create(CustomUserDetails currentUser, XhTlQuyetDinhHdrReq objReq) throws Exception {
        if (currentUser == null) throw new Exception("Bad request.");
        if (!DataUtils.isNullObject(objReq.getSoQd())) {
            Optional<XhTlQuyetDinhHdr> optional = xhTlQuyetDinhRepository.findBySoQd(objReq.getSoQd());
            if (optional.isPresent()) throw new Exception("số quyết định đã tồn tại");
        }
        XhTlQuyetDinhHdr data = new XhTlQuyetDinhHdr();
        BeanUtils.copyProperties(objReq, data);
        data.setMaDvi(currentUser.getUser().getDepartment());
        data.setTrangThai(Contains.DUTHAO);
        data.getQuyetDinhDtl().forEach(f -> {
            f.setTrangThaiThucHien(Contains.CHUACAPNHAT);
            f.setQuyetDinhHdr(data);
        });
        XhTlQuyetDinhHdr created = xhTlQuyetDinhRepository.save(data);
        return created;
    }

    @Transactional
    public XhTlQuyetDinhHdr update(CustomUserDetails currentUser, XhTlQuyetDinhHdrReq req) throws Exception {
        if (currentUser == null) throw new Exception("Bad request.");
        Optional<XhTlQuyetDinhHdr> optional = xhTlQuyetDinhRepository.findById(req.getId());
        if (!optional.isPresent()) throw new Exception("Không tìm thấy dữ liệu cần sửa");
        Optional<XhTlQuyetDinhHdr> soQd = xhTlQuyetDinhRepository.findBySoQd(req.getSoQd());
        if (soQd.isPresent()) {
            if (!soQd.get().getId().equals(req.getId())) throw new Exception("số quyết định đã tồn tại");
        }
        XhTlQuyetDinhHdr data = optional.get();
        data.getQuyetDinhDtl().forEach(f ->{
            f.setQuyetDinhHdr(null);
        });
        BeanUtils.copyProperties(req, data, "id", "maDvi");
        data.getQuyetDinhDtl().forEach(f ->{
            f.setQuyetDinhHdr(data);
        });
        XhTlQuyetDinhHdr updated = xhTlQuyetDinhRepository.save(data);
        updated.setQuyetDinhDtl(null);
        return updated;
    }

    public List<XhTlQuyetDinhHdr> detail(List<Long> ids) throws Exception {
        if (DataUtils.isNullOrEmpty(ids)) throw new Exception("Tham số không hợp lệ.");
        List<XhTlQuyetDinhHdr> optional = xhTlQuyetDinhRepository.findByIdIn(ids);
        if (DataUtils.isNullOrEmpty(optional)) throw new Exception("Không tìm thấy dữ liệu");
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        Map<String, String> mapVthh = getListDanhMucHangHoa();
        List<XhTlQuyetDinhHdr> allById = xhTlQuyetDinhRepository.findAllById(ids);
        allById.forEach(data -> {
            data.setMapDmucDvi(mapDmucDvi);
            data.setTrangThai(data.getTrangThai());
            data.getQuyetDinhDtl().forEach(s -> {
                s.setMapDmucDvi(mapDmucDvi);
                s.setMapVthh(mapVthh);
                s.setTrangThaiThucHien(s.getTrangThaiThucHien());
                List<XhTlToChucHdr> xhTlToChucHdr = xhTlToChucRepository.findByIdQdTlDtlOrderByLanDauGia(s.getId());
                xhTlToChucHdr.forEach(f -> {
                    f.setTrangThai(f.getTrangThai());
                });
                s.setXhTlToChucHdr(xhTlToChucHdr);
            });
        });
        return allById;
    }

    @Transactional
    public void delete(IdSearchReq idSearchReq) throws Exception {
        Optional<XhTlQuyetDinhHdr> optional = xhTlQuyetDinhRepository.findById(idSearchReq.getId());
        if (!optional.isPresent()) throw new Exception("Bản ghi không tồn tại");
        XhTlQuyetDinhHdr data = optional.get();
        if (!DataUtils.isNullObject(data.getIdHoSo())) {
            Optional<XhTlHoSoHdr> hoSo = xhTlHoSoRepository.findById(data.getIdHoSo());
            if (hoSo.isPresent()) {
                hoSo.get().setIdQd(null);
                hoSo.get().setSoQd(null);
                xhTlHoSoRepository.save(hoSo.get());
            }
        }
        xhTlQuyetDinhRepository.delete(data);
    }

    @Transient
    public void deleteMulti(IdSearchReq idSearchReq) throws Exception {
        List<XhTlQuyetDinhHdr> list = xhTlQuyetDinhRepository.findAllByIdIn(idSearchReq.getIdList());
        if (list.isEmpty()) throw new Exception("Bản ghi không tồn tại");
        List<Long> listHoSo = list.stream().map(XhTlQuyetDinhHdr::getIdHoSo).collect(Collectors.toList());
        List<XhTlHoSoHdr> listObjQdPd = xhTlHoSoRepository.findByIdIn(listHoSo);
        listObjQdPd.forEach(s -> {
            s.setIdQd(null);
            s.setSoQd(null);
        });
        xhTlHoSoRepository.saveAll(listObjQdPd);
        xhTlQuyetDinhRepository.deleteAll(list);
    }

    public XhTlQuyetDinhHdr approve(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {
        if (StringUtils.isEmpty(statusReq.getId())) throw new Exception("Không tìm thấy dữ liệu");
        Optional<XhTlQuyetDinhHdr> optional = xhTlQuyetDinhRepository.findById(Long.valueOf(statusReq.getId()));
        if (!optional.isPresent()) throw new Exception("Không tìm thấy dữ liệu");
        XhTlQuyetDinhHdr data = optional.get();
        String status = statusReq.getTrangThai() + data.getTrangThai();
        switch (status) {
            case Contains.CHODUYET_LDV + Contains.DUTHAO:
            case Contains.CHODUYET_LDTC + Contains.CHODUYET_LDV:
            case Contains.CHODUYET_LDV + Contains.TUCHOI_LDV:
            case Contains.CHODUYET_LDV + Contains.TUCHOI_LDTC:
                data.setNguoiGduyetId(currentUser.getUser().getId());
                data.setNgayGduyet(LocalDate.now());
                break;
            case Contains.TUCHOI_LDV + Contains.CHODUYET_LDV:
            case Contains.TUCHOI_LDTC + Contains.CHODUYET_LDTC:
                data.setNguoiPduyetId(currentUser.getUser().getId());
                data.setNgayPduyet(LocalDate.now());
                data.setLyDoTuChoi(statusReq.getLyDoTuChoi());
                break;
            case Contains.BAN_HANH + Contains.CHODUYET_LDTC:
                data.setNguoiPduyetId(currentUser.getUser().getId());
                data.setNgayPduyet(LocalDate.now());
                break;
            default:
                throw new Exception("Phê duyệt không thành công");
        }
        data.setTrangThai(statusReq.getTrangThai());
        if (statusReq.getTrangThai().equals(Contains.BAN_HANH)) {
            if (!DataUtils.isNullObject(data.getIdHoSo())) {
                Optional<XhTlHoSoHdr> hoSo = xhTlHoSoRepository.findById(data.getIdHoSo());
                if (hoSo.isPresent()) {
                    hoSo.get().setIdQd(data.getId());
                    hoSo.get().setSoQd(data.getSoQd());
                    xhTlHoSoRepository.save(hoSo.get());
                }
            }
        }
        XhTlQuyetDinhHdr created = xhTlQuyetDinhRepository.save(optional.get());
        return created;
    }

    public void export(CustomUserDetails currentUser, SearchXhTlQuyetDinh objReq, HttpServletResponse response) throws Exception {
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        objReq.setPaggingReq(paggingReq);
        Page<XhTlQuyetDinhHdr> page = this.searchPage(currentUser, objReq);
        List<XhTlQuyetDinhHdr> data = page.getContent();
        String title = "Danh sách quyết định thanh lý hàng DTQG ";
        String[] rowsName = new String[]{"STT", "Số quyết định", "Trích yếu", "Ngày ký",
                "Hồ sơ đề nghị thanh lý", "Trạng thái"};
        String fileName = "danh-sach-quyet-dinh-thanh-ly-hang-dtqg.xlsx";
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;
        for (int i = 0; i < data.size(); i++) {
            XhTlQuyetDinhHdr qd = data.get(i);
            objs = new Object[rowsName.length];
            objs[0] = i;
            objs[1] = qd.getSoQd();
            objs[2] = qd.getTrichYeu();
            objs[3] = qd.getNgayKy();
            objs[4] = qd.getSoHoSo();
            objs[5] = qd.getTenTrangThai();
            dataList.add(objs);
        }
        ExportExcel ex = new ExportExcel(title, fileName, rowsName, dataList, response);
        ex.export();
    }

    public XhTlQuyetDinhDtl approveDtl(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {
        if (StringUtils.isEmpty(statusReq.getId())) throw new Exception("Không tìm thấy dữ liệu");
        Optional<XhTlQuyetDinhDtl> optional = xhTlQuyetDinhDtlRepository.findById(Long.valueOf(statusReq.getId()));
        if (!optional.isPresent()) throw new Exception("Không tìm thấy dữ liệu");
        String status = statusReq.getTrangThai() + optional.get().getTrangThaiThucHien();
        switch (status) {
            case Contains.HOANTHANHCAPNHAT + Contains.DANGCAPNHAT:
                break;
            default:
                throw new Exception("Phê duyệt không thành công");
        }
        optional.get().setTrangThaiThucHien(statusReq.getTrangThai());
        xhTlQuyetDinhDtlRepository.save(optional.get());
        return optional.get();
    }
}
