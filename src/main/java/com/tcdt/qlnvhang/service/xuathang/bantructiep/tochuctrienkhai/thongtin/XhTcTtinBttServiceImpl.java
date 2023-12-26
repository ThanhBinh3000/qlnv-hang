package com.tcdt.qlnvhang.service.xuathang.bantructiep.tochuctrienkhai.thongtin;

import com.tcdt.qlnvhang.entities.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttDtl;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttDvi;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttDviDtl;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttHdr;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.tochuctrienkhai.thongtin.XhTcTtinBtt;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.hopdong.XhHopDongBttHdrRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttDviDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttDviRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttHdrRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.tochuctrienkhai.thongtin.XhTcTtinBttRepository;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttDtlReq;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttDviDtlReq;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttDviReq;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.tochuctrienkhai.thongtin.SearchXhTcTtinBttReq;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.tochuctrienkhai.thongtin.XhTcTtinBttReq;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.FileDinhKem;
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
public class XhTcTtinBttServiceImpl extends BaseServiceImpl {

    @Autowired
    private XhQdPdKhBttHdrRepository xhQdPdKhBttHdrRepository;
    @Autowired
    private XhQdPdKhBttDtlRepository xhQdPdKhBttDtlRepository;
    @Autowired
    private XhQdPdKhBttDviRepository xhQdPdKhBttDviRepository;
    @Autowired
    private XhQdPdKhBttDviDtlRepository xhQdPdKhBttDviDtlRepository;
    @Autowired
    private XhTcTtinBttRepository xhTcTtinBttRepository;
    @Autowired
    private XhHopDongBttHdrRepository xhHopDongBttHdrRepository;
    @Autowired
    private FileDinhKemService fileDinhKemService;

    public Page<XhQdPdKhBttDtl> searchPage(CustomUserDetails currentUser, SearchXhTcTtinBttReq request) throws Exception {
        request.setLastest(Integer.valueOf(1));
        request.setTrangThaiHdr(Contains.BAN_HANH);
        if (currentUser.getUser().getCapDvi().equals(Contains.CAP_CUC)) {
            request.setDvql(currentUser.getDvql());
        } else if (currentUser.getUser().getCapDvi().equals(Contains.CAP_CHI_CUC)) {
            request.setMaChiCuc(currentUser.getDvql());
        }
        Pageable pageable = PageRequest.of(request.getPaggingReq().getPage(), request.getPaggingReq().getLimit());
        Page<XhQdPdKhBttDtl> searchResultPage = xhQdPdKhBttDtlRepository.searchPage(request, pageable);
        Map<String, String> mapDmucVthh = getListDanhMucHangHoa();
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        searchResultPage.getContent().forEach(data -> {
            try {
                data.setMapDmucDvi(mapDmucDvi);
                data.setMapDmucVthh(mapDmucVthh);
                data.setTrangThai(data.getTrangThai());
                data.setTrangThaiXh(data.getTrangThaiXh());
                data.setTrangThaiHd(data.getTrangThaiHd());
                data.setXhQdPdKhBttHdr(xhQdPdKhBttHdrRepository.findById(data.getIdHdr()).orElse(null));
                List<XhQdPdKhBttDvi> listDvi = xhQdPdKhBttDviRepository.findAllByIdDtl(data.getId());
                data.setChildren(listDvi != null && !listDvi.isEmpty() ? listDvi : Collections.emptyList());
            } catch (Exception exception) {
                throw new RuntimeException(exception);
            }
        });
        return searchResultPage;
    }

    @Transactional
    public XhQdPdKhBttDtl create(CustomUserDetails currentUser, XhQdPdKhBttDtlReq request) throws Exception {
        if (currentUser == null || request == null) {
            throw new Exception("Bad request.");
        }
        XhQdPdKhBttDtl existingData = xhQdPdKhBttDtlRepository.findById(request.getIdDtl())
                .orElseThrow(() -> new Exception("Bản Ghi không tồn tại"));
        existingData.setPthucBanTrucTiep(request.getPthucBanTrucTiep());
        existingData.setDiaDiemChaoGia(request.getDiaDiemChaoGia());
        existingData.setGhiChuChaoGia(request.getGhiChuChaoGia());
        existingData.setTrangThai(Contains.DANG_THUC_HIEN);
        if (!DataUtils.isNullOrEmpty(request.getFileDinhKem())) {
            String tableName = request.getPthucBanTrucTiep().equals(Contains.UY_QUYEN) ?
                    XhQdPdKhBttDtl.TABLE_NAME + "_UY_QUYEN" :
                    XhQdPdKhBttDtl.TABLE_NAME + "_BAN_LE";
            List<FileDinhKem> fileDinhKem = fileDinhKemService.saveListFileDinhKem(request.getFileDinhKem(), existingData.getId(), tableName);
            existingData.setFileDinhKem(fileDinhKem);
        }
        if (existingData.getPthucBanTrucTiep().equals(Contains.CHAO_GIA)) {
            processChaoGiaData(request, existingData);
        }
        return xhQdPdKhBttDtlRepository.save(existingData);
    }

