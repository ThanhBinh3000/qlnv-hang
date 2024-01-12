package com.tcdt.qlnvhang.service.xuathang.bantructiep.ktracluong.phieuktracluong;

import com.tcdt.qlnvhang.entities.xuathang.bantructiep.ktracluong.phieuktracluong.XhPhieuKtraCluongBttDtl;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.ktracluong.phieuktracluong.XhPhieuKtraCluongBttHdr;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.UserInfoRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.ktracluong.phieuktracluong.XhPhieuKtraCluongBttDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.ktracluong.phieuktracluong.XhPhieuKtraCluongBttHdrRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.ktracluong.phieuktracluong.XhPhieuKtraCluongBttDtlReq;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.ktracluong.phieuktracluong.XhPhieuKtraCluongBttHdrReq;
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
public class XhPhieuKtraCluongBttServiceImpl extends BaseServiceImpl {

    @Autowired
    private XhPhieuKtraCluongBttHdrRepository xhPhieuKtraCluongBttHdrRepository;
    @Autowired
    private XhPhieuKtraCluongBttDtlRepository xhPhieuKtraCluongBttDtlRepository;
    @Autowired
    private UserInfoRepository userInfoRepository;

    public Page<XhPhieuKtraCluongBttHdr> searchPage(CustomUserDetails currentUser, XhPhieuKtraCluongBttHdrReq request) throws Exception {
        if (currentUser.getUser().getCapDvi().equals(Contains.CAP_CUC)) {
            request.setDvql(currentUser.getDvql());
        } else if (currentUser.getUser().getCapDvi().equals(Contains.CAP_CHI_CUC)) {
            request.setTrangThai(Contains.DADUYET_LDC);
            request.setMaDviCon(currentUser.getDvql());
        }
        Pageable pageable = PageRequest.of(request.getPaggingReq().getPage(), request.getPaggingReq().getLimit());
        Page<XhPhieuKtraCluongBttHdr> searchResultPage = xhPhieuKtraCluongBttHdrRepository.searchPage(request, pageable);
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
    public XhPhieuKtraCluongBttHdr create(CustomUserDetails currentUser, XhPhieuKtraCluongBttHdrReq request) throws Exception {
        if (currentUser == null || request == null) {
            throw new Exception("Bad request.");
        }
        if (!StringUtils.isEmpty(request.getSoPhieuKiemNghiem()) && xhPhieuKtraCluongBttHdrRepository.existsBySoPhieuKiemNghiem(request.getSoPhieuKiemNghiem())) {
            throw new Exception("Số phiếu kiểm nghiệm chất lượng " + request.getSoPhieuKiemNghiem() + " đã tồn tại");
        }
        XhPhieuKtraCluongBttHdr newData = new XhPhieuKtraCluongBttHdr();
        BeanUtils.copyProperties(request, newData);
        newData.setMaDvi(currentUser.getDvql());
        newData.setNgayTao(LocalDate.now());
        newData.setNguoiTaoId(currentUser.getUser().getId());
        newData.setIdKtvBaoQuan(currentUser.getUser().getId());
        newData.setId(Long.parseLong(newData.getSoPhieuKiemNghiem().split("/")[0]));
        newData.setTrangThai(Contains.DU_THAO);
        XhPhieuKtraCluongBttHdr createdRecord = xhPhieuKtraCluongBttHdrRepository.save(newData);
        this.saveDetail(request, createdRecord.getId(), false);
        return createdRecord;
    }


    @Transactional
    public XhPhieuKtraCluongBttHdr update(CustomUserDetails currentUser, XhPhieuKtraCluongBttHdrReq request) throws Exception {
        if (currentUser == null || request == null || request.getId() == null) {
            throw new Exception("Bad request.");
        }
        XhPhieuKtraCluongBttHdr existingData = xhPhieuKtraCluongBttHdrRepository.findById(request.getId())
                .orElseThrow(() -> new Exception("Không tìm thấy dữ liệu cần sửa"));
        if (xhPhieuKtraCluongBttHdrRepository.existsBySoPhieuKiemNghiemAndIdNot(request.getSoPhieuKiemNghiem(), request.getId())) {
            throw new Exception("Số phiếu kiểm nghiệm chất lượng " + request.getSoPhieuKiemNghiem() + " đã tồn tại");
        }
        BeanUtils.copyProperties(request, existingData, "id", "maDvi", "idKtvBaoQuan");
        existingData.setNgaySua(LocalDate.now());
        existingData.setNguoiSuaId(currentUser.getUser().getId());
        XhPhieuKtraCluongBttHdr updatedData = xhPhieuKtraCluongBttHdrRepository.save(existingData);
        this.saveDetail(request, updatedData.getId(), true);
        return updatedData;
    }

    void saveDetail(XhPhieuKtraCluongBttHdrReq request, Long headerId, Boolean isCheckRequired) {
        xhPhieuKtraCluongBttDtlRepository.deleteAllByIdHdr(isCheckRequired ? headerId : null);
        for (XhPhieuKtraCluongBttDtlReq detailRequest : request.getChildren()) {
            XhPhieuKtraCluongBttDtl detail = new XhPhieuKtraCluongBttDtl();
            BeanUtils.copyProperties(detailRequest, detail, "id");
            detail.setId(null);
            detail.setIdHdr(headerId);
            xhPhieuKtraCluongBttDtlRepository.save(detail);
        }
    }

    public List<XhPhieuKtraCluongBttHdr> detail(List<Long> ids) throws Exception {
        if (DataUtils.isNullOrEmpty(ids)) {
            throw new Exception("Tham số không hợp lệ.");
        }
        List<XhPhieuKtraCluongBttHdr> resultList = xhPhieuKtraCluongBttHdrRepository.findByIdIn(ids);
        if (DataUtils.isNullOrEmpty(resultList)) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        Map<String, String> mapDmucVthh = getListDanhMucHangHoa();
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        Map<String, String> mapDmucHinhThuc = getListDanhMucChung("HINH_THUC_BAO_QUAN");
        for (XhPhieuKtraCluongBttHdr item : resultList) {
            List<XhPhieuKtraCluongBttDtl> detailList = xhPhieuKtraCluongBttDtlRepository.findAllByIdHdr(item.getId());
            item.setChildren(detailList != null && !detailList.isEmpty() ? detailList : Collections.emptyList());
            item.setMapDmucDvi(mapDmucDvi);
            item.setMapDmucVthh(mapDmucVthh);
            item.setMapDmucHinhThuc(mapDmucHinhThuc);
            item.setTrangThai(item.getTrangThai());
            this.setFullNameIfNotNull(item.getIdThuKho(), item::setTenThuKho);
            this.setFullNameIfNotNull(item.getIdKtvBaoQuan(), item::setTenKtvBaoQuan);
            this.setFullNameIfNotNull(item.getIdLanhDaoCuc(), item::setTenLanhDaoCuc);
            this.setFullNameIfNotNull(item.getIdTphongKtvBaoQuan(), item::setTenTphongKtvBaoQuan);
        }
        return resultList;
    }

    private void setFullNameIfNotNull(Long userId, Consumer<String> fullNameSetter) {
        if (userId != null) {
            userInfoRepository.findById(userId).ifPresent(userInfo -> fullNameSetter.accept(userInfo.getFullName()));
        }
    }

    public XhPhieuKtraCluongBttHdr detail(Long id) throws Exception {
        if (id == null) {
            throw new Exception("Tham số không hợp lệ.");
        }
        List<XhPhieuKtraCluongBttHdr> details = detail(Collections.singletonList(id));
        return details.isEmpty() ? null : details.get(0);
    }

    @Transactional
    public void delete(IdSearchReq idSearchReq) throws Exception {
        if (idSearchReq == null || idSearchReq.getId() == null) {
            throw new Exception("Bad request.");
        }
        XhPhieuKtraCluongBttHdr proposalData = xhPhieuKtraCluongBttHdrRepository.findById(idSearchReq.getId())
                .orElseThrow(() -> new Exception("Bản ghi không tồn tại"));
        List<String> allowedStatus = Arrays.asList(Contains.DUTHAO, Contains.TU_CHOI_TP, Contains.TUCHOI_LDC);
        if (!allowedStatus.contains(proposalData.getTrangThai())) {
            throw new Exception("Chỉ thực hiện xóa với phiếu kiểm nghiệm chất lượng ở trạng thái bản nháp hoặc từ chối");
        }
        xhPhieuKtraCluongBttDtlRepository.deleteAllByIdHdr(proposalData.getId());
        xhPhieuKtraCluongBttHdrRepository.delete(proposalData);
    }

    public XhPhieuKtraCluongBttHdr approve(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {
        if (currentUser == null || StringUtils.isEmpty(statusReq.getId())) {
            throw new Exception("Bad request.");
        }
        XhPhieuKtraCluongBttHdr proposal = xhPhieuKtraCluongBttHdrRepository.findById(Long.valueOf(statusReq.getId()))
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
                proposal.setNgayPduyet(LocalDate.now());
                proposal.setLyDoTuChoi(statusReq.getLyDoTuChoi());
                break;
            case Contains.CHODUYET_LDC + Contains.CHODUYET_TP:
                proposal.setNguoiPduyetId(currentUser.getUser().getId());
                proposal.setNgayPduyet(LocalDate.now());
                proposal.setIdTphongKtvBaoQuan(currentUser.getUser().getId());
                break;
            case Contains.DADUYET_LDC + Contains.CHODUYET_LDC:
                proposal.setNguoiPduyetId(currentUser.getUser().getId());
                proposal.setNgayPduyet(LocalDate.now());
                proposal.setIdLanhDaoCuc(currentUser.getUser().getId());
                break;
            default:
                throw new Exception("Phê duyệt không thành công");
        }
        proposal.setTrangThai(statusReq.getTrangThai());
        return xhPhieuKtraCluongBttHdrRepository.save(proposal);
    }

    public void export(CustomUserDetails currentUser, XhPhieuKtraCluongBttHdrReq request, HttpServletResponse response) throws Exception {
        request.getPaggingReq().setPage(0);
        request.getPaggingReq().setLimit(Integer.MAX_VALUE);
        Page<XhPhieuKtraCluongBttHdr> page = this.searchPage(currentUser, request);
        List<XhPhieuKtraCluongBttHdr> dataList = page.getContent();
        String title = "Danh sách phiếu kiểm nghiệm chất lượng hàng DTQG";
        String[] rowsName = new String[]{"STT", "Số QĐ giao NVXH", "Năm KH", "Thời hạn XH", "Điểm kho",
                "Ngăn/Lô kho", "Số phiếu KNCL", "Ngày kiểm nghiệm", "Số BB LM/BGM", "Ngày lấy mẫu",
                "Số BB tịnh kho", "Ngày lập BB tịnh kho", "Trạng thái"};
        String fileName = "danh-sach-phieu-kiem-nghiem-chat-luong-hang-DTQG.xlsx";
        List<Object[]> excelDataList = new ArrayList<>();
        for (int i = 0; i < dataList.size(); i++) {
            XhPhieuKtraCluongBttHdr proposal = dataList.get(i);
            Object[] excelRow = new Object[rowsName.length];
            excelRow[0] = i;
            excelRow[1] = proposal.getSoQdNv();
            excelRow[2] = proposal.getNamKh();
            excelRow[3] = LocalDateTimeUtils.localDateToString(proposal.getTgianGiaoNhan());
            excelRow[4] = proposal.getTenDiemKho();
            excelRow[5] = proposal.getTenNganLoKho();
            excelRow[6] = proposal.getSoPhieuKiemNghiem();
            excelRow[7] = LocalDateTimeUtils.localDateToString(proposal.getNgayKiemNghiemMau());
            excelRow[8] = proposal.getSoBbLayMau();
            excelRow[9] = LocalDateTimeUtils.localDateToString(proposal.getNgayLayMau());
            excelRow[10] = proposal.getSoBbTinhKho();
            excelRow[11] = LocalDateTimeUtils.localDateToString(proposal.getNgayLapTinhKho());
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
            String templatePath = "bantructiep/" + templateName;
            FileInputStream templateInputStream = new FileInputStream(baseReportFolder + templatePath);
            XhPhieuKtraCluongBttHdr reportDetail = this.detail(DataUtils.safeToLong(requestParams.get("id")));
            return docxToPdfConverter.convertDocxToPdf(templateInputStream, reportDetail);
        } catch (IOException | XDocReportException exception) {
            exception.printStackTrace();
        }
        return null;
    }
}