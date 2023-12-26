package com.tcdt.qlnvhang.service.xuathang.daugia.xuatkho;

import com.tcdt.qlnvhang.entities.xuathang.daugia.xuatkho.XhDgPhieuXuatKho;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.UserInfoRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.nhiemvuxuat.XhQdGiaoNvXhDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.nhiemvuxuat.XhQdGiaoNvXhRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.xuatkho.XhDgBangKeHdrRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.xuatkho.XhDgPhieuXuatKhoRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.xuathang.daugia.xuatkho.XhDgPhieuXuatKhoReq;
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
public class XhDgPhieuXuatKhoService extends BaseServiceImpl {

    @Autowired
    private XhDgPhieuXuatKhoRepository xhDgPhieuXuatKhoRepository;
    @Autowired
    private XhQdGiaoNvXhRepository xhQdGiaoNvXhRepository;
    @Autowired
    private XhQdGiaoNvXhDtlRepository xhQdGiaoNvXhDtlRepository;
    @Autowired
    private XhDgBangKeHdrRepository xhDgBangKeHdrRepository;
    @Autowired
    private UserInfoRepository userInfoRepository;

    public Page<XhDgPhieuXuatKho> searchPage(CustomUserDetails currentUser, XhDgPhieuXuatKhoReq request) throws Exception {
        if (currentUser.getUser().getCapDvi().equals(Contains.CAP_CUC)) {
            request.setTrangThai(Contains.DADUYET_LDCC);
            request.setMaDviCha(currentUser.getDvql());
        } else if (currentUser.getUser().getCapDvi().equals(Contains.CAP_CHI_CUC)) {
            request.setDvql(currentUser.getDvql());
        }
        Pageable pageable = PageRequest.of(request.getPaggingReq().getPage(), request.getPaggingReq().getLimit());
        Page<XhDgPhieuXuatKho> searchResultPage = xhDgPhieuXuatKhoRepository.searchPage(request, pageable);
        Map<String, String> mapDmucVthh = getListDanhMucHangHoa();
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        Map<String, String> mapDmucHinhThuc = getListDanhMucChung("HINH_THUC_BAO_QUAN");
        searchResultPage.getContent().forEach(data -> {
            try {
                data.setMapDmucDvi(mapDmucDvi);
                data.setMapDmucVthh(mapDmucVthh);
                data.setMapDmucHinhThuc(mapDmucHinhThuc);
                data.setTrangThai(data.getTrangThai());
            } catch (Exception exception) {
                throw new RuntimeException(exception);
            }
        });
        return searchResultPage;
    }

    @Transactional
    public XhDgPhieuXuatKho create(CustomUserDetails currentUser, XhDgPhieuXuatKhoReq request) throws Exception {
        if (currentUser == null || request == null) {
            throw new Exception("Bad request.");
        }
        if (!StringUtils.isEmpty(request.getSoPhieuXuatKho()) && xhDgPhieuXuatKhoRepository.existsBySoPhieuXuatKho(request.getSoPhieuXuatKho())) {
            throw new Exception("Số phiếu xuất kho " + request.getSoPhieuXuatKho() + " đã tồn tại");
        }
        XhDgPhieuXuatKho newData = new XhDgPhieuXuatKho();
        BeanUtils.copyProperties(request, newData);
        newData.setMaDvi(currentUser.getDvql());
        newData.setNgayTao(LocalDate.now());
        newData.setNguoiTaoId(currentUser.getUser().getId());
        newData.setIdThuKho(currentUser.getUser().getId());
        newData.setId(Long.parseLong(newData.getSoPhieuXuatKho().split("/")[0]));
        newData.setTrangThai(Contains.DU_THAO);
        XhDgPhieuXuatKho createdRecord = xhDgPhieuXuatKhoRepository.save(newData);
        if (createdRecord.getIdQdNv() != null && createdRecord.getIdQdNvDtl() != null) {
            xhQdGiaoNvXhRepository.findById(createdRecord.getIdQdNv()).ifPresent(decision -> {
                decision.setTrangThaiXh(Contains.DANG_THUC_HIEN);
                xhQdGiaoNvXhDtlRepository.findById(createdRecord.getIdQdNvDtl()).ifPresent(decisionDetail -> {
                    decisionDetail.setTrangThai(Contains.DANG_THUC_HIEN);
                    xhQdGiaoNvXhDtlRepository.save(decisionDetail);
                });
                xhQdGiaoNvXhRepository.save(decision);
            });
        }
        return createdRecord;
    }

