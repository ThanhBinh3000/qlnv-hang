package com.tcdt.qlnvhang.service.xuathang.daugia.kehoach.dexuat;

import com.tcdt.qlnvhang.entities.xuathang.daugia.kehoach.dexuat.XhDxKhBanDauGia;
import com.tcdt.qlnvhang.entities.xuathang.daugia.kehoach.dexuat.XhDxKhBanDauGiaDtl;
import com.tcdt.qlnvhang.entities.xuathang.daugia.kehoach.dexuat.XhDxKhBanDauGiaPhanLo;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.xuathang.daugia.kehoach.dexuat.XhDxKhBanDauGiaDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.kehoach.dexuat.XhDxKhBanDauGiaPhanLoRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.kehoach.dexuat.XhDxKhBanDauGiaRepository;
import com.tcdt.qlnvhang.request.CountKhlcntSlReq;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.getGiaDuocDuyet;
import com.tcdt.qlnvhang.request.xuathang.daugia.kehoachbdg.dexuat.XhDxKhBanDauGiaReq;
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
public class XhDxKhBanDauGiaServiceImpl extends BaseServiceImpl {

    @Autowired
    private XhDxKhBanDauGiaRepository xhDxKhBanDauGiaRepository;
    @Autowired
    private XhDxKhBanDauGiaDtlRepository xhDxKhBanDauGiaDtlRepository;
    @Autowired
    private XhDxKhBanDauGiaPhanLoRepository xhDxKhBanDauGiaPhanLoRepository;

    public Page<XhDxKhBanDauGia> searchPage(CustomUserDetails currentUser, XhDxKhBanDauGiaReq request) throws Exception {
        if (currentUser.getUser().getCapDvi().equals(Contains.CAP_CUC)) {
            request.setDvql(currentUser.getDvql());
        }
        Pageable pageable = PageRequest.of(request.getPaggingReq().getPage(), request.getPaggingReq().getLimit());
        Page<XhDxKhBanDauGia> searchResultPage = xhDxKhBanDauGiaRepository.searchPage(request, pageable);
        Map<String, String> mapDmucVthh = getListDanhMucHangHoa();
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        searchResultPage.getContent().forEach(data -> {
            try {
                data.setMapDmucVthh(mapDmucVthh);
                data.setMapDmucDvi(mapDmucDvi);
                data.setTrangThai(data.getTrangThai());
                data.setTrangThaiTh(data.getTrangThaiTh());
            } catch (Exception exception) {
                throw new RuntimeException(exception);
            }
        });
        return searchResultPage;
    }

    @Transactional
    public XhDxKhBanDauGia create(CustomUserDetails currentUser, XhDxKhBanDauGiaReq request) throws Exception {
        if (currentUser == null || request == null) {
            throw new Exception("Bad request.");
        }
        if (!StringUtils.isEmpty(request.getSoDxuat()) && xhDxKhBanDauGiaRepository.existsBySoDxuat(request.getSoDxuat())) {
            throw new Exception("Số đề xuất " + request.getSoDxuat() + " đã tồn tại");
        }
        XhDxKhBanDauGia newData = new XhDxKhBanDauGia();
        BeanUtils.copyProperties(request, newData);
        newData.setMaDvi(currentUser.getDvql());
        newData.setNguoiTaoId(currentUser.getUser().getId());
        newData.setTrangThai(Contains.DUTHAO);
        newData.setTrangThaiTh(Contains.CHUATONGHOP);
        int uniqueMaDviTsanCount = newData.getChildren().stream()
                .flatMap(item -> item.getChildren().stream())
                .map(XhDxKhBanDauGiaPhanLo::getMaDviTsan)
                .collect(Collectors.toSet())
                .size();
        newData.setSlDviTsan(DataUtils.safeToInt(uniqueMaDviTsanCount));
        XhDxKhBanDauGia createdRecord = xhDxKhBanDauGiaRepository.save(newData);
        this.saveDetail(request, createdRecord.getId(), false);
        return createdRecord;
    }

