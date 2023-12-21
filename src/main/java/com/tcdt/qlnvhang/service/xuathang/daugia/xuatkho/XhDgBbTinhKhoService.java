package com.tcdt.qlnvhang.service.xuathang.daugia.xuatkho;

import com.tcdt.qlnvhang.entities.xuathang.daugia.xuatkho.XhDgBbTinhKhoDtl;
import com.tcdt.qlnvhang.entities.xuathang.daugia.xuatkho.XhDgBbTinhKhoHdr;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.UserInfoRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.ktracluong.bienbanlaymau.XhBbLayMauRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.ktracluong.kiemnghiemcl.XhPhieuKnghiemCluongRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.nhiemvuxuat.XhQdGiaoNvXhDdiemRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.xuatkho.XhDgBbTinhKhoDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.xuatkho.XhDgBbTinhKhoHdrRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.xuathang.daugia.xuatkho.XhDgBbTinhKhoHdrReq;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.DataUtils;
import com.tcdt.qlnvhang.util.ExportExcel;
import com.tcdt.qlnvhang.util.LocalDateTimeUtils;
import fr.opensagres.xdocreport.core.XDocReportException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Consumer;

@Service
public class XhDgBbTinhKhoService extends BaseServiceImpl {

    @Autowired
    private XhDgBbTinhKhoHdrRepository xhDgBbTinhKhoHdrRepository;
    @Autowired
    private XhDgBbTinhKhoDtlRepository xhDgBbTinhKhoDtlRepository;
    @Autowired
    private XhPhieuKnghiemCluongRepository xhPhieuKnghiemCluongRepository;
    @Autowired
    private XhBbLayMauRepository xhBbLayMauRepository;
    @Autowired
    private XhQdGiaoNvXhDdiemRepository xhQdGiaoNvXhDdiemRepository;
    @Autowired
    private UserInfoRepository userInfoRepository;

    public Page<XhDgBbTinhKhoHdr> searchPage(CustomUserDetails currentUser, XhDgBbTinhKhoHdrReq request) throws Exception {
        if (currentUser.getUser().getCapDvi().equals(Contains.CAP_CUC)) {
            request.setTrangThai(Contains.DADUYET_LDCC);
            request.setMaDviCha(currentUser.getDvql());
        } else if (currentUser.getUser().getCapDvi().equals(Contains.CAP_CHI_CUC)) {
            request.setDvql(currentUser.getDvql());
        }
        Pageable pageable = PageRequest.of(request.getPaggingReq().getPage(), request.getPaggingReq().getLimit());
        Page<XhDgBbTinhKhoHdr> searchResultPage = xhDgBbTinhKhoHdrRepository.searchPage(request, pageable);
        Map<String, String> mapDmucVthh = getListDanhMucHangHoa();
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        Map<String, String> mapDmucHinhThuc = getListDanhMucChung("HINH_THUC_BAO_QUAN");
        searchResultPage.getContent().forEach(data -> {
            try {
                data.setMapDmucDvi(mapDmucDvi);
                data.setMapDmucVthh(mapDmucVthh);
                data.setMapDmucHinhThuc(mapDmucHinhThuc);
                data.setTrangThai(data.getTrangThai());
                List<XhDgBbTinhKhoDtl> childList = xhDgBbTinhKhoDtlRepository.findAllByIdHdr(data.getId());
                data.setChildren(childList != null && !childList.isEmpty() ? childList : Collections.emptyList());
            } catch (Exception exception) {
                throw new RuntimeException(exception);
            }
        });
        return searchResultPage;
    }

    @Transactional
    public XhDgBbTinhKhoHdr create(CustomUserDetails currentUser, XhDgBbTinhKhoHdrReq request) throws Exception {
        if (currentUser == null || request == null) {
            throw new Exception("Bad request.");
        }
        if (!StringUtils.isEmpty(request.getSoBbTinhKho()) && xhDgBbTinhKhoHdrRepository.existsBySoBbTinhKho(request.getSoBbTinhKho())) {
            throw new Exception("Số biên bản tịnh kho " + request.getSoBbTinhKho() + " đã tồn tại");
        }
        XhDgBbTinhKhoHdr newData = new XhDgBbTinhKhoHdr();
        BeanUtils.copyProperties(request, newData);
        newData.setMaDvi(currentUser.getDvql());
        newData.setNgayTao(LocalDate.now());
        newData.setNguoiTaoId(currentUser.getUser().getId());
        newData.setIdThuKho(currentUser.getUser().getId());
        newData.setId(Long.parseLong(newData.getSoBbTinhKho().split("/")[0]));
        newData.setTrangThai(Contains.DU_THAO);
        XhDgBbTinhKhoHdr createdRecord = xhDgBbTinhKhoHdrRepository.save(newData);
        this.saveDetail(request, createdRecord.getId(), false);
        return createdRecord;
    }

