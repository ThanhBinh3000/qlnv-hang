package com.tcdt.qlnvhang.service.xuathang.daugia.xuatkho;

import com.tcdt.qlnvhang.entities.xuathang.daugia.xuatkho.XhDgBangKeDtl;
import com.tcdt.qlnvhang.entities.xuathang.daugia.xuatkho.XhDgBangKeHdr;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.UserInfoRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.xuatkho.XhDgBangKeDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.xuatkho.XhDgBangKeHdrRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.xuatkho.XhDgPhieuXuatKhoRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.xuathang.daugia.xuatkho.XhDgBangKeReq;
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
public class XhDgBangKeService extends BaseServiceImpl {

    @Autowired
    private XhDgBangKeHdrRepository xhDgBangKeHdrRepository;
    @Autowired
    private XhDgBangKeDtlRepository xhDgBangKeDtlRepository;
    @Autowired
    private XhDgPhieuXuatKhoRepository xhDgPhieuXuatKhoRepository;
    @Autowired
    private UserInfoRepository userInfoRepository;

    public Page<XhDgBangKeHdr> searchPage(CustomUserDetails currentUser, XhDgBangKeReq request) throws Exception {
        if (currentUser.getUser().getCapDvi().equals(Contains.CAP_CUC)) {
            request.setTrangThai(Contains.DADUYET_LDCC);
            request.setMaDviCha(currentUser.getDvql());
        } else if (currentUser.getUser().getCapDvi().equals(Contains.CAP_CHI_CUC)) {
            request.setDvql(currentUser.getDvql());
        }
        Pageable pageable = PageRequest.of(request.getPaggingReq().getPage(), request.getPaggingReq().getLimit());
        Page<XhDgBangKeHdr> searchResultPage = xhDgBangKeHdrRepository.searchPage(request, pageable);
        Map<String, String> mapDmucVthh = getListDanhMucHangHoa();
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        searchResultPage.getContent().forEach(data -> {
            try {
                data.setMapDmucDvi(mapDmucDvi);
                data.setMapDmucVthh(mapDmucVthh);
                data.setTrangThai(data.getTrangThai());
            } catch (Exception exception) {
                throw new RuntimeException(exception);
            }
        });
        return searchResultPage;
    }

    @Transactional
    public XhDgBangKeHdr create(CustomUserDetails currentUser, XhDgBangKeReq request) throws Exception {
        if (currentUser == null) {
            throw new Exception("Bad request.");
        }
        if (!StringUtils.isEmpty(request.getSoBangKeHang()) && xhDgBangKeHdrRepository.existsBySoBangKeHang(request.getSoBangKeHang())) {
            throw new Exception("Số bảng cân kê hàng " + request.getSoBangKeHang() + " đã tồn tại");
        }
        XhDgBangKeHdr newData = new XhDgBangKeHdr();
        BeanUtils.copyProperties(request, newData);
        newData.setMaDvi(currentUser.getDvql());
        newData.setNgayTao(LocalDate.now());
        newData.setNguoiTaoId(currentUser.getUser().getId());
        newData.setIdThuKho(currentUser.getUser().getId());
        newData.setId(Long.parseLong(newData.getSoBangKeHang().split("/")[0]));
        newData.setTrangThai(Contains.DU_THAO);
        XhDgBangKeHdr createdRecord = xhDgBangKeHdrRepository.save(newData);
        xhDgPhieuXuatKhoRepository.findById(createdRecord.getIdPhieuXuatKho()).ifPresent(shipment -> {
            shipment.setIdBangKeHang(createdRecord.getId());
            xhDgPhieuXuatKhoRepository.save(shipment);
        });
        this.saveDetail(request, createdRecord.getId(), false);
        return createdRecord;
    }


