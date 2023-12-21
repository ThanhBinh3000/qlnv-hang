package com.tcdt.qlnvhang.service.xuathang.daugia.tochuctrienkhai.ketqua;

import com.tcdt.qlnvhang.entities.xuathang.daugia.hopdong.XhHopDongHdr;
import com.tcdt.qlnvhang.entities.xuathang.daugia.tochuctrienkhai.ketqua.XhKqBdgHdr;
import com.tcdt.qlnvhang.entities.xuathang.daugia.tochuctrienkhai.thongtin.XhTcTtinBdgDtl;
import com.tcdt.qlnvhang.entities.xuathang.daugia.tochuctrienkhai.thongtin.XhTcTtinBdgHdr;
import com.tcdt.qlnvhang.entities.xuathang.daugia.tochuctrienkhai.thongtin.XhTcTtinBdgPlo;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.xuathang.daugia.hopdong.XhHopDongHdrRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.kehoach.pheduyet.XhQdPdKhBdgDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.tochuctrienkhai.ketqua.XhKqBdgHdrRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.tochuctrienkhai.thongtin.XhTcTtinBdgDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.tochuctrienkhai.thongtin.XhTcTtinBdgPloRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.xuathang.daugia.tochuctrienkhai.ketqua.XhKqBdgHdrReq;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.service.xuathang.daugia.tochuctrienkhai.thongtin.XhTcTtinBdgHdrServiceImpl;
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
import java.util.stream.Collectors;

@Service
public class XhKqBdgHdrServiceImpl extends BaseServiceImpl {

    @Autowired
    private XhKqBdgHdrRepository xhKqBdgHdrRepository;
    @Autowired
    private XhHopDongHdrRepository xhHopDongHdrRepository;
    @Autowired
    private XhQdPdKhBdgDtlRepository xhQdPdKhBdgDtlRepository;
    @Autowired
    private XhTcTtinBdgHdrServiceImpl xhTcTtinBdgHdrServiceImpl;
    @Autowired
    private XhTcTtinBdgDtlRepository xhTcTtinBdgDtlRepository;
    @Autowired
    private XhTcTtinBdgPloRepository xhTcTtinBdgPloRepository;

    public Page<XhKqBdgHdr> searchPage(CustomUserDetails currentUser, XhKqBdgHdrReq request) throws Exception {
        if (currentUser.getUser().getCapDvi().equals(Contains.CAP_TONG_CUC)) {
            request.setTrangThai(Contains.BAN_HANH);
        } else if (currentUser.getUser().getCapDvi().equals(Contains.CAP_CUC)) {
            request.setDvql(currentUser.getDvql());
        }
        Pageable pageable = PageRequest.of(request.getPaggingReq().getPage(), request.getPaggingReq().getLimit());
        Page<XhKqBdgHdr> searchResultPage = xhKqBdgHdrRepository.searchPage(request, pageable);
        Map<String, String> mapDmucVthh = getListDanhMucHangHoa();
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        Map<String, String> mapDmucHinhThuc = getListDanhMucChung("HINH_THUC_DG");
        Map<String, String> mapDmucPhuongThuc = getListDanhMucChung("PHUONG_THUC_DG");
        Map<String, String> mapDmucLoaiXuat = getListDanhMucChung("LOAI_HINH_NHAP_XUAT");
        Map<String, String> mapDmucKieuXuat = getListDanhMucChung("KIEU_NHAP_XUAT");
        searchResultPage.getContent().forEach(data -> {
            try {
                data.setMapDmucDvi(mapDmucDvi);
                data.setMapDmucVthh(mapDmucVthh);
                data.setMapDmucLoaiXuat(mapDmucLoaiXuat);
                data.setMapDmucKieuXuat(mapDmucKieuXuat);
                data.setMapDmucHinhThuc(mapDmucHinhThuc);
                data.setMapDmucPhuongThuc(mapDmucPhuongThuc);
                data.setTrangThai(data.getTrangThai());
                data.setTrangThaiHd(data.getTrangThaiHd());
                data.setTrangThaiXh(data.getTrangThaiXh());
            } catch (Exception exception) {
                throw new RuntimeException(exception);
            }
        });
        return searchResultPage;
    }