    @Transactional
    public XhDgBbTinhKhoHdr update(CustomUserDetails currentUser, XhDgBbTinhKhoHdrReq request) throws Exception {
        if (currentUser == null || request == null || request.getId() == null) {
            throw new Exception("Bad request.");
        }
        XhDgBbTinhKhoHdr existingData = xhDgBbTinhKhoHdrRepository.findById(request.getId())
                .orElseThrow(() -> new Exception("Không tìm thấy dữ liệu cần sửa"));
        if (xhDgBbTinhKhoHdrRepository.existsBySoBbTinhKhoAndIdNot(request.getSoBbTinhKho(), request.getId())) {
            throw new Exception("Số biên bản tịnh kho " + request.getSoBbTinhKho() + " đã tồn tại");
        }
        BeanUtils.copyProperties(request, existingData, "id", "maDvi", "idThuKho");
        existingData.setNgaySua(LocalDate.now());
        existingData.setNguoiSuaId(currentUser.getUser().getId());
        XhDgBbTinhKhoHdr updatedData = xhDgBbTinhKhoHdrRepository.save(existingData);
        this.saveDetail(request, updatedData.getId(), true);
        return updatedData;
    }

    void saveDetail(XhDgBbTinhKhoHdrReq request, Long headerId, Boolean isCheckRequired) {
        xhDgBbTinhKhoDtlRepository.deleteAllByIdHdr(isCheckRequired ? headerId : null);
        for (XhDgBbTinhKhoDtl detailRequest : request.getChildren()) {
            XhDgBbTinhKhoDtl detail = new XhDgBbTinhKhoDtl();
            BeanUtils.copyProperties(detailRequest, detail, "id");
            detail.setId(null);
            detail.setIdHdr(headerId);
            xhDgBbTinhKhoDtlRepository.save(detail);
        }
    }

    public List<XhDgBbTinhKhoHdr> detail(List<Long> ids) throws Exception {
        if (DataUtils.isNullOrEmpty(ids)) {
            throw new Exception("Tham số không hợp lệ.");
        }
        List<XhDgBbTinhKhoHdr> resultList = xhDgBbTinhKhoHdrRepository.findByIdIn(ids);
        if (DataUtils.isNullOrEmpty(resultList)) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        Map<String, String> mapDmucVthh = getListDanhMucHangHoa();
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        Map<String, String> mapDmucHinhThuc = getListDanhMucChung("HINH_THUC_BAO_QUAN");
        for (XhDgBbTinhKhoHdr item : resultList) {
            List<XhDgBbTinhKhoDtl> detailList = xhDgBbTinhKhoDtlRepository.findAllByIdHdr(item.getId());
            item.setChildren(detailList != null && !detailList.isEmpty() ? detailList : Collections.emptyList());
            item.setMapDmucDvi(mapDmucDvi);
            item.setMapDmucVthh(mapDmucVthh);
            item.setMapDmucHinhThuc(mapDmucHinhThuc);
            this.setFullNameIfNotNull(item.getIdThuKho(), item::setTenThuKho);
            this.setFullNameIfNotNull(item.getIdKeToan(), item::setTenKeToan);
            this.setFullNameIfNotNull(item.getIdKtvBaoQuan(), item::setTenKtvBaoQuan);
            this.setFullNameIfNotNull(item.getIdLanhDaoChiCuc(), item::setTenLanhDaoChiCuc);
        }
        return resultList;
    }

    private void setFullNameIfNotNull(Long userId, Consumer<String> fullNameSetter) {
        if (userId != null) {
            userInfoRepository.findById(userId).ifPresent(userInfo -> fullNameSetter.accept(userInfo.getFullName()));
        }
    }

