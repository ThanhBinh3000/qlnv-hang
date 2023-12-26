package com.tcdt.qlnvhang.service.xuathang.bantructiep.kehoach.tonghop;

import com.tcdt.qlnvhang.entities.xuathang.bantructiep.kehoach.dexuat.XhDxKhBanTrucTiepHdr;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.kehoach.tonghop.XhThopDxKhBttDtl;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.kehoach.tonghop.XhThopDxKhBttHdr;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.kehoach.dexuat.XhDxKhBanTrucTiepHdrRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.kehoach.tonghop.XhThopDxKhBttRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.kehoach.tonghop.SearchXhThopDxKhBtt;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.kehoach.tonghop.XhThopDxKhBttHdrReq;
import com.tcdt.qlnvhang.request.xuathang.daugia.XhThopChiTieuReq;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.service.xuathang.bantructiep.kehoach.dexuat.XhDxKhBanTrucTiepServicelmpl;
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
public class XhThopDxKhBttService extends BaseServiceImpl {

    @Autowired
    private XhThopDxKhBttRepository xhThopDxKhBttRepository;
    @Autowired
    private XhDxKhBanTrucTiepHdrRepository xhDxKhBanTrucTiepHdrRepository;
    @Autowired
    private XhDxKhBanTrucTiepServicelmpl xhDxKhBanTrucTiepServicelmpl;

