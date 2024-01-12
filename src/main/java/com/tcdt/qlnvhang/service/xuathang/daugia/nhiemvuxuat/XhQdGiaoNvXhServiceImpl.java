package com.tcdt.qlnvhang.service.xuathang.daugia.nhiemvuxuat;

import com.tcdt.qlnvhang.entities.xuathang.daugia.nhiemvuxuat.XhQdGiaoNvXh;
import com.tcdt.qlnvhang.entities.xuathang.daugia.nhiemvuxuat.XhQdGiaoNvXhDdiem;
import com.tcdt.qlnvhang.entities.xuathang.daugia.nhiemvuxuat.XhQdGiaoNvXhDtl;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.UserInfoRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.hopdong.XhHopDongHdrRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.nhiemvuxuat.XhQdGiaoNvXhDdiemRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.nhiemvuxuat.XhQdGiaoNvXhDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.nhiemvuxuat.XhQdGiaoNvXhRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.xuathang.quyetdinhgiaonhiemvuxuat.XhQdGiaoNvXhDdiemReq;
import com.tcdt.qlnvhang.request.xuathang.quyetdinhgiaonhiemvuxuat.XhQdGiaoNvuXuatCtReq;
import com.tcdt.qlnvhang.request.xuathang.quyetdinhgiaonhiemvuxuat.XhQdGiaoNvuXuatReq;
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
public class XhQdGiaoNvXhServiceImpl extends BaseServiceImpl {

    @Autowired
    private XhQdGiaoNvXhRepository xhQdGiaoNvXhRepository;
    @Autowired
    private XhQdGiaoNvXhDtlRepository xhQdGiaoNvXhDtlRepository;
    @Autowired
    private XhQdGiaoNvXhDdiemRepository xhQdGiaoNvXhDdiemRepository;
    @Autowired
    private XhHopDongHdrRepository xhHopDongHdrRepository;
    @Autowired
    private UserInfoRepository userInfoRepository;

    public Page<XhQdGiaoNvXh> searchPage(CustomUserDetails currentUser, XhQdGiaoNvuXuatReq request) throws Exception {
        if (currentUser.getUser().getCapDvi().equals(Contains.CAP_TONG_CUC)) {
            request.setTrangThai(Contains.BAN_HANH);
        } else if (currentUser.getUser().getCapDvi().equals(Contains.CAP_CUC)) {
            request.setDvql(currentUser.getDvql());
        } else if (currentUser.getUser().getCapDvi().equals(Contains.CAP_CHI_CUC)) {
            request.setTrangThai(Contains.BAN_HANH);
            request.setMaDviCon(currentUser.getDvql());
        }
        Pageable pageable = PageRequest.of(request.getPaggingReq().getPage(), request.getPaggingReq().getLimit());
        Page<XhQdGiaoNvXh> searchResultPage = xhQdGiaoNvXhRepository.searchPage(request, pageable);
        Map<String, String> mapDmucVthh = getListDanhMucHangHoa();
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        searchResultPage.getContent().forEach(data -> {
            try {
                data.setMapDmucDvi(mapDmucDvi);
                data.setMapDmucVthh(mapDmucVthh);
                data.setTrangThai(data.getTrangThai());
                data.setTrangThaiXh(data.getTrangThaiXh());
                List<XhQdGiaoNvXhDtl> childList = xhQdGiaoNvXhDtlRepository.findAllByIdHdr(data.getId());
                data.setChildren(childList != null && !childList.isEmpty() ? childList : Collections.emptyList());
            } catch (Exception exception) {
                throw new RuntimeException(exception);
            }
        });
        return searchResultPage;
    }

    @Transactional
    public XhQdGiaoNvXh create(CustomUserDetails currentUser, XhQdGiaoNvuXuatReq request) throws Exception {
        if (currentUser == null || request == null) {
            throw new Exception("Bad request.");
        }
        if (!StringUtils.isEmpty(request.getSoQdNv()) && xhQdGiaoNvXhRepository.existsBySoQdNv(request.getSoQdNv())) {
            throw new Exception("Số quyết định nhiệm vụ " + request.getSoQdNv() + " đã tồn tại");
        }
        XhQdGiaoNvXh newData = new XhQdGiaoNvXh();
        BeanUtils.copyProperties(request, newData);
        newData.setMaDvi(currentUser.getDvql());
        newData.setNgayTao(LocalDate.now());
        newData.setNguoiTaoId(currentUser.getUser().getId());
        newData.setIdCanBoPhong(currentUser.getUser().getId());
        newData.setTrangThai(Contains.DU_THAO);
        newData.setTrangThaiXh(Contains.CHUA_THUC_HIEN);
        XhQdGiaoNvXh createdRecord = xhQdGiaoNvXhRepository.save(newData);
        this.saveDetail(request, createdRecord.getId(), false);
        return createdRecord;
    }