    public XhDgBbTinhKhoHdr detail(Long id) throws Exception {
        if (id == null) {
            throw new Exception("Tham số không hợp lệ.");
        }
        List<XhDgBbTinhKhoHdr> details = detail(Collections.singletonList(id));
        return details.isEmpty() ? null : details.get(0);
    }

    @Transactional
    public void delete(IdSearchReq idSearchReq) throws Exception {
        if (idSearchReq == null || idSearchReq.getId() == null) {
            throw new Exception("Bad request.");
        }
        XhDgBbTinhKhoHdr proposalData = xhDgBbTinhKhoHdrRepository.findById(idSearchReq.getId())
                .orElseThrow(() -> new Exception("Bản ghi không tồn tại"));
        List<String> allowedStatus = Arrays.asList(Contains.DUTHAO, Contains.TUCHOI_KTVBQ, Contains.TUCHOI_KT, Contains.TUCHOI_LDCC);
        if (!allowedStatus.contains(proposalData.getTrangThai())) {
            throw new Exception("Chỉ thực hiện xóa với biên bản tịnh kho ở trạng thái bản nháp hoặc từ chối");
        }
        xhDgBbTinhKhoDtlRepository.deleteAllByIdHdr(proposalData.getId());
        xhDgBbTinhKhoHdrRepository.delete(proposalData);
    }

