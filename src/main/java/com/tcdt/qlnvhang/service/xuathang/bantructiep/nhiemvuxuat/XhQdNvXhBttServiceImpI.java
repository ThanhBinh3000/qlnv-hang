package com.tcdt.qlnvhang.service.xuathang.bantructiep.nhiemvuxuat;

import com.tcdt.qlnvhang.entities.xuathang.bantructiep.nhiemvuxuat.XhQdNvXhBttDtl;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.nhiemvuxuat.XhQdNvXhBttDvi;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.nhiemvuxuat.XhQdNvXhBttHdr;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.UserInfoRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.hopdong.XhHopDongBttHdrRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.nhiemvuxuat.XhQdNvXhBttDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.nhiemvuxuat.XhQdNvXhBttDviRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.nhiemvuxuat.XhQdNvXhBttHdrRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.nhiemvuxuat.XhQdNvXhBttDtlReq;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.nhiemvuxuat.XhQdNvXhBttDviReq;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.nhiemvuxuat.XhQdNvXhBttHdrReq;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.DataUtils;
import com.tcdt.qlnvhang.util.ExportExcel;
import com.tcdt.qlnvhang.util.LocalDateTimeUtils;
import fr.opensagres.xdocreport.core.XDocReportException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
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
@Log4j2
@RequiredArgsConstructor
public class XhQdNvXhBttServiceImpI extends BaseServiceImpl {

    @Autowired
    private XhQdNvXhBttHdrRepository xhQdNvXhBttHdrRepository;
    @Autowired
    private XhQdNvXhBttDtlRepository xhQdNvXhBttDtlRepository;
    @Autowired
    private XhQdNvXhBttDviRepository xhQdNvXhBttDviRepository;
    @Autowired
    private XhHopDongBttHdrRepository xhHopDongBttHdrRepository;
    @Autowired
    private XhQdPdKhBttDtlRepository xhQdPdKhBttDtlRepository;
    @Autowired
    private UserInfoRepository userInfoRepository;

    public Page<XhQdNvXhBttHdr> searchPage(CustomUserDetails currentUser, XhQdNvXhBttHdrReq request) throws Exception {
        if (currentUser.getUser().getCapDvi().equals(Contains.CAP_TONG_CUC)) {
            request.setTrangThai(Contains.BAN_HANH);
        } else if (currentUser.getUser().getCapDvi().equals(Contains.CAP_CUC)) {
            request.setDvql(currentUser.getDvql());
        } else if (currentUser.getUser().getCapDvi().equals(Contains.CAP_CHI_CUC)) {
            request.setTrangThai(Contains.BAN_HANH);
            request.setMaDviCon(currentUser.getDvql());
        }
        Pageable pageable = PageRequest.of(request.getPaggingReq().getPage(), request.getPaggingReq().getLimit());
        Page<XhQdNvXhBttHdr> searchResultPage = xhQdNvXhBttHdrRepository.searchPage(request, pageable);
        Map<String, String> mapDmucVthh = getListDanhMucHangHoa();
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        searchResultPage.getContent().forEach(data -> {
            try {
                data.setMapDmucDvi(mapDmucDvi);
                data.setMapDmucVthh(mapDmucVthh);
                data.setTrangThai(data.getTrangThai());
                data.setTrangThaiXh(data.getTrangThaiXh());
                List<XhQdNvXhBttDtl> childList = xhQdNvXhBttDtlRepository.findAllByIdHdr(data.getId());
                data.setChildren(childList != null && !childList.isEmpty() ? childList : Collections.emptyList());
            } catch (Exception exception) {
                throw new RuntimeException(exception);
            }
        });
        return searchResultPage;
    }

    @Transactional
    public XhQdNvXhBttHdr create(CustomUserDetails currentUser, XhQdNvXhBttHdrReq request) throws Exception {
        if (currentUser == null || request == null) {
            throw new Exception("Bad request.");
        }
        if (!StringUtils.isEmpty(request.getSoQdNv()) && xhQdNvXhBttHdrRepository.existsBySoQdNv(request.getSoQdNv())) {
            throw new Exception("Số quyết định nhiệm vụ " + request.getSoQdNv() + " đã tồn tại");
        }
        if (request.getPhanLoai().equals("CG")) {
            xhHopDongBttHdrRepository.findById(request.getIdHopDong())
                    .orElseThrow(() -> new Exception("Không tìm thấy thông tin chào giá"));
        } else {
            xhQdPdKhBttDtlRepository.findById(request.getIdChaoGia())
                    .orElseThrow(() -> new Exception("Không tìm thấy thông tin ủy quyền/bán lẻ"));
        }
        XhQdNvXhBttHdr newData = new XhQdNvXhBttHdr();
        BeanUtils.copyProperties(request, newData);
        newData.setMaDvi(currentUser.getDvql());
        newData.setNgayTao(LocalDate.now());
        newData.setNguoiTaoId(currentUser.getUser().getId());
        newData.setTrangThai(Contains.DU_THAO);
        newData.setTrangThaiXh(Contains.CHUA_THUC_HIEN);
        if (newData.getPhanLoai().equals("UQBL")) {
            List<String> maDviTsanList = request.getListMaDviTsan();
            newData.setMaDviTsan(maDviTsanList.isEmpty() ? null : String.join(",", maDviTsanList));
        }
        XhQdNvXhBttHdr createdRecord = xhQdNvXhBttHdrRepository.save(newData);
        this.saveDetail(request, createdRecord.getId(), false);
        return createdRecord;
    }