    @Transactional
    public XhDgPhieuXuatKho update(CustomUserDetails currentUser, XhDgPhieuXuatKhoReq request) throws Exception {
        if (currentUser == null || request == null || request.getId() == null) {
            throw new Exception("Bad request.");
        }
        XhDgPhieuXuatKho existingData = xhDgPhieuXuatKhoRepository.findById(request.getId())
                .orElseThrow(() -> new Exception("Không tìm thấy dữ liệu cần sửa"));
        if (xhDgPhieuXuatKhoRepository.existsBySoPhieuXuatKhoAndIdNot(request.getSoPhieuXuatKho(), request.getId())) {
            throw new Exception("Số phiếu xuất kho " + request.getSoPhieuXuatKho() + " đã tồn tại");
        }
        BeanUtils.copyProperties(request, existingData, "id", "maDvi", "idThuKho");
        existingData.setNgaySua(LocalDate.now());
        existingData.setNguoiSuaId(currentUser.getUser().getId());
        XhDgPhieuXuatKho updatedData = xhDgPhieuXuatKhoRepository.save(existingData);
        xhDgBangKeHdrRepository.findById(updatedData.getIdBangKeHang()).ifPresent(shippingList -> {
            shippingList.setTenNguoiGiao(updatedData.getTenNguoiGiao());
            shippingList.setCmtNguoiGiao(updatedData.getCmtNguoiGiao());
            shippingList.setCongTyNguoiGiao(updatedData.getCongTyNguoiGiao());
            shippingList.setDiaChiNguoiGiao(updatedData.getDiaChiNguoiGiao());
            xhDgBangKeHdrRepository.save(shippingList);
        });
        return updatedData;
    }

