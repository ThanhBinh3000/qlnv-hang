package com.tcdt.qlnvhang.service.xuathang.bantructiep.tochuctrienkhai.ketqua;

import com.tcdt.qlnvhang.entities.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttDvi;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttDviDtl;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.tochuctrienkhai.ketqua.XhKqBttHdr;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.tochuctrienkhai.thongtin.XhTcTtinBtt;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.hopdong.XhHopDongBttHdrRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttDviDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttDviRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.tochuctrienkhai.ketqua.XhKqBttHdrRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.tochuctrienkhai.thongtin.XhTcTtinBttRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttDviDtlReq;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttDviReq;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.tochuctrienkhai.ketqua.XhKqBttHdrReq;
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
import java.util.List;
import java.util.stream.Collectors;

@Service
public class XhKqBttHdrServiceImpl extends BaseServiceImpl {

    @Autowired
    private XhKqBttHdrRepository xhKqBttHdrRepository;
    @Autowired
    private XhQdPdKhBttDviRepository xhQdPdKhBttDviRepository;
    @Autowired
    private XhQdPdKhBttDviDtlRepository xhQdPdKhBttDviDtlRepository;
    @Autowired
    private XhTcTtinBttRepository xhTcTtinBttRepository;
    @Autowired
    private XhQdPdKhBttDtlRepository xhQdPdKhBttDtlRepository;
    @Autowired
    private XhHopDongBttHdrRepository xhHopDongBttHdrRepository;
    @Autowired
    private FileDinhKemService fileDinhKemService;


    public Page<XhKqBttHdr> searchPage(CustomUserDetails currentUser, XhKqBttHdrReq request) throws Exception {
        if (currentUser.getUser().getCapDvi().equals(Contains.CAP_TONG_CUC)) {
            request.setTrangThai(Contains.DA_DUYET_LDC);
        } else if (currentUser.getUser().getCapDvi().equals(Contains.CAP_CUC)) {
            request.setDvql(currentUser.getDvql());
        }
        Pageable pageable = PageRequest.of(request.getPaggingReq().getPage(), request.getPaggingReq().getLimit());
        Page<XhKqBttHdr> searchResultPage = xhKqBttHdrRepository.searchPage(request, pageable);
        Map<String, String> mapDmucVthh = getListDanhMucHangHoa();
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        searchResultPage.getContent().forEach(data -> {
            try {
                data.setMapDmucDvi(mapDmucDvi);
                data.setMapDmucVthh(mapDmucVthh);
                data.setTrangThai(data.getTrangThai());
                data.setTrangThaiHd(data.getTrangThaiHd());
                data.setTrangThaiXh(data.getTrangThaiXh());
                List<XhQdPdKhBttDvi> listDtl =  xhQdPdKhBttDviRepository.findAllByIdQdKqHdr(data.getId());
                data.setChildren(listDtl != null && !listDtl.isEmpty() ? listDtl : Collections.emptyList());
            } catch (Exception exception) {
                throw new RuntimeException(exception);
            }
        });
        return searchResultPage;
    }

    @Transactional
    public XhKqBttHdr create(CustomUserDetails currentUser, XhKqBttHdrReq request) throws Exception {
        if (currentUser == null || request == null) {
            throw new Exception("Bad request.");
        }
        if (!StringUtils.isEmpty(request.getSoQdKq()) && xhKqBttHdrRepository.existsBySoQdKq(request.getSoQdKq())) {
            throw new Exception("Số quyết định kết quả " + request.getSoQdKq() + " đã tồn tại");
        }
        XhKqBttHdr newData = new XhKqBttHdr();
        BeanUtils.copyProperties(request, newData);
        newData.setMaDvi(currentUser.getDvql());
        newData.setNgayTao(LocalDate.now());
        newData.setNguoiTaoId(currentUser.getUser().getId());
        newData.setTrangThai(Contains.DUTHAO);
        newData.setTrangThaiHd(Contains.CHUA_THUC_HIEN);
        newData.setTrangThaiXh(Contains.CHUA_THUC_HIEN);
        XhKqBttHdr createdRecord = xhKqBttHdrRepository.save(newData);
        this.saveDetail(request, createdRecord.getId());
        return createdRecord;
    }

