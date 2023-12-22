package com.tcdt.qlnvhang.service.xuathang.daugia.ktracluong.bienbanlaymau;

import com.tcdt.qlnvhang.entities.xuathang.daugia.ktracluong.bienbanlaymau.XhBbLayMau;
import com.tcdt.qlnvhang.entities.xuathang.daugia.ktracluong.bienbanlaymau.XhBbLayMauCt;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.UserInfoRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.ktracluong.bienbanlaymau.XhBbLayMauCtRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.ktracluong.bienbanlaymau.XhBbLayMauRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.nhiemvuxuat.XhQdGiaoNvXhRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.bandaugia.bienbanlaymau.XhBbLayMauCtRequest;
import com.tcdt.qlnvhang.request.bandaugia.bienbanlaymau.XhBbLayMauRequest;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.*;
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
import org.springframework.util.CollectionUtils;
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
public class XhBbLayMauServiceImpl extends BaseServiceImpl {

    @Autowired
    private XhBbLayMauRepository xhBbLayMauRepository;
    @Autowired
    private XhBbLayMauCtRepository xhBbLayMauCtRepository;
    @Autowired
    private XhQdGiaoNvXhRepository xhQdGiaoNvXhRepository;
    @Autowired
    private UserInfoRepository userInfoRepository;