    @Transactional
    public XhQdGiaoNvXh update(CustomUserDetails currentUser, XhQdGiaoNvuXuatReq request) throws Exception {
        if (currentUser == null || request == null || request.getId() == null) {
            throw new Exception("Bad request.");
        }
        XhQdGiaoNvXh existingData = xhQdGiaoNvXhRepository.findById(request.getId())
                .orElseThrow(() -> new Exception("Không tìm thấy dữ liệu cần sửa"));
        if (xhQdGiaoNvXhRepository.existsBySoQdNvAndIdNot(request.getSoQdNv(), request.getId())) {
            throw new Exception("Số quyết định nhiệm vụ " + request.getSoQdNv() + " đã tồn tại");
        }
        BeanUtils.copyProperties(request, existingData, "id", "maDvi", "idCanBoPhong", "trangThaiXh");
        existingData.setNgaySua(LocalDate.now());
        existingData.setNguoiSuaId(currentUser.getUser().getId());
        XhQdGiaoNvXh updatedData = xhQdGiaoNvXhRepository.save(existingData);
        this.saveDetail(request, updatedData.getId(), true);
        return updatedData;
    }

    public void saveDetail(XhQdGiaoNvuXuatReq request, Long headerId, Boolean isCheckRequired) {
        xhQdGiaoNvXhDtlRepository.deleteAllByIdHdr(isCheckRequired ? headerId : null);
        for (XhQdGiaoNvuXuatCtReq detailRequest : request.getChildren()) {
            XhQdGiaoNvXhDtl detail = new XhQdGiaoNvXhDtl();
            BeanUtils.copyProperties(detailRequest, detail, "id");
            detail.setIdHdr(headerId);
            detail.setTrangThai(Contains.CHUA_THUC_HIEN);
            xhQdGiaoNvXhDtlRepository.save(detail);
            xhQdGiaoNvXhDdiemRepository.deleteAllByIdDtl(isCheckRequired ? detailRequest.getId() : null);
            for (XhQdGiaoNvXhDdiemReq venueRequest : detailRequest.getChildren()) {
                XhQdGiaoNvXhDdiem venue = new XhQdGiaoNvXhDdiem();
                BeanUtils.copyProperties(venueRequest, venue, "id");
                venue.setId(null);
                venue.setIdDtl(detail.getId());
                xhQdGiaoNvXhDdiemRepository.save(venue);
            }
        }
    }

    public List<XhQdGiaoNvXh> detail(List<Long> ids) throws Exception {
        if (DataUtils.isNullOrEmpty(ids)) {
            throw new Exception("Tham số không hợp lệ.");
        }
        List<XhQdGiaoNvXh> resultList = xhQdGiaoNvXhRepository.findByIdIn(ids);
        if (DataUtils.isNullOrEmpty(resultList)) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        Map<String, String> mapDmucVthh = getListDanhMucHangHoa();
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        Map<String, String> mapDmucLoaiXuat = getListDanhMucChung("LOAI_HINH_NHAP_XUAT");
        Map<String, String> mapDmucKieuXuat = getListDanhMucChung("KIEU_NHAP_XUAT");
        for (XhQdGiaoNvXh item : resultList) {
            List<XhQdGiaoNvXhDtl> detailList = xhQdGiaoNvXhDtlRepository.findAllByIdHdr(item.getId());
            for (XhQdGiaoNvXhDtl detailItem : detailList) {
                List<XhQdGiaoNvXhDdiem> deliveryPointList = xhQdGiaoNvXhDdiemRepository.findAllByIdDtl(detailItem.getId());
                deliveryPointList.forEach(deliveryPointItem -> deliveryPointItem.setMapDmucDvi(mapDmucDvi));
                detailItem.setMapDmucDvi(mapDmucDvi);
                detailItem.setTrangThai(detailItem.getTrangThai());
                detailItem.setChildren(deliveryPointList);
            }
            this.setFullNameIfNotNull(item.getIdCanBoPhong(), item::setTenCanBoPhong);
            this.setFullNameIfNotNull(item.getIdTruongPhong(), item::setTenTruongPhong);
            this.setFullNameIfNotNull(item.getIdLanhDaoCuc(), item::setTenLanhDaoCuc);
            item.setMapDmucDvi(mapDmucDvi);
            item.setMapDmucVthh(mapDmucVthh);
            item.setMapDmucLoaiXuat(mapDmucLoaiXuat);
            item.setMapDmucKieuXuat(mapDmucKieuXuat);
            item.setTrangThai(item.getTrangThai());
            item.setChildren(detailList);
        }
        return resultList;
    }

