package com.tcdt.qlnvhang.service.dieuchuyennoibo.impl;

import com.google.common.collect.Lists;
import com.tcdt.qlnvhang.common.DocxToPdfConverter;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.FileDinhKemRepository;
import com.tcdt.qlnvhang.repository.dieuchuyennoibo.*;
import com.tcdt.qlnvhang.request.dieuchuyennoibo.DcnbPhieuNhapKhoHdrReq;
import com.tcdt.qlnvhang.request.object.dcnbBangKeCanHang.DcnbPhieuNhapKhoPreview;
import com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbPhieuNhapKhoDtlDto;
import com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbPhieuNhapKhoHdrDTO;
import com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbPhieuNhapKhoHdrListDTO;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.dieuchuyennoibo.DcnbPhieuNhapKhoService;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.*;
import com.tcdt.qlnvhang.table.report.ReportTemplate;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.UserUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DcnbPhieuNhapKhoServiceImpl extends BaseServiceImpl implements DcnbPhieuNhapKhoService {

    @Autowired
    private DcnbPhieuNhapKhoHdrRepository hdrRepository;

    @Autowired
    private DcnbPhieuNhapKhoDtlRepository dtlRepository;

    @Autowired
    private FileDinhKemService fileDinhKemService;

    @Autowired
    private DcnbBBNTBQHdrRepository dcnbBBNTBQHdrRepository;

    @Autowired
    public DocxToPdfConverter docxToPdfConverter;

    @Autowired
    public FileDinhKemRepository fileDinhKemRepository;

    @Override
    public Page<DcnbPhieuNhapKhoHdr> searchPage(DcnbPhieuNhapKhoHdrReq req) throws Exception {
        return null;
    }

    public Page<DcnbPhieuNhapKhoHdrDTO> search(DcnbPhieuNhapKhoHdrReq req) throws Exception {
        CustomUserDetails currentUser = UserUtils.getUserLoginInfo();
        req.setMaDvi(currentUser.getDvql());
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        Page<DcnbPhieuNhapKhoHdrDTO> searchDto = null;
        if (req.getIsVatTu() == null) {
            req.setIsVatTu(false);
        }
        if (req.getIsVatTu()) {
            req.setDsLoaiHang(Arrays.asList("VT"));
        } else {
            req.setDsLoaiHang(Arrays.asList("LT", "M"));
        }
        req.setTypeQd(Contains.NHAN_DIEU_CHUYEN);
        searchDto = hdrRepository.searchPage(req, pageable);

        return searchDto;
    }

    @Override
    public DcnbPhieuNhapKhoHdr create(DcnbPhieuNhapKhoHdrReq req) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null) {
            throw new Exception("Access denied.");
        }
        if (!userInfo.getCapDvi().equals(Contains.CAP_CHI_CUC)) {
            throw new Exception("Văn bản này chỉ có thêm ở cấp chi cục");
        }
