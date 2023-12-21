package com.tcdt.qlnvhang.service.xuathang.daugia.ktracluong.kiemnghiemcl;

import com.tcdt.qlnvhang.entities.xuathang.daugia.ktracluong.phieukiemnghiemcl.XhPhieuKnghiemCluong;
import com.tcdt.qlnvhang.entities.xuathang.daugia.ktracluong.phieukiemnghiemcl.XhPhieuKnghiemCluongCt;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.UserInfoRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.ktracluong.bienbanlaymau.XhBbLayMauRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.ktracluong.kiemnghiemcl.XhPhieuKnghiemCluongCtRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.ktracluong.kiemnghiemcl.XhPhieuKnghiemCluongRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.xuathang.phieukiemnghiemchatluong.XhPhieuKnghiemCluongCtReq;
import com.tcdt.qlnvhang.request.xuathang.phieukiemnghiemchatluong.XhPhieuKnghiemCluongReq;
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
import java.util.stream.Collectors;

@Service
public class XhPhieuKnghiemCluongServiceImpl extends BaseServiceImpl {

    @Autowired
    private XhPhieuKnghiemCluongRepository xhPhieuKnghiemCluongRepository;
    @Autowired
    private XhPhieuKnghiemCluongCtRepository xhPhieuKnghiemCluongCtRepository;
    @Autowired
    private UserInfoRepository userInfoRepository;