    public Page<XhBbLayMau> searchPage(CustomUserDetails currentUser, XhBbLayMauRequest request) throws Exception {
        if (currentUser.getUser().getCapDvi().equals(Contains.CAP_CUC)) {
            request.setTrangThai(Contains.DADUYET_LDCC);
            request.setMaDviCha(currentUser.getDvql());
        } else if (currentUser.getUser().getCapDvi().equals(Contains.CAP_CHI_CUC)) {
            request.setDvql(currentUser.getDvql());
        }
        Pageable pageable = PageRequest.of(request.getPaggingReq().getPage(), request.getPaggingReq().getLimit());
        Page<XhBbLayMau> searchResultPage = xhBbLayMauRepository.searchPage(request, pageable);
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
    public XhBbLayMau create(CustomUserDetails currentUser, XhBbLayMauRequest request) throws Exception {
        if (currentUser == null || request == null) {
            throw new Exception("Bad request.");
        }
        if (!StringUtils.isEmpty(request.getSoBbLayMau()) && xhBbLayMauRepository.existsBySoBbLayMau(request.getSoBbLayMau())) {
            throw new Exception("Số biên bản lấy mẫu " + request.getSoBbLayMau() + " đã tồn tại");
        }
        XhBbLayMau newData = new XhBbLayMau();
        BeanUtils.copyProperties(request, newData);
        newData.setMaDvi(currentUser.getDvql());
        newData.setNgayTao(LocalDate.now());
        newData.setNguoiTaoId(currentUser.getUser().getId());
        newData.setIdKtvBaoQuan(currentUser.getUser().getId());
        newData.setId(Long.parseLong(newData.getSoBbLayMau().split("/")[0]));
        newData.setTrangThai(Contains.DU_THAO);
        XhBbLayMau createdRecord = xhBbLayMauRepository.save(newData);
        this.saveDetail(request, createdRecord.getId(), false);
        return createdRecord;
    }

    @Transactional
    public XhBbLayMau update(CustomUserDetails currentUser, XhBbLayMauRequest request) throws Exception {
        if (currentUser == null || request == null || request.getId() == null) {
            throw new Exception("Bad request.");
        }
        XhBbLayMau existingData = xhBbLayMauRepository.findById(request.getId())
                .orElseThrow(() -> new Exception("Không tìm thấy dữ liệu cần sửa"));
        if (!StringUtils.isEmpty(request.getSoBbLayMau()) && xhBbLayMauRepository.existsBySoBbLayMauAndIdNot(request.getSoBbLayMau(), request.getId())) {
            throw new Exception("Số biên bản lấy mẫu " + request.getSoBbLayMau() + " đã tồn tại");
        }
        BeanUtils.copyProperties(request, existingData, "id", "maDvi", "idKtvBaoQuan");
        existingData.setNgaySua(LocalDate.now());
        existingData.setNguoiSuaId(currentUser.getUser().getId());
        XhBbLayMau updatedData = xhBbLayMauRepository.save(existingData);
        this.saveDetail(request, updatedData.getId(), true);
        return updatedData;
    }

    void saveDetail(XhBbLayMauRequest request, Long headerId, Boolean isCheckRequired) {
        xhBbLayMauCtRepository.deleteAllByIdHdr(isCheckRequired ? headerId : null);
        for (XhBbLayMauCtRequest detailRequest : request.getChildren()) {
            XhBbLayMauCt detail = new XhBbLayMauCt();
            BeanUtils.copyProperties(detailRequest, detail, "id");
            detail.setId(null);
            detail.setIdHdr(headerId);
            xhBbLayMauCtRepository.save(detail);
        }
    }

    public List<XhBbLayMau> detail(List<Long> ids) throws Exception {
        if (DataUtils.isNullOrEmpty(ids)) {
            throw new Exception("Tham số không hợp lệ.");
        }
        List<XhBbLayMau> resultList = xhBbLayMauRepository.findByIdIn(ids);
        if (DataUtils.isNullOrEmpty(resultList)) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        Map<String, String> mapDmucVthh = getListDanhMucHangHoa();
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        Map<String, String> mapDmucLoaiXuat = getListDanhMucChung("LOAI_HINH_NHAP_XUAT");
        Map<String, String> mapDmucKieuXuat = getListDanhMucChung("KIEU_NHAP_XUAT");
        for (XhBbLayMau item : resultList) {
            List<XhBbLayMauCt> detailList = xhBbLayMauCtRepository.findAllByIdHdr(item.getId());
            item.setChildren(detailList != null && !detailList.isEmpty() ? detailList : Collections.emptyList());
            item.setMapDmucDvi(mapDmucDvi);
            item.setMapDmucVthh(mapDmucVthh);
            item.setMapDmucLoaiXuat(mapDmucLoaiXuat);
            item.setMapDmucKieuXuat(mapDmucKieuXuat);
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

    public XhBbLayMau detail(Long id) throws Exception {
        if (id == null) {
            throw new Exception("Tham số không hợp lệ.");
        }
        List<XhBbLayMau> details = detail(Collections.singletonList(id));
        return details.isEmpty() ? null : details.get(0);
    }

    @Transactional
    public void delete(IdSearchReq idSearchReq) throws Exception {
        if (idSearchReq == null || idSearchReq.getId() == null) {
            throw new Exception("Bad request.");
        }
        XhBbLayMau proposalData = xhBbLayMauRepository.findById(idSearchReq.getId())
                .orElseThrow(() -> new Exception("Bản ghi không tồn tại"));
        List<String> allowedStatus = Arrays.asList(Contains.DUTHAO, Contains.TUCHOI_LDCC);
        if (!allowedStatus.contains(proposalData.getTrangThai())) {
            throw new Exception("Chỉ thực hiện xóa với biên bản lấy mẫu ở trạng thái bản nháp hoặc từ chối.");
        }
        xhBbLayMauCtRepository.deleteAllByIdHdr(proposalData.getId());
        xhBbLayMauRepository.delete(proposalData);
    }

    @Transactional
    public void deleteMulti(IdSearchReq idSearchReq) throws Exception {
        if (idSearchReq == null || idSearchReq.getIdList() == null) {
            throw new Exception("Bad request.");
        }
        List<XhBbLayMau> proposalList = xhBbLayMauRepository.findAllByIdIn(idSearchReq.getIdList());
        if (proposalList.isEmpty()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        boolean isValidToDelete = proposalList.stream().allMatch(proposal ->
                proposal.getTrangThai().equals(Contains.DUTHAO) ||
                        proposal.getTrangThai().equals(Contains.TUCHOI_LDCC));
        if (!isValidToDelete) {
            throw new Exception("Chỉ thực hiện xóa với biên bản lấy mẫu ở trạng thái bản nháp hoặc từ chối.");
        }
        List<Long> proposalIds = proposalList.stream().map(XhBbLayMau::getId).collect(Collectors.toList());
        List<XhBbLayMauCt> proposalDetailsList = xhBbLayMauCtRepository.findByIdHdrIn(proposalIds);
        xhBbLayMauCtRepository.deleteAll(proposalDetailsList);
        xhBbLayMauRepository.deleteAll(proposalList);
    }

    public XhBbLayMau approve(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {
        if (currentUser == null || StringUtils.isEmpty(statusReq.getId())) {
            throw new Exception("Bad request.");
        }
        XhBbLayMau proposal = xhBbLayMauRepository.findById(Long.valueOf(statusReq.getId()))
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
        return xhBbLayMauRepository.save(proposal);
    }

    public void export(CustomUserDetails currentUser, XhBbLayMauRequest request, HttpServletResponse response) throws Exception {
        request.getPaggingReq().setPage(0);
        request.getPaggingReq().setLimit(Integer.MAX_VALUE);
        Page<XhBbLayMau> page = this.searchPage(currentUser, request);
        List<XhBbLayMau> dataList = page.getContent();
        String title = "Danh sách biên bản lấy mẫu/bàn giao mẫu hàng DTQG";
        String[] rowsName;
        boolean isVattuType = dataList.stream().anyMatch(item -> item.getLoaiVthh().startsWith(Contains.LOAI_VTHH_VATTU));
        String[] commonRowsName = new String[]{"STT", "Số QĐ giao NVXH", "Năm KH", "Thời hạn XH", "Điểm kho", "Ngăn/Lô kho", "Số BB LM/BGM", "Ngày lấy mẫu", "Số BB tịnh kho", "Ngày xuất dốc kho"};
        if (isVattuType) {
            String[] vattuRowsName = Arrays.copyOf(commonRowsName, commonRowsName.length + 1);
            vattuRowsName[10] = "Trạng thái";
            rowsName = vattuRowsName;
        } else {
            String[] nonVattuRowsName = Arrays.copyOf(commonRowsName, commonRowsName.length + 2);
            nonVattuRowsName[10] = "Số BB hao dôi";
            nonVattuRowsName[11] = "Trạng thái";
            rowsName = nonVattuRowsName;
        }
        String fileName = "danh-sach-bien-ban-lay-mau/ban-giao-mau-hang-DTQG.xlsx";
        List<Object[]> excelDataList = new ArrayList<>();
        for (int i = 0; i < dataList.size(); i++) {
            XhBbLayMau proposal = dataList.get(i);
            Object[] excelRow = new Object[rowsName.length];
            excelRow[0] = i;
            excelRow[1] = proposal.getSoQdNv();
            excelRow[2] = proposal.getNam();
            excelRow[3] = LocalDateTimeUtils.localDateToString(proposal.getTgianGiaoHang());
            excelRow[4] = proposal.getTenDiemKho();
            excelRow[5] = proposal.getTenNganLoKho();
            excelRow[6] = proposal.getSoBbLayMau();
            excelRow[7] = LocalDateTimeUtils.localDateToString(proposal.getNgayLayMau());
            excelRow[8] = proposal.getSoBbTinhKho();
            excelRow[9] = LocalDateTimeUtils.localDateToString(proposal.getNgayXuatDocKho());
            if (isVattuType) {
                excelRow[10] = proposal.getTenTrangThai();
            } else {
                excelRow[10] = proposal.getSoBbHaoDoi();
                excelRow[11] = proposal.getTenTrangThai();
            }
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
            XhBbLayMau reportDetail = this.detail(DataUtils.safeToLong(requestParams.get("id")));
            Map<String, Map<String, Object>> mapDmucDvi = getListDanhMucDviObject(null, null, "01");
            xhQdGiaoNvXhRepository.findById(reportDetail.getIdQdNv())
                    .ifPresent(xhQdGiaoNvXh -> reportDetail.setMaDviCha(xhQdGiaoNvXh.getMaDvi()));
            if (mapDmucDvi.containsKey((reportDetail.getMaDviCha()))) {
                Map<String, Object> objDonVi = mapDmucDvi.get(reportDetail.getMaDviCha());
                reportDetail.setTenDviCha(objDonVi.get("tenDvi").toString());
            }
            List<XhBbLayMauCt> detailList = xhBbLayMauCtRepository.findAllByIdHdr(reportDetail.getId());
            if (detailList != null || !detailList.isEmpty()) {
                List<XhBbLayMauCt> filteredPhuongPhapLayMau = detailList.stream().filter(type -> "PPLM".equals(type.getType()) && type.getChecked()).collect(Collectors.toList());
                List<XhBbLayMauCt> filteredChiTieuKiemTra = detailList.stream().filter(type -> "CTCL".equals(type.getType()) && type.getChecked()).collect(Collectors.toList());
                if (!filteredPhuongPhapLayMau.isEmpty()) {
                    reportDetail.setPhuongPhapLayMau(filteredPhuongPhapLayMau.get(0).getTen());
                }
                if (!filteredChiTieuKiemTra.isEmpty()) {
                    reportDetail.setChiTieuKiemTra(String.join(",", filteredChiTieuKiemTra.stream().map(XhBbLayMauCt::getTen).collect(Collectors.toList())));
                }
                reportDetail.setChildren(detailList.stream().filter(type -> "NLQ".equals(type.getType())).collect(Collectors.toList()));
            }
            return docxToPdfConverter.convertDocxToPdf(templateInputStream, reportDetail);
        } catch (IOException | XDocReportException exception) {
            exception.printStackTrace();
        }
        return null;
    }
}