package com.tcdt.qlnvhang.service.xuathang.bantructiep.kehoach.dexuat;

import com.tcdt.qlnvhang.entities.xuathang.bantructiep.kehoach.dexuat.XhDxKhBanTrucTiepDdiem;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.kehoach.dexuat.XhDxKhBanTrucTiepDtl;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.kehoach.dexuat.XhDxKhBanTrucTiepHdr;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.kehoach.dexuat.XhDxKhBanTrucTiepDdiemRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.kehoach.dexuat.XhDxKhBanTrucTiepDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.kehoach.dexuat.XhDxKhBanTrucTiepHdrRepository;
import com.tcdt.qlnvhang.request.CountKhlcntSlReq;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.kehoach.dexuat.XhDxKhBanTrucTiepHdrReq;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
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

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class XhDxKhBanTrucTiepServicelmpl extends BaseServiceImpl {

    @Autowired
    private XhDxKhBanTrucTiepHdrRepository xhDxKhBanTrucTiepHdrRepository;
    @Autowired
    private XhDxKhBanTrucTiepDtlRepository xhDxKhBanTrucTiepDtlRepository;
    @Autowired
    private XhDxKhBanTrucTiepDdiemRepository xhDxKhBanTrucTiepDdiemRepository;

    public Page<XhDxKhBanTrucTiepHdr> searchPage(CustomUserDetails currentUser, XhDxKhBanTrucTiepHdrReq req) throws Exception {
        String dvql = currentUser.getDvql();
        if (currentUser.getUser().getCapDvi().equals(Contains.CAP_CUC)) {
            req.setDvql(dvql);
        } else if (currentUser.getUser().getCapDvi().equals(Contains.CAP_TONG_CUC)) {
            req.setDvql(dvql.substring(0, 4));
            req.setTrangThai(Contains.DADUYET_LDC);
        }
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        Page<XhDxKhBanTrucTiepHdr> search = xhDxKhBanTrucTiepHdrRepository.searchPage(req, pageable);
        Map<String, String> mapDmucVthh = getListDanhMucHangHoa();
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        search.getContent().forEach(data -> {
            data.setMapVthh(mapDmucVthh);
            data.setMapDmucDvi(mapDmucDvi);
            data.setTrangThai(data.getTrangThai());
            data.setTrangThaiTh(data.getTrangThaiTh());
        });
        return search;
    }

    @Transactional
    public XhDxKhBanTrucTiepHdr create(CustomUserDetails currentUser, XhDxKhBanTrucTiepHdrReq req) throws Exception {
        if (currentUser == null) throw new Exception("Bad request.");
        if (!StringUtils.isEmpty(req.getSoDxuat())) {
            Optional<XhDxKhBanTrucTiepHdr> optional = xhDxKhBanTrucTiepHdrRepository.findBySoDxuat(req.getSoDxuat());
            if (optional.isPresent()) throw new Exception("Số đề xuất " + req.getSoDxuat() + " đã tồn tại");
        }
        XhDxKhBanTrucTiepHdr data = new XhDxKhBanTrucTiepHdr();
        BeanUtils.copyProperties(req, data);
        data.setMaDvi(currentUser.getUser().getDepartment());
        data.setTrangThai(Contains.DUTHAO);
        data.setTrangThaiTh(Contains.CHUATONGHOP);
        int slDviTsan = data.getChildren().stream()
                .flatMap(item -> item.getChildren().stream())
                .map(XhDxKhBanTrucTiepDdiem::getMaDviTsan).collect(Collectors.toSet()).size();
        data.setSlDviTsan(DataUtils.safeToInt(slDviTsan));
        XhDxKhBanTrucTiepHdr created = xhDxKhBanTrucTiepHdrRepository.save(data);
        this.saveDetail(req, created.getId());
        return created;
    }

    void saveDetail(XhDxKhBanTrucTiepHdrReq req, Long idHdr) {
        xhDxKhBanTrucTiepDtlRepository.deleteAllByIdHdr(idHdr);
        for (XhDxKhBanTrucTiepDtl dataDtlReq : req.getChildren()) {
            XhDxKhBanTrucTiepDtl dataDtl = new XhDxKhBanTrucTiepDtl();
            BeanUtils.copyProperties(dataDtlReq, dataDtl, "id");
            dataDtl.setIdHdr(idHdr);
            xhDxKhBanTrucTiepDtlRepository.save(dataDtl);
            xhDxKhBanTrucTiepDdiemRepository.deleteAllByIdDtl(dataDtlReq.getId());
            for (XhDxKhBanTrucTiepDdiem dataDtlDiaDiemReq : dataDtlReq.getChildren()) {
                XhDxKhBanTrucTiepDdiem dataDtlDiaDiem = new XhDxKhBanTrucTiepDdiem();
                BeanUtils.copyProperties(dataDtlDiaDiemReq, dataDtlDiaDiem, "id");
                dataDtlDiaDiem.setIdDtl(dataDtl.getId());
                xhDxKhBanTrucTiepDdiemRepository.save(dataDtlDiaDiem);
            }
        }
    }

    @Transactional
    public XhDxKhBanTrucTiepHdr update(CustomUserDetails currentUser, XhDxKhBanTrucTiepHdrReq req) throws Exception {
        if (currentUser == null) throw new Exception("Bad request.");
        Optional<XhDxKhBanTrucTiepHdr> optional = xhDxKhBanTrucTiepHdrRepository.findById(req.getId());
        if (!optional.isPresent()) throw new Exception("Không tìm thấy dữ liệu cần sửa");
        Optional<XhDxKhBanTrucTiepHdr> bySoDxuat = xhDxKhBanTrucTiepHdrRepository.findBySoDxuat(req.getSoDxuat());
        if (bySoDxuat.isPresent()) {
            if (!bySoDxuat.get().getId().equals(req.getId())) throw new Exception("số đề xuất đã tồn tại");
        }
        XhDxKhBanTrucTiepHdr data = optional.get();
        BeanUtils.copyProperties(req, data, "id", "maDvi", "trangThaiTh");
        int slDviTsan = data.getChildren().stream()
                .flatMap(item -> item.getChildren().stream())
                .map(XhDxKhBanTrucTiepDdiem::getMaDviTsan).collect(Collectors.toSet()).size();
        data.setSlDviTsan(DataUtils.safeToInt(slDviTsan));
        XhDxKhBanTrucTiepHdr updated = xhDxKhBanTrucTiepHdrRepository.save(data);
        this.saveDetail(req, updated.getId());
        return updated;
    }

    public List<XhDxKhBanTrucTiepHdr> detail(List<Long> ids) throws Exception {
        if (DataUtils.isNullOrEmpty(ids)) throw new Exception("Tham số không hợp lệ.");
        List<XhDxKhBanTrucTiepHdr> list = xhDxKhBanTrucTiepHdrRepository.findByIdIn(ids);
        if (DataUtils.isNullOrEmpty(list)) throw new Exception("Không tìm thấy dữ liệu");
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        Map<String, String> mapVthh = getListDanhMucHangHoa();
        Map<String, String> mapLoaiHinhNx = getListDanhMucChung("LOAI_HINH_NHAP_XUAT");
        Map<String, String> mapKieuNx = getListDanhMucChung("KIEU_NHAP_XUAT");
        Map<String, String> mapPthucTtoan = getListDanhMucChung("PHUONG_THUC_TT");
        List<XhDxKhBanTrucTiepHdr> allById = xhDxKhBanTrucTiepHdrRepository.findAllById(ids);
        allById.forEach(data -> {
            List<XhDxKhBanTrucTiepDtl> listDtl = xhDxKhBanTrucTiepDtlRepository.findAllByIdHdr(data.getId());
            listDtl.forEach(dataDtl -> {
                List<XhDxKhBanTrucTiepDdiem> listDiaDiem = xhDxKhBanTrucTiepDdiemRepository.findAllByIdDtl(dataDtl.getId());
                listDiaDiem.forEach(dataDiaDiem -> {
                    dataDiaDiem.setTenDiemKho(StringUtils.isEmpty(dataDiaDiem.getMaDiemKho()) ? null : mapDmucDvi.get(dataDiaDiem.getMaDiemKho()));
                    dataDiaDiem.setTenNhaKho(StringUtils.isEmpty(dataDiaDiem.getMaNhaKho()) ? null : mapDmucDvi.get(dataDiaDiem.getMaNhaKho()));
                    dataDiaDiem.setTenNganKho(StringUtils.isEmpty(dataDiaDiem.getMaNganKho()) ? null : mapDmucDvi.get(dataDiaDiem.getMaNganKho()));
                    dataDiaDiem.setTenLoKho(StringUtils.isEmpty(dataDiaDiem.getMaLoKho()) ? null : mapDmucDvi.get(dataDiaDiem.getMaLoKho()));
                    dataDiaDiem.setTenLoaiVthh(StringUtils.isEmpty(dataDiaDiem.getLoaiVthh()) ? null : mapVthh.get(dataDiaDiem.getLoaiVthh()));
                    dataDiaDiem.setTenCloaiVthh(StringUtils.isEmpty(dataDiaDiem.getCloaiVthh()) ? null : mapVthh.get(dataDiaDiem.getCloaiVthh()));
                });
                dataDtl.setTenDvi(StringUtils.isEmpty(dataDtl.getMaDvi()) ? null : mapDmucDvi.get(dataDtl.getMaDvi()));
                dataDtl.setChildren(listDiaDiem);
            });
            data.setMapDmucDvi(mapDmucDvi);
            data.setMapVthh(mapVthh);
            data.setMapLoaiHinhNx(mapLoaiHinhNx);
            data.setMapKieuNx(mapKieuNx);
            data.setMapPthucTtoan(mapPthucTtoan);
            data.setTrangThai(data.getTrangThai());
            data.setChildren(listDtl);
        });
        return allById;
    }

    @Transactional
    public void delete(IdSearchReq idSearchReq) throws Exception {
        Optional<XhDxKhBanTrucTiepHdr> optional = xhDxKhBanTrucTiepHdrRepository.findById(idSearchReq.getId());
        if (!optional.isPresent()) throw new Exception("Bản ghi không tồn tại");
        XhDxKhBanTrucTiepHdr data = optional.get();
        if (!data.getTrangThai().equals(Contains.DUTHAO)
                && !data.getTrangThai().equals(Contains.TU_CHOI_TP)
                && !data.getTrangThai().equals(Contains.TUCHOI_LDC)) {
            throw new Exception("Chỉ thực hiện xóa với kế hoạch ở trạng thái bản nháp hoặc từ chối");
        }
        List<XhDxKhBanTrucTiepDtl> listDtl = xhDxKhBanTrucTiepDtlRepository.findAllByIdHdr(data.getId());
        for (XhDxKhBanTrucTiepDtl dtl : listDtl) {
            xhDxKhBanTrucTiepDdiemRepository.deleteAllByIdDtl(dtl.getId());
        }
        xhDxKhBanTrucTiepDtlRepository.deleteAllByIdHdr(data.getId());
        xhDxKhBanTrucTiepHdrRepository.delete(data);
    }

    @Transactional
    public void deleteMulti(IdSearchReq idSearchReq) throws Exception {
        List<XhDxKhBanTrucTiepHdr> list = xhDxKhBanTrucTiepHdrRepository.findAllByIdIn(idSearchReq.getIdList());
        if (list.isEmpty()) throw new Exception("Bản ghi không tồn tại");
        for (XhDxKhBanTrucTiepHdr hdr : list) {
            if (!hdr.getTrangThai().equals(Contains.DUTHAO)
                    && !hdr.getTrangThai().equals(Contains.TU_CHOI_TP)
                    && !hdr.getTrangThai().equals(Contains.TUCHOI_LDC)) {
                throw new Exception("Chỉ thực hiện xóa với kế hoạch ở trạng thái bản nháp hoặc từ chối");
            }
        }
        List<Long> listIdHdr = list.stream().map(XhDxKhBanTrucTiepHdr::getId).collect(Collectors.toList());
        List<XhDxKhBanTrucTiepDtl> listDtl = xhDxKhBanTrucTiepDtlRepository.findByIdHdrIn(listIdHdr);
        List<Long> listIdDtl = listDtl.stream().map(XhDxKhBanTrucTiepDtl::getId).collect(Collectors.toList());
        List<XhDxKhBanTrucTiepDdiem> listDiaDiem = xhDxKhBanTrucTiepDdiemRepository.findByIdDtlIn(listIdDtl);
        xhDxKhBanTrucTiepDdiemRepository.deleteAll(listDiaDiem);
        xhDxKhBanTrucTiepDtlRepository.deleteAll(listDtl);
        xhDxKhBanTrucTiepHdrRepository.deleteAll(list);
    }

    public XhDxKhBanTrucTiepHdr approve(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {
        if (currentUser == null) throw new Exception("Bad request.");
        if (StringUtils.isEmpty(statusReq.getId())) throw new Exception("Không tìm thấy dữ liệu");
        Optional<XhDxKhBanTrucTiepHdr> optional = xhDxKhBanTrucTiepHdrRepository.findById(Long.valueOf(statusReq.getId()));
        if (!optional.isPresent()) throw new Exception("Không tìm thấy dữ liệu");
        XhDxKhBanTrucTiepHdr data = optional.get();
        String status = statusReq.getTrangThai() + data.getTrangThai();
        switch (status) {
            case Contains.CHODUYET_TP + Contains.DUTHAO:
            case Contains.CHODUYET_TP + Contains.TUCHOI_TP:
            case Contains.CHODUYET_TP + Contains.TUCHOI_LDC:
                data.setNguoiGuiDuyetId(currentUser.getUser().getId());
                data.setNgayGuiDuyet(LocalDate.now());
                break;
            case Contains.TUCHOI_TP + Contains.CHODUYET_TP:
            case Contains.TUCHOI_LDC + Contains.CHODUYET_LDC:
                data.setNguoiPduyetId(currentUser.getUser().getId());
                data.setNgayPduyet(LocalDate.now());
                data.setLyDoTuChoi(statusReq.getLyDoTuChoi());
                break;
            case Contains.CHODUYET_LDC + Contains.CHODUYET_TP:
            case Contains.DADUYET_LDC + Contains.CHODUYET_LDC:
                data.setNguoiPduyetId(currentUser.getUser().getId());
                data.setNgayPduyet(LocalDate.now());
                break;
            default:
                throw new Exception("Phê duyệt không thành công");
        }
        data.setTrangThai(statusReq.getTrangThai());
        XhDxKhBanTrucTiepHdr created = xhDxKhBanTrucTiepHdrRepository.save(data);
        return created;
    }

    public void export(CustomUserDetails currentUser, XhDxKhBanTrucTiepHdrReq req, HttpServletResponse response) throws Exception {
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        req.setPaggingReq(paggingReq);
        Page<XhDxKhBanTrucTiepHdr> page = this.searchPage(currentUser, req);
        List<XhDxKhBanTrucTiepHdr> data = page.getContent();
        String title = "Danh sách đề xuất kế hoạch bán trực tiếp";
        String[] rowsName = new String[]{"STT", "Năm KH", "Số KH/đề xuất", "Ngày lập KH",
                "Ngày duyệt KH", "Số QĐ duyệt KH bán TT", "Ngày ký QĐ",
                "Trích yếu", "Loại hàng hóa", "Chủng loại hàng hóa",
                "Số ĐV tài sản", "SL HĐ đã ký", "Số QĐ giao chỉ tiêu", "Trạng thái"};
        String fileName = "danh-sach-dx-kh-ban-truc-tiep.xlsx";
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;
        for (int i = 0; i < data.size(); i++) {
            XhDxKhBanTrucTiepHdr hdr = data.get(i);
            objs = new Object[rowsName.length];
            objs[0] = i;
            objs[1] = hdr.getNamKh();
            objs[2] = hdr.getSoDxuat();
            objs[3] = hdr.getNgayTao();
            objs[4] = hdr.getNgayPduyet();
            objs[5] = hdr.getSoQdPd();
            objs[6] = hdr.getNgayKyQd();
            objs[7] = hdr.getTrichYeu();
            objs[8] = hdr.getTenLoaiVthh();
            objs[9] = hdr.getTenCloaiVthh();
            objs[10] = hdr.getSoQdCtieu();
            objs[11] = hdr.getTenTrangThai();
            dataList.add(objs);
        }
        ExportExcel ex = new ExportExcel(title, fileName, rowsName, dataList, response);
        ex.export();
    }

    public BigDecimal countSoLuongKeHoachNam(CountKhlcntSlReq req) {
        return xhDxKhBanTrucTiepHdrRepository.countSLDalenKh(req.getYear(), req.getLoaiVthh(), req.getMaDvi(), req.getLastest());
    }
}