    @Transactional
    public XhQdNvXhBttHdr update(CustomUserDetails currentUser, XhQdNvXhBttHdrReq request) throws Exception {
        if (currentUser == null || request == null || request.getId() == null) {
            throw new Exception("Bad request.");
        }
        XhQdNvXhBttHdr existingData = xhQdNvXhBttHdrRepository.findById(request.getId())
                .orElseThrow(() -> new Exception("Không tìm thấy dữ liệu cần sửa"));
        if (!StringUtils.isEmpty(request.getSoQdNv()) && xhQdNvXhBttHdrRepository.existsBySoQdNvAndIdNot(request.getSoQdNv(), request.getId())) {
            throw new Exception("Số quyết định nhiệm vụ " + request.getSoQdNv() + " đã tồn tại");
        }
        if (existingData.getPhanLoai().equals("CG")) {
            xhHopDongBttHdrRepository.findById(existingData.getIdHopDong())
                    .orElseThrow(() -> new Exception("Không tìm thấy thông tin chào giá"));
        } else {
            xhQdPdKhBttDtlRepository.findById(existingData.getIdChaoGia())
                    .orElseThrow(() -> new Exception("Không tìm thấy thông tin ủy quyền/bán lẻ"));
        }
        BeanUtils.copyProperties(request, existingData, "id", "maDvi", "trangThaiXh");
        existingData.setNgaySua(LocalDate.now());
        existingData.setNguoiSuaId(currentUser.getUser().getId());
        if (existingData.getPhanLoai().equals("UQBL")) {
            List<String> maDviTsanList = request.getListMaDviTsan();
            existingData.setMaDviTsan(maDviTsanList.isEmpty() ? null : String.join(",", maDviTsanList));
        }
        XhQdNvXhBttHdr updatedData = xhQdNvXhBttHdrRepository.save(existingData);
        this.saveDetail(request, updatedData.getId(), true);
        return updatedData;
    }

    public void saveDetail(XhQdNvXhBttHdrReq request, Long headerId, Boolean isCheckRequired) {
        xhQdNvXhBttDtlRepository.deleteAllByIdHdr(isCheckRequired ? headerId : null);
        for (XhQdNvXhBttDtlReq detailRequest : request.getChildren()) {
            XhQdNvXhBttDtl detail = new XhQdNvXhBttDtl();
            BeanUtils.copyProperties(detailRequest, detail, "id");
            detail.setIdHdr(headerId);
            xhQdNvXhBttDtlRepository.save(detail);
            xhQdNvXhBttDviRepository.deleteAllByIdDtl(isCheckRequired ? detailRequest.getId() : null);
            for (XhQdNvXhBttDviReq venueRequest : detailRequest.getChildren()) {
                XhQdNvXhBttDvi venue = new XhQdNvXhBttDvi();
                BeanUtils.copyProperties(venueRequest, venue, "id");
                venue.setId(null);
                venue.setIdDtl(detail.getId());
                xhQdNvXhBttDviRepository.save(venue);
            }
        }
    }