    @Transactional
    public XhDxKhBanDauGia update(CustomUserDetails currentUser, XhDxKhBanDauGiaReq request) throws Exception {
        if (currentUser == null || request == null || request.getId() == null) {
            throw new Exception("Bad request.");
        }
        XhDxKhBanDauGia existingData = xhDxKhBanDauGiaRepository.findById(request.getId())
                .orElseThrow(() -> new Exception("Không tìm thấy dữ liệu cần sửa"));
        if (!StringUtils.isEmpty(request.getSoDxuat()) && xhDxKhBanDauGiaRepository.existsBySoDxuatAndIdNot(request.getSoDxuat(), request.getId())) {
            throw new Exception("Số đề xuất " + request.getSoDxuat() + " đã tồn tại");
        }
        BeanUtils.copyProperties(request, existingData, "id", "maDvi", "trangThaiTh");
        existingData.setNgaySua(LocalDate.now());
        existingData.setNguoiSuaId(currentUser.getUser().getId());
        int uniqueMaDviTsanCount = existingData.getChildren().stream()
                .flatMap(item -> item.getChildren().stream())
                .map(XhDxKhBanDauGiaPhanLo::getMaDviTsan)
                .collect(Collectors.toSet())
                .size();
        existingData.setSlDviTsan(DataUtils.safeToInt(uniqueMaDviTsanCount));
        XhDxKhBanDauGia updatedData = xhDxKhBanDauGiaRepository.save(existingData);
        this.saveDetail(request, updatedData.getId(), true);
        return updatedData;
    }

    void saveDetail(XhDxKhBanDauGiaReq request, Long headerId, Boolean isCheckRequired) {
        xhDxKhBanDauGiaDtlRepository.deleteAllByIdHdr(isCheckRequired ? headerId : null);
        for (XhDxKhBanDauGiaDtl detailRequest : request.getChildren()) {
            XhDxKhBanDauGiaDtl detail = new XhDxKhBanDauGiaDtl();
            BeanUtils.copyProperties(detailRequest, detail, "id");
            detail.setIdHdr(headerId);
            xhDxKhBanDauGiaDtlRepository.save(detail);
            xhDxKhBanDauGiaPhanLoRepository.deleteAllByIdDtl(isCheckRequired ? detailRequest.getId() : null);
            for (XhDxKhBanDauGiaPhanLo detailPhanLoReq : detailRequest.getChildren()) {
                XhDxKhBanDauGiaPhanLo detailPhanLo = new XhDxKhBanDauGiaPhanLo();
                BeanUtils.copyProperties(detailPhanLoReq, detailPhanLo, "id");
                detailPhanLo.setId(null);
                detailPhanLo.setIdDtl(detail.getId());
                xhDxKhBanDauGiaPhanLoRepository.save(detailPhanLo);
            }
        }
    }

    public List<XhDxKhBanDauGia> detail(List<Long> ids) throws Exception {
        if (DataUtils.isNullOrEmpty(ids)) {
            throw new Exception("Tham số không hợp lệ.");
        }

        List<XhDxKhBanDauGia> resultList = xhDxKhBanDauGiaRepository.findByIdIn(ids);
        if (DataUtils.isNullOrEmpty(resultList)) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        Map<String, String> mapDmucVthh = getListDanhMucHangHoa();
        Map<String, String> mapDmucLoaiXuat = getListDanhMucChung("LOAI_HINH_NHAP_XUAT");
        Map<String, String> mapDmucKieuXuat = getListDanhMucChung("KIEU_NHAP_XUAT");
        Map<String, String> mapDmucThanhToan = getListDanhMucChung("PHUONG_THUC_TT");
        for (XhDxKhBanDauGia item : resultList) {
            List<XhDxKhBanDauGiaDtl> detailList = xhDxKhBanDauGiaDtlRepository.findAllByIdHdr(item.getId());
            for (XhDxKhBanDauGiaDtl detailItem : detailList) {
                List<XhDxKhBanDauGiaPhanLo> subDetailList = xhDxKhBanDauGiaPhanLoRepository.findAllByIdDtl(detailItem.getId());
                subDetailList.forEach(subDetailItem -> {
                    subDetailItem.setMapDmucDvi(mapDmucDvi);
                    subDetailItem.setMapDmucVthh(mapDmucVthh);
                    this.calculateGiaDuocDuyet(subDetailItem, detailItem, item, subDetailList, detailList);
                });
                detailItem.setMapDmucDvi(mapDmucDvi);
                detailItem.setChildren(subDetailList);
            }
            item.setMapDmucThanhToan(mapDmucThanhToan);
            item.setMapDmucLoaiXuat(mapDmucLoaiXuat);
            item.setMapDmucKieuXuat(mapDmucKieuXuat);
            item.setMapDmucVthh(mapDmucVthh);
            item.setMapDmucDvi(mapDmucDvi);
            item.setTrangThai(item.getTrangThai());
            item.setChildren(detailList);
        }
        return resultList;
    }

