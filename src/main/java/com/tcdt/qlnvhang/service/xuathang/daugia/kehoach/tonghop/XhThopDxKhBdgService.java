package com.tcdt.qlnvhang.service.xuathang.daugia.kehoach.tonghop;

import com.tcdt.qlnvhang.entities.xuathang.daugia.kehoach.dexuat.XhDxKhBanDauGia;
import com.tcdt.qlnvhang.entities.xuathang.daugia.kehoach.tonghop.XhThopDxKhBdg;
import com.tcdt.qlnvhang.entities.xuathang.daugia.kehoach.tonghop.XhThopDxKhBdgDtl;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.xuathang.daugia.kehoach.dexuat.XhDxKhBanDauGiaRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.kehoach.tonghop.XhThopDxKhBdgRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.xuathang.daugia.SearchXhThopDxKhBdg;
import com.tcdt.qlnvhang.request.xuathang.daugia.XhThopChiTieuReq;
import com.tcdt.qlnvhang.request.xuathang.daugia.kehoachbdg.tonghop.XhThopDxKhBdgReq;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.service.xuathang.daugia.kehoach.dexuat.XhDxKhBanDauGiaServiceImpl;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.DataUtils;
import com.tcdt.qlnvhang.util.ExportExcel;
import com.tcdt.qlnvhang.util.LocalDateTimeUtils;
import fr.opensagres.xdocreport.core.XDocReportException;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class XhThopDxKhBdgService extends BaseServiceImpl {
    @Autowired
    private XhThopDxKhBdgRepository xhThopDxKhBdgRepository;
    @Autowired
    private XhDxKhBanDauGiaRepository xhDxKhBanDauGiaRepository;
    @Autowired
    private XhDxKhBanDauGiaServiceImpl xhDxKhBanDauGiaServiceImpl;

    public Page<XhThopDxKhBdg> searchPage(CustomUserDetails currentUser, SearchXhThopDxKhBdg request) throws Exception {
        request.setDvql(currentUser.getDvql());
        Pageable pageable = PageRequest.of(request.getPaggingReq().getPage(), request.getPaggingReq().getLimit());
        Page<XhThopDxKhBdg> searchResultPage = xhThopDxKhBdgRepository.searchPage(request, pageable);
        Map<String, String> mapDmucVthh = getListDanhMucHangHoa();
        searchResultPage.getContent().forEach(data -> {
            try {
                data.setMapDmucVthh(mapDmucVthh);
                data.setTrangThai(data.getTrangThai());
            } catch (Exception exception) {
                throw new RuntimeException(exception);
            }
        });
        return searchResultPage;
    }

    public XhThopDxKhBdg sumarryData(CustomUserDetails currentUser, XhThopChiTieuReq request) throws Exception {
        if (currentUser == null || request == null) {
            throw new Exception("Bad request.");
        }
        List<XhDxKhBanDauGia> resultList = xhDxKhBanDauGiaRepository.listTongHop(request);
        if (resultList.isEmpty()) {
            throw new Exception("Không tìm thấy dữ liệu để tổng hợp.");
        }
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        XhThopDxKhBdg summary = new XhThopDxKhBdg();
        summary.setId(getNextSequence("XH_THOP_DX_KH_BDG_SEQ"));
        List<XhThopDxKhBdgDtl> summaryDetails = new ArrayList<>();
        for (XhDxKhBanDauGia result : resultList) {
            result.setMapDmucDvi(mapDmucDvi);
            XhThopDxKhBdgDtl summaryDetail = new XhThopDxKhBdgDtl();
            BeanUtils.copyProperties(result, summaryDetail, "id");
            summaryDetail.setMaDvi(result.getMaDvi());
            summaryDetail.setTenDvi(result.getTenDvi());
            summaryDetail.setDiaChi(result.getDiaChi());
            summaryDetail.setIdDxHdr(result.getId());
            summaryDetail.setSoDxuat(result.getSoDxuat());
            summaryDetail.setNgayTao(result.getNgayTao());
            summaryDetail.setNgayPduyet(result.getNgayPduyet());
            summaryDetail.setTrichYeu(result.getTrichYeu());
            summaryDetail.setSlDviTsan(result.getSlDviTsan());
            summaryDetail.setTongSoLuong(result.getTongSoLuong());
            summaryDetail.setTongTienKhoiDiemDx(result.getTongTienKhoiDiemDx());
            summaryDetail.setKhoanTienDatTruoc(result.getKhoanTienDatTruoc());
            summaryDetail.setTongTienDatTruocDx(result.getTongTienDatTruocDx());
            summaryDetail.setTrangThai(result.getTrangThai());
            summaryDetail.setTenTrangThai(result.getTrangThai());
            summaryDetails.add(summaryDetail);
        }
        summary.setChildren(summaryDetails);
        return summary;
    }

    @Transactional
    public XhThopDxKhBdg create(CustomUserDetails currentUser, XhThopDxKhBdgReq request) throws Exception {
        if (currentUser == null || request == null) {
            throw new Exception("Bad request.");
        }
        if (request.getIdTh() != null && xhThopDxKhBdgRepository.existsById(request.getIdTh())) {
            throw new Exception("Mã tổng hợp " + request.getIdTh() + " đã tồn tại");
        }
        XhThopDxKhBdg newData = new XhThopDxKhBdg();
        BeanUtils.copyProperties(request, newData);
        newData.setId(request.getIdTh());
        newData.setMaDvi(currentUser.getDvql());
        newData.setNgayTao(LocalDate.now());
        newData.setNguoiTaoId(currentUser.getUser().getId());
        newData.setTrangThai(Contains.CHUATAO_QD);
        XhThopDxKhBdg createdRecord = xhThopDxKhBdgRepository.save(newData);
        if (createdRecord.getId() > 0 && !CollectionUtils.isEmpty(newData.getChildren())) {
            List<String> orderNumbers = newData.getChildren()
                    .stream()
                    .map(XhThopDxKhBdgDtl::getSoDxuat)
                    .collect(Collectors.toList());
            xhDxKhBanDauGiaRepository.updateStatusInList(orderNumbers, Contains.DATONGHOP, createdRecord.getId());
        }
        return createdRecord;
    }

    @Transactional
    public XhThopDxKhBdg update(CustomUserDetails currentUser, XhThopDxKhBdgReq request) throws Exception {
        if (currentUser == null || request == null || request.getId() == null) {
            throw new Exception("Bad request.");
        }
        XhThopDxKhBdg existingData = xhThopDxKhBdgRepository.findById(request.getId())
                .orElseThrow(() -> new Exception("Không tìm thấy dữ liệu cần sửa"));
        BeanUtils.copyProperties(request, existingData, "id", "maDvi");
        existingData.setNgaySua(LocalDate.now());
        existingData.setNguoiSuaId(currentUser.getUser().getId());
        return xhThopDxKhBdgRepository.save(existingData);
    }

    public List<XhThopDxKhBdg> detail(List<Long> ids) throws Exception {
        if (DataUtils.isNullOrEmpty(ids)) {
            throw new Exception("Tham số không hợp lệ.");
        }
        List<XhThopDxKhBdg> resultList = xhThopDxKhBdgRepository.findByIdIn(ids);
        if (DataUtils.isNullOrEmpty(resultList)) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        Map<String, String> mapDmucVthh = getListDanhMucHangHoa();
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        for (XhThopDxKhBdg item : resultList) {
            item.getChildren().forEach(detailItem -> {
                detailItem.setMapDmucDvi(mapDmucDvi);
                detailItem.setTenTrangThai(detailItem.getTrangThai());
            });
            item.setMapDmucVthh(mapDmucVthh);
            item.setTrangThai(item.getTrangThai());
            List<Long> childIds = item.getChildren()
                    .stream()
                    .map(XhThopDxKhBdgDtl::getIdDxHdr)
                    .collect(Collectors.toList());
            List<XhDxKhBanDauGia> childList = xhDxKhBanDauGiaServiceImpl.detail(childIds);
            item.setXhDxKhBanDauGia(childList);
        }
        return resultList;
    }

    public XhThopDxKhBdg detail(Long id) throws Exception {
        if (id == null) {
            throw new Exception("Tham số không hợp lệ.");
        }
        List<XhThopDxKhBdg> details = detail(Collections.singletonList(id));
        return details.isEmpty() ? null : details.get(0);
    }

    @Transactional
    public void delete(IdSearchReq idSearchReq) throws Exception {
        if (idSearchReq == null || idSearchReq.getId() == null) {
            throw new Exception("Bad request.");
        }
        XhThopDxKhBdg proposalData = xhThopDxKhBdgRepository.findById(idSearchReq.getId())
                .orElseThrow(() -> new Exception("Bản ghi không tồn tại"));
        if (!proposalData.getTrangThai().equals(Contains.CHUATAO_QD)) {
            throw new Exception("Chỉ thực hiện xóa với tổng hợp ở trạng thái bản nháp.");
        }
        List<XhDxKhBanDauGia> childList = xhDxKhBanDauGiaRepository.findAllByIdThop(proposalData.getId());
        childList.forEach(dataChild -> {
            dataChild.setIdThop(null);
            dataChild.setTrangThaiTh(Contains.CHUATONGHOP);
        });
        xhDxKhBanDauGiaRepository.saveAll(childList);
        xhThopDxKhBdgRepository.delete(proposalData);
    }

    @Transactional
    public void deleteMulti(IdSearchReq idSearchReq) throws Exception {
        if (idSearchReq == null || idSearchReq.getIdList() == null) {
            throw new Exception("Bad request.");
        }
        List<XhThopDxKhBdg> proposalList = xhThopDxKhBdgRepository.findAllByIdIn(idSearchReq.getIdList());
        if (proposalList.isEmpty()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        boolean isValidToDelete = proposalList.stream().allMatch(proposal ->
                proposal.getTrangThai().equals(Contains.CHUATAO_QD));
        if (!isValidToDelete) {
            throw new Exception("Chỉ thực hiện xóa với tổng hợp ở trạng thái bản nháp.");
        }
        List<Long> proposalIds = proposalList.stream().map(XhThopDxKhBdg::getId).collect(Collectors.toList());
        List<XhDxKhBanDauGia> childList = xhDxKhBanDauGiaRepository.findByIdThopIn(proposalIds);
        childList.forEach(dataChild -> {
            dataChild.setIdThop(null);
            dataChild.setTrangThaiTh(Contains.CHUATONGHOP);
        });
        xhDxKhBanDauGiaRepository.saveAll(childList);
        xhThopDxKhBdgRepository.deleteAll(proposalList);
    }

    public void export(CustomUserDetails currentUser, SearchXhThopDxKhBdg request, HttpServletResponse response) throws Exception {
        request.getPaggingReq().setPage(0);
        request.getPaggingReq().setLimit(Integer.MAX_VALUE);
        Page<XhThopDxKhBdg> page = this.searchPage(currentUser, request);
        List<XhThopDxKhBdg> dataList = page.getContent();
        String title = "Danh sách tổng hợp bán đấu giá hàng DTQG";
        String[] rowsName;
        boolean isVattuType = dataList.stream().anyMatch(item -> item.getLoaiVthh().startsWith(Contains.LOAI_VTHH_VATTU));
        String[] commonRowsName = new String[]{"STT", "Mã tổng hợp", "Ngày tổng hợp", "Nội dung tổng hợp ", "Năm kế hoạch", "Số QĐ phê duyệt KH BĐG"};
        if (isVattuType) {
            String[] vattuRowsName = Arrays.copyOf(commonRowsName, commonRowsName.length + 3);
            vattuRowsName[6] = "Loại hàng DTQG";
            vattuRowsName[7] = "Chủng loại hàng DTQG";
            vattuRowsName[8] = "Trạng thái";
            rowsName = vattuRowsName;
        } else {
            String[] nonVattuRowsName = Arrays.copyOf(commonRowsName, commonRowsName.length + 2);
            nonVattuRowsName[6] = "Chủng loại hàng DTQG";
            nonVattuRowsName[7] = "Trạng thái";
            rowsName = nonVattuRowsName;
        }
        String fileName = "danh-sach-tong-hop-ban-dau-gia-hang-DTQG.xlsx";
        List<Object[]> excelDataList = new ArrayList<>();
        for (int i = 0; i < dataList.size(); i++) {
            XhThopDxKhBdg proposal = dataList.get(i);
            Object[] excelRow = new Object[rowsName.length];
            excelRow[0] = i;
            excelRow[1] = proposal.getId();
            excelRow[2] = LocalDateTimeUtils.localDateToString(proposal.getNgayThop());
            excelRow[3] = proposal.getNoiDungThop();
            excelRow[4] = proposal.getNamKh();
            excelRow[5] = proposal.getSoQdPd();
            if (isVattuType) {
                excelRow[6] = proposal.getTenLoaiVthh();
                excelRow[7] = proposal.getTenCloaiVthh();
                excelRow[8] = proposal.getTenTrangThai();
            } else {
                excelRow[6] = proposal.getTenCloaiVthh();
                excelRow[7] = proposal.getTenTrangThai();
            }
            excelDataList.add(excelRow);
        }
        ExportExcel ex = new ExportExcel(title, fileName, rowsName, excelDataList, response);
        ex.export();
    }

    public ReportTemplateResponse preview(HashMap<String, Object> requestParams, CustomUserDetails currentUser) throws Exception {
        if (currentUser == null || requestParams == null) {
            throw new Exception("Bad request.");
        }
        try {
            String templateName = DataUtils.safeToString(requestParams.get("tenBaoCao"));
            String templatePath = "bandaugia/" + templateName;
            FileInputStream templateInputStream = new FileInputStream(baseReportFolder + templatePath);
            XhThopDxKhBdg reportDetail = this.detail(DataUtils.safeToLong(requestParams.get("id")));
            List<Long> childIds = reportDetail.getChildren().stream().map(XhThopDxKhBdgDtl::getIdDxHdr).collect(Collectors.toList());
            List<XhDxKhBanDauGia> tableData = xhDxKhBanDauGiaServiceImpl.detail(childIds);
            Map<String, Object> reportData = new HashMap<>();
            reportData.put("nam", reportDetail.getNamKh());
            reportData.put("tenCloaiVthh", reportDetail.getTenCloaiVthh().toUpperCase());
            reportData.put("table", tableData);
            return docxToPdfConverter.convertDocxToPdf(templateInputStream, reportData);
        } catch (IOException | XDocReportException exception) {
            exception.printStackTrace();
        }
        return null;
    }
}