    @Transactional
    public XhDgBangKeHdr update(CustomUserDetails currentUser, XhDgBangKeReq request) throws Exception {
        if (currentUser == null || request == null || request.getId() == null) {
            throw new Exception("Bad request.");
        }
        XhDgBangKeHdr existingData = xhDgBangKeHdrRepository.findById(request.getId())
                .orElseThrow(() -> new Exception("Không tìm thấy dữ liệu cần sửa"));
        if (xhDgBangKeHdrRepository.existsBySoBangKeHangAndIdNot(request.getSoBangKeHang(), request.getId())) {
            throw new Exception("Số phiếu xuất kho " + request.getSoPhieuXuatKho() + " đã tồn tại");
        }
        BeanUtils.copyProperties(request, existingData, "id", "maDvi", "idThuKho");
        existingData.setNgaySua(LocalDate.now());
        existingData.setNguoiSuaId(currentUser.getUser().getId());
        XhDgBangKeHdr updatedData = xhDgBangKeHdrRepository.save(existingData);
        xhDgPhieuXuatKhoRepository.findById(updatedData.getIdPhieuXuatKho()).ifPresent(shipment -> {
            shipment.setIdBangKeHang(updatedData.getId());
            xhDgPhieuXuatKhoRepository.save(shipment);
        });
        this.saveDetail(request, updatedData.getId(), true);
        return updatedData;
    }

    void saveDetail(XhDgBangKeReq request, Long headerId, Boolean isCheckRequired) {
        xhDgBangKeDtlRepository.deleteAllByIdHdr(isCheckRequired ? headerId : null);
        for (XhDgBangKeDtl detailRequest : request.getChildren()) {
            XhDgBangKeDtl detail = new XhDgBangKeDtl();
            BeanUtils.copyProperties(detailRequest, detail, "id");
            detail.setId(null);
            detail.setIdHdr(headerId);
            xhDgBangKeDtlRepository.save(detail);
        }
    }

    public List<XhDgBangKeHdr> detail(List<Long> ids) throws Exception {
        if (DataUtils.isNullOrEmpty(ids)) {
            throw new Exception("Tham số không hợp lệ.");
        }
        List<XhDgBangKeHdr> resultList = xhDgBangKeHdrRepository.findByIdIn(ids);
        if (DataUtils.isNullOrEmpty(resultList)) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        Map<String, String> mapDmucVthh = getListDanhMucHangHoa();
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        Map<String, String> mapDmucLoaiXuat = getListDanhMucChung("LOAI_HINH_NHAP_XUAT");
        Map<String, String> mapDmucKieuXuat = getListDanhMucChung("KIEU_NHAP_XUAT");
        for (XhDgBangKeHdr item : resultList) {
            List<XhDgBangKeDtl> detailList = xhDgBangKeDtlRepository.findAllByIdHdr(item.getId());
            item.setChildren(detailList != null && !detailList.isEmpty() ? detailList : Collections.emptyList());
            item.setMapDmucDvi(mapDmucDvi);
            item.setMapDmucVthh(mapDmucVthh);
            item.setMapDmucLoaiXuat(mapDmucLoaiXuat);
            item.setMapDmucKieuXuat(mapDmucKieuXuat);
            item.setTrangThai(item.getTrangThai());
            this.setFullNameIfNotNull(item.getIdThuKho(), item::setTenThuKho);
            this.setFullNameIfNotNull(item.getIdLanhDaoChiCuc(), item::setTenLanhDaoChiCuc);
        }
        return resultList;
    }

    private void setFullNameIfNotNull(Long userId, Consumer<String> fullNameSetter) {
        if (userId != null) {
            userInfoRepository.findById(userId).ifPresent(userInfo -> fullNameSetter.accept(userInfo.getFullName()));
        }
    }

    public XhDgBangKeHdr detail(Long id) throws Exception {
        if (id == null) {
            throw new Exception("Tham số không hợp lệ.");
        }
        List<XhDgBangKeHdr> details = detail(Collections.singletonList(id));
        return details.isEmpty() ? null : details.get(0);
    }

