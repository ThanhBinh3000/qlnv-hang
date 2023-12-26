package com.tcdt.qlnvhang.service.xuathang.bantructiep.xuatkho.bienbanhaodoi;

import com.tcdt.qlnvhang.entities.xuathang.bantructiep.xuatkho.bienbanhaodoi.XhBbHdoiBttDtl;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.xuatkho.bienbanhaodoi.XhBbHdoiBttHdr;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.UserInfoRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.ktracluong.bienbanlaymau.XhBbLayMauBttHdrRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.ktracluong.phieuktracluong.XhPhieuKtraCluongBttHdrRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.xuatkho.bienbanhaodoi.XhBbHdoiBttDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.xuatkho.bienbanhaodoi.XhBbHdoiBttHdrRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.xuatkho.bienbanhaodoi.XhBbHdoiBttDtlReq;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.xuatkho.bienbanhaodoi.XhBbHdoiBttHdrReq;
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
public class XhBbHdoiBttServiceIpml extends BaseServiceImpl {

    @Autowired
    private XhBbHdoiBttHdrRepository xhBbHdoiBttHdrRepository;
    @Autowired
    private XhBbHdoiBttDtlRepository xhBbHdoiBttDtlRepository;
    @Autowired
    private XhBbLayMauBttHdrRepository xhBbLayMauBttHdrRepository;
    @Autowired
    private XhPhieuKtraCluongBttHdrRepository xhPhieuKtraCluongBttHdrRepository;
    @Autowired
    private UserInfoRepository userInfoRepository;

    public Page<XhBbHdoiBttHdr> searchPage(CustomUserDetails currentUser, XhBbHdoiBttHdrReq request) throws Exception {
        if (currentUser.getUser().getCapDvi().equals(Contains.CAP_CUC)) {
            request.setTrangThai(Contains.DADUYET_LDCC);
            request.setMaDviCha(currentUser.getDvql());
        } else if (currentUser.getUser().getCapDvi().equals(Contains.CAP_CHI_CUC)) {
            request.setDvql(currentUser.getDvql());
        }
        Pageable pageable = PageRequest.of(request.getPaggingReq().getPage(), request.getPaggingReq().getLimit());
        Page<XhBbHdoiBttHdr> searchResultPage = xhBbHdoiBttHdrRepository.searchPage(request, pageable);
        Map<String, String> mapDmucVthh = getListDanhMucHangHoa();
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        searchResultPage.getContent().forEach(data -> {
            try {
                data.setMapDmucDvi(mapDmucDvi);
                data.setMapDmucVthh(mapDmucVthh);
                data.setTrangThai(data.getTrangThai());
                List<XhBbHdoiBttDtl> childList = xhBbHdoiBttDtlRepository.findAllByIdHdr(data.getId());
                data.setChildren(childList != null && !childList.isEmpty() ? childList : Collections.emptyList());
            } catch (Exception exception) {
                throw new RuntimeException(exception);
            }
        });
        return searchResultPage;
    }

    @Transactional
    public XhBbHdoiBttHdr create(CustomUserDetails currentUser, XhBbHdoiBttHdrReq request) throws Exception {
        if (currentUser == null || request == null) {
            throw new Exception("Bad request.");
        }
        if (!StringUtils.isEmpty(request.getSoBbHaoDoi()) && xhBbHdoiBttHdrRepository.existsBySoBbHaoDoi(request.getSoBbHaoDoi())) {
            throw new Exception("Số biên bản hao dôi " + request.getSoBbHaoDoi() + " đã tồn tại");
        }
        XhBbHdoiBttHdr newData = new XhBbHdoiBttHdr();
        BeanUtils.copyProperties(request, newData);
        newData.setMaDvi(currentUser.getDvql());
        newData.setNgayTao(LocalDate.now());
        newData.setNguoiTaoId(currentUser.getUser().getId());
        newData.setIdThuKho(currentUser.getUser().getId());
        newData.setId(Long.parseLong(newData.getSoBbHaoDoi().split("/")[0]));
        newData.setTrangThai(Contains.DU_THAO);
        XhBbHdoiBttHdr createdRecord = xhBbHdoiBttHdrRepository.save(newData);
        this.saveDetail(request, createdRecord.getId(), false);
        return createdRecord;
    }