    @Transactional
    public XhKqBdgHdr create(CustomUserDetails currentUser, XhKqBdgHdrReq request) throws Exception {
        if (currentUser == null || request == null) {
            throw new Exception("Bad request.");
        }
        if (!StringUtils.isEmpty(request.getSoQdKq()) && xhKqBdgHdrRepository.existsBySoQdKq(request.getSoQdKq())) {
            throw new Exception("Số quyết định kết quả " + request.getSoQdKq() + " đã tồn tại");
        }
        xhKqBdgHdrRepository.findByMaThongBao(request.getMaThongBao()).ifPresent(result -> {
            throw new RuntimeException("Mã thông báo này đã được quyết định kết quả bán đấu giá, xin vui lòng chọn mã thông báo khác.");
        });
        XhKqBdgHdr newData = new XhKqBdgHdr();
        BeanUtils.copyProperties(request, newData);
        newData.setMaDvi(currentUser.getDvql());
        newData.setNgayTao(LocalDate.now());
        newData.setNguoiTaoId(currentUser.getUser().getId());
        newData.setTrangThai(Contains.DUTHAO);
        newData.setTrangThaiHd(Contains.CHUA_THUC_HIEN);
        newData.setTrangThaiXh(Contains.CHUA_THUC_HIEN);
        return xhKqBdgHdrRepository.save(newData);
    }

    @Transactional
    public XhKqBdgHdr update(CustomUserDetails currentUser, XhKqBdgHdrReq request) throws Exception {
        if (currentUser == null || request == null || request.getId() == null) {
            throw new Exception("Bad request.");
        }
        XhKqBdgHdr existingData = xhKqBdgHdrRepository.findById(request.getId())
                .orElseThrow(() -> new Exception("Không tìm thấy dữ liệu cần sửa"));
        if (!StringUtils.isEmpty(request.getSoQdKq()) && xhKqBdgHdrRepository.existsBySoQdKqAndIdNot(request.getSoQdKq(), request.getId())) {
            throw new Exception("Số quyết định kết quả " + request.getSoQdKq() + " đã tồn tại");
        }
        BeanUtils.copyProperties(request, existingData, "id", "maDvi", "trangThaiHd", "trangThaiXh");
        existingData.setNgaySua(LocalDate.now());
        existingData.setNguoiSuaId(currentUser.getUser().getId());
        return xhKqBdgHdrRepository.save(existingData);
    }

    public List<XhKqBdgHdr> detail(List<Long> ids) throws Exception {
        if (DataUtils.isNullOrEmpty(ids)) {
            throw new Exception("Tham số không hợp lệ.");
        }
        List<XhKqBdgHdr> resultList = xhKqBdgHdrRepository.findByIdIn(ids);
        if (DataUtils.isNullOrEmpty(resultList)) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        Map<String, String> mapDmucVthh = getListDanhMucHangHoa();
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        Map<String, String> mapDmucHinhThuc = getListDanhMucChung("HINH_THUC_DG");
        Map<String, String> mapDmucPhuongThuc = getListDanhMucChung("PHUONG_THUC_DG");
        Map<String, String> mapDmucLoaiXuat = getListDanhMucChung("LOAI_HINH_NHAP_XUAT");
        Map<String, String> mapDmucKieuXuat = getListDanhMucChung("KIEU_NHAP_XUAT");
        for (XhKqBdgHdr item : resultList) {
            List<XhHopDongHdr> detailList = xhHopDongHdrRepository.findAllByIdQdKq(item.getId());
            detailList.forEach(detailItem -> {
                detailItem.setMapDmucDvi(mapDmucDvi);
                detailItem.setMapDmucVthh(mapDmucVthh);
                detailItem.setMapDmucLoaiXuat(mapDmucLoaiXuat);
                detailItem.setMapDmucKieuXuat(mapDmucKieuXuat);
                detailItem.setTrangThai(detailItem.getTrangThai());
                detailItem.setTrangThaiXh(detailItem.getTrangThaiXh());
            });
            item.setListHopDong(detailList);
            item.setMapDmucDvi(mapDmucDvi);
            item.setMapDmucVthh(mapDmucVthh);
            item.setMapDmucLoaiXuat(mapDmucLoaiXuat);
            item.setMapDmucKieuXuat(mapDmucKieuXuat);
            item.setMapDmucHinhThuc(mapDmucHinhThuc);
            item.setMapDmucPhuongThuc(mapDmucPhuongThuc);
            item.setTrangThai(item.getTrangThai());
            item.setTrangThaiXh(item.getTrangThaiXh());
            item.setTrangThaiHd(item.getTrangThaiHd());
        }
        return resultList;
    }