    @Transactional
    public XhKqBttHdr update(CustomUserDetails currentUser, XhKqBttHdrReq request) throws Exception {
        if (currentUser == null || request == null || request.getId() == null) {
            throw new Exception("Bad request.");
        }
        XhKqBttHdr existingData = xhKqBttHdrRepository.findById(request.getId())
                .orElseThrow(() -> new Exception("Không tìm thấy dữ liệu cần sửa"));
        if (!StringUtils.isEmpty(request.getSoQdKq()) && xhKqBttHdrRepository.existsBySoQdKqAndIdNot(request.getSoQdKq(), request.getId())) {
            throw new Exception("Số quyết định kết quả " + request.getSoQdKq() + " đã tồn tại");
        }
        BeanUtils.copyProperties(request, existingData, "id", "maDvi", "trangThaiHd", "trangThaiXh");
        existingData.setNgaySua(LocalDate.now());
        existingData.setNguoiSuaId(currentUser.getUser().getId());
        XhKqBttHdr updatedData = xhKqBttHdrRepository.save(existingData);
        this.saveDetail(request, updatedData.getId());
        return updatedData;
    }

    void saveDetail(XhKqBttHdrReq request, Long headerId) {
        xhQdPdKhBttDviRepository.deleteAllByIdQdKqHdr(headerId);
        for (XhQdPdKhBttDviReq childReq : request.getChildren().stream().filter(item -> item.getIsKetQua()).collect(Collectors.toList())) {
            XhQdPdKhBttDvi child = new XhQdPdKhBttDvi();
            BeanUtils.copyProperties(childReq, child, "id");
            child.setIdQdKqHdr(headerId);
            child.setIsKetQua(true);
            xhQdPdKhBttDviRepository.save(child);
            xhQdPdKhBttDviDtlRepository.deleteAllByIdDvi(childReq.getId());
            for (XhQdPdKhBttDviDtlReq childDtlReq : childReq.getChildren()) {
                XhQdPdKhBttDviDtl childDtl = new XhQdPdKhBttDviDtl();
                BeanUtils.copyProperties(childDtlReq, childDtl, "id");
                childDtl.setIdDvi(child.getId());
                xhQdPdKhBttDviDtlRepository.save(childDtl);
                xhTcTtinBttRepository.deleteAllByIdDviDtl(childDtlReq.getId());
                for (XhTcTtinBttReq tTinReq : childDtlReq.getChildren()) {
                    XhTcTtinBtt tTtin = new XhTcTtinBtt();
                    BeanUtils.copyProperties(tTinReq, tTtin, "id");
                    tTtin.setId(null);
                    tTtin.setIdDviDtl(childDtl.getId());
                    XhTcTtinBtt saveTtin = xhTcTtinBttRepository.save(tTtin);
                    if (!DataUtils.isNullObject(tTinReq.getFileDinhKems())) {
                        tTinReq.getFileDinhKems().setId(null);
                        List<FileDinhKem> fileDinhKem = fileDinhKemService.saveListFileDinhKem(
                                Collections.singletonList(tTinReq.getFileDinhKems()), saveTtin.getId(), XhTcTtinBtt.TABLE_NAME);
                        saveTtin.setFileDinhKems(fileDinhKem.get(0));
                    }
                }
            }
        }
    }