    private void setFullNameIfNotNull(Long userId, Consumer<String> fullNameSetter) {
        if (userId != null) {
            userInfoRepository.findById(userId).ifPresent(userInfo -> fullNameSetter.accept(userInfo.getFullName()));
        }
    }

    public XhQdGiaoNvXh detail(Long id) throws Exception {
        if (id == null) {
            throw new Exception("Tham số không hợp lệ.");
        }
        List<XhQdGiaoNvXh> details = detail(Collections.singletonList(id));
        return details.isEmpty() ? null : details.get(0);
    }

    @Transactional
    public void delete(IdSearchReq idSearchReq) throws Exception {
        if (idSearchReq == null || idSearchReq.getId() == null) {
            throw new Exception("Bad request.");
        }
        XhQdGiaoNvXh proposalData = xhQdGiaoNvXhRepository.findById(idSearchReq.getId())
                .orElseThrow(() -> new Exception("Bản ghi không tồn tại"));
        List<String> allowedStatus = Arrays.asList(Contains.DUTHAO, Contains.TU_CHOI_TP, Contains.TUCHOI_LDC);
        if (!allowedStatus.contains(proposalData.getTrangThai())) {
            throw new Exception("Chỉ thực hiện xóa với quyết định ở trạng thái bản nháp hoặc từ chối");
        }
        List<XhQdGiaoNvXhDtl> proposalDetailsList = xhQdGiaoNvXhDtlRepository.findAllByIdHdr(proposalData.getId());
        for (XhQdGiaoNvXhDtl proposalDetail : proposalDetailsList) {
            xhQdGiaoNvXhDdiemRepository.deleteAllByIdDtl(proposalDetail.getId());
        }
        xhQdGiaoNvXhDtlRepository.deleteAllByIdHdr(proposalData.getId());
        xhQdGiaoNvXhRepository.delete(proposalData);
    }