    public XhKqBdgHdr detail(Long id) throws Exception {
        if (id == null) {
            throw new Exception("Tham số không hợp lệ.");
        }
        List<XhKqBdgHdr> details = detail(Collections.singletonList(id));
        return details.isEmpty() ? null : details.get(0);
    }

    @Transactional
    public void delete(IdSearchReq idSearchReq) throws Exception {
        if (idSearchReq == null || idSearchReq.getId() == null) {
            throw new Exception("Bad request.");
        }
        XhKqBdgHdr proposalData = xhKqBdgHdrRepository.findById(idSearchReq.getId())
                .orElseThrow(() -> new Exception("Bản ghi không tồn tại"));
        List<String> allowedStatusList = Arrays.asList(Contains.DUTHAO, Contains.TU_CHOI_TP, Contains.TUCHOI_LDC);
        if (!allowedStatusList.contains(proposalData.getTrangThai())) {
            throw new Exception("Chỉ thực hiện xóa với quyết định ở trạng thái bản nháp hoặc từ chối.");
        }
        xhKqBdgHdrRepository.delete(proposalData);
    }

    @Transactional
    public void deleteMulti(IdSearchReq idSearchReq) throws Exception {
        if (idSearchReq == null || idSearchReq.getIdList() == null) {
            throw new Exception("Bad request.");
        }
        List<XhKqBdgHdr> proposalList = xhKqBdgHdrRepository.findAllByIdIn(idSearchReq.getIdList());
        if (proposalList.isEmpty()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        boolean isValidToDelete = proposalList.stream().allMatch(proposal ->
                proposal.getTrangThai().equals(Contains.DUTHAO) ||
                        proposal.getTrangThai().equals(Contains.TU_CHOI_TP) ||
                        proposal.getTrangThai().equals(Contains.TUCHOI_LDC));
        if (!isValidToDelete) {
            throw new Exception("Chỉ thực hiện xóa với quyết định ở trạng thái bản nháp hoặc từ chối.");
        }
        xhKqBdgHdrRepository.deleteAll(proposalList);
    }

    public XhKqBdgHdr approve(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {
        if (currentUser == null || StringUtils.isEmpty(statusReq.getId())) {
            throw new Exception("Bad request.");
        }
        XhKqBdgHdr proposal = xhKqBdgHdrRepository.findById(Long.valueOf(statusReq.getId()))
                .orElseThrow(() -> new Exception("Không tìm thấy dữ liệu"));
        String statusCombination = statusReq.getTrangThai() + proposal.getTrangThai();
        if (statusReq.getTrangThai().equals(Contains.DA_HOAN_THANH)
                && proposal.getTrangThaiHd().equals(Contains.DANG_THUC_HIEN)) {
            proposal.setTrangThaiHd(statusReq.getTrangThai());
        } else {
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
                case Contains.BAN_HANH + Contains.CHODUYET_LDC:
                    proposal.setNguoiPduyetId(currentUser.getUser().getId());
                    proposal.setNgayPduyet(LocalDate.now());
                    break;
                default:
                    throw new Exception("Phê duyệt không thành công");
            }
            proposal.setTrangThai(statusReq.getTrangThai());
            if (statusReq.getTrangThai().equals(Contains.BAN_HANH)) {
                xhQdPdKhBdgDtlRepository.findById(proposal.getIdQdPdDtl()).ifPresent(decision -> {
                    decision.setIdQdKq(proposal.getId());
                    decision.setSoQdKq(proposal.getSoQdKq());
                    decision.setNgayKyQdKq(proposal.getNgayKy());
                    xhQdPdKhBdgDtlRepository.save(decision);
                });
            }
        }
        return xhKqBdgHdrRepository.save(proposal);
    }

    public void export(CustomUserDetails currentUser, XhKqBdgHdrReq request, HttpServletResponse response) throws Exception {
        request.getPaggingReq().setPage(0);
        request.getPaggingReq().setLimit(Integer.MAX_VALUE);
        Page<XhKqBdgHdr> page = this.searchPage(currentUser, request);
        List<XhKqBdgHdr> dataList = page.getContent();
        String title = "Danh sách quyết định phê duyệt kết quả đấu giá hàng DTQG";
        String[] rowsName;
        boolean isVattuType = dataList.stream().anyMatch(item -> item.getLoaiVthh().startsWith(Contains.LOAI_VTHH_VATTU));
        String[] commonRowsName = new String[]{"STT", "Năm kế hoạch", "Số QĐ PD KQ BĐG", "Ngày ký", "Trích yếu", "Số QĐ PD KH BĐG", "Mã thông báo BĐG"};
        if (isVattuType) {
            String[] vattuRowsName = Arrays.copyOf(commonRowsName, commonRowsName.length + 6);
            vattuRowsName[7] = "Loại hàng DTQG";
            vattuRowsName[8] = "Chủng loại hàng DTQG";
            vattuRowsName[9] = "Hình thức đấu giá";
            vattuRowsName[10] = "Phương thức đấu giá";
            vattuRowsName[11] = "Số biên bản đấu giá";
            vattuRowsName[12] = "trạng thái";
            rowsName = vattuRowsName;
        } else {
            String[] nonVattuRowsName = Arrays.copyOf(commonRowsName, commonRowsName.length + 5);
            nonVattuRowsName[7] = "Chủng loại hàng DTQG";
            nonVattuRowsName[8] = "Hình thức đấu giá";
            nonVattuRowsName[9] = "Phương thức đấu giá";
            nonVattuRowsName[10] = "Số biên bản đấu giá";
            nonVattuRowsName[11] = "trạng thái";
            rowsName = nonVattuRowsName;
        }
        String fileName = "danh-sach-quyet-dinh-phe-duyet-ket-qua-dau-gia-hang-DTQG.xlsx";
        List<Object[]> excelDataList = new ArrayList<>();
        for (int i = 0; i < dataList.size(); i++) {
            XhKqBdgHdr proposal = dataList.get(i);
            Object[] excelRow = new Object[rowsName.length];
            excelRow[0] = i;
            excelRow[1] = proposal.getNam();
            excelRow[2] = proposal.getSoQdKq();
            excelRow[3] = LocalDateTimeUtils.localDateToString(proposal.getNgayKy());
            excelRow[4] = proposal.getTrichYeu();
            excelRow[5] = proposal.getSoQdPd();
            excelRow[6] = proposal.getMaThongBao();
            if (isVattuType) {
                excelRow[7] = proposal.getTenLoaiVthh();
                excelRow[8] = proposal.getTenCloaiVthh();
                excelRow[9] = proposal.getTenHinhThucDauGia();
                excelRow[10] = proposal.getTenPhuongThucDauGia();
                excelRow[11] = proposal.getSoBienBan();
                excelRow[12] = proposal.getTenTrangThai();
            } else {
                excelRow[7] = proposal.getTenCloaiVthh();
                excelRow[8] = proposal.getTenHinhThucDauGia();
                excelRow[9] = proposal.getTenPhuongThucDauGia();
                excelRow[10] = proposal.getSoBienBan();
                excelRow[11] = proposal.getTenTrangThai();
            }
            excelDataList.add(excelRow);
        }
        ExportExcel exportExcel = new ExportExcel(title, fileName, rowsName, excelDataList, response);
        exportExcel.export();
    }

    public void exportHd(CustomUserDetails currentUser, XhKqBdgHdrReq request, HttpServletResponse response) throws Exception {
        request.getPaggingReq().setPage(0);
        request.getPaggingReq().setLimit(Integer.MAX_VALUE);
        Page<XhKqBdgHdr> page = this.searchPage(currentUser, request);
        List<XhKqBdgHdr> dataList = page.getContent();
        String title = "Danh sách quản lý thông tin hợp đồng bán đấu giá hàng DTQG";
        String[] rowsName;
        boolean isVattuType = dataList.stream().anyMatch(item -> item.getLoaiVthh().startsWith(Contains.LOAI_VTHH_VATTU));
        String[] commonRowsName = new String[]{"STT", "Năm KH", "QĐ PD KHBĐG", "QĐ PD KQBĐG", "Tổng số ĐV tài sản", "Số ĐVTS ĐG thành công", "SL HĐ đã ký", "Thời hạn thanh toán"};
        if (isVattuType) {
            String[] vattuRowsName = Arrays.copyOf(commonRowsName, commonRowsName.length + 7);
            vattuRowsName[8] = "Loại hàng DTQG";
            vattuRowsName[9] = "Chủng loại hàng DTQG";
            vattuRowsName[10] = "ĐV thực hiện";
            vattuRowsName[11] = "Tổng SL xuất bán";
            vattuRowsName[12] = "Thành tiền (đ)";
            vattuRowsName[13] = "trạng thái HĐ";
            vattuRowsName[14] = "trạng thái XH";
            rowsName = vattuRowsName;
        } else {
            String[] nonVattuRowsName = Arrays.copyOf(commonRowsName, commonRowsName.length + 6);
            nonVattuRowsName[8] = "Chủng loại hàng DTQG";
            nonVattuRowsName[9] = "ĐV thực hiện";
            nonVattuRowsName[10] = "Tổng SL xuất bán (kg)";
            nonVattuRowsName[11] = "Thành tiền (đ)";
            nonVattuRowsName[12] = "trạng thái HĐ";
            nonVattuRowsName[13] = "trạng thái XH";
            rowsName = nonVattuRowsName;
        }
        String fileName = "danh-sach-quan-ly-thong-tin=hop-dong-ban-dau-gia-hang-DTQG.xlsx";
        List<Object[]> excelDataList = new ArrayList<>();
        for (int i = 0; i < dataList.size(); i++) {
            XhKqBdgHdr proposal = dataList.get(i);
            Object[] excelRow = new Object[rowsName.length];
            excelRow[0] = i;
            excelRow[1] = proposal.getNam();
            excelRow[2] = proposal.getSoQdPd();
            excelRow[3] = proposal.getSoQdKq();
            excelRow[4] = proposal.getTongDviTsan();
            excelRow[5] = proposal.getSlDviTsanThanhCong();
            excelRow[6] = proposal.getSlHopDongDaKy();
            excelRow[7] = LocalDateTimeUtils.localDateToString(proposal.getNgayKy());
            if (isVattuType) {
                excelRow[8] = proposal.getTenLoaiVthh();
                excelRow[9] = proposal.getTenCloaiVthh();
                excelRow[10] = proposal.getTenDvi();
                excelRow[11] = proposal.getTongSlXuat();
                excelRow[12] = proposal.getThanhTien();
                excelRow[13] = proposal.getTenTrangThaiHd();
                excelRow[14] = proposal.getTenTrangThaiXh();
            } else {
                excelRow[8] = proposal.getTenCloaiVthh();
                excelRow[9] = proposal.getTenDvi();
                excelRow[10] = proposal.getTongSlXuat();
                excelRow[11] = proposal.getThanhTien();
                excelRow[12] = proposal.getTenTrangThaiHd();
                excelRow[13] = proposal.getTenTrangThaiXh();
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
            XhKqBdgHdr reportDetail = this.detail(DataUtils.safeToLong(requestParams.get("id")));
            XhTcTtinBdgHdr informationHeader = null;
            if (reportDetail.getIdThongBao() != null) {
                informationHeader = xhTcTtinBdgHdrServiceImpl.detail(reportDetail.getIdThongBao());
                if (informationHeader != null) {
                    informationHeader.setTenDvi(informationHeader.getTenDvi().toUpperCase());
                    informationHeader.setTenCloaiVthh(informationHeader.getTenCloaiVthh().toUpperCase());
                    List<XhTcTtinBdgDtl> informationDetailsList = xhTcTtinBdgDtlRepository.findAllByIdHdr(informationHeader.getId());
                    for (XhTcTtinBdgDtl informationDetail : informationDetailsList) {
                        List<XhTcTtinBdgPlo> informationLoList = xhTcTtinBdgPloRepository.findAllByIdDtl(informationDetail.getId());
                        List<XhTcTtinBdgPlo> filteredLoList = informationLoList.stream().filter(type -> type.getToChucCaNhan() != null).collect(Collectors.toList());
                        informationDetail.setChildren(filteredLoList);
                    }
                    List<XhTcTtinBdgDtl> filteredThongTinDtl = informationDetailsList.stream().filter(type -> type.getChildren() != null && !type.getChildren().isEmpty()).collect(Collectors.toList());
                    informationHeader.setChildren(filteredThongTinDtl);
                }
            }
            if (informationHeader != null) {
                return docxToPdfConverter.convertDocxToPdf(templateInputStream, informationHeader);
            }
        } catch (IOException | XDocReportException exception) {
            exception.printStackTrace();
        }
        return null;
    }
}