    public List<XhKqBttHdr> detail(List<Long> ids) throws Exception {
        if (DataUtils.isNullOrEmpty(ids)) {
            throw new Exception("Tham số không hợp lệ.");
        }
        List<XhKqBttHdr> resultList = xhKqBttHdrRepository.findAllById(ids);
        if (DataUtils.isNullOrEmpty(resultList)) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        Map<String, String> mapDmucVthh = getListDanhMucHangHoa();
        Map<String, String> mapDmucLoaiXuat = getListDanhMucChung("LOAI_HINH_NHAP_XUAT");
        Map<String, String> mapDmucKieuXuat = getListDanhMucChung("KIEU_NHAP_XUAT");
        for (XhKqBttHdr item : resultList) {
            List<XhQdPdKhBttDvi> subCategoryList = xhQdPdKhBttDviRepository.findAllByIdQdKqHdr(item.getId());
            for (XhQdPdKhBttDvi subCategoryItem : subCategoryList) {
                List<XhQdPdKhBttDviDtl> subCategoryDetailList = xhQdPdKhBttDviDtlRepository.findAllByIdDvi(subCategoryItem.getId());
                for (XhQdPdKhBttDviDtl subCategoryDetailItem : subCategoryDetailList) {
                    subCategoryDetailItem.setMapDmucDvi(mapDmucDvi);
                    subCategoryDetailItem.setMapDmucVthh(mapDmucVthh);
                    subCategoryItem.setDonGiaDuocDuyet(subCategoryDetailItem.getDonGiaDuocDuyet());
                    List<XhTcTtinBtt> organization = xhTcTtinBttRepository.findAllByIdDviDtl(subCategoryDetailItem.getId());
                    organization.forEach(organizationItem -> {
                        List<FileDinhKem> fileDinhKem = fileDinhKemService.search(organizationItem.getId(), Arrays.asList(XhTcTtinBtt.TABLE_NAME));
                        if (!DataUtils.isNullOrEmpty(fileDinhKem)) {
                            organizationItem.setFileDinhKems(fileDinhKem.get(0));
                        }
                    });
                    subCategoryDetailItem.setChildren(organization);
                }
                item.setDonViTinh(subCategoryItem.getDonViTinh());
                subCategoryItem.setMapDmucDvi(mapDmucDvi);
                subCategoryItem.setChildren(subCategoryDetailList);
            }
            item.setMapDmucDvi(mapDmucDvi);
            item.setMapDmucVthh(mapDmucVthh);
            item.setMapDmucLoaiXuat(mapDmucLoaiXuat);
            item.setMapDmucKieuXuat(mapDmucKieuXuat);
            item.setTrangThai(item.getTrangThai());
            item.setTrangThaiHd(item.getTrangThaiHd());
            item.setTrangThaiXh(item.getTrangThaiXh());
            item.setListHopDongBtt(xhHopDongBttHdrRepository.findAllByIdQdKq(item.getId()));
            item.setChildren(subCategoryList.stream().filter(type -> type.getIsKetQua()).collect(Collectors.toList()));
        }
        return resultList;
    }

    public XhKqBttHdr detail(Long id) throws Exception {
        if (id == null) {
            throw new Exception("Tham số không hợp lệ.");
        }
        List<XhKqBttHdr> details = detail(Collections.singletonList(id));
        return details.isEmpty() ? null : details.get(0);
    }

    @Transactional
    public void delete(IdSearchReq idSearchReq) throws Exception {
        if (idSearchReq == null || idSearchReq.getId() == null) {
            throw new Exception("Bad request.");
        }
        XhKqBttHdr proposalData = xhKqBttHdrRepository.findById(idSearchReq.getId())
                .orElseThrow(() -> new Exception("Bản ghi không tồn tại"));
        List<String> allowedStatusList = Arrays.asList(Contains.DU_THAO, Contains.TU_CHOI_TP, Contains.TUCHOI_LDC);
        if (!allowedStatusList.contains(proposalData.getTrangThai())) {
            throw new Exception("Chỉ thực hiện xóa với quyết định ở trạng thái bản nháp hoặc từ chối.");
        }
        List<XhQdPdKhBttDvi> proposalDetailsList = xhQdPdKhBttDviRepository.findAllByIdQdKqHdr(proposalData.getId());
        proposalDetailsList.forEach(proposalDetai -> {
            List<XhQdPdKhBttDviDtl> proposalDonViList = xhQdPdKhBttDviDtlRepository.findAllByIdDvi(proposalDetai.getId());
            proposalDonViList.forEach(proposalDonVi -> xhTcTtinBttRepository.deleteAllByIdDviDtl(proposalDonVi.getId()));
            xhQdPdKhBttDviDtlRepository.deleteAllByIdDvi(proposalDetai.getId());
        });
        xhQdPdKhBttDviRepository.deleteAllByIdQdKqHdr(proposalData.getId());
        xhKqBttHdrRepository.delete(proposalData);
        fileDinhKemService.delete(proposalData.getId(), Collections.singleton(XhTcTtinBtt.TABLE_NAME));
    }