    @Transactional
    public void deleteMulti(IdSearchReq idSearchReq) throws Exception {
        if (idSearchReq == null || idSearchReq.getIdList() == null) {
            throw new Exception("Bad request.");
        }
        List<XhQdGiaoNvXh> proposalList = xhQdGiaoNvXhRepository.findAllByIdIn(idSearchReq.getIdList());
        if (proposalList.isEmpty()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        boolean isValidToDelete = proposalList.stream().allMatch(
                proposal -> proposal.getTrangThai().equals(Contains.DUTHAO) ||
                        proposal.getTrangThai().equals(Contains.TU_CHOI_TP) ||
                        proposal.getTrangThai().equals(Contains.TUCHOI_LDC));
        if (!isValidToDelete) {
            throw new Exception("Chỉ thực hiện xóa với quyết định ở trạng thái bản nháp hoặc từ chối");
        }
        List<Long> proposalIds = proposalList.stream().map(XhQdGiaoNvXh::getId).collect(Collectors.toList());
        List<XhQdGiaoNvXhDtl> proposalDetailsList = xhQdGiaoNvXhDtlRepository.findByIdHdrIn(proposalIds);
        List<Long> detailIds = proposalDetailsList.stream().map(XhQdGiaoNvXhDtl::getId).collect(Collectors.toList());
        List<XhQdGiaoNvXhDdiem> proposalDiaDiem = xhQdGiaoNvXhDdiemRepository.findByIdDtlIn(detailIds);
        xhQdGiaoNvXhDdiemRepository.deleteAll(proposalDiaDiem);
        xhQdGiaoNvXhDtlRepository.deleteAll(proposalDetailsList);
        xhQdGiaoNvXhRepository.deleteAll(proposalList);
    }

    public XhQdGiaoNvXh approve(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {
        if (currentUser == null || StringUtils.isEmpty(statusReq.getId())) {
            throw new Exception("Bad request.");
        }
        XhQdGiaoNvXh proposal = xhQdGiaoNvXhRepository.findById(Long.valueOf(statusReq.getId()))
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
                proposal.setIdTruongPhong(currentUser.getUser().getId());
                break;
            case Contains.BAN_HANH + Contains.CHODUYET_LDC:
                proposal.setNguoiPduyetId(currentUser.getUser().getId());
                proposal.setNgayPduyet(LocalDate.now());
                proposal.setIdLanhDaoCuc(currentUser.getUser().getId());
                break;
            default:
                throw new Exception("Phê duyệt không thành công");
        }
        proposal.setTrangThai(statusReq.getTrangThai());
        XhQdGiaoNvXh updatedData = xhQdGiaoNvXhRepository.save(proposal);
        if (updatedData.getTrangThai().equals(Contains.BAN_HANH)) {
            xhHopDongHdrRepository.findById(updatedData.getIdHopDong()).ifPresent(contracts -> {
                contracts.setIdQdNv(updatedData.getId());
                contracts.setSoQdNv(updatedData.getSoQdNv());
                xhHopDongHdrRepository.save(contracts);
            });
        }
        return updatedData;
    }

    public void export(CustomUserDetails currentUser, XhQdGiaoNvuXuatReq request, HttpServletResponse response) throws Exception {
        request.getPaggingReq().setPage(0);
        request.getPaggingReq().setLimit(Integer.MAX_VALUE);
        Page<XhQdGiaoNvXh> page = this.searchPage(currentUser, request);
        List<XhQdGiaoNvXh> dataList = page.getContent();
        String title = "Danh sách quyết định giao nhiệm vụ xuất hàng bán đấu giá hàng DTQG";
        String[] rowsName;
        boolean isVattuType = dataList.stream().anyMatch(item -> item.getLoaiVthh().startsWith(Contains.LOAI_VTHH_VATTU));
        String[] commonRowsName = new String[]{"STT", "Năm xuất", "Số quyết định", "Ngày ký quyết định", "Số hợp đồng"};
        if (isVattuType) {
            String[] vattuRowsName = Arrays.copyOf(commonRowsName, commonRowsName.length + 6);
            vattuRowsName[5] = "Loại hàng DTQG";
            vattuRowsName[6] = "Chủng loại hàng DTQG";
            vattuRowsName[7] = "Thời gian giao nhận hàng";
            vattuRowsName[8] = "Trích yếu quyết định";
            vattuRowsName[9] = "Trạng thái QĐ";
            vattuRowsName[10] = "Trạng thái XH";
            rowsName = vattuRowsName;
        } else {
            String[] nonVattuRowsName = Arrays.copyOf(commonRowsName, commonRowsName.length + 5);
            nonVattuRowsName[5] = "Chủng loại hàng DTQG";
            nonVattuRowsName[6] = "Thời gian giao nhận hàng";
            nonVattuRowsName[7] = "Trích yếu quyết định";
            nonVattuRowsName[8] = "Trạng thái QĐ";
            nonVattuRowsName[9] = "Trạng thái XH";
            rowsName = nonVattuRowsName;
        }
        String fileName = "danh-sach-quyet-dinh-nhiem-vu-xuat-hang-ban-dau-gia-hang-DTQG.xlsx";
        List<Object[]> excelDataList = new ArrayList<>();
        for (int i = 0; i < dataList.size(); i++) {
            XhQdGiaoNvXh proposal = dataList.get(i);
            Object[] excelRow = new Object[rowsName.length];
            excelRow[0] = i;
            excelRow[1] = proposal.getNam();
            excelRow[2] = proposal.getSoQdNv();
            excelRow[3] = LocalDateTimeUtils.localDateToString(proposal.getNgayKy());
            excelRow[4] = proposal.getSoHopDong();
            if (isVattuType) {
                excelRow[5] = proposal.getTenLoaiVthh();
                excelRow[6] = proposal.getTenCloaiVthh();
                excelRow[7] = LocalDateTimeUtils.localDateToString(proposal.getTgianGiaoHang());
                excelRow[8] = proposal.getTrichYeu();
                excelRow[9] = proposal.getTenTrangThai();
                excelRow[10] = proposal.getTenTrangThaiXh();
            } else {
                excelRow[5] = proposal.getTenCloaiVthh();
                excelRow[6] = LocalDateTimeUtils.localDateToString(proposal.getTgianGiaoHang());
                excelRow[7] = proposal.getTrichYeu();
                excelRow[8] = proposal.getTenTrangThai();
                excelRow[9] = proposal.getTenTrangThaiXh();
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
            XhQdGiaoNvXh reportDetail = this.detail(DataUtils.safeToLong(requestParams.get("id")));
            return docxToPdfConverter.convertDocxToPdf(templateInputStream, reportDetail);
        } catch (IOException | XDocReportException exception) {
            exception.printStackTrace();
        }
        return null;
    }
}