    private BigDecimal calculateGiaDuocDuyet(XhDxKhBanDauGiaPhanLo subDetailItem, XhDxKhBanDauGiaDtl detailItem, XhDxKhBanDauGia item, List<XhDxKhBanDauGiaPhanLo> subDetailList, List<XhDxKhBanDauGiaDtl> detailList) {
        BigDecimal giaDuocDuyet = BigDecimal.ZERO;
        Long longNamKh = item.getNamKh() != null ? item.getNamKh().longValue() : null;
        if (subDetailItem.getLoaiVthh() != null && longNamKh != null && subDetailItem.getLoaiVthh().startsWith(Contains.LOAI_VTHH_VATTU)) {
            giaDuocDuyet = xhDxKhBanDauGiaPhanLoRepository.getGiaDuocDuyetVatTu(subDetailItem.getCloaiVthh(),
                    subDetailItem.getLoaiVthh(),
                    longNamKh);
        } else if (subDetailItem.getCloaiVthh() != null && subDetailItem.getLoaiVthh() != null && longNamKh != null && detailItem.getMaDvi() != null) {
            giaDuocDuyet = xhDxKhBanDauGiaPhanLoRepository.getGiaDuocDuyetLuongThuc(subDetailItem.getCloaiVthh(),
                    subDetailItem.getLoaiVthh(),
                    longNamKh,
                    detailItem.getMaDvi());
        }
        subDetailItem.setDonGiaDuocDuyet(giaDuocDuyet);
        Optional<BigDecimal> optionalGia = Optional.ofNullable(subDetailItem.getDonGiaDuocDuyet());
        BigDecimal thanhTienDuocDuyet = subDetailItem.getSoLuongDeXuat().multiply(optionalGia.orElse(BigDecimal.ZERO));
        BigDecimal tienDatTruocDuocDuyet = thanhTienDuocDuyet.multiply(item.getKhoanTienDatTruoc().divide(BigDecimal.valueOf(100)));
        subDetailItem.setThanhTienDuocDuyet(thanhTienDuocDuyet);
        subDetailItem.setTienDatTruocDuocDuyet(tienDatTruocDuocDuyet);
        BigDecimal sumSoTienDuocDuyet = subDetailList.stream()
                .map(XhDxKhBanDauGiaPhanLo::getThanhTienDuocDuyet)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal sumSoTienDtruocDduyet = subDetailList.stream()
                .map(XhDxKhBanDauGiaPhanLo::getTienDatTruocDuocDuyet)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        detailItem.setSoTienDuocDuyet(sumSoTienDuocDuyet);
        detailItem.setSoTienDtruocDduyet(sumSoTienDtruocDduyet);
        BigDecimal sumTongTienDuocDuyet = detailList.stream()
                .map(XhDxKhBanDauGiaDtl::getSoTienDuocDuyet)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal sumTongKtienDtruocDduyet = detailList.stream()
                .map(XhDxKhBanDauGiaDtl::getSoTienDtruocDduyet)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        item.setTongTienDuocDuyet(sumTongTienDuocDuyet);
        item.setTongKtienDtruocDduyet(sumTongKtienDtruocDduyet);
        return giaDuocDuyet;
    }


