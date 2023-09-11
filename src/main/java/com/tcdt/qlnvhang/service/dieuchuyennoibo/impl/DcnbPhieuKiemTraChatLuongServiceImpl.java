package com.tcdt.qlnvhang.service.dieuchuyennoibo.impl;

import com.google.common.collect.Lists;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.FileDinhKemRepository;
import com.tcdt.qlnvhang.repository.dieuchuyennoibo.DcnbPhieuKtChatLuongDtlRepository;
import com.tcdt.qlnvhang.repository.dieuchuyennoibo.DcnbPhieuKtChatLuongHdrRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.dieuchuyennoibo.*;
import com.tcdt.qlnvhang.request.object.dcnbBangKeCanHang.DcnbPhieuKtChatLuongHdrPreview;
import com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbPhieuKtChatLuongHdrDTO;
import com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbPhieuKtChatLuongHdrLsDTO;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.*;
import com.tcdt.qlnvhang.table.report.ReportTemplate;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.DataUtils;
import com.tcdt.qlnvhang.util.ExportExcel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.Transient;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DcnbPhieuKiemTraChatLuongServiceImpl extends BaseServiceImpl {

    @Autowired
    private DcnbPhieuKtChatLuongHdrRepository dcnbPhieuKtChatLuongHdrRepository;

    @Autowired
    private DcnbPhieuKtChatLuongDtlRepository dcnbPhieuKtChatLuongDtlRepository;

    @Autowired
    private FileDinhKemService fileDinhKemService;

    @Autowired
    private FileDinhKemRepository fileDinhKemRepository;

    public Page<DcnbPhieuKtChatLuongHdrDTO> searchPage(CustomUserDetails currentUser, SearchPhieuKtChatLuong req) throws Exception {
        String dvql = currentUser.getDvql();
        req.setMaDvi(dvql);
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        if(req.getIsVatTu() == null){
            req.setIsVatTu(false);
        }
        if(req.getIsVatTu()){
            req.setDsLoaiHang(Arrays.asList("VT"));
        }else {
            req.setDsLoaiHang(Arrays.asList("LT","M"));
        }
        Page<DcnbPhieuKtChatLuongHdrDTO> search = null;
        req.setTypeQd(Contains.NHAN_DIEU_CHUYEN);
        search = dcnbPhieuKtChatLuongHdrRepository.searchPage(req, pageable);

        return search;
    }

    @Transactional
    public DcnbPhieuKtChatLuongHdr save(CustomUserDetails currentUser, DcnbPhieuKtChatLuongHdrReq objReq) throws Exception {
        if (currentUser == null) {
            throw new Exception("Bad request.");
        }
        if(!currentUser.getUser().getCapDvi().equals(Contains.CAP_CHI_CUC)){
            throw new Exception("Chức năng thêm mới chỉ dành cho cấp chi cục");
        }
//        Optional<DcnbPhieuKtChatLuongHdr> optional = dcnbPhieuKtChatLuongHdrRepository.findFirstBySoPhieu(objReq.getSoPhieu());
//        if (optional.isPresent() && objReq.getSoPhieu().split("/").length == 1) {
//            throw new Exception("Số phiếu đã tồn tại");
//        }
        DcnbPhieuKtChatLuongHdr data = new DcnbPhieuKtChatLuongHdr();
        BeanUtils.copyProperties(objReq, data);
        data.setNguoiTaoId(currentUser.getUser().getId());
        data.setNgayTao(LocalDateTime.now());
        data.setMaDvi(currentUser.getDvql());
        data.setTrangThai(Contains.DUTHAO);
        if(objReq.getDcnbPhieuKtChatLuongDtl()!= null){
            objReq.getDcnbPhieuKtChatLuongDtl().forEach(e -> e.setDcnbPhieuKtChatLuongHdr(data));
        }
        DcnbPhieuKtChatLuongHdr created = dcnbPhieuKtChatLuongHdrRepository.save(data);
        String so = created.getId() + "/" + (new Date().getYear() + 1900) +"/PKTCL-"+ currentUser.getUser().getDvqlTenVietTat();
        created.setSoPhieu(so);
        dcnbPhieuKtChatLuongHdrRepository.save(created);
        List<FileDinhKem> bienBanLayMauDinhKem = fileDinhKemService.saveListFileDinhKem(objReq.getBienBanLayMauDinhKem(), created.getId(), DcnbPhieuKtChatLuongHdr.TABLE_NAME + "_CAN_CU");
        data.setBienBanLayMauDinhKem(bienBanLayMauDinhKem);
        return created;
    }

    @Transactional
    public DcnbPhieuKtChatLuongHdr update(CustomUserDetails currentUser, DcnbPhieuKtChatLuongHdrReq objReq) throws Exception {
        if (currentUser == null) {
            throw new Exception("Bad request.");
        }
        if(!currentUser.getUser().getCapDvi().equals(Contains.CAP_CHI_CUC)){
            throw new Exception("Chức năng cập nhật chỉ dành cho cấp cục");
        }
        Optional<DcnbPhieuKtChatLuongHdr> optional = dcnbPhieuKtChatLuongHdrRepository.findById(objReq.getId());
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu cần sửa");
        }
//        Optional<DcnbPhieuKtChatLuongHdr> soPhieu = dcnbPhieuKtChatLuongHdrRepository.findFirstBySoPhieu(objReq.getSoPhieu());
//        if (org.apache.commons.lang3.StringUtils.isNotEmpty(objReq.getSoPhieu())) {
//            if (soPhieu.isPresent() && objReq.getSoPhieu().split("/").length == 1) {
//                if (!soPhieu.get().getId().equals(objReq.getId())) {
//                    throw new Exception("Số phiếu đã tồn tại");
//                }
//            }
//        }

        DcnbPhieuKtChatLuongHdr data = optional.get();
        BeanUtils.copyProperties(objReq, data);
        data.setNguoiSuaId(currentUser.getUser().getId());
        data.setNgaySua(LocalDateTime.now());
        data.setDcnbPhieuKtChatLuongDtl(objReq.getDcnbPhieuKtChatLuongDtl());
        DcnbPhieuKtChatLuongHdr created = dcnbPhieuKtChatLuongHdrRepository.save(data);
        String so = created.getId() + "/" + (new Date().getYear() + 1900) +"/PKTCL-"+ currentUser.getUser().getDvqlTenVietTat();
        created.setSoPhieu(so);
        dcnbPhieuKtChatLuongHdrRepository.save(created);
        fileDinhKemService.delete(objReq.getId(), Lists.newArrayList(DcnbPhieuKtChatLuongHdr.TABLE_NAME + "_CAN_CU"));
        List<FileDinhKem> bienBanLayMauDinhKem = fileDinhKemService.saveListFileDinhKem(objReq.getBienBanLayMauDinhKem(), created.getId(), DcnbPhieuKtChatLuongHdr.TABLE_NAME + "_CAN_CU");
        data.setBienBanLayMauDinhKem(bienBanLayMauDinhKem);
        return created;
    }

    public List<DcnbPhieuKtChatLuongHdr> detail(List<Long> ids) throws Exception {
        if (DataUtils.isNullOrEmpty(ids))
            throw new Exception("Tham số không hợp lệ.");
        List<DcnbPhieuKtChatLuongHdr> optional = dcnbPhieuKtChatLuongHdrRepository.findByIdIn(ids);
        if (DataUtils.isNullOrEmpty(optional)) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        List<DcnbPhieuKtChatLuongHdr> allById = dcnbPhieuKtChatLuongHdrRepository.findAllById(ids);
        allById.forEach(data -> {
            List<FileDinhKem> bienBanLayMauDinhKem = fileDinhKemRepository.findByDataIdAndDataTypeIn(data.getId(),Collections.singleton(DcnbPhieuKtChatLuongHdr.TABLE_NAME + "_CAN_CU"));
            data.setBienBanLayMauDinhKem(bienBanLayMauDinhKem);
            List<DcnbPhieuKtChatLuongDtl> khs = dcnbPhieuKtChatLuongDtlRepository.findByHdrId(data.getId());
            data.setDcnbPhieuKtChatLuongDtl(khs);
        });
        return allById;
    }

    public DcnbPhieuKtChatLuongHdr detail(Long id) throws Exception {
        List<DcnbPhieuKtChatLuongHdr> details = detail(Arrays.asList(id));
        return details.isEmpty() ? null : details.get(0);
    }

    @Transient
    public void delete(IdSearchReq idSearchReq) throws Exception {
        Optional<DcnbPhieuKtChatLuongHdr> optional = dcnbPhieuKtChatLuongHdrRepository.findById(idSearchReq.getId());
        if (!optional.isPresent()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        DcnbPhieuKtChatLuongHdr data = optional.get();
        List<DcnbPhieuKtChatLuongDtl> list = dcnbPhieuKtChatLuongDtlRepository.findByHdrId(data.getId());
        dcnbPhieuKtChatLuongDtlRepository.deleteAll(list);
        fileDinhKemService.delete(idSearchReq.getId(), Lists.newArrayList(DcnbBienBanLayMauHdr.TABLE_NAME + "_CAN_CU"));
        dcnbPhieuKtChatLuongHdrRepository.delete(data);
    }

    @Transient
    public void deleteMulti(IdSearchReq idSearchReq) throws Exception {
        List<DcnbPhieuKtChatLuongHdr> list = dcnbPhieuKtChatLuongHdrRepository.findAllByIdIn(idSearchReq.getIdList());

        if (list.isEmpty()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        List<Long> listId = list.stream().map(DcnbPhieuKtChatLuongHdr::getId).collect(Collectors.toList());
        List<DcnbPhieuKtChatLuongDtl> listDtl = dcnbPhieuKtChatLuongDtlRepository.findByHdrIdIn(listId);
        dcnbPhieuKtChatLuongDtlRepository.deleteAll(listDtl);
        fileDinhKemService.deleteMultiple(idSearchReq.getIdList(), Lists.newArrayList(DcnbBienBanLayMauHdr.TABLE_NAME + "_CAN_CU"));
        dcnbPhieuKtChatLuongHdrRepository.deleteAll(list);
    }

    public void approve(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {
        if (StringUtils.isEmpty(statusReq.getId())) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        DcnbPhieuKtChatLuongHdr details = detail(Long.valueOf(statusReq.getId()));
        Optional<DcnbPhieuKtChatLuongHdr> optional = Optional.of(details);
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        this.approve(currentUser, statusReq, optional); // Truyền giá trị của optional vào
    }

    public DcnbPhieuKtChatLuongHdr approve(CustomUserDetails currentUser, StatusReq statusReq, Optional<DcnbPhieuKtChatLuongHdr> optional) throws Exception {
        String status = optional.get().getTrangThai() + statusReq.getTrangThai();
        switch (status) {
            case Contains.DUTHAO + Contains.CHODUYET_LDCC:
            case Contains.TUCHOI_LDCC + Contains.CHODUYET_LDCC:
                optional.get().setNguoiGDuyet(currentUser.getUser().getId());
                optional.get().setNgayGDuyet(LocalDate.now());
                break;
            case Contains.CHODUYET_LDCC + Contains.TUCHOI_LDCC:
                optional.get().setNguoiPDuyet(currentUser.getUser().getId());
                optional.get().setNgayPDuyet(LocalDate.now());
                optional.get().setLyDoTuChoi(statusReq.getLyDoTuChoi());
                break;
            case Contains.CHODUYET_LDCC + Contains.DADUYET_LDCC:
                optional.get().setNguoiPDuyet(currentUser.getUser().getId());
                optional.get().setNgayPDuyet(LocalDate.now());
//                DcnbDataLinkHdr dataLink = dcnbDataLinkHdrRepository.findDataLinkCuc(optional.get().getMaDvi(),
//                        optional.get().getQdDcId(),
//                        optional.get().getMaNganKhoXuat(),
//                        optional.get().getMaLoKhoXuat());
//                DcnbDataLinkDtl dataLinkDtl = new DcnbDataLinkDtl();
//                dataLinkDtl.setLinkId(optional.get().getId());
//                dataLinkDtl.setHdrId(dataLink.getId());
//                dataLinkDtl.setType(DcnbPhieuKtChatLuongHdr.TABLE_NAME);
//                dcnbDataLinkDtlRepository.save(dataLinkDtl);
                break;
            default:
                throw new Exception("Phê duyệt không thành công");
        }
        optional.get().setTrangThai(statusReq.getTrangThai());
        DcnbPhieuKtChatLuongHdr created = dcnbPhieuKtChatLuongHdrRepository.save(optional.get());
        return created;
    }

    public void export(CustomUserDetails currentUser, SearchPhieuKtChatLuong objReq, HttpServletResponse response) throws Exception {
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        objReq.setPaggingReq(paggingReq);
        Page<DcnbPhieuKtChatLuongHdrDTO> page = this.searchPage(currentUser, objReq);
        List<DcnbPhieuKtChatLuongHdrDTO> data = page.getContent();

        String title = "Danh sách phương án xuất cứu trợ, viện trợ ";
        String[] rowsName = new String[]{"STT", "Năm kH", "Số công văn/đề xuất", "Ngày duyệt LĐ Cục", "Loại điều chuyển", "Đơn vị đề xuất", "Trạng thái",};
        String fileName = "danh-sach-ke-hoach-dieu-chuyen-noi-bo-hang-dtqg.xlsx";
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;
        for (int i = 0; i < data.size(); i++) {
            DcnbPhieuKtChatLuongHdrDTO dx = data.get(i);
            objs = new Object[rowsName.length];
            objs[0] = i;
            objs[1] = dx.getSoQdinh();
            objs[2] = dx.getNam();
            objs[6] = dx.getThayDoiThuKho();
            dataList.add(objs);
        }
        ExportExcel ex = new ExportExcel(title, fileName, rowsName, dataList, response);
        ex.export();
    }

    public List<DcnbPhieuKtChatLuongHdrLsDTO> searchList(CustomUserDetails currentUser, SearchPhieuKtChatLuong req) {
        String dvql = currentUser.getDvql();
        req.setMaDvi(dvql);
        if(req.getIsVatTu() == null){
            req.setIsVatTu(false);
        }
        if(req.getIsVatTu()){
            req.setDsLoaiHang(Arrays.asList("VT"));
        }else {
            req.setDsLoaiHang(Arrays.asList("LT","M"));
        }
        List<DcnbPhieuKtChatLuongHdrLsDTO> search = dcnbPhieuKtChatLuongHdrRepository.searchList(req);
        return search;
    }

    public ReportTemplateResponse preview(DcnbPhieuKtChatLuongHdrReq objReq) throws Exception {
        Optional<DcnbPhieuKtChatLuongHdr> dcnbPhieuKtChatLuongHdr = dcnbPhieuKtChatLuongHdrRepository.findById(objReq.getId());
        if (!dcnbPhieuKtChatLuongHdr.isPresent()) throw new Exception("Không tồn tại bản ghi");
        ReportTemplate model = findByTenFile(objReq.getReportTemplateRequest());
        byte[] byteArray = Base64.getDecoder().decode(model.getFileUpload());
        ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArray);
        DcnbPhieuKtChatLuongHdrPreview dcnbPhieuKtChatLuongHdrPreview = setDataToPreview(dcnbPhieuKtChatLuongHdr);
        return docxToPdfConverter.convertDocxToPdf(inputStream, dcnbPhieuKtChatLuongHdrPreview);
    }

    private DcnbPhieuKtChatLuongHdrPreview setDataToPreview(Optional<DcnbPhieuKtChatLuongHdr> dcnbPhieuKtChatLuongHdr) {
        return DcnbPhieuKtChatLuongHdrPreview.builder()
                .tenDvi("Tên đơn vị")
                .maDvi(dcnbPhieuKtChatLuongHdr.get().getMaDvi())
                .maQhns(dcnbPhieuKtChatLuongHdr.get().getMaQhns())
                .loaiHangHoa("Loại hàng DTQG")
                .soPhieu(dcnbPhieuKtChatLuongHdr.get().getSoPhieu())
                .nguoiGiaoHang(dcnbPhieuKtChatLuongHdr.get().getNguoiGiaoHang())
                .dVGiaoHang(dcnbPhieuKtChatLuongHdr.get().getDVGiaoHang())
                .diaChiDonViGiaoHang(dcnbPhieuKtChatLuongHdr.get().getDiaChiDonViGiaoHang())
                .theoHopDongSo("Theo hợp đồng số")
                .ngayKyHopDong("Ngày ký hợp đồng")
                .chungLoaiHangHoa("Chủng loại hàng DTQG")
                .soChungThuGiamDinh(dcnbPhieuKtChatLuongHdr.get().getSoChungThuGiamDinh())
                .ngayGiamDinh(dcnbPhieuKtChatLuongHdr.get().getNgayGiamDinh().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                .toChucGiamDinh(dcnbPhieuKtChatLuongHdr.get().getToChucGiamDinh())
                .slNhapTheoKb(dcnbPhieuKtChatLuongHdr.get().getSlNhapTheoKb())
                .slNhapTheoKt(dcnbPhieuKtChatLuongHdr.get().getSlNhapTheoKt())
                .ngayLapPhieu(dcnbPhieuKtChatLuongHdr.get().getNgayLapPhieu().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                .tenNganKho(dcnbPhieuKtChatLuongHdr.get().getTenNganKho())
                .tenLoKho(dcnbPhieuKtChatLuongHdr.get().getTenLoKho())
                .tenDiemKho(dcnbPhieuKtChatLuongHdr.get().getTenDiemKho())
                .bienSoXe(dcnbPhieuKtChatLuongHdr.get().getBienSoXe())
                .danhGiaCamQuan(dcnbPhieuKtChatLuongHdr.get().getDanhGiaCamQuan())
                .nhanXetKetLuan(dcnbPhieuKtChatLuongHdr.get().getNhanXetKetLuan())
                .ngayNhap(dcnbPhieuKtChatLuongHdr.get().getNgayLapPhieu().getDayOfMonth())
                .thangNhap(dcnbPhieuKtChatLuongHdr.get().getNgayLapPhieu().getMonth().getValue())
                .namNhap(dcnbPhieuKtChatLuongHdr.get().getNgayLapPhieu().getYear())
                .ktvBaoQuan("KTV bảo quản")
                .tenThuKho(dcnbPhieuKtChatLuongHdr.get().getTenThuKho())
                .tenLanhDaoChiCuc("Lãnh đạo Chi cục")
                .dcnbPhieuKtChatLuongDtl(dcnbPhieuKtChatLuongHdr.get().getDcnbPhieuKtChatLuongDtl())
                .build();
    }
}