    public List<XhDgPhieuXuatKho> detail(List<Long> ids) throws Exception {
        if (DataUtils.isNullOrEmpty(ids)) {
            throw new Exception("Tham số không hợp lệ.");
        }
        List<XhDgPhieuXuatKho> resultList = xhDgPhieuXuatKhoRepository.findByIdIn(ids);
        if (DataUtils.isNullOrEmpty(resultList)) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        Map<String, String> mapDmucVthh = getListDanhMucHangHoa();
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        Map<String, String> mapDmucKieuXuat = getListDanhMucChung("KIEU_NHAP_XUAT");
        Map<String, String> mapDmucLoaiXuat = getListDanhMucChung("LOAI_HINH_NHAP_XUAT");
        Map<String, String> mapDmucHinhThuc = getListDanhMucChung("HINH_THUC_BAO_QUAN");
        for (XhDgPhieuXuatKho item : resultList) {
            item.setMapDmucDvi(mapDmucDvi);
            item.setMapDmucVthh(mapDmucVthh);
            item.setMapDmucLoaiXuat(mapDmucLoaiXuat);
            item.setMapDmucKieuXuat(mapDmucKieuXuat);
            item.setMapDmucHinhThuc(mapDmucHinhThuc);
            item.setTrangThai(item.getTrangThai());
            this.setFullNameIfNotNull(item.getIdThuKho(), item::setTenThuKho);
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

    public XhDgPhieuXuatKho detail(Long id) throws Exception {
        if (id == null) {
            throw new Exception("Tham số không hợp lệ.");
        }
        List<XhDgPhieuXuatKho> details = detail(Collections.singletonList(id));
        return details.isEmpty() ? null : details.get(0);
    }

    @Transactional
    public void delete(IdSearchReq idSearchReq) throws Exception {
        if (idSearchReq == null || idSearchReq.getId() == null) {
            throw new Exception("Bad request.");
        }
        XhDgPhieuXuatKho proposalData = xhDgPhieuXuatKhoRepository.findById(idSearchReq.getId())
                .orElseThrow(() -> new Exception("Bản ghi không tồn tại"));
        List<String> allowedStatus = Arrays.asList(Contains.DUTHAO, Contains.TUCHOI_LDCC);
        if (!allowedStatus.contains(proposalData.getTrangThai())) {
            throw new Exception("Chỉ thực hiện xóa với phiếu xuất kho ở trạng thái bản nháp hoặc từ chối");
        }
        if (proposalData.getIdQdNvDtl() != null) {
            xhQdGiaoNvXhDtlRepository.findById(proposalData.getIdQdNvDtl()).ifPresent(decisionDetail -> {
                decisionDetail.setTrangThai(Contains.CHUA_THUC_HIEN);
                xhQdGiaoNvXhDtlRepository.save(decisionDetail);
            });
        }
        xhDgPhieuXuatKhoRepository.delete(proposalData);
    }

    public XhDgPhieuXuatKho approve(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {
        if (currentUser == null || StringUtils.isEmpty(statusReq.getId())) {
            throw new Exception("Bad request.");
        }
        XhDgPhieuXuatKho proposal = xhDgPhieuXuatKhoRepository.findById(Long.valueOf(statusReq.getId()))
                .orElseThrow(() -> new Exception("Không tìm thấy dữ liệu"));
        String statusCombination = statusReq.getTrangThai() + proposal.getTrangThai();
        switch (statusCombination) {
            case Contains.CHODUYET_LDCC + Contains.DUTHAO:
            case Contains.CHODUYET_LDCC + Contains.TUCHOI_LDCC:
                proposal.setNguoiGuiDuyetId(currentUser.getUser().getId());
                proposal.setNgayGuiDuyet(LocalDate.now());
                break;
            case Contains.TUCHOI_LDCC + Contains.CHODUYET_LDCC:
                proposal.setNguoiPduyetId(currentUser.getUser().getId());
                proposal.setNgayPduyet(LocalDate.now());
                proposal.setLyDoTuChoi(statusReq.getLyDoTuChoi());
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
        return xhDgPhieuXuatKhoRepository.save(proposal);
    }

    public void export(CustomUserDetails currentUser, XhDgPhieuXuatKhoReq request, HttpServletResponse response) throws Exception {
        request.getPaggingReq().setPage(0);
        request.getPaggingReq().setLimit(Integer.MAX_VALUE);
        Page<XhDgPhieuXuatKho> page = this.searchPage(currentUser, request);
        List<XhDgPhieuXuatKho> dataList = page.getContent();
        String title = "Danh sách phiếu xuất kho hàng DTQG";
        String[] rowsName = new String[]{"STT", "Số QĐ giao NVXH", "Năm KH", "Thời hạn XH", "Điểm kho",
                "Lô kho", "Số phiếu KNCL", "Ngày giám định", "Số phiếu xuất kho", "Số bảng kê",
                "Ngày xuất kho", "Trạng thái"};
        String fileName = "danh-sach-phieu-xuat-kho-hang-DTQG.xlsx";
        List<Object[]> excelDataList = new ArrayList<>();
        for (int i = 0; i < dataList.size(); i++) {
            XhDgPhieuXuatKho proposal = dataList.get(i);
            Object[] excelRow = new Object[rowsName.length];
            excelRow[0] = i;
            excelRow[1] = proposal.getSoQdNv();
            excelRow[2] = proposal.getNam();
            excelRow[3] = LocalDateTimeUtils.localDateToString(proposal.getThoiGianGiaoNhan());
            excelRow[4] = proposal.getTenDiemKho();
            excelRow[5] = proposal.getTenNganLoKho();
            excelRow[6] = proposal.getSoPhieuKiemNghiem();
            excelRow[7] = LocalDateTimeUtils.localDateToString(proposal.getNgayKiemNghiemMau());
            excelRow[8] = proposal.getSoPhieuXuatKho();
            excelRow[9] = proposal.getSoBangKeHang();
            excelRow[10] = LocalDateTimeUtils.localDateToString(proposal.getNgayLapPhieu());
            excelRow[11] = proposal.getTenTrangThai();
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
            XhDgPhieuXuatKho reportDetail = this.detail(DataUtils.safeToLong(requestParams.get("id")));
            Map<String, Map<String, Object>> mapDmucDvi = getListDanhMucDviObject(null, null, "01");
            xhQdGiaoNvXhRepository.findById(reportDetail.getIdQdNv()).ifPresent(quyetDinh -> {
                reportDetail.setMaDviCha(quyetDinh.getMaDvi());
                if (mapDmucDvi.containsKey((reportDetail.getMaDviCha()))) {
                    Map<String, Object> objDonVi = mapDmucDvi.get(reportDetail.getMaDviCha());
                    reportDetail.setTenDviCha(objDonVi.get("tenDvi").toString());
                }
            });
            return docxToPdfConverter.convertDocxToPdf(templateInputStream, reportDetail);
        } catch (IOException | XDocReportException exception) {
            exception.printStackTrace();
        }
        return null;
    }
}