package com.tcdt.qlnvhang.service.xuathang.bantructiep.kehoach.dexuat;

import com.tcdt.qlnvhang.entities.xuathang.bantructiep.kehoach.dexuat.XhDxKhBanTrucTiepDdiem;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.kehoach.dexuat.XhDxKhBanTrucTiepDtl;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.kehoach.dexuat.XhDxKhBanTrucTiepHdr;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.kehoach.dexuat.XhDxKhBanTrucTiepDdiemRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.kehoach.dexuat.XhDxKhBanTrucTiepDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.kehoach.dexuat.XhDxKhBanTrucTiepHdrRepository;
import com.tcdt.qlnvhang.request.*;
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
        String userCapDvi = currentUser.getUser().getCapDvi();
        if (userCapDvi.equals(Contains.CAP_TONG_CUC)) {
            req.setTrangThai(Contains.DADUYET_LDC);
        } else if (userCapDvi.equals(Contains.CAP_CUC)) {
            req.setDvql(dvql);
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
        if (currentUser == null) {
            throw new Exception("Bad request.");
        }
        if (!StringUtils.isEmpty(req.getSoDxuat()) && xhDxKhBanTrucTiepHdrRepository.existsBySoDxuat(req.getSoDxuat())) {
            throw new Exception("Số đề xuất " + req.getSoDxuat() + " đã tồn tại");
        }
        XhDxKhBanTrucTiepHdr data = new XhDxKhBanTrucTiepHdr();
        BeanUtils.copyProperties(req, data);
        data.setMaDvi(currentUser.getDvql());
        //data.setNgayTao(LocalDate.now());
        data.setNguoiTaoId(currentUser.getUser().getId());
        data.setTrangThai(Contains.DUTHAO);
        data.setTrangThaiTh(Contains.CHUATONGHOP);
        int slDviTsan = data.getChildren().stream().flatMap(item -> item.getChildren().stream()).map(XhDxKhBanTrucTiepDdiem::getMaDviTsan).collect(Collectors.toSet()).size();
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
        if (currentUser == null || req == null || req.getId() == null) {
            throw new Exception("Bad request.");
        }
        XhDxKhBanTrucTiepHdr data = xhDxKhBanTrucTiepHdrRepository.findById(req.getId())
                .orElseThrow(() -> new Exception("Không tìm thấy dữ liệu cần sửa"));
        if (xhDxKhBanTrucTiepHdrRepository.existsBySoDxuatAndIdNot(req.getSoDxuat(), req.getId())) {
            throw new Exception("Số đề xuất " + req.getSoDxuat() + " đã tồn tại");
        }
        BeanUtils.copyProperties(req, data, "id", "maDvi", "trangThaiTh");
        data.setNgaySua(LocalDate.now());
        data.setNguoiSuaId(currentUser.getUser().getId());
        int slDviTsan = data.getChildren().stream().flatMap(item -> item.getChildren().stream()).map(XhDxKhBanTrucTiepDdiem::getMaDviTsan).collect(Collectors.toSet()).size();
        data.setSlDviTsan(DataUtils.safeToInt(slDviTsan));
        XhDxKhBanTrucTiepHdr updated = xhDxKhBanTrucTiepHdrRepository.save(data);
        this.saveDetail(req, updated.getId());
        return updated;
    }

    public List<XhDxKhBanTrucTiepHdr> detail(List<Long> ids) throws Exception {
        if (DataUtils.isNullOrEmpty(ids)) {
            throw new Exception("Tham số không hợp lệ.");
        }
        List<XhDxKhBanTrucTiepHdr> list = xhDxKhBanTrucTiepHdrRepository.findAllById(ids);
        if (DataUtils.isNullOrEmpty(list)) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        Map<String, String> mapVthh = getListDanhMucHangHoa();
        Map<String, String> mapLoaiHinhNx = getListDanhMucChung("LOAI_HINH_NHAP_XUAT");
        Map<String, String> mapKieuNx = getListDanhMucChung("KIEU_NHAP_XUAT");
        Map<String, String> mapPthucTtoan = getListDanhMucChung("PHUONG_THUC_TT");
        List<XhDxKhBanTrucTiepHdr> allById = xhDxKhBanTrucTiepHdrRepository.findAllById(ids);
        for (XhDxKhBanTrucTiepHdr data : allById) {
            List<XhDxKhBanTrucTiepDtl> listDtl = xhDxKhBanTrucTiepDtlRepository.findAllByIdHdr(data.getId());
            for (XhDxKhBanTrucTiepDtl dataDtl : listDtl) {
                List<XhDxKhBanTrucTiepDdiem> listDiaDiem = xhDxKhBanTrucTiepDdiemRepository.findAllByIdDtl(dataDtl.getId());
                listDiaDiem.forEach(dataDiaDiem -> {
                    dataDiaDiem.setTenDiemKho(mapDmucDvi.getOrDefault(dataDiaDiem.getMaDiemKho(), null));
                    dataDiaDiem.setTenNhaKho(mapDmucDvi.getOrDefault(dataDiaDiem.getMaNhaKho(), null));
                    dataDiaDiem.setTenNganKho(mapDmucDvi.getOrDefault(dataDiaDiem.getMaNganKho(), null));
                    dataDiaDiem.setTenLoKho(mapDmucDvi.getOrDefault(dataDiaDiem.getMaLoKho(), null));
                    dataDiaDiem.setTenLoaiVthh(mapVthh.getOrDefault(dataDiaDiem.getLoaiVthh(), null));
                    dataDiaDiem.setTenCloaiVthh(mapVthh.getOrDefault(dataDiaDiem.getCloaiVthh(), null));
                    BigDecimal giaDuocDuyet = BigDecimal.ZERO;
                    Long longNamKh = data.getNamKh() != null ? data.getNamKh().longValue() : null;
                    if (dataDiaDiem.getLoaiVthh() != null && longNamKh != null && dataDiaDiem.getLoaiVthh().startsWith(Contains.LOAI_VTHH_VATTU)) {
                        giaDuocDuyet = xhDxKhBanTrucTiepDdiemRepository.getGiaDuocDuyetVatTu(dataDiaDiem.getCloaiVthh(), dataDiaDiem.getLoaiVthh(), longNamKh);
                    } else if (dataDiaDiem.getCloaiVthh() != null && dataDiaDiem.getLoaiVthh() != null && longNamKh != null && dataDtl.getMaDvi() != null) {
                        giaDuocDuyet = xhDxKhBanTrucTiepDdiemRepository.getGiaDuocDuyetLuongThuc(dataDiaDiem.getCloaiVthh(), dataDiaDiem.getLoaiVthh(), longNamKh, dataDtl.getMaDvi());
                    }
                    Optional<BigDecimal> giaDuocDuyetOptional = Optional.ofNullable(giaDuocDuyet);
                    dataDiaDiem.setDonGiaDuocDuyet(giaDuocDuyet);
                    dataDiaDiem.setThanhTienDuocDuyet(dataDiaDiem.getSoLuongDeXuat().multiply(giaDuocDuyetOptional.orElse(BigDecimal.ZERO)));
                });
                dataDtl.setTenDvi(mapDmucDvi.getOrDefault(dataDtl.getMaDvi(), null));
                dataDtl.setDonGiaDeXuat(listDiaDiem.get(0).getDonGiaDeXuat());
                BigDecimal sumThanhTien = listDiaDiem.stream().map(XhDxKhBanTrucTiepDdiem::getThanhTienDeXuat).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
                BigDecimal sumTienDuocDuyet = listDiaDiem.stream().map(XhDxKhBanTrucTiepDdiem::getThanhTienDuocDuyet).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
                dataDtl.setTienDuocDuyet(sumTienDuocDuyet);
                dataDtl.setThanhTien(sumThanhTien);
                dataDtl.setChildren(listDiaDiem);
            }
            BigDecimal sumThanhTienDd = listDtl.stream().map(XhDxKhBanTrucTiepDtl::getTienDuocDuyet).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
            data.setThanhTienDuocDuyet(sumThanhTienDd);
            data.setMapDmucDvi(mapDmucDvi);
            data.setMapVthh(mapVthh);
            data.setMapLoaiHinhNx(mapLoaiHinhNx);
            data.setMapKieuNx(mapKieuNx);
            data.setMapPthucTtoan(mapPthucTtoan);
            data.setTrangThai(data.getTrangThai());
            data.setChildren(listDtl);
        }
        return allById;
    }

    public XhDxKhBanTrucTiepHdr detail(Long id) throws Exception {
        if (id == null) {
            throw new Exception("Tham số không hợp lệ.");
        }
        List<XhDxKhBanTrucTiepHdr> details = detail(Collections.singletonList(id));
        return details.isEmpty() ? null : details.get(0);
    }

    @Transactional
    public void delete(IdSearchReq idSearchReq) throws Exception {
        XhDxKhBanTrucTiepHdr data = xhDxKhBanTrucTiepHdrRepository.findById(idSearchReq.getId())
                .orElseThrow(() -> new Exception("Bản ghi không tồn tại"));
        List<String> allowedStatus = Arrays.asList(Contains.DUTHAO, Contains.TU_CHOI_TP, Contains.TUCHOI_LDC);
        if (!allowedStatus.contains(data.getTrangThai())) {
            throw new Exception("Chỉ thực hiện xóa với đề xuất ở trạng thái bản nháp hoặc từ chối");
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
        if (list.isEmpty()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        boolean isValidToDelete = list.stream().allMatch(hdr ->
                hdr.getTrangThai().equals(Contains.DUTHAO) ||
                        hdr.getTrangThai().equals(Contains.TU_CHOI_TP) ||
                        hdr.getTrangThai().equals(Contains.TUCHOI_LDC));
        if (!isValidToDelete) {
            throw new Exception("Chỉ thực hiện xóa với đề xuất ở trạng thái bản nháp hoặc từ chối.");
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
        if (currentUser == null || StringUtils.isEmpty(statusReq.getId())) {
            throw new Exception("Bad request.");
        }
        XhDxKhBanTrucTiepHdr data = xhDxKhBanTrucTiepHdrRepository.findById(Long.valueOf(statusReq.getId()))
                .orElseThrow(() -> new Exception("Không tìm thấy dữ liệu"));
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
                if (data.getNgayPduyet() == null) {
                    data.setNgayPduyet(LocalDate.now());
                }
                data.setLyDoTuChoi(statusReq.getLyDoTuChoi());
                break;
            case Contains.CHODUYET_LDC + Contains.CHODUYET_TP:
            case Contains.DADUYET_LDC + Contains.CHODUYET_LDC:
                data.setNguoiPduyetId(currentUser.getUser().getId());
                if (data.getNgayPduyet() == null) {
                    data.setNgayPduyet(LocalDate.now());
                }
                break;
            default:
                throw new Exception("Phê duyệt không thành công");
        }
        data.setTrangThai(statusReq.getTrangThai());
        XhDxKhBanTrucTiepHdr created = xhDxKhBanTrucTiepHdrRepository.save(data);
        return created;
    }

    public void export(CustomUserDetails currentUser, XhDxKhBanTrucTiepHdrReq req, HttpServletResponse response) throws Exception {
        req.getPaggingReq().setPage(0);
        req.getPaggingReq().setLimit(Integer.MAX_VALUE);
        Page<XhDxKhBanTrucTiepHdr> page = this.searchPage(currentUser, req);
        List<XhDxKhBanTrucTiepHdr> data = page.getContent();
        String title = "Danh sách đề xuất kế hoạch bán trực tiếp hàng DTQG";
        String[] rowsName;
        boolean isVattuType = data.stream().anyMatch(item -> item.getLoaiVthh().startsWith(Contains.LOAI_VTHH_VATTU));
        String[] commonRowsName = new String[]{"STT", "Năm KH", "Số công văn/tờ trình", "Ngày lập KH", "Ngày duyệt KH", "Số QĐ duyệt KH bán TT", "Ngày ký QĐ", "Trích yếu"};
        if (isVattuType) {
            String[] vattuRowsName = Arrays.copyOf(commonRowsName, commonRowsName.length + 5);
            vattuRowsName[8] = "Loại hàng DTQG";
            vattuRowsName[9] = "Chủng loại hàng DTQG";
            vattuRowsName[10] = "Số ĐV tài sản";
            vattuRowsName[11] = "Số QĐ giao chỉ tiêu";
            vattuRowsName[12] = "Trạng thái";
            rowsName = vattuRowsName;
        } else {
            String[] nonVattuRowsName = Arrays.copyOf(commonRowsName, commonRowsName.length + 4);
            nonVattuRowsName[8] = "Chủng loại hàng DTQG";
            nonVattuRowsName[9] = "Số ĐV tài sản";
            nonVattuRowsName[10] = "Số QĐ giao chỉ tiêu";
            nonVattuRowsName[11] = "Trạng thái";
            rowsName = nonVattuRowsName;
        }
        String fileName = "danh-sach-de-xuat-ke-hoạch-ban-truc-tiep-hang-DTQG.xlsx";
        List<Object[]> dataList = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            XhDxKhBanTrucTiepHdr hdr = data.get(i);
            Object[] objs = new Object[rowsName.length];
            objs[0] = i;
            objs[1] = hdr.getNamKh();
            objs[2] = hdr.getSoDxuat();
            objs[3] = hdr.getNgayTao();
            objs[4] = hdr.getNgayPduyet();
            objs[5] = hdr.getSoQdPd();
            objs[6] = hdr.getNgayKyQd();
            objs[7] = hdr.getTrichYeu();
            if (isVattuType) {
                objs[8] = hdr.getTenLoaiVthh();
                objs[9] = hdr.getTenCloaiVthh();
                objs[10] = hdr.getSlDviTsan();
                objs[11] = hdr.getSoQdCtieu();
                objs[12] = hdr.getTenTrangThai();
            } else {
                objs[8] = hdr.getTenCloaiVthh();
                objs[9] = hdr.getSlDviTsan();
                objs[10] = hdr.getSoQdCtieu();
                objs[11] = hdr.getTenTrangThai();
            }
            dataList.add(objs);
        }
        ExportExcel ex = new ExportExcel(title, fileName, rowsName, dataList, response);
        ex.export();
    }

    public BigDecimal countSoLuongKeHoachNam(CountKhlcntSlReq req) {
        return xhDxKhBanTrucTiepHdrRepository.countSLDalenKh(req.getYear(), req.getLoaiVthh(), req.getMaDvi(), req.getLastest());
    }

    public BigDecimal getGiaDuocDuyet(getGiaDuocDuyet req) {
        if (req == null) {
            return BigDecimal.ZERO;
        }
        Long longNamKh = req.getNam() != null ? req.getNam().longValue() : null;
        if (!Contains.LOAI_VTHH_VATTU.equals(req.getTypeLoaiVthh()) && req.getMaDvi() != null) {
            return xhDxKhBanTrucTiepDdiemRepository.getGiaDuocDuyetLuongThuc(req.getCloaiVthh(), req.getLoaiVthh(), longNamKh, req.getMaDvi());
        }
        return xhDxKhBanTrucTiepDdiemRepository.getGiaDuocDuyetVatTu(req.getCloaiVthh(), req.getLoaiVthh(), longNamKh);
    }
}