    public List<XhQdNvXhBttHdr> detail(List<Long> ids) throws Exception {
        if (DataUtils.isNullOrEmpty(ids)) {
            throw new Exception("Tham số không hợp lệ.");
        }
        List<XhQdNvXhBttHdr> resultList = xhQdNvXhBttHdrRepository.findByIdIn(ids);
        if (DataUtils.isNullOrEmpty(resultList)) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        Map<String, String> mapDmucVthh = getListDanhMucHangHoa();
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        Map<String, String> mapDmucLoaiXuat = getListDanhMucChung("LOAI_HINH_NHAP_XUAT");
        Map<String, String> mapDmucKieuXuat = getListDanhMucChung("KIEU_NHAP_XUAT");
        for (XhQdNvXhBttHdr item : resultList) {
            List<XhQdNvXhBttDtl> detailList = xhQdNvXhBttDtlRepository.findAllByIdHdr(item.getId());
            for (XhQdNvXhBttDtl detailItem : detailList) {
                List<XhQdNvXhBttDvi> deliveryPointList = xhQdNvXhBttDviRepository.findAllByIdDtl(detailItem.getId());
                deliveryPointList.forEach(deliveryPointItem -> deliveryPointItem.setMapDmucDvi(mapDmucDvi));
                detailItem.setMapDmucDvi(mapDmucDvi);
                detailItem.setChildren(deliveryPointList);
            }
            item.setMapDmucDvi(mapDmucDvi);
            item.setMapDmucVthh(mapDmucVthh);
            item.setMapDmucLoaiXuat(mapDmucLoaiXuat);
            item.setMapDmucKieuXuat(mapDmucKieuXuat);
            item.setTrangThai(item.getTrangThai());
            item.setChildren(detailList);
            this.setFullNameIfNotNull(item.getNguoiPduyetId(), item::setTenLanhDaoCuc);
            if (!DataUtils.isNullObject(item.getMaDviTsan())) {
                item.setListMaDviTsan(Arrays.asList(item.getMaDviTsan().split(",")));
            }
        }
        return resultList;
    }

    private void setFullNameIfNotNull(Long userId, Consumer<String> fullNameSetter) {
        if (userId != null) {
            userInfoRepository.findById(userId).ifPresent(userInfo -> fullNameSetter.accept(userInfo.getFullName()));
        }
    }

    public XhQdNvXhBttHdr detail(Long id) throws Exception {
        if (id == null) {
            throw new Exception("Tham số không hợp lệ.");
        }
        List<XhQdNvXhBttHdr> details = detail(Collections.singletonList(id));
        return details.isEmpty() ? null : details.get(0);
    }

    @Transactional
    public void delete(IdSearchReq idSearchReq) throws Exception {
        if (idSearchReq == null || idSearchReq.getId() == null) {
            throw new Exception("Bad request.");
        }
        XhQdNvXhBttHdr proposalData = xhQdNvXhBttHdrRepository.findById(idSearchReq.getId())
                .orElseThrow(() -> new Exception("Bản ghi không tồn tại"));
        List<String> allowedStatus = Arrays.asList(Contains.DUTHAO, Contains.TU_CHOI_TP, Contains.TUCHOI_LDC);
        if (!allowedStatus.contains(proposalData.getTrangThai())) {
            throw new Exception("Chỉ thực hiện xóa với quyết định ở trạng thái bản nháp hoặc từ chối");
        }
        List<XhQdNvXhBttDtl> proposalDetailsList = xhQdNvXhBttDtlRepository.findAllByIdHdr(proposalData.getId());
        for (XhQdNvXhBttDtl proposalDetail : proposalDetailsList) {
            xhQdNvXhBttDviRepository.deleteAllByIdDtl(proposalDetail.getId());
        }
        xhQdNvXhBttDtlRepository.deleteAllByIdHdr(proposalData.getId());
        xhQdNvXhBttHdrRepository.delete(proposalData);
    }