    @Transactional
    public void deleteMulti(IdSearchReq idSearchReq) throws Exception {
        if (idSearchReq == null || idSearchReq.getIdList() == null) {
            throw new Exception("Bad request.");
        }
        List<XhKqBttHdr> proposalList = xhKqBttHdrRepository.findAllByIdIn(idSearchReq.getIdList());
        if (proposalList.isEmpty()) {
            throw new Exception("Không tìm thấy bản ghi để xóa");
        }
        boolean isValidToDelete = proposalList.stream().allMatch(proposal ->
                proposal.getTrangThai().equals(Contains.DUTHAO) ||
                        proposal.getTrangThai().equals(Contains.TU_CHOI_TP) ||
                        proposal.getTrangThai().equals(Contains.TUCHOI_LDC));
        if (!isValidToDelete) {
            throw new Exception("Chỉ thực hiện xóa với quyết định ở trạng thái bản nháp hoặc từ chối.");
        }
        List<Long> proposalIds = proposalList.stream().map(XhKqBttHdr::getId).collect(Collectors.toList());
        List<XhQdPdKhBttDvi> proposalDetailsList = xhQdPdKhBttDviRepository.findByIdQdKqHdrIn(proposalIds);
        List<Long> detailIds = proposalDetailsList.stream().map(XhQdPdKhBttDvi::getId).collect(Collectors.toList());
        List<XhQdPdKhBttDviDtl> proposalDonViList = xhQdPdKhBttDviDtlRepository.findByIdDviIn(detailIds);
        List<Long> proposalDonViIds = proposalDonViList.stream().map(XhQdPdKhBttDviDtl::getId).collect(Collectors.toList());
        List<XhTcTtinBtt> organizationList = xhTcTtinBttRepository.findByIdDviDtlIn(proposalDonViIds);
        List<Long> organizationIds = organizationList.stream().map(XhTcTtinBtt::getId).collect(Collectors.toList());
        xhTcTtinBttRepository.deleteAll(organizationList);
        xhQdPdKhBttDviDtlRepository.deleteAll(proposalDonViList);
        xhQdPdKhBttDviRepository.deleteAll(proposalDetailsList);
        xhKqBttHdrRepository.deleteAll(proposalList);
        fileDinhKemService.deleteMultiple(organizationIds, Collections.singleton(XhTcTtinBtt.TABLE_NAME));
    }