    public Page<XhPhieuKnghiemCluong> searchPage(CustomUserDetails currentUser, XhPhieuKnghiemCluongReq request) throws Exception {
        if (currentUser.getUser().getCapDvi().equals(Contains.CAP_CUC)) {
            request.setDvql(currentUser.getDvql());
        } else if (currentUser.getUser().getCapDvi().equals(Contains.CAP_CHI_CUC)) {
            request.setTrangThai(Contains.DADUYET_LDC);
            request.setMaDviCon(currentUser.getDvql());
        }
        Pageable pageable = PageRequest.of(request.getPaggingReq().getPage(), request.getPaggingReq().getLimit());
        Page<XhPhieuKnghiemCluong> searchResultPage = xhPhieuKnghiemCluongRepository.searchPage(request, pageable);
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
    public XhPhieuKnghiemCluong create(CustomUserDetails currentUser, XhPhieuKnghiemCluongReq request) throws Exception {
        if (currentUser == null || request == null) {
            throw new Exception("Bad request.");
        }
        if (!StringUtils.isEmpty(request.getSoPhieuKiemNghiem()) && xhPhieuKnghiemCluongRepository.existsBySoPhieuKiemNghiem(request.getSoPhieuKiemNghiem())) {
            throw new Exception("Số phiếu kiểm nghiệm chất lượng " + request.getSoPhieuKiemNghiem() + " đã tồn tại");
        }
        XhPhieuKnghiemCluong newData = new XhPhieuKnghiemCluong();
        BeanUtils.copyProperties(request, newData);
        newData.setMaDvi(currentUser.getDvql());
        newData.setNgayTao(LocalDate.now());
        newData.setNguoiTaoId(currentUser.getUser().getId());
        newData.setIdNguoiKiemNghiem(currentUser.getUser().getId());
        newData.setId(Long.parseLong(newData.getSoPhieuKiemNghiem().split("/")[0]));
        newData.setTrangThai(Contains.DU_THAO);
        XhPhieuKnghiemCluong createdRecord = xhPhieuKnghiemCluongRepository.save(newData);
        this.saveDetail(request, createdRecord.getId(), false);
        return createdRecord;
    }

    @Transactional
    public XhPhieuKnghiemCluong update(CustomUserDetails currentUser, XhPhieuKnghiemCluongReq request) throws Exception {
        if (currentUser == null || request == null || request.getId() == null) {
            throw new Exception("Bad request.");
        }
        XhPhieuKnghiemCluong existingData = xhPhieuKnghiemCluongRepository.findById(request.getId())
                .orElseThrow(() -> new Exception("Không tìm thấy dữ liệu cần sửa"));
        if (xhPhieuKnghiemCluongRepository.existsBySoPhieuKiemNghiemAndIdNot(request.getSoPhieuKiemNghiem(), request.getId())) {
            throw new Exception("Số phiếu kiểm nghiệm chất lượng " + request.getSoPhieuKiemNghiem() + " đã tồn tại");
        }
        BeanUtils.copyProperties(request, existingData, "id", "maDvi", "idNguoiKiemNghiem");
        existingData.setNgaySua(LocalDate.now());
        existingData.setNguoiSuaId(currentUser.getUser().getId());
        XhPhieuKnghiemCluong updatedData = xhPhieuKnghiemCluongRepository.save(existingData);
        this.saveDetail(request, updatedData.getId(), true);
        return updatedData;
    }

    void saveDetail(XhPhieuKnghiemCluongReq request, Long headerId, Boolean isCheckRequired) {
        xhPhieuKnghiemCluongCtRepository.deleteAllByIdHdr(isCheckRequired ? headerId : null);
        for (XhPhieuKnghiemCluongCtReq detailRequest : request.getChildren()) {
            XhPhieuKnghiemCluongCt detail = new XhPhieuKnghiemCluongCt();
            BeanUtils.copyProperties(detailRequest, detail, "id");
            detail.setId(null);
            detail.setIdHdr(headerId);
            xhPhieuKnghiemCluongCtRepository.save(detail);
        }
    }

    public List<XhPhieuKnghiemCluong> detail(List<Long> ids) throws Exception {
        if (DataUtils.isNullOrEmpty(ids)) {
            throw new Exception("Tham số không hợp lệ.");
        }
        List<XhPhieuKnghiemCluong> resultList = xhPhieuKnghiemCluongRepository.findByIdIn(ids);
        if (DataUtils.isNullOrEmpty(resultList)) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        Map<String, String> mapDmucVthh = getListDanhMucHangHoa();
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        Map<String, String> mapDmucHinhThuc = getListDanhMucChung("HINH_THUC_BAO_QUAN");
        for (XhPhieuKnghiemCluong item : resultList) {
            List<XhPhieuKnghiemCluongCt> detailList = xhPhieuKnghiemCluongCtRepository.findAllByIdHdr(item.getId());
            item.setChildren(detailList != null && !detailList.isEmpty() ? detailList : Collections.emptyList());
            item.setMapDmucDvi(mapDmucDvi);
            item.setMapDmucVthh(mapDmucVthh);
            item.setMapDmucHinhThuc(mapDmucHinhThuc);
            this.setFullNameIfNotNull(item.getIdThuKho(), item::setTenThuKho);
            this.setFullNameIfNotNull(item.getIdNguoiKiemNghiem(), item::setTenNguoiKiemNghiem);
            this.setFullNameIfNotNull(item.getIdTruongPhongKtvbq(), item::setTenTruongPhongKtvbq);
            this.setFullNameIfNotNull(item.getIdLanhDaoCuc(), item::setTenLanhDaoCuc);
        }
        return resultList;
    }

    private void setFullNameIfNotNull(Long userId, Consumer<String> fullNameSetter) {
        if (userId != null) {
            userInfoRepository.findById(userId).ifPresent(userInfo -> fullNameSetter.accept(userInfo.getFullName()));
        }
    }

    public XhPhieuKnghiemCluong detail(Long id) throws Exception {
        if (id == null) {
            throw new Exception("Tham số không hợp lệ.");
        }
        List<XhPhieuKnghiemCluong> details = detail(Collections.singletonList(id));
        return details.isEmpty() ? null : details.get(0);
    }

    @Transactional
    public void delete(IdSearchReq idSearchReq) throws Exception {
        if (idSearchReq == null || idSearchReq.getId() == null) {
            throw new Exception("Bad request.");
        }
        XhPhieuKnghiemCluong proposalData = xhPhieuKnghiemCluongRepository.findById(idSearchReq.getId())
                .orElseThrow(() -> new Exception("Bản ghi không tồn tại"));
        List<String> allowedStatus = Arrays.asList(Contains.DUTHAO, Contains.TU_CHOI_TP, Contains.TUCHOI_LDC);
        if (!allowedStatus.contains(proposalData.getTrangThai())) {
            throw new Exception("Chỉ thực hiện xóa với phiếu kiểm nghiệm chất lượng ở trạng thái bản nháp hoặc từ chối");
        }
        xhPhieuKnghiemCluongCtRepository.deleteAllByIdHdr(proposalData.getId());
        xhPhieuKnghiemCluongRepository.delete(proposalData);
    }

    @Transactional
    public void deleteMulti(IdSearchReq idSearchReq) throws Exception {
        if (idSearchReq == null || idSearchReq.getIdList() == null) {
            throw new Exception("Bad request.");
        }
        List<XhPhieuKnghiemCluong> proposalList = xhPhieuKnghiemCluongRepository.findAllByIdIn(idSearchReq.getIdList());
        if (proposalList.isEmpty()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        boolean isValidToDelete = proposalList.stream().allMatch(proposal ->
                proposal.getTrangThai().equals(Contains.DUTHAO) ||
                        proposal.getTrangThai().equals(Contains.TU_CHOI_TP) ||
                        proposal.getTrangThai().equals(Contains.TUCHOI_LDC));
        if (!isValidToDelete) {
            throw new Exception("Chỉ thực hiện xóa với phiếu kiểm nghiệm chất lượng ở trạng thái bản nháp hoặc từ chối");
        }
        List<Long> proposalIds = proposalList.stream().map(XhPhieuKnghiemCluong::getId).collect(Collectors.toList());
        List<XhPhieuKnghiemCluongCt> proposalDetailsList = xhPhieuKnghiemCluongCtRepository.findByIdHdrIn(proposalIds);
        xhPhieuKnghiemCluongCtRepository.deleteAll(proposalDetailsList);
        xhPhieuKnghiemCluongRepository.deleteAll(proposalList);
    }

    public XhPhieuKnghiemCluong approve(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {
        if (currentUser == null || StringUtils.isEmpty(statusReq.getId())) {
            throw new Exception("Bad request.");
        }
        XhPhieuKnghiemCluong proposal = xhPhieuKnghiemCluongRepository.findById(Long.valueOf(statusReq.getId()))
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
                proposal.setIdTruongPhongKtvbq(currentUser.getUser().getId());
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
        return xhPhieuKnghiemCluongRepository.save(proposal);
    }

    public void export(CustomUserDetails currentUser, XhPhieuKnghiemCluongReq request, HttpServletResponse response) throws Exception {
        request.getPaggingReq().setPage(0);
        request.getPaggingReq().setLimit(Integer.MAX_VALUE);
        Page<XhPhieuKnghiemCluong> page = this.searchPage(currentUser, request);
        List<XhPhieuKnghiemCluong> dataList = page.getContent();
        String title = "Danh sách phiếu kiểm nghiệm hàng DTQG";
        String[] rowsName = new String[]{"STT", "Số QĐ giao NVXH", "Năm KH", "Thời hạn XH", "Điểm kho",
                "Ngăn/Lô kho", "Số phiếu KNCL", "Ngày kiểm nghiệm", "Số BB LM/BGM", "Ngày lấy mẫu",
                "Số BB tịnh kho", "Ngày lập BB tịnh kho", "Trạng thái"};
        String fileName = "danh-sach-phieu-kiem-nghiem-chat-luong-hang-DTQG.xlsx";
        List<Object[]> excelDataList = new ArrayList<>();
        for (int i = 0; i < dataList.size(); i++) {
            XhPhieuKnghiemCluong proposal = dataList.get(i);
            Object[] excelRow = new Object[rowsName.length];
            excelRow[0] = i;
            excelRow[1] = proposal.getSoQdNv();
            excelRow[2] = proposal.getNam();
            excelRow[3] = LocalDateTimeUtils.localDateToString(proposal.getTgianGiaoHang());
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
            String templatePath = "bandaugia/" + templateName;
            FileInputStream templateInputStream = new FileInputStream(baseReportFolder + templatePath);
            XhPhieuKnghiemCluong reportDetail = this.detail(DataUtils.safeToLong(requestParams.get("id")));
            return docxToPdfConverter.convertDocxToPdf(templateInputStream, reportDetail);
        } catch (IOException | XDocReportException exception) {
            exception.printStackTrace();
        }
        return null;
    }
}