    @Transactional
    public void deleteMulti(IdSearchReq idSearchReq) throws Exception {
        if (idSearchReq == null || idSearchReq.getIdList() == null) {
            throw new Exception("Bad request.");
        }
        List<XhQdNvXhBttHdr> proposalList = xhQdNvXhBttHdrRepository.findAllByIdIn(idSearchReq.getIdList());
        if (proposalList.isEmpty()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        boolean isValidToDelete = proposalList.stream().allMatch(hdr -> hdr.getTrangThai().equals(Contains.DUTHAO) ||
                hdr.getTrangThai().equals(Contains.TU_CHOI_TP) ||
                hdr.getTrangThai().equals(Contains.TUCHOI_LDC));
        if (!isValidToDelete) {
            throw new Exception("Chỉ thực hiện xóa với quyết định ở trạng thái bản nháp hoặc từ chối");
        }
        List<Long> proposalIds = proposalList.stream().map(XhQdNvXhBttHdr::getId).collect(Collectors.toList());
        List<XhQdNvXhBttDtl> proposalDetailsList = xhQdNvXhBttDtlRepository.findByIdHdrIn(proposalIds);
        List<Long> detailIds = proposalDetailsList.stream().map(XhQdNvXhBttDtl::getId).collect(Collectors.toList());
        List<XhQdNvXhBttDvi> proposalDonVi = xhQdNvXhBttDviRepository.findByIdDtlIn(detailIds);
        xhQdNvXhBttDviRepository.deleteAll(proposalDonVi);
        xhQdNvXhBttDtlRepository.deleteAll(proposalDetailsList);
        xhQdNvXhBttHdrRepository.deleteAll(proposalList);
    }

    @Transactional
    public XhQdNvXhBttHdr approve(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {
        if (currentUser == null || StringUtils.isEmpty(statusReq.getId())) {
            throw new Exception("Bad request.");
        }
        XhQdNvXhBttHdr proposal = xhQdNvXhBttHdrRepository.findById(Long.valueOf(statusReq.getId()))
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
            case Contains.CHODUYET_LDC + Contains.CHODUYET_TP:
                proposal.setNguoiPduyetId(currentUser.getUser().getId());
                proposal.setNgayPduyet(LocalDate.now());
                proposal.setLyDoTuChoi(statusReq.getLyDoTuChoi());
                break;
            case Contains.BAN_HANH + Contains.CHODUYET_LDC:
                proposal.setNguoiPduyetId(currentUser.getUser().getId());
                proposal.setNgayPduyet(LocalDate.now());
                break;
            default:
                throw new Exception("Phê duyệt không thành công");
        }
        proposal.setTrangThai(statusReq.getTrangThai());
        XhQdNvXhBttHdr updatedData = xhQdNvXhBttHdrRepository.save(proposal);
        if (updatedData.getTrangThai().equals(Contains.BAN_HANH)) {
            this.updateContractWithQdNv(updatedData);
        }
        return updatedData;
    }

    private void updateContractWithQdNv(XhQdNvXhBttHdr proposal) {
        if ("CG".equals(proposal.getPhanLoai())) {
            xhHopDongBttHdrRepository.findById(proposal.getIdHopDong()).ifPresent(contracts -> {
                contracts.setIdQdNv(proposal.getId());
                contracts.setSoQdNv(proposal.getSoQdNv());
                xhHopDongBttHdrRepository.save(contracts);
            });
        } else {
            xhQdPdKhBttDtlRepository.findById(proposal.getIdChaoGia()).ifPresent(proxy -> {
                proxy.setIdQdNv(proposal.getId());
                proxy.setSoQdNv(proposal.getSoQdNv());
                xhQdPdKhBttDtlRepository.save(proxy);
            });
        }
    }

    public void export(CustomUserDetails currentUser, XhQdNvXhBttHdrReq req, HttpServletResponse response) throws Exception {
        req.getPaggingReq().setPage(0);
        req.getPaggingReq().setLimit(Integer.MAX_VALUE);
        Page<XhQdNvXhBttHdr> page = this.searchPage(currentUser, req);
        List<XhQdNvXhBttHdr> dataList = page.getContent();
        String title = "Danh sách quyết định giao nhiệm vụ xuất hàng bán trực tiếp hàng DTQG";
        String[] rowsName;
        boolean isVattuType = dataList.stream().anyMatch(item -> item.getLoaiVthh().startsWith(Contains.LOAI_VTHH_VATTU));
        String[] commonRowsName = new String[]{"STT", "Năm xuất", "Số quyết định", "Ngày quyết định", "Số hợp đồng"};
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
        String fileName = "danh-sach-quyet-dinh-nhiem-vu-xuat-hang-ban-truc-tiep-hang-DTQG.xlsx";
        List<Object[]> excelDataList = new ArrayList<>();
        for (int i = 0; i < dataList.size(); i++) {
            XhQdNvXhBttHdr proposal = dataList.get(i);
            Object[] excelRow = new Object[rowsName.length];
            excelRow[0] = i;
            excelRow[1] = proposal.getNamKh();
            excelRow[2] = proposal.getSoQdNv();
            excelRow[3] = LocalDateTimeUtils.localDateToString(proposal.getNgayKyQdNv());
            excelRow[4] = proposal.getSoHopDong();
            if (isVattuType) {
                excelRow[5] = proposal.getTenLoaiVthh();
                excelRow[6] = proposal.getTenCloaiVthh();
                excelRow[7] = LocalDateTimeUtils.localDateToString(proposal.getTgianGiaoNhan());
                excelRow[8] = proposal.getTrichYeu();
                excelRow[9] = proposal.getTenTrangThai();
                excelRow[10] = proposal.getTenTrangThaiXh();
            } else {
                excelRow[5] = proposal.getTenCloaiVthh();
                excelRow[6] = LocalDateTimeUtils.localDateToString(proposal.getTgianGiaoNhan());
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
            String templatePath = "bantructiep/" + templateName;
            FileInputStream templateInputStream = new FileInputStream(baseReportFolder + templatePath);
            XhQdNvXhBttHdr reportDetail = this.detail(DataUtils.safeToLong(requestParams.get("id")));
            return docxToPdfConverter.convertDocxToPdf(templateInputStream, reportDetail);
        } catch (IOException | XDocReportException exception) {
            exception.printStackTrace();
        }
        return null;
    }
}