    public XhDgBbTinhKhoHdr approve(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {
        if (currentUser == null || StringUtils.isEmpty(statusReq.getId())) {
            throw new Exception("Bad request.");
        }
        XhDgBbTinhKhoHdr proposal = xhDgBbTinhKhoHdrRepository.findById(Long.valueOf(statusReq.getId()))
                .orElseThrow(() -> new Exception("Không tìm thấy dữ liệu"));
        String statusCombination = statusReq.getTrangThai() + proposal.getTrangThai();
        switch (statusCombination) {
            case Contains.CHODUYET_KTVBQ + Contains.DUTHAO:
            case Contains.CHODUYET_KTVBQ + Contains.TUCHOI_KTVBQ:
            case Contains.CHODUYET_KTVBQ + Contains.TUCHOI_KT:
            case Contains.CHODUYET_KTVBQ + Contains.TUCHOI_LDCC:
                proposal.setNguoiGuiDuyetId(currentUser.getUser().getId());
                proposal.setNgayGuiDuyet(LocalDate.now());
                break;
            case Contains.TUCHOI_KTVBQ + Contains.CHODUYET_KTVBQ:
            case Contains.TUCHOI_KT + Contains.CHODUYET_KT:
            case Contains.TUCHOI_LDCC + Contains.CHODUYET_LDCC:
                proposal.setNguoiPduyetId(currentUser.getUser().getId());
                proposal.setNgayPduyet(LocalDate.now());
                proposal.setLyDoTuChoi(statusReq.getLyDoTuChoi());
                break;
            case Contains.CHODUYET_KT + Contains.CHODUYET_KTVBQ:
                proposal.setIdKtvBaoQuan(currentUser.getUser().getId());
                break;
            case Contains.CHODUYET_LDCC + Contains.CHODUYET_KT:
                proposal.setIdKeToan(currentUser.getUser().getId());
                break;
            case Contains.DADUYET_LDCC + Contains.CHODUYET_LDCC:
                proposal.setNguoiPduyetId(currentUser.getUser().getId());
                proposal.setNgayPduyet(LocalDate.now());
                proposal.setIdLanhDaoChiCuc(currentUser.getUser().getId());
                break;
            default:
                throw new Exception("Phê duyệt không thành công");
        }
        proposal.setTrangThai(statusReq.getTrangThai());
        XhDgBbTinhKhoHdr updateDate = xhDgBbTinhKhoHdrRepository.save(proposal);
        if (updateDate.getTrangThai().equals(Contains.DADUYET_LDCC)) {
            this.updateKiemNghiemAndLayMauAndQuyetDinh(updateDate);
        }
        return updateDate;
    }

    private void updateKiemNghiemAndLayMauAndQuyetDinh(XhDgBbTinhKhoHdr proposal) {
        xhPhieuKnghiemCluongRepository.findById(proposal.getIdPhieuKiemNghiem()).ifPresent(qualityTest -> {
            qualityTest.setIdTinhKho(proposal.getId());
            qualityTest.setSoBbTinhKho(proposal.getSoBbTinhKho());
            qualityTest.setNgayLapTinhKho(proposal.getNgayLapBienBan());
            xhBbLayMauRepository.findById(qualityTest.getIdBbLayMau()).ifPresent(sampling -> {
                sampling.setIdTinhKho(proposal.getId());
                sampling.setSoBbTinhKho(proposal.getSoBbTinhKho());
                sampling.setNgayXuatDocKho(proposal.getNgayLapBienBan());
                xhBbLayMauRepository.save(sampling);
            });
            xhPhieuKnghiemCluongRepository.save(qualityTest);
        });
        xhQdGiaoNvXhDdiemRepository.findById(proposal.getIdKho()).ifPresent(decisionPoint -> {
            decisionPoint.setIdBbTinhKho(proposal.getId());
            decisionPoint.setSoBbTinhKho(proposal.getSoBbTinhKho());
            decisionPoint.setNgayLapBbTinhKho(proposal.getNgayLapBienBan());
            xhQdGiaoNvXhDdiemRepository.save(decisionPoint);
        });
    }

    public void export(CustomUserDetails currentUser, XhDgBbTinhKhoHdrReq request, HttpServletResponse response) throws Exception {
        request.getPaggingReq().setPage(0);
        request.getPaggingReq().setLimit(Integer.MAX_VALUE);
        Page<XhDgBbTinhKhoHdr> page = this.searchPage(currentUser, request);
        List<XhDgBbTinhKhoHdr> dataList = page.getContent();
        String title = "Danh sách biên bản tịnh kho hàng DTQG";
        String[] rowsName = new String[]{"STT", "Số QĐ giao NVXH", "Năm KH", "Thời hạn XH", "Điểm kho",
                "Ngăn/Lô kho", "Số BB tịnh kho", "Ngày lập BB tịnh kho", "Số phiếu KNCL", "Số bảng kê",
                "Số phiếu xuất kho", "Ngày xuất kho", "Trạng thái"};
        String fileName = "danh-sach-bien-ban-tinh-kho-hang-DTQG.xlsx";
        List<Object[]> excelDataList = new ArrayList<>();
        for (int i = 0; i < dataList.size(); i++) {
            XhDgBbTinhKhoHdr proposal = dataList.get(i);
            Object[] excelRow = new Object[rowsName.length];
            excelRow[0] = i;
            excelRow[1] = proposal.getSoQdNv();
            excelRow[2] = proposal.getNam();
            excelRow[3] = LocalDateTimeUtils.localDateToString(proposal.getThoiGianGiaoNhan());
            excelRow[4] = proposal.getTenDiemKho();
            excelRow[5] = proposal.getTenNganLoKho();
            excelRow[6] = proposal.getSoBbTinhKho();
            excelRow[7] = LocalDateTimeUtils.localDateToString(proposal.getNgayLapBienBan());
            excelRow[8] = proposal.getSoPhieuKiemNghiem();
            proposal.getChildren().forEach(proposalDetail -> {
                excelRow[9] = proposalDetail.getSoBangKeHang();
                excelRow[10] = proposalDetail.getSoPhieuXuatKho();
                excelRow[11] = LocalDateTimeUtils.localDateToString(proposalDetail.getNgayXuatKho());
            });
            excelRow[12] = proposal.getTenTrangThai();
            excelDataList.add(excelRow);
        }
        ExportExcel exportExcel = new ExportExcel(title, fileName, rowsName, excelDataList, response);
        exportExcel.export();
    }

    public ReportTemplateResponse preview(HashMap<String, Object> requestParams, CustomUserDetails currentUser) throws Exception {
        if (currentUser == null || requestParams == null) {
            throw new Exception("Bad request.");
        }
        try {
            String templateName = DataUtils.safeToString(requestParams.get("tenBaoCao"));
            String templatePath = "bandaugia/" + templateName;
            FileInputStream templateInputStream = new FileInputStream(baseReportFolder + templatePath);
            XhDgBbTinhKhoHdr reportDetail = this.detail(DataUtils.safeToLong(requestParams.get("id")));
            return docxToPdfConverter.convertDocxToPdf(templateInputStream, reportDetail);
        } catch (IOException | XDocReportException exception) {
            exception.printStackTrace();
        }
        return null;
    }
}