    @Transactional
    public XhBbHdoiBttHdr update(CustomUserDetails currentUser, XhBbHdoiBttHdrReq request) throws Exception {
        if (currentUser == null || request == null || request.getId() == null) {
            throw new Exception("Bad request.");
        }
        XhBbHdoiBttHdr existingData = xhBbHdoiBttHdrRepository.findById(request.getId())
                .orElseThrow(() -> new Exception("Không tìm thấy dữ liệu cần sửa"));
        if (xhBbHdoiBttHdrRepository.existsBySoBbHaoDoiAndIdNot(request.getSoBbHaoDoi(), request.getId())) {
            throw new Exception("Số biên bản tịnh kho " + request.getSoBbHaoDoi() + " đã tồn tại");
        }
        BeanUtils.copyProperties(request, existingData, "id", "maDvi");
        existingData.setNgaySua(LocalDate.now());
        existingData.setNguoiSuaId(currentUser.getUser().getId());
        XhBbHdoiBttHdr updatedData = xhBbHdoiBttHdrRepository.save(existingData);
        this.saveDetail(request, updatedData.getId(), true);
        return updatedData;
    }

    void saveDetail(XhBbHdoiBttHdrReq request, Long headerId, Boolean isCheckRequired) {
        xhBbHdoiBttDtlRepository.deleteAllByIdHdr(isCheckRequired ? headerId : null);
        for (XhBbHdoiBttDtlReq detailRequest : request.getChildren()) {
            XhBbHdoiBttDtl detail = new XhBbHdoiBttDtl();
            BeanUtils.copyProperties(detailRequest, detail, "id");
            detail.setId(null);
            detail.setIdHdr(headerId);
            xhBbHdoiBttDtlRepository.save(detail);
        }
    }