    public XhKqBttHdr approve(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {
        if (currentUser == null || StringUtils.isEmpty(statusReq.getId())) {
            throw new Exception("Bad request.");
        }
        XhKqBttHdr proposal = xhKqBttHdrRepository.findById(Long.valueOf(statusReq.getId()))
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
                case Contains.DA_DUYET_LDC + Contains.CHODUYET_LDC:
                    proposal.setNguoiPduyetId(currentUser.getUser().getId());
                    proposal.setNgayPduyet(LocalDate.now());
                    break;
                default:
                    throw new Exception("Phê duyệt không thành công");
            }
            proposal.setTrangThai(statusReq.getTrangThai());
        }
        XhKqBttHdr updateData = xhKqBttHdrRepository.save(proposal);
        if (updateData.getTrangThai().equals(Contains.DA_DUYET_LDC)) {
            xhQdPdKhBttDtlRepository.findById(updateData.getIdChaoGia()).ifPresent(proposalQd -> {
                proposalQd.setIdQdKq(updateData.getId());
                proposalQd.setSoQdKq(updateData.getSoQdKq());
                xhQdPdKhBttDtlRepository.save(proposalQd);
            });
        }
        return updateData;
    }

    public void export(CustomUserDetails currentUser, XhKqBttHdrReq request, HttpServletResponse response) throws Exception {
        request.getPaggingReq().setPage(0);
        request.getPaggingReq().setLimit(Integer.MAX_VALUE);
        Page<XhKqBttHdr> page = this.searchPage(currentUser, request);
        List<XhKqBttHdr> dataList = page.getContent();
        String title = "Danh sách quyết định phê duyệt kết quả chào giá hàng DTQG";
        String[] rowsName;
        boolean isVattuType = dataList.stream().anyMatch(item -> item.getLoaiVthh().startsWith(Contains.LOAI_VTHH_VATTU));
        String[] commonRowsName = new String[]{"STT", "Số QĐ PDKQ chào giá", "Ngày ký QĐ", "Trích yếu", "Đơn vị", "Số QĐ PDKH bán trực tiếp"};
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
        String fileName = "danh-sach-quyet-dinh-phe-duyet-ket-qua-chao-gia-hang-DTQG.xlsx";
        List<Object[]> excelDataList = new ArrayList<>();
        for (int i = 0; i < dataList.size(); i++) {
            XhKqBttHdr proposal = dataList.get(i);
            Object[] excelRow = new Object[rowsName.length];
            excelRow[0] = i;
            excelRow[1] = proposal.getSoQdKq();
            excelRow[2] = LocalDateTimeUtils.localDateToString(proposal.getNgayKy());
            excelRow[3] = proposal.getTrichYeu();
            excelRow[4] = proposal.getTenDvi();
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

    public void exportHdong(CustomUserDetails currentUser, XhKqBttHdrReq req, HttpServletResponse response) throws Exception {
        req.getPaggingReq().setPage(0);
        req.getPaggingReq().setLimit(Integer.MAX_VALUE);
        Page<XhKqBttHdr> page = this.searchPage(currentUser, req);
        List<XhKqBttHdr> dataList = page.getContent();
        String title = "Danh sách quản lý hợp đồng hàng DTQG theo phương thức trực tiếp";
        String[] rowsName;
        boolean isVattuType = dataList.stream().anyMatch(item -> item.getLoaiVthh().startsWith(Contains.LOAI_VTHH_VATTU));
        String[] commonRowsName = new String[]{"STT", "Năm KH", "QĐ PD KHBTT", "QĐ PD KQ chào giá", "SL HĐ cần ký", "SL HĐ đã ký", "Thời hạn xuất kho"};
        if (isVattuType) {
            String[] vattuRowsName = Arrays.copyOf(commonRowsName, commonRowsName.length + 5);
            vattuRowsName[7] = "Loại hàng DTQG";
            vattuRowsName[8] = "Chủng loại hàng DTQG";
            vattuRowsName[9] = "Tổng giá trị hợp đồng";
            vattuRowsName[10] = "Trạng thái Ký HĐ";
            vattuRowsName[11] = "Trạng thái Xh";
            rowsName = vattuRowsName;
        } else {
            String[] nonVattuRowsName = Arrays.copyOf(commonRowsName, commonRowsName.length + 4);
            nonVattuRowsName[7] = "Chủng loại hàng DTQG";
            nonVattuRowsName[8] = "Tổng giá trị hợp đồng";
            nonVattuRowsName[9] = "Trạng thái Ký HĐ";
            nonVattuRowsName[10] = "Trạng thái Xh";
            rowsName = nonVattuRowsName;
        }
        String fileName = "danh-sach-quan-ly-hop-dong-hang-DTQG-theo-phuong-thuc-ban-truc-tiep.xlsx";
        List<Object[]> excelDataList = new ArrayList<>();
        for (int i = 0; i < dataList.size(); i++) {
            XhKqBttHdr proposal = dataList.get(i);
            Object[] excelRow = new Object[rowsName.length];
            excelRow[0] = i;
            excelRow[1] = proposal.getNamKh();
            excelRow[2] = proposal.getSoQdPd();
            excelRow[3] = proposal.getSoQdKq();
            excelRow[4] = proposal.getSlHdChuaKy();
            excelRow[5] = proposal.getSlHdDaKy();
            excelRow[6] = LocalDateTimeUtils.localDateToString(proposal.getNgayKthuc());
            if (isVattuType) {
                excelRow[7] = proposal.getTenLoaiVthh();
                excelRow[8] = proposal.getTenCloaiVthh();
                excelRow[9] = proposal.getTongGiaTriHdong();
                excelRow[10] = proposal.getTenTrangThaiHd();
                excelRow[11] = proposal.getTenTrangThaiXh();
            } else {
                excelRow[7] = proposal.getTenCloaiVthh();
                excelRow[8] = proposal.getTongGiaTriHdong();
                excelRow[9] = proposal.getTenTrangThaiHd();
                excelRow[10] = proposal.getTenTrangThaiXh();
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
            XhKqBttHdr reportDetail = this.detail(DataUtils.safeToLong(requestParams.get("id")));
            List<XhQdPdKhBttDvi> listDvi = xhQdPdKhBttDviRepository.findAllByIdQdKqHdr(reportDetail.getId());
            listDvi.forEach(dataDvi -> {
                List<XhQdPdKhBttDviDtl> listDviDtl = xhQdPdKhBttDviDtlRepository.findAllByIdDvi(dataDvi.getId());
                listDviDtl.forEach(dataDviDtl -> {
                    List<XhTcTtinBtt> listTtin = xhTcTtinBttRepository.findAllByIdDviDtl(dataDviDtl.getId());
                    dataDviDtl.setChildren(listTtin.stream().filter(item -> item.getLuaChon()).collect(Collectors.toList()));
                });
            });
            return docxToPdfConverter.convertDocxToPdf(templateInputStream, reportDetail);
        } catch (IOException | XDocReportException exception) {
            exception.printStackTrace();
        }
        return null;
    }
}