    private void processChaoGiaData(XhQdPdKhBttDtlReq request, XhQdPdKhBttDtl existingData) {
        for (XhQdPdKhBttDviReq detailRequest : request.getChildren()) {
            for (XhQdPdKhBttDviDtlReq subDetailRequest : detailRequest.getChildren()) {
                xhTcTtinBttRepository.deleteAllByIdDviDtl(subDetailRequest.getId());
                for (XhTcTtinBttReq quotationReq : subDetailRequest.getChildren()) {
                    XhTcTtinBtt quotation = new XhTcTtinBtt();
                    BeanUtils.copyProperties(quotationReq, quotation, "id");
                    quotation.setId(null);
                    quotation.setIdQdPdDtl(existingData.getId());
                    XhTcTtinBtt newData = xhTcTtinBttRepository.save(quotation);
                    fileDinhKemService.delete(newData.getId(), Collections.singleton(XhTcTtinBtt.TABLE_NAME));
                    if (!DataUtils.isNullObject(quotationReq.getFileDinhKems())) {
                        List<FileDinhKem> fileDinhKem = fileDinhKemService.saveListFileDinhKem(Collections.singletonList(quotationReq.getFileDinhKems()), newData.getId(), XhTcTtinBtt.TABLE_NAME);
                        quotation.setFileDinhKems(fileDinhKem.get(0));
                    }
                }
            }
        }
    }

    public List<XhQdPdKhBttDtl> detail(List<Long> ids) throws Exception {
        if (DataUtils.isNullOrEmpty(ids)) {
            throw new Exception("Tham số không hợp lệ.");
        }
        List<XhQdPdKhBttDtl> resultList = xhQdPdKhBttDtlRepository.findByIdIn(ids);
        if (DataUtils.isNullOrEmpty(resultList)) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        Map<String, String> mapDmucVthh = getListDanhMucHangHoa();
        Map<String, String> mapDmucLoaiXuat = getListDanhMucChung("LOAI_HINH_NHAP_XUAT");
        Map<String, String> mapDmucKieuXuat = getListDanhMucChung("KIEU_NHAP_XUAT");
        for (XhQdPdKhBttDtl item : resultList) {
            List<XhQdPdKhBttDvi> subCategoryList = xhQdPdKhBttDviRepository.findAllByIdDtl(item.getId());
            for (XhQdPdKhBttDvi subCategoryItem : subCategoryList) {
                List<XhQdPdKhBttDviDtl> subCategoryDetailList = xhQdPdKhBttDviDtlRepository.findAllByIdDvi(subCategoryItem.getId());
                subCategoryDetailList.forEach(subCategoryDetailItem -> {
                    List<XhTcTtinBtt> organization = xhTcTtinBttRepository.findAllByIdDviDtl(subCategoryDetailItem.getId());
                    organization.forEach(organizationItem -> {
                        List<FileDinhKem> fileDinhKem = fileDinhKemService.search(organizationItem.getId(), Arrays.asList(XhTcTtinBtt.TABLE_NAME));
                        if (!DataUtils.isNullOrEmpty(fileDinhKem)) {
                            organizationItem.setFileDinhKems(fileDinhKem.get(0));
                        }
                    });
                    subCategoryDetailItem.setMapDmucDvi(mapDmucDvi);
                    subCategoryDetailItem.setMapDmucVthh(mapDmucVthh);
                    subCategoryDetailItem.setChildren(organization);
                });
                subCategoryItem.setMapDmucDvi(mapDmucDvi);
                subCategoryItem.setChildren(subCategoryDetailList);
            }
            item.setMapDmucDvi(mapDmucDvi);
            item.setMapDmucVthh(mapDmucVthh);
            item.setMapDmucLoaiXuat(mapDmucLoaiXuat);
            item.setMapDmucKieuXuat(mapDmucKieuXuat);
            item.setTrangThai(item.getTrangThai());
            item.setTrangThaiXh(item.getTrangThaiXh());
            item.setTrangThaiHd(item.getTrangThaiHd());
            item.setListHopDongBtt(xhHopDongBttHdrRepository.findAllByIdChaoGia(item.getId()));
            item.setXhQdPdKhBttHdr(xhQdPdKhBttHdrRepository.findById(item.getIdHdr()).orElse(null));
            if (!DataUtils.isNullObject(item.getPthucBanTrucTiep())) {
                String tableSuffix = item.getPthucBanTrucTiep().equals(Contains.UY_QUYEN) ? "_UY_QUYEN" :
                        item.getPthucBanTrucTiep().equals(Contains.BAN_LE) ? "_BAN_LE" : "";
                List<FileDinhKem> fileDinhKems = fileDinhKemService.search(item.getId(), Arrays.asList(XhQdPdKhBttDtl.TABLE_NAME + tableSuffix));
                item.setFileDinhKem(fileDinhKems != null ? fileDinhKems : new ArrayList<>());
            }
            item.setChildren(subCategoryList.stream().filter(type -> !type.getIsKetQua()).collect(Collectors.toList()));
        }
        return resultList;
    }