//        Optional<DcnbPhieuNhapKhoHdr> optional = hdrRepository.findBySoPhieuNhapKho(req.getSoPhieuNhapKho());
//        if (optional.isPresent()) {
//            throw new Exception("Số biên bản đã tồn tại");
//        }

        DcnbPhieuNhapKhoHdr data = new DcnbPhieuNhapKhoHdr();
        BeanUtils.copyProperties(req, data);
        data.setMaDvi(userInfo.getDvql());
        data.setId(null);
        req.getChildren().forEach(e -> {
            e.setParent(data);
        });
        double sum = req.getChildren().stream().map(DcnbPhieuNhapKhoDtl::getThucTeKinhPhi).mapToDouble(BigDecimal::doubleValue).sum();
        data.setTongKinhPhi(new BigDecimal(sum));
        DcnbPhieuNhapKhoHdr created = hdrRepository.save(data);
        String so = created.getId() + "/" + (new Date().getYear() + 1900) + "/PNK-" + userInfo.getDvqlTenVietTat();
        created.setSoPhieuNhapKho(so);
        hdrRepository.save(created);
        List<FileDinhKem> canCu = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKemReq(), created.getId(), DcnbPhieuNhapKhoHdr.TABLE_NAME);
        created.setFileDinhKems(canCu);
        // lưu biên bản nghiệp thu bảo quản lần đầu
        List<DcnbBBNTBQHdr> bbntbqHdrList = new ArrayList<>();
        if (created.getMaLoKho() == null) {
            bbntbqHdrList = dcnbBBNTBQHdrRepository.findByQdDcCucIdAndMaNganKho(created.getQdDcCucId(), created.getMaNganKho());
        } else {
            bbntbqHdrList = dcnbBBNTBQHdrRepository.findByQdDcCucIdAndMaNganKhoAndMaLoKho(created.getQdDcCucId(), created.getMaNganKho(), created.getMaLoKho());
        }
        String bbntbqld = bbntbqHdrList.stream().map(DcnbBBNTBQHdr::getSoBban).collect(Collectors.joining(","));
        created.setBbNghiemThuBqld(bbntbqld);
        created = hdrRepository.save(created);
        return created;
    }

    @Override
    public DcnbPhieuNhapKhoHdr update(DcnbPhieuNhapKhoHdrReq req) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null) {
            throw new Exception("Access denied.");
        }
        if (!userInfo.getCapDvi().equals(Contains.CAP_CHI_CUC)) {
            throw new Exception("Văn bản này chỉ có thêm ở cấp chi cục");
        }
        Optional<DcnbPhieuNhapKhoHdr> optional = hdrRepository.findById(req.getId());
        if (!optional.isPresent()) {
            throw new Exception("Số biên bản không tồn tại");
        }
        DcnbPhieuNhapKhoHdr data = optional.get();
        BeanUtils.copyProperties(req, data);
        data.setChildren(req.getChildren());
        DcnbPhieuNhapKhoHdr update = hdrRepository.save(data);
        String so = update.getId() + "/" + (new Date().getYear() + 1900) + "/PNK-" + userInfo.getDvqlTenVietTat();
        update.setSoPhieuNhapKho(so);
        double sum = req.getChildren().stream().map(DcnbPhieuNhapKhoDtl::getThucTeKinhPhi).mapToDouble(BigDecimal::doubleValue).sum();
        data.setTongKinhPhi(new BigDecimal(sum));
        hdrRepository.save(update);
        fileDinhKemService.delete(update.getId(), Lists.newArrayList(DcnbPhieuNhapKhoHdr.TABLE_NAME));
        List<FileDinhKem> canCu = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKemReq(), update.getId(), DcnbPhieuNhapKhoHdr.TABLE_NAME);
        update.setFileDinhKems(canCu);
        // lưu biên bản nghiệp thu bảo quản lần đầu
        List<DcnbBBNTBQHdr> bbntbqHdrList = new ArrayList<>();
        if (update.getMaLoKho() == null) {
            bbntbqHdrList = dcnbBBNTBQHdrRepository.findByQdDcCucIdAndMaNganKho(update.getQdDcCucId(), update.getMaNganKho());
        } else {
            bbntbqHdrList = dcnbBBNTBQHdrRepository.findByQdDcCucIdAndMaNganKhoAndMaLoKho(update.getQdDcCucId(), update.getMaNganKho(), update.getMaLoKho());
        }
        String bbntbqld = bbntbqHdrList.stream().map(DcnbBBNTBQHdr::getSoBban).collect(Collectors.joining(","));
        update.setBbNghiemThuBqld(bbntbqld);
        update = hdrRepository.save(update);
        return update;
    }

    @Override
    public DcnbPhieuNhapKhoHdr detail(Long id) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null) {
            throw new Exception("Access denied.");
        }
        if (Objects.isNull(id)) {
            throw new Exception("Id is null");
        }
        Optional<DcnbPhieuNhapKhoHdr> optional = hdrRepository.findById(id);
        if (!optional.isPresent()) {
            throw new Exception("Số biên bản không tồn tại");
        }
        DcnbPhieuNhapKhoHdr data = optional.get();
        data.setFileDinhKems(fileDinhKemService.search(id, Collections.singleton(DcnbPhieuNhapKhoHdr.TABLE_NAME)));
        return data;
    }

    @Override
    public DcnbPhieuNhapKhoHdr approve(DcnbPhieuNhapKhoHdrReq req) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null) {
            throw new Exception("Access denied.");
        }
        DcnbPhieuNhapKhoHdr hdr = detail(req.getId());
        String status = hdr.getTrangThai() + req.getTrangThai();
        switch (status) {
            // Arena các roll back approve
            case Contains.DUTHAO + Contains.CHODUYET_LDCC:
            case Contains.TUCHOI_LDCC + Contains.CHODUYET_LDCC:
                hdr.setNguoiGDuyet(userInfo.getId());
                hdr.setNgayGDuyet(LocalDate.now());
                break;
            case Contains.CHODUYET_LDCC + Contains.TUCHOI_LDCC:
                if (hdr.getIdLanhDao() == null) {
                    hdr.setIdLanhDao(userInfo.getId());
                    hdr.setTenLanhDao(userInfo.getFullName());
                }
                hdr.setNguoiPDuyet(userInfo.getId());
                hdr.setNgayPDuyet(LocalDate.now());
                hdr.setLyDoTuChoi(req.getLyDoTuChoi());
                break;
            case Contains.CHODUYET_LDCC + Contains.DADUYET_LDCC:
                if (hdr.getIdLanhDao() == null) {
                    hdr.setIdLanhDao(userInfo.getId());
                    hdr.setTenLanhDao(userInfo.getFullName());
                }
                hdr.setNguoiPDuyet(userInfo.getId());
                hdr.setNgayPDuyet(LocalDate.now());
//                DcnbDataLinkHdr dataLink = dcnbDataLinkHdrRepository.findDataLinkChiCucNhan(hdr.getMaDvi(),
//                        hdr.getQdDcCucId(),
//                        hdr.getMaNganKho(),
//                        hdr.getMaLoKho());
//                DcnbDataLinkDtl dataLinkDtl = new DcnbDataLinkDtl();
//                dataLinkDtl.setLinkId(hdr.getId());
//                dataLinkDtl.setHdrId(dataLink.getId());
//                dataLinkDtl.setType(DcnbPhieuNhapKhoHdr.TABLE_NAME);
//                dcnbDataLinkDtlRepository.save(dataLinkDtl);

                List<DcnbBBNTBQHdr> bbntbqHdrList = new ArrayList<>();
                if (hdr.getMaLoKho() == null) {
                    bbntbqHdrList = dcnbBBNTBQHdrRepository.findByQdDcCucIdAndMaNganKho(hdr.getQdDcCucId(), hdr.getMaNganKho());
                } else {
                    bbntbqHdrList = dcnbBBNTBQHdrRepository.findByQdDcCucIdAndMaNganKhoAndMaLoKho(hdr.getQdDcCucId(), hdr.getMaNganKho(), hdr.getMaLoKho());
                }

                // lưu biên bản nghiệp thu bảo quản lần đầu
                for (DcnbBBNTBQHdr hdrbq : bbntbqHdrList) {
                    if (hdrbq.getDsPhieuNhapKho() == null) {
                        hdrbq.setDsPhieuNhapKho(hdr.getSoPhieuNhapKho());
                        hdrbq.setSlThucNhapDc(hdr.getTongSoLuong());
                    } else {
                        hdrbq.setDsPhieuNhapKho(hdrbq.getDsPhieuNhapKho() + "," + hdr.getSoPhieuNhapKho());
                        hdrbq.setSlThucNhapDc(hdrbq.getSlThucNhapDc().add(hdr.getTongSoLuong()));
                    }
                    dcnbBBNTBQHdrRepository.save(hdrbq);
                }
                break;
            default:
                throw new Exception("Phê duyệt không thành công");
        }
        hdr.setTrangThai(req.getTrangThai());
        DcnbPhieuNhapKhoHdr created = hdrRepository.save(hdr);
        return created;
    }

    @Override
    public void delete(Long id) throws Exception {
        Optional<DcnbPhieuNhapKhoHdr> optional = hdrRepository.findById(id);
        if (!optional.isPresent()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        DcnbPhieuNhapKhoHdr data = optional.get();
        List<DcnbPhieuNhapKhoDtl> list = dtlRepository.findByHdrId(data.getId());
        dtlRepository.deleteAll(list);
        hdrRepository.delete(data);
    }

    @Override
    public void deleteMulti(List<Long> listMulti) throws Exception {
        if (listMulti != null && !listMulti.isEmpty()) {
            listMulti.forEach(i -> {
                try {
                    delete(i);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        } else {
            throw new Exception("List id is null");
        }
    }

    @Override
    public void export(DcnbPhieuNhapKhoHdrReq req, HttpServletResponse response) throws Exception {

    }

    @Override
    public List<DcnbPhieuNhapKhoHdrListDTO> searchList(DcnbPhieuNhapKhoHdrReq objReq) throws Exception {
        CustomUserDetails currentUser = UserUtils.getUserLoginInfo();
        objReq.setMaDvi(currentUser.getDvql());
        objReq.setTypeQd(Contains.NHAN_DIEU_CHUYEN);
        if (objReq.getIsVatTu() == null) {
            objReq.setIsVatTu(false);
        }
        if (objReq.getIsVatTu()) {
            objReq.setDsLoaiHang(Arrays.asList("VT"));
        } else {
            objReq.setDsLoaiHang(Arrays.asList("LT", "M"));
        }
        List<DcnbPhieuNhapKhoHdrListDTO> searchDto = hdrRepository.searchList(objReq);
        return searchDto;
    }

    @Override
    public List<DcnbPhieuNhapKhoHdrListDTO> searchListChung(DcnbPhieuNhapKhoHdrReq objReq) throws Exception {
        CustomUserDetails currentUser = UserUtils.getUserLoginInfo();
        objReq.setMaDvi(currentUser.getDvql());
        objReq.setTypeQd(Contains.NHAN_DIEU_CHUYEN);
        if (objReq.getIsVatTu() == null) {
            objReq.setIsVatTu(false);
        }
        if (objReq.getIsVatTu()) {
            objReq.setDsLoaiHang(Arrays.asList("VT"));
        } else {
            objReq.setDsLoaiHang(Arrays.asList("LT", "M"));
        }
        List<DcnbPhieuNhapKhoHdrListDTO> searchDto = hdrRepository.searchListChung(objReq);
        return searchDto;
    }

    @Override
    public ReportTemplateResponse preview(DcnbPhieuNhapKhoHdrReq objReq) throws Exception {
        Optional<DcnbPhieuNhapKhoHdr> dcnbPhieuNhapKhoHdr = hdrRepository.findById(objReq.getId());
        if (!dcnbPhieuNhapKhoHdr.isPresent()) throw new Exception("Không tồn tại bản ghi");
//        ReportTemplate model = findByTenFile(objReq.getReportTemplateRequest());
        FileInputStream inputStream = new FileInputStream("/Users/lethanhdat/tecapro/qlnv-hang/src/main/resources/reports/dieuchuyennoibo/Nhập_VT_Phiếu nhập kho.docx");
//        byte[] byteArray = Base64.getDecoder().decode(model.getFileUpload());
//        ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArray);

        DcnbPhieuNhapKhoPreview dcnbPhieuNhapKhoPreview = setDataToPreview(dcnbPhieuNhapKhoHdr);
        return docxToPdfConverter.convertDocxToPdf(inputStream, dcnbPhieuNhapKhoPreview);
    }

    private DcnbPhieuNhapKhoPreview setDataToPreview(Optional<DcnbPhieuNhapKhoHdr> dcnbPhieuNhapKhoHdr) {
        return DcnbPhieuNhapKhoPreview.builder()
                .tenDvi(dcnbPhieuNhapKhoHdr.get().getTenDvi())
                .maQhns(dcnbPhieuNhapKhoHdr.get().getMaQhns())
                .ngayNhap(dcnbPhieuNhapKhoHdr.get().getNgayLap().getDayOfMonth())
                .thangNhap(dcnbPhieuNhapKhoHdr.get().getNgayLap().getMonth().getValue())
                .namNhap(dcnbPhieuNhapKhoHdr.get().getNgayLap().getYear())
                .soPhieuNhapKho(dcnbPhieuNhapKhoHdr.get().getSoPhieuNhapKho())
                .soNo(dcnbPhieuNhapKhoHdr.get().getSoNo())
                .soCo(dcnbPhieuNhapKhoHdr.get().getSoCo())
                .hoVaTenNguoiGiao(dcnbPhieuNhapKhoHdr.get().getHoVaTenNguoiGiao())
                .cmndNguoiGiao(dcnbPhieuNhapKhoHdr.get().getCmndNguoiGiao())
                .donViNguoiGiao(dcnbPhieuNhapKhoHdr.get().getDonViNguoiGiao())
                .diaChi(dcnbPhieuNhapKhoHdr.get().getDiaChi())
                .donViCungCapHang(dcnbPhieuNhapKhoHdr.get().getDonViNguoiGiao())
                .soQdGiaoVnNhapHang(dcnbPhieuNhapKhoHdr.get().getSoQdDcCuc())
                .ngayKyQdGiaoNvNhapHang(dcnbPhieuNhapKhoHdr.get().getNgayQdDcCuc().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                .donViCapChaCuaTruongDonVi("Đơn vị cấp cha của trường Đơn vị")
                .tenNganKho(dcnbPhieuNhapKhoHdr.get().getTenNganKho())
                .tenLoKho(dcnbPhieuNhapKhoHdr.get().getTenLoKho())
                .tenNhaKho(dcnbPhieuNhapKhoHdr.get().getTenNhaKho())
                .tenDiemKho(dcnbPhieuNhapKhoHdr.get().getTenDiemKho())
                .tenThuKho(dcnbPhieuNhapKhoHdr.get().getTenThuKho())
                .tgianGiaoNhanHang(dcnbPhieuNhapKhoHdr.get().getTgianGiaoNhanHang().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                .tongSoLuongBc(dcnbPhieuNhapKhoHdr.get().getTongSoLuongBc())
                .tongKinhPhiBc(dcnbPhieuNhapKhoHdr.get().getTongKinhPhiBc())
                .tenNguoiLap(dcnbPhieuNhapKhoHdr.get().getTenNguoiLap())
                .keToanTruong(dcnbPhieuNhapKhoHdr.get().getKeToanTruong())
                .tenLanhDao(dcnbPhieuNhapKhoHdr.get().getTenLanhDao())
                .chungTuDinhKem(dcnbPhieuNhapKhoHdr.get().getChungTuDinhKem())
                .dcnbPhieuNhapKhoDtl(dcnbPhieuNhapKhoDtlToDto(dcnbPhieuNhapKhoHdr.get().getChildren()))
                .tongSoLuongTheoChungTu(BigDecimal.ZERO)
                .tongSoLuongThucNhap(tongSoLuongThucNhap(dcnbPhieuNhapKhoHdr.get().getChildren()))
                .tongSoTien(tongSoTien(dcnbPhieuNhapKhoHdr.get().getChildren()))
                .build();
    }

    private List<DcnbPhieuNhapKhoDtlDto> dcnbPhieuNhapKhoDtlToDto(List<DcnbPhieuNhapKhoDtl> children) {
        List<DcnbPhieuNhapKhoDtlDto> dcnbPhieuNhapKhoDtl = new ArrayList<>();
        int stt = 1;
        for (DcnbPhieuNhapKhoDtl res : children) {
            DcnbPhieuNhapKhoDtlDto dcnbPhieuNhapKhoDtlDto = new DcnbPhieuNhapKhoDtlDto();
            dcnbPhieuNhapKhoDtlDto.setStt(stt++);
            dcnbPhieuNhapKhoDtlDto.setNoiDung(res.getNoiDung());
            dcnbPhieuNhapKhoDtlDto.setMaSo(res.getMaSo());
            dcnbPhieuNhapKhoDtlDto.setDviTinh(res.getDviTinh());
            dcnbPhieuNhapKhoDtlDto.setSoLuongTheoChungTu(BigDecimal.ZERO);
            dcnbPhieuNhapKhoDtlDto.setSoLuongThucNhap(res.getSoLuongNhapDc());
            dcnbPhieuNhapKhoDtlDto.setDonGia(BigDecimal.ZERO);
            dcnbPhieuNhapKhoDtlDto.setThanhTien(res.getSoLuongNhapDc().multiply(BigDecimal.ZERO));
            dcnbPhieuNhapKhoDtl.add(dcnbPhieuNhapKhoDtlDto);
        }
        return dcnbPhieuNhapKhoDtl;
    }

    private BigDecimal tongSoTien(List<DcnbPhieuNhapKhoDtl> children) {
        BigDecimal tongSoTien = new BigDecimal("0.0");
        for (DcnbPhieuNhapKhoDtl res : children) {
            tongSoTien = tongSoTien.add(res.getSoLuongNhapDc().multiply(BigDecimal.ZERO));
        }
        return tongSoTien;
    }
    private BigDecimal tongSoLuongThucNhap(List<DcnbPhieuNhapKhoDtl> children) {
        BigDecimal tongSoLuongThucNhap = new BigDecimal("0.0");
        for (DcnbPhieuNhapKhoDtl res : children) {
            tongSoLuongThucNhap = tongSoLuongThucNhap.add(res.getSoLuongNhapDc());
        }
        return tongSoLuongThucNhap;
    }
}