    public List<XhBbHdoiBttHdr> detail(List<Long> ids) throws Exception {
        if (DataUtils.isNullOrEmpty(ids)) {
            throw new Exception("Tham số không hợp lệ.");
        }
        List<XhBbHdoiBttHdr> resultList = xhBbHdoiBttHdrRepository.findByIdIn(ids);
        if (DataUtils.isNullOrEmpty(resultList)) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        Map<String, String> mapDmucVthh = getListDanhMucHangHoa();
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        for (XhBbHdoiBttHdr item : resultList) {
            List<XhBbHdoiBttDtl> detailList = xhBbHdoiBttDtlRepository.findAllByIdHdr(item.getId());
            item.setChildren(detailList != null && !detailList.isEmpty() ? detailList : Collections.emptyList());
            item.setMapDmucDvi(mapDmucDvi);
            item.setMapDmucVthh(mapDmucVthh);
            item.setTrangThai(item.getTrangThai());
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

    public XhBbHdoiBttHdr detail(Long id) throws Exception {
        if (id == null) {
            throw new Exception("Tham số không hợp lệ.");
        }
        List<XhBbHdoiBttHdr> details = detail(Collections.singletonList(id));
        return details.isEmpty() ? null : details.get(0);
    }

    @Transactional
    public void delete(IdSearchReq idSearchReq) throws Exception {
        if (idSearchReq == null || idSearchReq.getId() == null) {
            throw new Exception("Bad request.");
        }
        XhBbHdoiBttHdr proposalData = xhBbHdoiBttHdrRepository.findById(idSearchReq.getId())
                .orElseThrow(() -> new Exception("Bản ghi không tồn tại"));
        List<String> allowedStatus = Arrays.asList(Contains.DUTHAO, Contains.TUCHOI_KTVBQ, Contains.TUCHOI_KT, Contains.TUCHOI_LDCC);
        if (!allowedStatus.contains(proposalData.getTrangThai())) {
            throw new Exception("Chỉ thực hiện xóa với biên bản hao dôi ở trạng thái bản nháp hoặc từ chối");
        }
        xhBbHdoiBttDtlRepository.deleteAllByIdHdr(proposalData.getId());
        xhBbHdoiBttHdrRepository.delete(proposalData);
    }

    public XhBbHdoiBttHdr approve(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {
        if (currentUser == null || StringUtils.isEmpty(statusReq.getId())) {
            throw new Exception("Bad request.");
        }
        XhBbHdoiBttHdr proposal = xhBbHdoiBttHdrRepository.findById(Long.valueOf(statusReq.getId()))
                .orElseThrow(() -> new Exception("Không tìm thấy dữ liệu"));
        String status = statusReq.getTrangThai() + proposal.getTrangThai();
        switch (status) {
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
        XhBbHdoiBttHdr updateDate = xhBbHdoiBttHdrRepository.save(proposal);
        if (updateDate.getTrangThai().equals(Contains.DADUYET_LDCC)) {
            this.updateKiemNghiemAndLayMauAndQuyetDinh(updateDate);
        }
        return updateDate;
    }

    private void updateKiemNghiemAndLayMauAndQuyetDinh(XhBbHdoiBttHdr proposal) {
        xhPhieuKtraCluongBttHdrRepository.findById(proposal.getIdPhieuKiemNghiem()).ifPresent(qualityTest -> {
            xhBbLayMauBttHdrRepository.findById(qualityTest.getIdBbLayMau()).ifPresent(sampling -> {
                sampling.setIdHaoDoi(proposal.getId());
                sampling.setSoBbHaoDoi(proposal.getSoBbHaoDoi());
                xhBbLayMauBttHdrRepository.save(sampling);
            });
        });
    }

    public void export(CustomUserDetails currentUser, XhBbHdoiBttHdrReq req, HttpServletResponse response) throws Exception {
        req.getPaggingReq().setPage(0);
        req.getPaggingReq().setLimit(Integer.MAX_VALUE);
        Page<XhBbHdoiBttHdr> page = this.searchPage(currentUser, req);
        List<XhBbHdoiBttHdr> dataList = page.getContent();
        String title = "Danh sách biên bản hao dôi hàng DTQG";
        String[] rowsName = new String[]{"STT", "Số QĐ giao NVXH", "Năm KH", "Thời hạn XH", "Điểm kho",
                "Ngăn/Lô kho", "Số BB hao dôi", "Ngày lập BB hao dôi", "Số BB tịnh kho", "Số phiếu KNCL", "Số bảng kê",
                "Số phiếu xuất kho", "Ngày xuất kho", "Trạng thái"};
        String fileName = "danh-sach-bien-ban-hao-doi-hang-DTQG.xlsx";
        List<Object[]> excelDataList = new ArrayList<>();
        for (int i = 0; i < dataList.size(); i++) {
            XhBbHdoiBttHdr proposal = dataList.get(i);
            Object[] excelRow = new Object[rowsName.length];
            excelRow[0] = i;
            excelRow[1] = proposal.getSoQdNv();
            excelRow[2] = proposal.getNamKh();
            excelRow[3] = LocalDateTimeUtils.localDateToString(proposal.getTgianGiaoNhan());
            excelRow[4] = proposal.getTenDiemKho();
            excelRow[5] = proposal.getTenNganLoKho();
            excelRow[6] = proposal.getSoBbHaoDoi();
            excelRow[7] = LocalDateTimeUtils.localDateToString(proposal.getNgayLapBienBan());
            excelRow[8] = proposal.getSoBbTinhKho();
            excelRow[9] = proposal.getSoPhieuKiemNghiem();
            proposal.getChildren().forEach(proposalDetail -> {
                excelRow[10] = proposalDetail.getSoBangKeHang();
                excelRow[11] = proposalDetail.getSoPhieuXuatKho();
                excelRow[12] = LocalDateTimeUtils.localDateToString(proposalDetail.getNgayXuatKho());
            });
            excelRow[13] = proposal.getTenTrangThai();
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
            XhBbHdoiBttHdr reportDetail = this.detail(DataUtils.safeToLong(requestParams.get("id")));
            return docxToPdfConverter.convertDocxToPdf(templateInputStream, reportDetail);
        } catch (IOException | XDocReportException exception) {
            exception.printStackTrace();
        }
        return null;
    }
}