    public XhDxKhBanDauGia detail(Long id) throws Exception {
        if (id == null) {
            throw new Exception("Tham số không hợp lệ.");
        }
        List<XhDxKhBanDauGia> details = detail(Collections.singletonList(id));
        return details.isEmpty() ? null : details.get(0);
    }

    @Transactional
    public void delete(IdSearchReq idSearchReq) throws Exception {
        if (idSearchReq == null || idSearchReq.getId() == null) {
            throw new Exception("Bad request.");
        }
        XhDxKhBanDauGia proposalData = xhDxKhBanDauGiaRepository.findById(idSearchReq.getId())
                .orElseThrow(() -> new Exception("Bản ghi không tồn tại"));
        List<String> allowedStatusList = Arrays.asList(Contains.DUTHAO, Contains.TU_CHOI_TP, Contains.TUCHOI_LDC, Contains.TU_CHOI_CBV);
        if (!allowedStatusList.contains(proposalData.getTrangThai())) {
            throw new Exception("Chỉ thực hiện xóa với đề xuất ở trạng thái bản nháp hoặc từ chối.");
        }
        List<XhDxKhBanDauGiaDtl> proposalDetailsList = xhDxKhBanDauGiaDtlRepository.findAllByIdHdr(proposalData.getId());
        for (XhDxKhBanDauGiaDtl proposalDetail : proposalDetailsList) {
            xhDxKhBanDauGiaPhanLoRepository.deleteAllByIdDtl(proposalDetail.getId());
        }
        xhDxKhBanDauGiaDtlRepository.deleteAllByIdHdr(proposalData.getId());
        xhDxKhBanDauGiaRepository.delete(proposalData);
    }