    public Page<XhThopDxKhBttHdr> searchPage(CustomUserDetails currentUser, SearchXhThopDxKhBtt request) throws Exception {
        request.setDvql(currentUser.getDvql());
        if (request.getNgayThopTu() != null) {
            request.setNgayThopTu(request.getNgayThopTu().toLocalDate().atTime(LocalTime.MAX));
        }
        if (request.getNgayThopDen() != null) {
            request.setNgayThopDen(request.getNgayThopDen().toLocalDate().atTime(LocalTime.MIN));
        }
        Pageable pageable = PageRequest.of(request.getPaggingReq().getPage(), request.getPaggingReq().getLimit());
        Page<XhThopDxKhBttHdr> searchResultPage = xhThopDxKhBttRepository.searchPage(request, pageable);
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

    public XhThopDxKhBttHdr sumarryData(CustomUserDetails currentUser, XhThopChiTieuReq request) throws Exception {
        if (currentUser == null || request == null) {
            throw new Exception("Bad request.");
        }
        List<XhDxKhBanTrucTiepHdr> resultList = xhDxKhBanTrucTiepHdrRepository.listTongHop(request);
        if (resultList.isEmpty()) {
            throw new Exception("Không tìm thấy dữ liệu để tổng hợp.");
        }
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        XhThopDxKhBttHdr summary = new XhThopDxKhBttHdr();
        summary.setId(getNextSequence("XH_THOP_DX_KH_BTT_HDR_SEQ"));
        List<XhThopDxKhBttDtl> summaryDetails = new ArrayList<>(resultList.size());
        for (XhDxKhBanTrucTiepHdr result : resultList) {
            result.setMapDmucDvi(mapDmucDvi);
            XhThopDxKhBttDtl summaryDetail = new XhThopDxKhBttDtl();
            BeanUtils.copyProperties(result, summaryDetail, "id");
            summaryDetail.setMaDvi(result.getMaDvi());
            summaryDetail.setTenDvi(result.getTenDvi());
            summaryDetail.setIdDxHdr(result.getId());
            summaryDetail.setSoDxuat(result.getSoDxuat());
            summaryDetail.setNgayPduyet(result.getNgayPduyet());
            summaryDetail.setTrichYeu(result.getTrichYeu());
            summaryDetail.setSlDviTsan(result.getSlDviTsan());
            summaryDetail.setTongSoLuong(result.getTongSoLuong());
            summaryDetail.setThanhTien(result.getThanhTien());
            summaryDetail.setTrangThai(result.getTrangThai());
            summaryDetail.setTenTrangThai(result.getTrangThai());
            summaryDetails.add(summaryDetail);
        }
        summary.setChildren(summaryDetails);
        return summary;
    }

    @Transactional
    public XhThopDxKhBttHdr create(CustomUserDetails currentUser, XhThopDxKhBttHdrReq request) throws Exception {
        if (currentUser == null || request == null) {
            throw new Exception("Bad request.");
        }
        if (request.getIdTh() != null && xhThopDxKhBttRepository.existsById(request.getIdTh())) {
            throw new Exception("Mã tổng hợp " + request.getIdTh() + " đã tồn tại");
        }
        XhThopDxKhBttHdr newData = new XhThopDxKhBttHdr();
        BeanUtils.copyProperties(request, newData);
        newData.setId(request.getIdTh());
        newData.setMaDvi(currentUser.getDvql());
        newData.setNgayTao(LocalDate.now());
        newData.setNguoiTaoId(currentUser.getUser().getId());
        newData.setTrangThai(Contains.CHUATAO_QD);
        XhThopDxKhBttHdr createdRecord = xhThopDxKhBttRepository.save(newData);
        if (createdRecord.getId() > 0 && !CollectionUtils.isEmpty(newData.getChildren())) {
            List<String> orderNumbers = newData.getChildren()
                    .stream()
                    .map(XhThopDxKhBttDtl::getSoDxuat)
                    .collect(Collectors.toList());
            xhDxKhBanTrucTiepHdrRepository.updateStatusInList(orderNumbers, Contains.DATONGHOP, createdRecord.getId());
        }
        return createdRecord;
    }

    @Transactional
    public XhThopDxKhBttHdr update(CustomUserDetails currentUser, XhThopDxKhBttHdrReq request) throws Exception {
        if (currentUser == null || request == null || request.getId() == null) {
            throw new Exception("Bad request.");
        }
        XhThopDxKhBttHdr existingData = xhThopDxKhBttRepository.findById(request.getId())
                .orElseThrow(() -> new Exception("Không tìm thấy dữ liệu cần sửa"));
        BeanUtils.copyProperties(request, existingData, "id", "maDvi");
        existingData.setNgaySua(LocalDate.now());
        existingData.setNguoiSuaId(currentUser.getUser().getId());
        return xhThopDxKhBttRepository.save(existingData);
    }

    public List<XhThopDxKhBttHdr> detail(List<Long> ids) throws Exception {
        if (DataUtils.isNullOrEmpty(ids)) {
            throw new Exception("Tham số không hợp lệ.");
        }
        List<XhThopDxKhBttHdr> resultList = xhThopDxKhBttRepository.findByIdIn(ids);
        if (DataUtils.isNullOrEmpty(resultList)) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        Map<String, String> mapDmucVthh = getListDanhMucHangHoa();
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        for (XhThopDxKhBttHdr item : resultList) {
            item.getChildren().forEach(detailItem -> {
                detailItem.setMapDmucDvi(mapDmucDvi);
                detailItem.setTrangThai(detailItem.getTrangThai());
            });
            item.setMapDmucVthh(mapDmucVthh);
            item.setTrangThai(item.getTrangThai());
            List<Long> childIds = item.getChildren()
                    .stream()
                    .map(XhThopDxKhBttDtl::getIdDxHdr)
                    .collect(Collectors.toList());
            List<XhDxKhBanTrucTiepHdr> listDx = xhDxKhBanTrucTiepServicelmpl.detail(childIds);
            item.setXhDxKhBanTrucTiepHdr(listDx);
        }
        return resultList;
    }

    public XhThopDxKhBttHdr detail(Long id) throws Exception {
        if (id == null) {
            throw new Exception("Tham số không hợp lệ.");
        }
        List<XhThopDxKhBttHdr> details = detail(Collections.singletonList(id));
        return details.isEmpty() ? null : details.get(0);
    }

    @Transactional
    public void delete(IdSearchReq idSearchReq) throws Exception {
        if (idSearchReq == null || idSearchReq.getId() == null) {
            throw new Exception("Bad request.");
        }
        XhThopDxKhBttHdr proposalData = xhThopDxKhBttRepository.findById(idSearchReq.getId())
                .orElseThrow(() -> new Exception("Bản ghi không tồn tại"));
        if (!proposalData.getTrangThai().equals(Contains.CHUATAO_QD)) {
            throw new Exception("Chỉ thực hiện xóa với tổng hợp ở trạng thái bản nháp.");
        }
        List<XhDxKhBanTrucTiepHdr> childList = xhDxKhBanTrucTiepHdrRepository.findAllByIdThop(proposalData.getId());
        childList.forEach(dataChild -> {
            dataChild.setIdThop(null);
            dataChild.setTrangThaiTh(Contains.CHUATONGHOP);
        });
        xhDxKhBanTrucTiepHdrRepository.saveAll(childList);
        xhThopDxKhBttRepository.delete(proposalData);
    }

    @Transactional
    public void deleteMulti(IdSearchReq idSearchReq) throws Exception {
        if (idSearchReq == null || idSearchReq.getIdList() == null) {
            throw new Exception("Bad request.");
        }
        List<XhThopDxKhBttHdr> proposalList = xhThopDxKhBttRepository.findAllByIdIn(idSearchReq.getIdList());
        if (proposalList.isEmpty()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        boolean isValidToDelete = proposalList.stream().allMatch(proposal ->
                proposal.getTrangThai().equals(Contains.CHUATAO_QD));
        if (!isValidToDelete) {
            throw new Exception("Chỉ thực hiện xóa với tổng hợp ở trạng thái bản nháp.");
        }
        List<Long> proposalIds = proposalList.stream().map(XhThopDxKhBttHdr::getId).collect(Collectors.toList());
        List<XhDxKhBanTrucTiepHdr> childList = xhDxKhBanTrucTiepHdrRepository.findByIdThopIn(proposalIds);
        childList.forEach(dataChild -> {
            dataChild.setIdThop(null);
            dataChild.setTrangThaiTh(Contains.CHUATONGHOP);
        });
        xhDxKhBanTrucTiepHdrRepository.saveAll(childList);
        xhThopDxKhBttRepository.deleteAll(proposalList);
    }

    public void export(CustomUserDetails currentUser, SearchXhThopDxKhBtt request, HttpServletResponse response) throws Exception {
        request.getPaggingReq().setPage(0);
        request.getPaggingReq().setLimit(Integer.MAX_VALUE);
        Page<XhThopDxKhBttHdr> page = this.searchPage(currentUser, request);
        List<XhThopDxKhBttHdr> dataList = page.getContent();
        String title = "Danh sách thông tin tổng hợp kế hoạch bán trực tiếp hàng DTQG";
        String[] rowsName;
        boolean isVattuType = dataList.stream().anyMatch(item -> item.getLoaiVthh().startsWith(Contains.LOAI_VTHH_VATTU));
        String[] commonRowsName = new String[]{"STT", "Năm kế hoạch", "Mã tổng hợp", "Ngày tổng hợp", "Nội dung tổng hợp", "Số QĐ phê duyệt KH bán trực tiếp"};
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
        String fileName = "danh-sach-thong-tin-tong-hop-ke-hoach-ban-truc-tiep-hang-DTQG.xlsx";
        List<Object[]> excelDataList = new ArrayList<>();
        for (int i = 0; i < dataList.size(); i++) {
            XhThopDxKhBttHdr proposal = dataList.get(i);
            Object[] excelRow = new Object[rowsName.length];
            excelRow[0] = i;
            excelRow[1] = proposal.getNamKh();
            excelRow[2] = proposal.getId();
            excelRow[3] = LocalDateTimeUtils.localDateToString(proposal.getNgayThop());
            excelRow[4] = proposal.getNoiDungThop();
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
        ExportExcel exportExcel = new ExportExcel(title, fileName, rowsName, excelDataList, response);
        exportExcel.export();
    }

    public ReportTemplateResponse preview(HashMap<String, Object> requestParams, CustomUserDetails currentUser) throws Exception {
        if (currentUser == null) {
            throw new Exception("Bad request.");
        }
        try {
            String templateName = DataUtils.safeToString(requestParams.get("tenBaoCao"));
            String templatePath = "bantructiep/" + templateName;
            FileInputStream templateInputStream = new FileInputStream(baseReportFolder + templatePath);
            XhThopDxKhBttHdr reportDetail = this.detail(DataUtils.safeToLong(requestParams.get("id")));
            List<Long> listIdChild = reportDetail.getChildren().stream().map(XhThopDxKhBttDtl::getIdDxHdr).collect(Collectors.toList());
            List<XhDxKhBanTrucTiepHdr> tableData = xhDxKhBanTrucTiepServicelmpl.detail(listIdChild);
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