    public XhQdPdKhBttDtl detail(Long id) throws Exception {
        if (id == null) {
            throw new Exception("Tham số không hợp lệ.");
        }
        List<XhQdPdKhBttDtl> details = detail(Collections.singletonList(id));
        return details.isEmpty() ? null : details.get(0);
    }

    public XhQdPdKhBttDtl approve(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {
        if (currentUser == null || StringUtils.isEmpty(statusReq.getId())) {
            throw new Exception("Bad request.");
        }
        XhQdPdKhBttDtl proposal = xhQdPdKhBttDtlRepository.findById(Long.valueOf(statusReq.getId()))
                .orElseThrow(() -> new Exception("Không tìm thấy dữ liệu"));
        String statusCombination = statusReq.getTrangThai() + proposal.getTrangThai();
        if (statusReq.getTrangThai().equals(NhapXuatHangTrangThaiEnum.DA_HOAN_THANH.getId())
                && proposal.getTrangThaiHd().equals(NhapXuatHangTrangThaiEnum.DANG_THUC_HIEN.getId())) {
            proposal.setTrangThaiHd(statusReq.getTrangThai());
        } else {
            switch (statusCombination) {
                case Contains.DA_HOAN_THANH + Contains.DANG_THUC_HIEN:
                    proposal.setNgayNhanCgia(LocalDate.now());
                    break;
                default:
                    throw new Exception("Phê duyệt không thành công");
            }
            proposal.setTrangThai(statusReq.getTrangThai());
        }
        return xhQdPdKhBttDtlRepository.save(proposal);
    }

    public void export(CustomUserDetails currentUser, SearchXhTcTtinBttReq request, HttpServletResponse response) throws Exception {
        request.getPaggingReq().setPage(0);
        request.getPaggingReq().setLimit(Integer.MAX_VALUE);
        Page<XhQdPdKhBttDtl> page = this.searchPage(currentUser, request);
        List<XhQdPdKhBttDtl> dataList = page.getContent();
        boolean isVattuType = dataList.stream().anyMatch(item -> item.getLoaiVthh().startsWith(Contains.LOAI_VTHH_VATTU));
        boolean userLevel = Contains.CAP_CHI_CUC.equals(currentUser.getUser().getCapDvi());
        if (userLevel) {
            this.exportChiCuc(response, dataList, isVattuType);
        } else {
            this.exportCuc(response, dataList, isVattuType);
        }
    }

    public void exportCuc(HttpServletResponse response, List<XhQdPdKhBttDtl> dataList, boolean isVattuType) throws Exception {
        String title = "Danh sách thông tin triển khai kế hoạch bán trực tiếp hàng DTQG";
        String[] rowsName;
        String[] commonRowsName = new String[]{"STT", "Số QĐ phê duyệt KH bán trực tiếp", "Số QĐ điều chỉnh KH bán trực tiếp", "Số đề xuất KH bán trực tiếp", "Phương thức bán trực tiếp", "Ngày nhận chào giá/Ngày ủy quyền", "Số QĐ PD KQ chào giá"};
        if (isVattuType) {
            String[] vattuRowsName = Arrays.copyOf(commonRowsName, commonRowsName.length + 3);
            vattuRowsName[7] = "Loại hàng DTQG";
            vattuRowsName[8] = "Chủng loại hàng DTQG";
            vattuRowsName[9] = "Trạng thái";
            rowsName = vattuRowsName;
        } else {
            String[] nonVattuRowsName = Arrays.copyOf(commonRowsName, commonRowsName.length + 2);
            nonVattuRowsName[7] = "Chủng loại hàng DTQG";
            nonVattuRowsName[8] = "Trạng thái";
            rowsName = nonVattuRowsName;
        }
        Map<String, String> pthucBanTrucTiepMap = new HashMap<>();
        pthucBanTrucTiepMap.put(Contains.CHAO_GIA, "Chào giá");
        pthucBanTrucTiepMap.put(Contains.UY_QUYEN, "Ủy quyền");
        pthucBanTrucTiepMap.put(Contains.BAN_LE, "Bán lẻ");
        String fileName = "danh-sach-thong-tin-trien-khai-ke-hoach-ban-truc-tiep-hang-DTQG.xlsx";
        List<Object[]> excelDataList = new ArrayList<>();
        for (int i = 0; i < dataList.size(); i++) {
            XhQdPdKhBttDtl proposal = dataList.get(i);
            String pthucBanTrucTiepValue = pthucBanTrucTiepMap.get(proposal.getPthucBanTrucTiep());
            Object[] excelRow = new Object[rowsName.length];
            excelRow[0] = i;
            excelRow[1] = proposal.getSoQdPd();
            excelRow[2] = proposal.getSoQdDc();
            excelRow[3] = proposal.getSoDxuat();
            excelRow[4] = pthucBanTrucTiepValue;
            excelRow[5] = LocalDateTimeUtils.localDateToString(proposal.getNgayNhanCgia());
            excelRow[6] = proposal.getSoQdKq();
            if (isVattuType) {
                excelRow[7] = proposal.getTenLoaiVthh();
                excelRow[8] = proposal.getTenCloaiVthh();
                excelRow[9] = proposal.getTenTrangThai();
            } else {
                excelRow[7] = proposal.getTenCloaiVthh();
                excelRow[8] = proposal.getTenTrangThai();
            }
            excelDataList.add(excelRow);
        }
        ExportExcel exportExcel = new ExportExcel(title, fileName, rowsName, excelDataList, response);
        exportExcel.export();
    }

    public void exportChiCuc(HttpServletResponse response, List<XhQdPdKhBttDtl> dataList, boolean isVattuType) throws Exception {
        String title = "Danh sách quyết định phê duyệt kế hoạch bán trực tiếp được ủy quyền/bán lẻ hàng DTQG";
        String[] rowsName;
        String[] commonRowsName = new String[]{"STT", "Số quyết định", "Số kế hoạch", "Năm kế hoạch", "Ngày duyệt", "Ngày ủy quyền", "Trích yếu"};
        if (isVattuType) {
            String[] vattuRowsName = Arrays.copyOf(commonRowsName, commonRowsName.length + 5);
            vattuRowsName[7] = "Loại hàng DTQG";
            vattuRowsName[8] = "Chủng loại hàng DTQG";
            vattuRowsName[9] = "Số lượng";
            vattuRowsName[10] = "Phương thức bán trực tiếp";
            vattuRowsName[11] = "Trạng thái";
            rowsName = vattuRowsName;
        } else {
            String[] nonVattuRowsName = Arrays.copyOf(commonRowsName, commonRowsName.length + 4);
            nonVattuRowsName[7] = "Chủng loại hàng DTQG";
            nonVattuRowsName[8] = "Số lượng (kg)";
            nonVattuRowsName[9] = "Phương thức bán trực tiếp";
            nonVattuRowsName[10] = "Trạng thái";
            rowsName = nonVattuRowsName;
        }
        Map<String, String> pthucBanTrucTiepMap = new HashMap<>();
        pthucBanTrucTiepMap.put(Contains.UY_QUYEN, "Ủy quyền");
        pthucBanTrucTiepMap.put(Contains.BAN_LE, "Bán lẻ");
        String fileName = "danh-sach-quyet-dinh-phe-duyet-ke-hoach-ban-truc-tiep-duoc-uy-quyen/ban-le-hang-DTQG.xlsx";
        List<Object[]> excelDataList = new ArrayList<>();
        for (int i = 0; i < dataList.size(); i++) {
            XhQdPdKhBttDtl proposal = dataList.get(i);
            String pthucBanTrucTiepValue = pthucBanTrucTiepMap.get(proposal.getPthucBanTrucTiep());
            XhQdPdKhBttHdr xhQdPdKhBttHdr = proposal.getXhQdPdKhBttHdr();
            String tenTrangThai = null;
            String trichYeu = null;
            LocalDate ngayPheDuyet = null;
            if (xhQdPdKhBttHdr != null) {
                tenTrangThai = NhapXuatHangTrangThaiEnum.getTenById(xhQdPdKhBttHdr.getTrangThai());
                trichYeu = xhQdPdKhBttHdr.getTrichYeu();
                ngayPheDuyet = xhQdPdKhBttHdr.getNgayPduyet();
            }
            Object[] excelRow = new Object[rowsName.length];
            excelRow[0] = i;
            excelRow[1] = proposal.getSoQdPd();
            excelRow[2] = proposal.getSoDxuat();
            excelRow[3] = proposal.getNamKh();
            excelRow[4] = ngayPheDuyet;
            excelRow[5] = LocalDateTimeUtils.localDateToString(proposal.getNgayNhanCgia());
            excelRow[6] = trichYeu;
            if (isVattuType) {
                excelRow[7] = proposal.getTenLoaiVthh();
                excelRow[8] = proposal.getTenCloaiVthh();
                excelRow[9] = proposal.getTongSoLuong();
                excelRow[10] = pthucBanTrucTiepValue;
                excelRow[11] = tenTrangThai;
            } else {
                excelRow[7] = proposal.getTenCloaiVthh();
                excelRow[8] = proposal.getTongSoLuong();
                excelRow[9] = pthucBanTrucTiepValue;
                excelRow[10] = tenTrangThai;
            }
            excelDataList.add(excelRow);
        }
        ExportExcel ex = new ExportExcel(title, fileName, rowsName, excelDataList, response);
        ex.export();
    }

    public void exportHd(CustomUserDetails currentUser, SearchXhTcTtinBttReq request, HttpServletResponse response) throws Exception {
        request.getPaggingReq().setPage(0);
        request.getPaggingReq().setLimit(Integer.MAX_VALUE);
        Page<XhQdPdKhBttDtl> page = this.searchPage(currentUser, request);
        List<XhQdPdKhBttDtl> dataList = page.getContent();
        String title = "Danh sách hợp đồng bán trực tiếp hàng DTQG";
        String[] rowsName;
        boolean isVattuType = dataList.stream().anyMatch(item -> item.getLoaiVthh().startsWith(Contains.LOAI_VTHH_VATTU));
        String[] commonRowsName = new String[]{"STT", "Năm KH", "QĐ PD KHBTT", "SL HĐ cần ký", "SL HĐ đã ký", "Thời hạn xuất kho"};
        if (isVattuType) {
            String[] vattuRowsName = Arrays.copyOf(commonRowsName, commonRowsName.length + 5);
            vattuRowsName[6] = "Loại hàng DTQG";
            vattuRowsName[7] = "Chủng loại hàng DTQG";
            vattuRowsName[8] = "Tổng giá trị hợp đồng";
            vattuRowsName[9] = "Trạng thái ký HĐ";
            vattuRowsName[10] = "trạng thái XH";
            rowsName = vattuRowsName;
        } else {
            String[] nonVattuRowsName = Arrays.copyOf(commonRowsName, commonRowsName.length + 4);
            nonVattuRowsName[6] = "Chủng loại hàng DTQG";
            nonVattuRowsName[7] = "Tổng giá trị hợp đồng";
            nonVattuRowsName[8] = "Trạng thái ký HĐ";
            nonVattuRowsName[9] = "trạng thái XH";
            rowsName = nonVattuRowsName;
        }
        String fileName = "danh-sach-hop-dong-ban-truc-tiep-hang-DTQG.xlsx";
        List<Object[]> excelDataList = new ArrayList<>();
        for (int i = 0; i < dataList.size(); i++) {
            XhQdPdKhBttDtl proposal = dataList.get(i);
            Object[] excelRow = new Object[rowsName.length];
            excelRow[0] = i;
            excelRow[1] = proposal.getNamKh();
            excelRow[2] = proposal.getSoQdPd();
            excelRow[3] = proposal.getSlHdChuaKy();
            excelRow[4] = proposal.getSlHdDaKy();
            excelRow[5] = LocalDateTimeUtils.localDateToString(proposal.getTgianDkienDen());
            if (isVattuType) {
                excelRow[6] = proposal.getTenLoaiVthh();
                excelRow[7] = proposal.getTenCloaiVthh();
                excelRow[8] = proposal.getThanhTienDuocDuyet();
                excelRow[9] = proposal.getTenTrangThaiHd();
                excelRow[10] = proposal.getTenTrangThaiXh();
            } else {
                excelRow[6] = proposal.getTenCloaiVthh();
                excelRow[7] = proposal.getThanhTienDuocDuyet();
                excelRow[8] = proposal.getTenTrangThaiHd();
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
            XhQdPdKhBttDtl reportDetail = this.detail(DataUtils.safeToLong(requestParams.get("id")));
            return docxToPdfConverter.convertDocxToPdf(templateInputStream, reportDetail);
        } catch (IOException | XDocReportException exception) {
            exception.printStackTrace();
        }
        return null;
    }
}