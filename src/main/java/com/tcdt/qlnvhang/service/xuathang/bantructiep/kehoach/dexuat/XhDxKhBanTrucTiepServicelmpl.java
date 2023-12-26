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
import com.tcdt.qlnvhang.util.LocalDateTimeUtils;
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

    public Page<XhDxKhBanTrucTiepHdr> searchPage(CustomUserDetails currentUser, XhDxKhBanTrucTiepHdrReq request) throws Exception {
        if (currentUser.getUser().getCapDvi().equals(Contains.CAP_TONG_CUC)) {
            request.setTrangThai(Contains.DADUYET_LDC);
        } else if (currentUser.getUser().getCapDvi().equals(Contains.CAP_CUC)) {
            request.setDvql(currentUser.getDvql());
        }
        Pageable pageable = PageRequest.of(request.getPaggingReq().getPage(), request.getPaggingReq().getLimit());
        Page<XhDxKhBanTrucTiepHdr> searchResultPage = xhDxKhBanTrucTiepHdrRepository.searchPage(request, pageable);
        Map<String, String> mapDmucVthh = getListDanhMucHangHoa();
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        searchResultPage.getContent().forEach(data -> {
            try {
                data.setMapDmucDvi(mapDmucDvi);
                data.setMapDmucVthh(mapDmucVthh);
                data.setTrangThai(data.getTrangThai());
                data.setTrangThaiTh(data.getTrangThaiTh());
            } catch (Exception exception) {
                throw new RuntimeException(exception);
            }
        });
        return searchResultPage;
    }

    @Transactional
    public XhDxKhBanTrucTiepHdr create(CustomUserDetails currentUser, XhDxKhBanTrucTiepHdrReq request) throws Exception {
        if (currentUser == null || request == null) {
            throw new Exception("Bad request.");
        }
        if (!StringUtils.isEmpty(request.getSoDxuat()) && xhDxKhBanTrucTiepHdrRepository.existsBySoDxuat(request.getSoDxuat())) {
            throw new Exception("Số đề xuất " + request.getSoDxuat() + " đã tồn tại");
        }
        XhDxKhBanTrucTiepHdr newData = new XhDxKhBanTrucTiepHdr();
        BeanUtils.copyProperties(request, newData);
        newData.setMaDvi(currentUser.getDvql());
        newData.setNguoiTaoId(currentUser.getUser().getId());
        newData.setTrangThai(Contains.DUTHAO);
        newData.setTrangThaiTh(Contains.CHUATONGHOP);
        int uniqueMaDviTsanCount = newData.getChildren().stream()
                .flatMap(item -> item.getChildren().stream())
                .map(XhDxKhBanTrucTiepDdiem::getMaDviTsan)
                .collect(Collectors.toSet())
                .size();
        newData.setSlDviTsan(DataUtils.safeToInt(uniqueMaDviTsanCount));
        XhDxKhBanTrucTiepHdr createdRecord = xhDxKhBanTrucTiepHdrRepository.save(newData);
        this.saveDetail(request, createdRecord.getId(), false);
        return createdRecord;
    }

    @Transactional
    public XhDxKhBanTrucTiepHdr update(CustomUserDetails currentUser, XhDxKhBanTrucTiepHdrReq request) throws Exception {
        if (currentUser == null || request == null || request.getId() == null) {
            throw new Exception("Bad request.");
        }
        XhDxKhBanTrucTiepHdr existingData = xhDxKhBanTrucTiepHdrRepository.findById(request.getId())
                .orElseThrow(() -> new Exception("Không tìm thấy dữ liệu cần sửa"));
        if (xhDxKhBanTrucTiepHdrRepository.existsBySoDxuatAndIdNot(request.getSoDxuat(), request.getId())) {
            throw new Exception("Số đề xuất " + request.getSoDxuat() + " đã tồn tại");
        }
        BeanUtils.copyProperties(request, existingData, "id", "maDvi", "trangThaiTh");
        existingData.setNgaySua(LocalDate.now());
        existingData.setNguoiSuaId(currentUser.getUser().getId());
        int uniqueMaDviTsanCount = existingData.getChildren().stream()
                .flatMap(item -> item.getChildren().stream())
                .map(XhDxKhBanTrucTiepDdiem::getMaDviTsan)
                .collect(Collectors.toSet())
                .size();
        existingData.setSlDviTsan(DataUtils.safeToInt(uniqueMaDviTsanCount));
        XhDxKhBanTrucTiepHdr updatedData = xhDxKhBanTrucTiepHdrRepository.save(existingData);
        this.saveDetail(request, updatedData.getId(), true);
        return updatedData;
    }

    void saveDetail(XhDxKhBanTrucTiepHdrReq request, Long headerId, Boolean isCheckRequired) {
        xhDxKhBanTrucTiepDtlRepository.deleteAllByIdHdr(isCheckRequired ? headerId : null);
        for (XhDxKhBanTrucTiepDtl detailRequest : request.getChildren()) {
            XhDxKhBanTrucTiepDtl detail = new XhDxKhBanTrucTiepDtl();
            BeanUtils.copyProperties(detailRequest, detail, "id");
            detail.setIdHdr(headerId);
            xhDxKhBanTrucTiepDtlRepository.save(detail);
            xhDxKhBanTrucTiepDdiemRepository.deleteAllByIdDtl(isCheckRequired ? detailRequest.getId() : null);
            for (XhDxKhBanTrucTiepDdiem detailDdiemReq : detailRequest.getChildren()) {
                XhDxKhBanTrucTiepDdiem detailDdiem = new XhDxKhBanTrucTiepDdiem();
                BeanUtils.copyProperties(detailDdiemReq, detailDdiem, "id");
                detailDdiem.setId(null);
                detailDdiem.setIdDtl(detail.getId());
                xhDxKhBanTrucTiepDdiemRepository.save(detailDdiem);
            }
        }
    }

    public List<XhDxKhBanTrucTiepHdr> detail(List<Long> ids) throws Exception {
        if (DataUtils.isNullOrEmpty(ids)) {
            throw new Exception("Tham số không hợp lệ.");
        }
        List<XhDxKhBanTrucTiepHdr> resultList = xhDxKhBanTrucTiepHdrRepository.findAllById(ids);
        if (DataUtils.isNullOrEmpty(resultList)) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        Map<String, String> mapDmucVthh = getListDanhMucHangHoa();
        Map<String, String> mapDmucLoaiXuat = getListDanhMucChung("LOAI_HINH_NHAP_XUAT");
        Map<String, String> mapDmucKieuXuat = getListDanhMucChung("KIEU_NHAP_XUAT");
        Map<String, String> mapDmucThanhToan = getListDanhMucChung("PHUONG_THUC_TT");
        for (XhDxKhBanTrucTiepHdr item : resultList) {
            List<XhDxKhBanTrucTiepDtl> detailList = xhDxKhBanTrucTiepDtlRepository.findAllByIdHdr(item.getId());
            for (XhDxKhBanTrucTiepDtl detailItem : detailList) {
                List<XhDxKhBanTrucTiepDdiem> subDetailList = xhDxKhBanTrucTiepDdiemRepository.findAllByIdDtl(detailItem.getId());
                subDetailList.forEach(subDetailItem -> {
                    subDetailItem.setMapDmucDvi(mapDmucDvi);
                    subDetailItem.setMapDmucVthh(mapDmucVthh);
                    this.calculateGiaDuocDuyet(subDetailItem, detailItem, item, subDetailList, detailList);
                });
                detailItem.setMapDmucDvi(mapDmucDvi);
                detailItem.setChildren(subDetailList);
            }
            item.setMapDmucDvi(mapDmucDvi);
            item.setMapDmucVthh(mapDmucVthh);
            item.setMapDmucLoaiXuat(mapDmucLoaiXuat);
            item.setMapDmucKieuXuat(mapDmucKieuXuat);
            item.setMapDmucThanhToan(mapDmucThanhToan);
            item.setTrangThai(item.getTrangThai());
            item.setChildren(detailList);
        }
        return resultList;
    }

    private BigDecimal calculateGiaDuocDuyet(XhDxKhBanTrucTiepDdiem subDetailItem, XhDxKhBanTrucTiepDtl detailItem, XhDxKhBanTrucTiepHdr item, List<XhDxKhBanTrucTiepDdiem> subDetailList, List<XhDxKhBanTrucTiepDtl> detailList) {
        BigDecimal giaDuocDuyet = BigDecimal.ZERO;
        Long longNamKh = item.getNamKh() != null ? item.getNamKh().longValue() : null;
        if (subDetailItem.getLoaiVthh() != null && longNamKh != null && subDetailItem.getLoaiVthh().startsWith(Contains.LOAI_VTHH_VATTU)) {
            giaDuocDuyet = xhDxKhBanTrucTiepDdiemRepository.getGiaDuocDuyetVatTu(
                    subDetailItem.getCloaiVthh(),
                    subDetailItem.getLoaiVthh(),
                    longNamKh);
        } else if (subDetailItem.getCloaiVthh() != null && subDetailItem.getLoaiVthh() != null && longNamKh != null && detailItem.getMaDvi() != null) {
            giaDuocDuyet = xhDxKhBanTrucTiepDdiemRepository.getGiaDuocDuyetLuongThuc(
                    subDetailItem.getCloaiVthh(),
                    subDetailItem.getLoaiVthh(),
                    longNamKh,
                    detailItem.getMaDvi());
        }
        subDetailItem.setDonGiaDuocDuyet(giaDuocDuyet);
        Optional<BigDecimal> optionalGia = Optional.ofNullable(giaDuocDuyet);
        detailItem.setDonGiaDeXuat(subDetailList.get(0).getDonGiaDeXuat());
        subDetailItem.setThanhTienDuocDuyet(subDetailItem.getSoLuongDeXuat().multiply(optionalGia.orElse(BigDecimal.ZERO)));
        BigDecimal sumThanhTien = subDetailList.stream()
                .map(XhDxKhBanTrucTiepDdiem::getThanhTienDeXuat)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal sumTienDuocDuyet = subDetailList.stream()
                .map(XhDxKhBanTrucTiepDdiem::getThanhTienDuocDuyet)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        detailItem.setThanhTien(sumThanhTien);
        detailItem.setTienDuocDuyet(sumTienDuocDuyet);
        BigDecimal sumThanhTienDuocDuyet = detailList.stream()
                .map(XhDxKhBanTrucTiepDtl::getTienDuocDuyet)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        item.setThanhTienDuocDuyet(sumThanhTienDuocDuyet);
        return giaDuocDuyet;
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
        if (idSearchReq == null || idSearchReq.getId() == null) {
            throw new Exception("Bad request.");
        }
        XhDxKhBanTrucTiepHdr proposalData = xhDxKhBanTrucTiepHdrRepository.findById(idSearchReq.getId())
                .orElseThrow(() -> new Exception("Bản ghi không tồn tại"));
        List<String> allowedStatus = Arrays.asList(Contains.DUTHAO, Contains.TU_CHOI_TP, Contains.TUCHOI_LDC);
        if (!allowedStatus.contains(proposalData.getTrangThai())) {
            throw new Exception("Chỉ thực hiện xóa với đề xuất ở trạng thái bản nháp hoặc từ chối.");
        }
        List<XhDxKhBanTrucTiepDtl> proposalDetailsList = xhDxKhBanTrucTiepDtlRepository.findAllByIdHdr(proposalData.getId());
        for (XhDxKhBanTrucTiepDtl proposalDetail : proposalDetailsList) {
            xhDxKhBanTrucTiepDdiemRepository.deleteAllByIdDtl(proposalDetail.getId());
        }
        xhDxKhBanTrucTiepDtlRepository.deleteAllByIdHdr(proposalData.getId());
        xhDxKhBanTrucTiepHdrRepository.delete(proposalData);
    }

    @Transactional
    public void deleteMulti(IdSearchReq idSearchReq) throws Exception {
        if (idSearchReq == null || idSearchReq.getIdList() == null) {
            throw new Exception("Bad request.");
        }
        List<XhDxKhBanTrucTiepHdr> proposalList = xhDxKhBanTrucTiepHdrRepository.findAllByIdIn(idSearchReq.getIdList());
        if (proposalList.isEmpty()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        boolean isValidToDelete = proposalList.stream().allMatch(hdr ->
                hdr.getTrangThai().equals(Contains.DUTHAO) ||
                        hdr.getTrangThai().equals(Contains.TU_CHOI_TP) ||
                        hdr.getTrangThai().equals(Contains.TUCHOI_LDC));
        if (!isValidToDelete) {
            throw new Exception("Chỉ thực hiện xóa với đề xuất ở trạng thái bản nháp hoặc từ chối.");
        }
        List<Long> proposalIds = proposalList.stream().map(XhDxKhBanTrucTiepHdr::getId).collect(Collectors.toList());
        List<XhDxKhBanTrucTiepDtl> proposalDetailsList = xhDxKhBanTrucTiepDtlRepository.findByIdHdrIn(proposalIds);
        List<Long> detailIds = proposalDetailsList.stream().map(XhDxKhBanTrucTiepDtl::getId).collect(Collectors.toList());
        List<XhDxKhBanTrucTiepDdiem> proposalDiaDiemList = xhDxKhBanTrucTiepDdiemRepository.findByIdDtlIn(detailIds);
        xhDxKhBanTrucTiepDdiemRepository.deleteAll(proposalDiaDiemList);
        xhDxKhBanTrucTiepDtlRepository.deleteAll(proposalDetailsList);
        xhDxKhBanTrucTiepHdrRepository.deleteAll(proposalList);
    }

    public XhDxKhBanTrucTiepHdr approve(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {
        if (currentUser == null || StringUtils.isEmpty(statusReq.getId())) {
            throw new Exception("Bad request.");
        }
        XhDxKhBanTrucTiepHdr proposal = xhDxKhBanTrucTiepHdrRepository.findById(Long.valueOf(statusReq.getId()))
                .orElseThrow(() -> new Exception("Không tìm thấy dữ liệu"));
        String statusCombination = statusReq.getTrangThai() + proposal.getTrangThai();
        switch (statusCombination) {
            case Contains.CHODUYET_TP + Contains.DUTHAO:
            case Contains.CHODUYET_TP + Contains.TUCHOI_TP:
            case Contains.CHODUYET_TP + Contains.TUCHOI_LDC:
                proposal.setNguoiGuiDuyetId(currentUser.getUser().getId());
                proposal.setNgayGuiDuyet(LocalDate.now());
                break;
            case Contains.TUCHOI_TP + Contains.CHODUYET_TP:
            case Contains.TUCHOI_LDC + Contains.CHODUYET_LDC:
                proposal.setNguoiPduyetId(currentUser.getUser().getId());
                proposal.setNgayPduyet(proposal.getNgayPduyet() == null ? LocalDate.now() : proposal.getNgayPduyet());
                proposal.setLyDoTuChoi(statusReq.getLyDoTuChoi());
                break;
            case Contains.CHODUYET_LDC + Contains.CHODUYET_TP:
            case Contains.DADUYET_LDC + Contains.CHODUYET_LDC:
                proposal.setNguoiPduyetId(currentUser.getUser().getId());
                proposal.setNgayPduyet(proposal.getNgayPduyet() == null ? LocalDate.now() : proposal.getNgayPduyet());
                break;
            default:
                throw new Exception("Phê duyệt không thành công");
        }
        proposal.setTrangThai(statusReq.getTrangThai());
        return xhDxKhBanTrucTiepHdrRepository.save(proposal);
    }

    public void export(CustomUserDetails currentUser, XhDxKhBanTrucTiepHdrReq request, HttpServletResponse response) throws Exception {
        request.getPaggingReq().setPage(0);
        request.getPaggingReq().setLimit(Integer.MAX_VALUE);
        Page<XhDxKhBanTrucTiepHdr> page = this.searchPage(currentUser, request);
        List<XhDxKhBanTrucTiepHdr> dataList = page.getContent();
        String title = "Danh sách đề xuất kế hoạch bán trực tiếp hàng DTQG";
        String[] rowsName;
        boolean isVattuType = dataList.stream().anyMatch(item -> item.getLoaiVthh().startsWith(Contains.LOAI_VTHH_VATTU));
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
        List<Object[]> excelDataList = new ArrayList<>();
        for (int i = 0; i < dataList.size(); i++) {
            XhDxKhBanTrucTiepHdr proposal = dataList.get(i);
            Object[] excelRow = new Object[rowsName.length];
            excelRow[0] = i;
            excelRow[1] = proposal.getNamKh();
            excelRow[2] = proposal.getSoDxuat();
            excelRow[3] = LocalDateTimeUtils.localDateToString(proposal.getNgayTao());
            excelRow[4] = LocalDateTimeUtils.localDateToString(proposal.getNgayPduyet());
            excelRow[5] = proposal.getSoQdPd();
            excelRow[6] = LocalDateTimeUtils.localDateToString(proposal.getNgayKyQd());
            excelRow[7] = proposal.getTrichYeu();
            if (isVattuType) {
                excelRow[8] = proposal.getTenLoaiVthh();
                excelRow[9] = proposal.getTenCloaiVthh();
                excelRow[10] = proposal.getSlDviTsan();
                excelRow[11] = proposal.getSoQdCtieu();
                excelRow[12] = proposal.getTenTrangThai();
            } else {
                excelRow[8] = proposal.getTenCloaiVthh();
                excelRow[9] = proposal.getSlDviTsan();
                excelRow[10] = proposal.getSoQdCtieu();
                excelRow[11] = proposal.getTenTrangThai();
            }
            excelDataList.add(excelRow);
        }
        ExportExcel exportExcel = new ExportExcel(title, fileName, rowsName, excelDataList, response);
        exportExcel.export();
    }

    public BigDecimal countSoLuongKeHoachNam(CountKhlcntSlReq request) {
        if (request == null) {
            return BigDecimal.ZERO;
        }
        return xhDxKhBanTrucTiepHdrRepository.countSLDalenKh(
                request.getYear(),
                request.getLoaiVthh(),
                request.getMaDvi(),
                request.getLastest());
    }

    public BigDecimal getGiaDuocDuyet(getGiaDuocDuyet request) {
        if (request == null) {
            return BigDecimal.ZERO;
        }
        Long longNamKh = request.getNam() != null ? request.getNam().longValue() : null;
        if (!Contains.LOAI_VTHH_VATTU.equals(request.getTypeLoaiVthh()) && request.getMaDvi() != null) {
            return xhDxKhBanTrucTiepDdiemRepository.getGiaDuocDuyetLuongThuc(
                    request.getCloaiVthh(),
                    request.getLoaiVthh(),
                    longNamKh,
                    request.getMaDvi());
        }
        return xhDxKhBanTrucTiepDdiemRepository.getGiaDuocDuyetVatTu(
                request.getCloaiVthh(),
                request.getLoaiVthh(),
                longNamKh);
    }
}