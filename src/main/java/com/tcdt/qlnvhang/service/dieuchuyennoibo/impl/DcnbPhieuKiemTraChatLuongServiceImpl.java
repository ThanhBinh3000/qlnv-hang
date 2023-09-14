package com.tcdt.qlnvhang.service.dieuchuyennoibo.impl;

import com.google.common.collect.Lists;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.FileDinhKemRepository;
import com.tcdt.qlnvhang.repository.QlnvDmDonviRepository;
import com.tcdt.qlnvhang.repository.UserInfoRepository;
import com.tcdt.qlnvhang.repository.dieuchuyennoibo.DcnbPhieuKtChatLuongDtlRepository;
import com.tcdt.qlnvhang.repository.dieuchuyennoibo.DcnbPhieuKtChatLuongHdrRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.dieuchuyennoibo.*;
import com.tcdt.qlnvhang.request.object.dcnbBangKeCanHang.DcnbPhieuKtChatLuongHdrPreview;
import com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbPhieuKtChatLuongDtlDto;
import com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbPhieuKtChatLuongHdrDTO;
import com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbPhieuKtChatLuongHdrLsDTO;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.table.catalog.QlnvDmDonvi;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.*;
import com.tcdt.qlnvhang.table.report.ReportTemplate;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.DataUtils;
import com.tcdt.qlnvhang.util.ExportExcel;
import lombok.var;
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

    @Autowired
    private QlnvDmDonviRepository qlnvDmDonviRepository;

    @Autowired
    private UserInfoRepository userInfoRepository;

    public Page<DcnbPhieuKtChatLuongHdrDTO> searchPage(CustomUserDetails currentUser, SearchPhieuKtChatLuong req) throws Exception {
        String dvql = currentUser.getDvql();
        req.setMaDvi(dvql);
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        if (req.getIsVatTu() == null) {
            req.setIsVatTu(false);
        }
        if (req.getIsVatTu()) {
            req.setDsLoaiHang(Arrays.asList("VT"));
        } else {
            req.setDsLoaiHang(Arrays.asList("LT", "M"));
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
        if (!currentUser.getUser().getCapDvi().equals(Contains.CAP_CHI_CUC)) {
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
        if (objReq.getDcnbPhieuKtChatLuongDtl() != null) {
            objReq.getDcnbPhieuKtChatLuongDtl().forEach(e -> e.setDcnbPhieuKtChatLuongHdr(data));
        }
        DcnbPhieuKtChatLuongHdr created = dcnbPhieuKtChatLuongHdrRepository.save(data);
        String so = created.getId() + "/" + (new Date().getYear() + 1900) + "/PKTCL-" + currentUser.getUser().getDvqlTenVietTat();
        created.setSoPhieu(so);
        dcnbPhieuKtChatLuongHdrRepository.save(created);
        List<FileDinhKem> bienBanLayMauDinhKem = fileDinhKemService.saveListFileDinhKem(objReq.getBienBanLayMauDinhKem(), created.getId(), DcnbPhieuKtChatLuongHdr.TABLE_NAME + "_CAN_CU");
        List<FileDinhKem> phieuKiemTra = fileDinhKemService.saveListFileDinhKem(objReq.getPhieuKTCLDinhKem(), created.getId(), DcnbPhieuKtChatLuongHdr.TABLE_NAME + "_PKT");
        data.setBienBanLayMauDinhKem(bienBanLayMauDinhKem);
        data.setPhieuKTCLDinhKem(phieuKiemTra);
        return created;
    }

    @Transactional
    public DcnbPhieuKtChatLuongHdr update(CustomUserDetails currentUser, DcnbPhieuKtChatLuongHdrReq objReq) throws Exception {
        if (currentUser == null) {
            throw new Exception("Bad request.");
        }
        if (!currentUser.getUser().getCapDvi().equals(Contains.CAP_CHI_CUC)) {
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
        String so = created.getId() + "/" + (new Date().getYear() + 1900) + "/PKTCL-" + currentUser.getUser().getDvqlTenVietTat();
        created.setSoPhieu(so);
        dcnbPhieuKtChatLuongHdrRepository.save(created);
        fileDinhKemService.delete(objReq.getId(), Lists.newArrayList(DcnbPhieuKtChatLuongHdr.TABLE_NAME + "_CAN_CU"));
        fileDinhKemService.delete(objReq.getId(), Lists.newArrayList(DcnbPhieuKtChatLuongHdr.TABLE_NAME + "_PKT"));
        List<FileDinhKem> bienBanLayMauDinhKem = fileDinhKemService.saveListFileDinhKem(objReq.getBienBanLayMauDinhKem(), created.getId(), DcnbPhieuKtChatLuongHdr.TABLE_NAME + "_CAN_CU");
        List<FileDinhKem> phieuKiemTra = fileDinhKemService.saveListFileDinhKem(objReq.getPhieuKTCLDinhKem(), created.getId(), DcnbPhieuKtChatLuongHdr.TABLE_NAME + "_PKT");
        data.setBienBanLayMauDinhKem(bienBanLayMauDinhKem);
        data.setPhieuKTCLDinhKem(phieuKiemTra);
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
            List<FileDinhKem> bienBanLayMauDinhKem = fileDinhKemRepository.findByDataIdAndDataTypeIn(data.getId(), Collections.singleton(DcnbPhieuKtChatLuongHdr.TABLE_NAME + "_CAN_CU"));
            List<FileDinhKem> phieuKiemTra = fileDinhKemRepository.findByDataIdAndDataTypeIn(data.getId(), Collections.singleton(DcnbPhieuKtChatLuongHdr.TABLE_NAME + "_PKT"));
            data.setBienBanLayMauDinhKem(bienBanLayMauDinhKem);
            data.setPhieuKTCLDinhKem(phieuKiemTra);
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

        String title = "Danh sách phiếu kiểm tra chất lượng";
        String[] rowsName = new String[]{"STT", "Số QĐ điều chuyển của Cục", "Năm KH", "Thời hạn điều chuyển", "Lô kho xuất ĐC", "Trạng thái xuất ĐC", "Điểm kho nhập ĐC", "Lô kho nhập ĐC", "Trạng thái nhập ĐC", "Số BB NT kê lót BQLĐ", "Số phiếu KTCL", "Ngày giám định", "Kết quả đánh giá", "Số phiếu nhập kho", "Ngày nhập kho", "Trạng thái"};
        String fileName = "danh-sach-phieu-kiem-tra-chat-luong.xlsx";
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;
        for (int i = 0; i < data.size(); i++) {
            DcnbPhieuKtChatLuongHdrDTO dx = data.get(i);
            objs = new Object[rowsName.length];
            objs[0] = i;
            objs[1] = dx.getSoQdinh();
            objs[2] = dx.getNam();
            objs[3] = dx.getThoiGianDieuChuyen();
            objs[4] = dx.getTenLoKhoXuat();
            objs[5] = dx.getTenTrangThaiXuat();
            objs[6] = dx.getTenDiemKhoNhan();
            objs[7] = dx.getTenLoKhoNhan(); //"Trạng thái nhập ĐC", "Số BB NT kê lót BQLĐ", "Số phiếu KTCL", "Ngày giám định", "Kết quả đánh giá", "Số phiếu nhập kho", "Ngày nhập kho"
            objs[8] = dx.getTenTrangThaiNhan();
            objs[9] = dx.getSoBBNtLd();
            objs[10] = dx.getSoPhieuKtChatLuong();
            objs[11] = dx.getNgayGiamDinh();
            objs[12] = dx.getKetQuaDanhGia();
            objs[13] = dx.getSoPhieuNhapKho();
            objs[14] = dx.getNgayNhapKho();
            dataList.add(objs);
        }
        ExportExcel ex = new ExportExcel(title, fileName, rowsName, dataList, response);
        ex.export();
    }

    public List<DcnbPhieuKtChatLuongHdrLsDTO> searchList(CustomUserDetails currentUser, SearchPhieuKtChatLuong req) {
        String dvql = currentUser.getDvql();
        req.setMaDvi(dvql);
        if (req.getIsVatTu() == null) {
            req.setIsVatTu(false);
        }
        if (req.getIsVatTu()) {
            req.setDsLoaiHang(Arrays.asList("VT"));
        } else {
            req.setDsLoaiHang(Arrays.asList("LT", "M"));
        }
        List<DcnbPhieuKtChatLuongHdrLsDTO> search = dcnbPhieuKtChatLuongHdrRepository.searchList(req);
        return search;
    }

    public ReportTemplateResponse preview(DcnbPhieuKtChatLuongHdrReq objReq) throws Exception {
        var dcnbPhieuKtChatLuongHdr = dcnbPhieuKtChatLuongHdrRepository.findById(objReq.getId());
        if (!dcnbPhieuKtChatLuongHdr.isPresent()) throw new Exception("Không tồn tại bản ghi");
        var qlnvDmDonvi = qlnvDmDonviRepository.findByMaDvi(dcnbPhieuKtChatLuongHdr.get().getMaDvi());
        if (qlnvDmDonvi == null) throw new Exception("Không tồn tại bản ghi");
        var userInfo = userInfoRepository.findById(dcnbPhieuKtChatLuongHdr.get().getNguoiPDuyet());
        if (!userInfo.isPresent()) throw new Exception("Không tồn tại bản ghi");
        ReportTemplate model = findByTenFile(objReq.getReportTemplateRequest());
        byte[] byteArray = Base64.getDecoder().decode(model.getFileUpload());
        ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArray);
        var dcnbPhieuKtChatLuongHdrPreview = setDataToPreview(dcnbPhieuKtChatLuongHdr, qlnvDmDonvi, userInfo);
        return docxToPdfConverter.convertDocxToPdf(inputStream, dcnbPhieuKtChatLuongHdrPreview);
    }

    private DcnbPhieuKtChatLuongHdrPreview setDataToPreview(Optional<DcnbPhieuKtChatLuongHdr> dcnbPhieuKtChatLuongHdr,
                                                            QlnvDmDonvi qlnvDmDonvi, Optional<UserInfo> userInfo) {
        return DcnbPhieuKtChatLuongHdrPreview.builder()
                .tenDvi(qlnvDmDonvi.getTenDvi())
                .maDvi(dcnbPhieuKtChatLuongHdr.get().getMaDvi())
                .maQhns(dcnbPhieuKtChatLuongHdr.get().getMaQhns())
                .loaiHangHoa(dcnbPhieuKtChatLuongHdr.get().getTenLoaiVthh())
                .soPhieu(dcnbPhieuKtChatLuongHdr.get().getSoPhieu())
                .nguoiGiaoHang(dcnbPhieuKtChatLuongHdr.get().getNguoiGiaoHang())
                .dVGiaoHang(dcnbPhieuKtChatLuongHdr.get().getDVGiaoHang())
                .diaChiDonViGiaoHang(dcnbPhieuKtChatLuongHdr.get().getDiaChiDonViGiaoHang())
                .chungLoaiHangHoa(dcnbPhieuKtChatLuongHdr.get().getCloaiVthh())
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
                .ktvBaoQuan(dcnbPhieuKtChatLuongHdr.get().getNguoiKt())
                .tenThuKho(dcnbPhieuKtChatLuongHdr.get().getTenThuKho())
                .tenLanhDaoChiCuc(userInfo.get().getFullName())
                .dcnbPhieuKtChatLuongDtl(dcnbPhieuKtChatLuongDtlToDto(dcnbPhieuKtChatLuongHdr.get().getDcnbPhieuKtChatLuongDtl()))
                .build();
    }

    private List<DcnbPhieuKtChatLuongDtlDto> dcnbPhieuKtChatLuongDtlToDto(List<DcnbPhieuKtChatLuongDtl> dcnbPhieuKtChatLuongDtl) {
        List<DcnbPhieuKtChatLuongDtlDto> dcnbPhieuKtChatLuongDtlDtos = new ArrayList<>();
        int stt = 1;
        for (var res : dcnbPhieuKtChatLuongDtl) {
            var  dcnbPhieuKtChatLuongDtlDto = DcnbPhieuKtChatLuongDtlDto.builder()
                    .stt(stt++)
                    .chiTieuCl(res.getChiTieuCl())
                    .chiSoCl(res.getChiSoCl())
                    .ketQuaPt(res.getKetQuaPt())
                    .phuongPhap(res.getPhuongPhap())
                    .danhGia(res.getDanhGia())
                    .build();
            dcnbPhieuKtChatLuongDtlDtos.add(dcnbPhieuKtChatLuongDtlDto);
        }
        return dcnbPhieuKtChatLuongDtlDtos;
    }
}