    @Transactional
    public void deleteMulti(IdSearchReq idSearchReq) throws Exception {
        if (idSearchReq == null || idSearchReq.getIdList() == null) {
            throw new Exception("Bad request.");
        }
        List<XhDxKhBanDauGia> proposalList = xhDxKhBanDauGiaRepository.findAllByIdIn(idSearchReq.getIdList());
        if (proposalList.isEmpty()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        boolean isValidToDelete = proposalList.stream().allMatch(proposal ->
                proposal.getTrangThai().equals(Contains.DUTHAO) ||
                        proposal.getTrangThai().equals(Contains.TU_CHOI_TP) ||
                        proposal.getTrangThai().equals(Contains.TUCHOI_LDC) ||
                        proposal.getTrangThai().equals(Contains.TU_CHOI_CBV));
        if (!isValidToDelete) {
            throw new Exception("Chỉ thực hiện xóa với đề xuất ở trạng thái bản nháp hoặc từ chối.");
        }
        List<Long> proposalIds = proposalList.stream().map(XhDxKhBanDauGia::getId).collect(Collectors.toList());
        List<XhDxKhBanDauGiaDtl> proposalDetailsList = xhDxKhBanDauGiaDtlRepository.findByIdHdrIn(proposalIds);
        List<Long> detailIds = proposalDetailsList.stream().map(XhDxKhBanDauGiaDtl::getId).collect(Collectors.toList());
        List<XhDxKhBanDauGiaPhanLo> proposalPhanLoList = xhDxKhBanDauGiaPhanLoRepository.findByIdDtlIn(detailIds);
        xhDxKhBanDauGiaPhanLoRepository.deleteAll(proposalPhanLoList);
        xhDxKhBanDauGiaDtlRepository.deleteAll(proposalDetailsList);
        xhDxKhBanDauGiaRepository.deleteAll(proposalList);
    }

    public XhDxKhBanDauGia approve(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {
        if (currentUser == null || StringUtils.isEmpty(statusReq.getId())) {
            throw new Exception("Bad request.");
        }
        XhDxKhBanDauGia proposal = xhDxKhBanDauGiaRepository.findById(Long.valueOf(statusReq.getId()))
                .orElseThrow(() -> new Exception("Không tìm thấy dữ liệu"));
        String statusCombination = statusReq.getTrangThai() + proposal.getTrangThai();
        switch (statusCombination) {
            case Contains.CHODUYET_TP + Contains.DUTHAO:
            case Contains.CHODUYET_TP + Contains.TUCHOI_TP:
            case Contains.CHODUYET_TP + Contains.TUCHOI_LDC:
            case Contains.CHODUYET_TP + Contains.TU_CHOI_CBV:
                proposal.setNguoiGuiDuyetId(currentUser.getUser().getId());
                proposal.setNgayGuiDuyet(LocalDate.now());
                break;
            case Contains.TUCHOI_TP + Contains.CHODUYET_TP:
            case Contains.TUCHOI_LDC + Contains.CHODUYET_LDC:
            case Contains.TU_CHOI_CBV + Contains.DADUYET_LDC:
                proposal.setNguoiPduyetId(currentUser.getUser().getId());
                proposal.setNgayPduyet(proposal.getNgayPduyet() == null ? LocalDate.now() : proposal.getNgayPduyet());
                proposal.setLyDoTuChoi(statusReq.getLyDoTuChoi());
                break;
            case Contains.CHODUYET_LDC + Contains.CHODUYET_TP:
            case Contains.DADUYET_LDC + Contains.CHODUYET_LDC:
            case Contains.DA_DUYET_CBV + Contains.DADUYET_LDC:
                proposal.setNguoiPduyetId(currentUser.getUser().getId());
                proposal.setNgayPduyet(proposal.getNgayPduyet() == null ? LocalDate.now() : proposal.getNgayPduyet());
                break;
            default:
                throw new Exception("Phê duyệt không thành công");
        }
        proposal.setTrangThai(statusReq.getTrangThai());
        return xhDxKhBanDauGiaRepository.save(proposal);
    }

    public void export(CustomUserDetails currentUser, XhDxKhBanDauGiaReq request, HttpServletResponse response) throws Exception {
        request.getPaggingReq().setPage(0);
        request.getPaggingReq().setLimit(Integer.MAX_VALUE);
        Page<XhDxKhBanDauGia> page = this.searchPage(currentUser, request);
        List<XhDxKhBanDauGia> dataList = page.getContent();
        String title = "Danh sách đề xuất kế hoạch bán đấu giá hàng DTQG";
        String[] rowsName;
        boolean isVattuType = dataList.stream().anyMatch(item -> item.getLoaiVthh().startsWith(Contains.LOAI_VTHH_VATTU));
        String[] commonRowsName = new String[]{"STT", "Năm KH", "Số công văn/tờ trình", "Ngày lập KH", "Ngày duyệt KH", "Số QĐ duyệt KH bán ĐG", "Ngày ký QĐ", "Trích yếu"};
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
        String fileName = "danh-sach-de-xuat-ke-hoạch-ban-dau-gia-hang-DTQG.xlsx";
        List<Object[]> excelDataList = new ArrayList<>();
        for (int i = 0; i < dataList.size(); i++) {
            XhDxKhBanDauGia proposal = dataList.get(i);
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
        return xhDxKhBanDauGiaRepository.countSLDalenKh(
                request.getMaDvi(),
                request.getYear(),
                request.getLoaiVthh(),
                request.getLastest());
    }

    public BigDecimal getGiaDuocDuyet(getGiaDuocDuyet request) {
        if (request == null) {
            return BigDecimal.ZERO;
        }
        Long longNamKh = request.getNam() != null ? request.getNam().longValue() : null;
        if (!Contains.LOAI_VTHH_VATTU.equals(request.getTypeLoaiVthh()) && request.getMaDvi() != null) {
            return xhDxKhBanDauGiaPhanLoRepository.getGiaDuocDuyetLuongThuc(
                    request.getCloaiVthh(),
                    request.getLoaiVthh(),
                    longNamKh,
                    request.getMaDvi());
        }
        return xhDxKhBanDauGiaPhanLoRepository.getGiaDuocDuyetVatTu(
                request.getCloaiVthh(),
                request.getLoaiVthh(),
                longNamKh);
    }
}