    @Transactional
    public void delete(IdSearchReq idSearchReq) throws Exception {
        if (idSearchReq == null || idSearchReq.getId() == null) {
            throw new Exception("Bad request.");
        }
        XhDgBangKeHdr proposalData = xhDgBangKeHdrRepository.findById(idSearchReq.getId())
                .orElseThrow(() -> new Exception("Bản ghi không tồn tại"));
        List<String> allowedStatus = Arrays.asList(Contains.DUTHAO, Contains.TUCHOI_LDCC);
        if (!allowedStatus.contains(proposalData.getTrangThai())) {
            throw new Exception("Chỉ thực hiện xóa với bảng kê cân hàng ở trạng thái bản nháp hoặc từ chối");
        }
        xhDgBangKeDtlRepository.deleteAllByIdHdr(proposalData.getId());
        xhDgBangKeHdrRepository.delete(proposalData);
    }


    public XhDgBangKeHdr approve(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {
        if (currentUser == null || StringUtils.isEmpty(statusReq.getId())) {
            throw new Exception("Bad request.");
        }
        XhDgBangKeHdr proposal = xhDgBangKeHdrRepository.findById(Long.valueOf(statusReq.getId()))
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
        XhDgBangKeHdr updateData = xhDgBangKeHdrRepository.save(proposal);
        if (updateData.getTrangThai().equals(Contains.DADUYET_LDCC)) {
            xhDgPhieuXuatKhoRepository.findById(proposal.getIdPhieuXuatKho()).ifPresent(shipment -> {
                shipment.setIdBangKeHang(updateData.getId());
                shipment.setSoBangKeHang(updateData.getSoBangKeHang());
                xhDgPhieuXuatKhoRepository.save(shipment);
            });
        }
        return updateData;
    }

    public void export(CustomUserDetails currentUser, XhDgBangKeReq request, HttpServletResponse response) throws Exception {
        request.getPaggingReq().setPage(0);
        request.getPaggingReq().setLimit(Integer.MAX_VALUE);
        Page<XhDgBangKeHdr> page = this.searchPage(currentUser, request);
        List<XhDgBangKeHdr> dataList = page.getContent();
        String title = "Danh sách bảng kê cân hàng DTQG";
        String[] rowsName = new String[]{"STT", "Số QĐ giao NVXH", "Năm KH", "Thời hạn XH trước ngày", "Điểm kho",
                "Lô kho", "Số phiếu KNCL", "Ngày giám định", "Số bảng kê", "Số phiếu xuất kho",
                "Ngày tạo bảng kê", "Trạng thái"};
        String fileName = "danh-sach-bang-ke-can-hang-DTQG.xlsx";
        List<Object[]> excelDataList = new ArrayList<>();
        for (int i = 0; i < dataList.size(); i++) {
            XhDgBangKeHdr proposal = dataList.get(i);
            Object[] excelRow = new Object[rowsName.length];
            excelRow[0] = i;
            excelRow[1] = proposal.getSoQdNv();
            excelRow[2] = proposal.getNam();
            excelRow[3] = LocalDateTimeUtils.localDateToString(proposal.getThoiGianGiaoNhan());
            excelRow[4] = proposal.getTenDiemKho();
            excelRow[5] = proposal.getTenNganLoKho();
            excelRow[6] = proposal.getSoPhieuKiemNghiem();
            excelRow[7] = LocalDateTimeUtils.localDateToString(proposal.getNgayKiemNghiemMau());
            excelRow[8] = proposal.getSoBangKeHang();
            excelRow[9] = proposal.getSoPhieuXuatKho();
            excelRow[10] = LocalDateTimeUtils.localDateToString(proposal.getNgayLapBangKe());
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
            XhDgBangKeHdr reportDetail = this.detail(DataUtils.safeToLong(requestParams.get("id")));
            return docxToPdfConverter.convertDocxToPdf(templateInputStream, reportDetail);
        } catch (IOException | XDocReportException exception) {
            exception.printStackTrace();
        }
        return null;
    }
}