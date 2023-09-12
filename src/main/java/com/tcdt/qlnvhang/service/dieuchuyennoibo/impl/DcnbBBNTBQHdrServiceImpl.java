package com.tcdt.qlnvhang.service.dieuchuyennoibo.impl;

import com.google.common.collect.Lists;
import com.tcdt.qlnvhang.common.DocxToPdfConverter;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.dieuchuyennoibo.DcnbBBNTBQDtlRepository;
import com.tcdt.qlnvhang.repository.dieuchuyennoibo.DcnbBBNTBQHdrRepository;
import com.tcdt.qlnvhang.repository.dieuchuyennoibo.DcnbDataLinkDtlRepository;
import com.tcdt.qlnvhang.repository.dieuchuyennoibo.DcnbDataLinkHdrRepository;
import com.tcdt.qlnvhang.request.dieuchuyennoibo.DcnbBBNTBQHdrReq;
import com.tcdt.qlnvhang.request.object.dcnbBangKeCanHang.DcnbBBNTBQHdrPreview;
import com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbBBNTBQHdrDTO;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.dieuchuyennoibo.DcnbBBNTBQHdrService;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBBNTBQHdr;
import com.tcdt.qlnvhang.table.report.ReportTemplate;
import com.tcdt.qlnvhang.util.Contains;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class DcnbBBNTBQHdrServiceImpl extends BaseServiceImpl implements DcnbBBNTBQHdrService {

    @Autowired
    private DcnbBBNTBQHdrRepository hdrRepository;

    @Autowired
    private DcnbBBNTBQDtlRepository dtlRepository;

    @Autowired
    private FileDinhKemService fileDinhKemService;

    @Autowired
    private DcnbDataLinkHdrRepository dcnbDataLinkHdrRepository;
    @Autowired
    private DcnbDataLinkDtlRepository dcnbDataLinkDtlRepository;
    @Autowired
    private DcnbQuyetDinhDcCHdrServiceImpl dcnbQuyetDinhDcCHdrServiceImpl;
    @Autowired
    public DocxToPdfConverter docxToPdfConverter;


    @Override
    public Page<DcnbBBNTBQHdr> searchPage(DcnbBBNTBQHdrReq req) throws Exception {
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        Page<DcnbBBNTBQHdr> search = hdrRepository.search(req, pageable);
        return search;
    }

    @Override
    public Page<DcnbBBNTBQHdrDTO> searchPage(CustomUserDetails currentUser, DcnbBBNTBQHdrReq req) throws Exception {
        String dvql = currentUser.getDvql();
        req.setMaDvi(dvql);
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        Page<DcnbBBNTBQHdrDTO> searchDto = null;
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
    public List<DcnbBBNTBQHdr> searchList(CustomUserDetails currentUser, DcnbBBNTBQHdrReq req) {
        req.setTrangThai("17");
        List<DcnbBBNTBQHdr> search = hdrRepository.list(req);
        return search;
    }

    @Override
    public DcnbBBNTBQHdr create(DcnbBBNTBQHdrReq req) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null) {
            throw new Exception("Access denied.");
        }
        if (!userInfo.getCapDvi().equals(Contains.CAP_CHI_CUC)) {
            throw new Exception("Văn bản này chỉ có thêm ở cấp chi cục");
        }
//        Optional<DcnbBBNTBQHdr> optional = hdrRepository.findBySoBban(req.getSoBban());
//        if (optional.isPresent()) {
//            throw new Exception("Số biên bản đã tồn tại");
//        }

        DcnbBBNTBQHdr data = new DcnbBBNTBQHdr();
        BeanUtils.copyProperties(req, data);
        data.setMaDvi(userInfo.getDvql());
        data.getDcnbBBNTBQDtl().forEach(e -> {
            e.setDcnbBBNTBQHdr(data);
        });
        DcnbBBNTBQHdr created = hdrRepository.save(data);
        String so = created.getId() + "/" + (new Date().getYear() + 1900) + "/BBBQLD-" + userInfo.getDvqlTenVietTat();
        created.setSoBban(so);
        hdrRepository.save(created);
        List<FileDinhKem> canCu = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKemReq(), created.getId(), DcnbBBNTBQHdr.TABLE_NAME);
        created.setFileDinhKems(canCu);
        return created;
    }

    @Override
    public DcnbBBNTBQHdr update(DcnbBBNTBQHdrReq req) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null) {
            throw new Exception("Access denied.");
        }
        if (!userInfo.getCapDvi().equals(Contains.CAP_CHI_CUC)) {
            throw new Exception("Văn bản này chỉ có thêm ở cấp chi cục");
        }
        Optional<DcnbBBNTBQHdr> optional = hdrRepository.findById(req.getId());
        if (!optional.isPresent()) {
            throw new Exception("Số biên bản không tồn tại");
        }
        DcnbBBNTBQHdr data = optional.get();
        BeanUtils.copyProperties(req, data);
        data.setDcnbBBNTBQDtl(req.getDcnbBBNTBQDtl());
        DcnbBBNTBQHdr created = hdrRepository.save(data);
        String so = created.getId() + "/" + (new Date().getYear() + 1900) + "/BBBQLD-" + userInfo.getDvqlTenVietTat();
        created.setSoBban(so);
        hdrRepository.save(created);
        fileDinhKemService.delete(created.getId(), Lists.newArrayList(DcnbBBNTBQHdr.TABLE_NAME));
        List<FileDinhKem> canCu = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKemReq(), created.getId(), DcnbBBNTBQHdr.TABLE_NAME);
        created.setFileDinhKems(canCu);
        return created;
    }

    @Override
    public DcnbBBNTBQHdr detail(Long id) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null) {
            throw new Exception("Access denied.");
        }
        if (Objects.isNull(id)) {
            throw new Exception("Id is null");
        }
        Optional<DcnbBBNTBQHdr> optional = hdrRepository.findById(id);
        if (!optional.isPresent()) {
            throw new Exception("Số biên bản không tồn tại");
        }
        DcnbBBNTBQHdr data = optional.get();
        data.setFileDinhKems(fileDinhKemService.search(id, Collections.singleton(DcnbBBNTBQHdr.TABLE_NAME)));
        return data;
    }

    @Override
    public DcnbBBNTBQHdr approve(DcnbBBNTBQHdrReq req) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null) {
            throw new Exception("Access denied.");
        }
        DcnbBBNTBQHdr hdr = detail(req.getId());
        String status = hdr.getTrangThai() + req.getTrangThai();
        switch (status) {
            case Contains.TUCHOI_TK + Contains.DUTHAO:
            case Contains.TUCHOI_KT + Contains.DUTHAO:
            case Contains.TUCHOI_LDCC + Contains.DUTHAO:
            case Contains.DUTHAO + Contains.CHODUYET_TK:
                hdr.setNguoiGduyetId(userInfo.getId());
                hdr.setNgayGduyet(LocalDate.now());
                break;
            case Contains.CHODUYET_TK + Contains.CHODUYET_KT:
                hdr.setNgayPDuyetKt(LocalDate.now());
                hdr.setNguoiPDuyeKt(userInfo.getId());
                hdr.setThuKho(userInfo.getFullName());
                break;
            case Contains.CHODUYET_KT + Contains.CHODUYET_LDCC:
                hdr.setNgayPDuyetKt(LocalDate.now());
                hdr.setNguoiPDuyeKt(userInfo.getId());
                hdr.setKeToan(userInfo.getFullName());
                break;
            case Contains.CHODUYET_LDCC + Contains.DADUYET_LDCC:
                hdr.setNgayPduyet(LocalDate.now());
                hdr.setNguoiPduyetId(userInfo.getId());
                hdr.setLdChiCuc(userInfo.getFullName());
//                DcnbDataLinkHdr dataLink = dcnbDataLinkHdrRepository.findDataLinkChiCuc(hdr.getMaDvi(),
//                        hdr.getQdDcCucId(),
//                        hdr.getMaNganKhoXuat(),
//                        hdr.getMaLoKhoXuat());
//                DcnbDataLinkDtl dataLinkDtl = new DcnbDataLinkDtl();
//                dataLinkDtl.setLinkId(hdr.getId());
//                dataLinkDtl.setHdrId(dataLink.getId());
//                dataLinkDtl.setType(DcnbBBNTBQHdr.TABLE_NAME);
//                dcnbDataLinkDtlRepository.save(dataLinkDtl);
                break;
            case Contains.CHODUYET_TK + Contains.TUCHOI_TK:
                hdr.setNgayPDuyetKt(LocalDate.now());
                hdr.setNguoiPDuyeKt(userInfo.getId());
                hdr.setLyDoTuChoi(req.getLyDoTuChoi());
                break;
            case Contains.CHODUYET_LDCC + Contains.TUCHOI_LDCC:
                hdr.setNgayPduyet(LocalDate.now());
                hdr.setNguoiPduyetId(userInfo.getId());
                hdr.setLyDoTuChoi(req.getLyDoTuChoi());
                break;
            default:
                throw new Exception("Phê duyệt không thành công");
        }
        hdr.setTrangThai(req.getTrangThai());
        DcnbBBNTBQHdr created = hdrRepository.save(hdr);
        return created;
    }

    @Override
    public void delete(Long id) throws Exception {
        DcnbBBNTBQHdr detail = detail(id);
        hdrRepository.delete(detail);
        dtlRepository.deleteAllByHdrId(id);
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
    public void export(DcnbBBNTBQHdrReq req, HttpServletResponse response) throws Exception {

    }
    @Override
    public ReportTemplateResponse preview(DcnbBBNTBQHdrReq objReq) throws Exception {
        Optional<DcnbBBNTBQHdr> dcnbBBNTBQHdr = hdrRepository.findById(objReq.getId());
        if (!dcnbBBNTBQHdr.isPresent()) throw new Exception("Không tồn tại bản ghi");
        ReportTemplate model = findByTenFile(objReq.getReportTemplateRequest());
        byte[] byteArray = Base64.getDecoder().decode(model.getFileUpload());
        ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArray);
        DcnbBBNTBQHdrPreview dcnbBBNTBQHdrPreview = setDataToPreview(dcnbBBNTBQHdr);
        return docxToPdfConverter.convertDocxToPdf(inputStream, dcnbBBNTBQHdrPreview);
    }

    private DcnbBBNTBQHdrPreview setDataToPreview(Optional<DcnbBBNTBQHdr> dcnbBBNTBQHdr) {
        return DcnbBBNTBQHdrPreview.builder()
                .maDvi(dcnbBBNTBQHdr.get().getMaDvi())
                .tenDvi(dcnbBBNTBQHdr.get().getTenDvi())
                .maQhns(dcnbBBNTBQHdr.get().getMaQhns())
                .soBban(dcnbBBNTBQHdr.get().getSoBban())
                .ngayLap(dcnbBBNTBQHdr.get().getNgayLap().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                .tenChiCuc(dcnbBBNTBQHdr.get().getTenDvi())
                .ldChiCuc(dcnbBBNTBQHdr.get().getLdChiCuc())
                .keToan(dcnbBBNTBQHdr.get().getKeToan())
                .ktvBaoQuan(dcnbBBNTBQHdr.get().getKthuatVien())
                .thuKho(dcnbBBNTBQHdr.get().getThuKho())
                .chungLoaiHangHoa(dcnbBBNTBQHdr.get().getTenCloaiVthh())
                .tenNganKho(dcnbBBNTBQHdr.get().getTenNganKho())
                .tenLoKho(dcnbBBNTBQHdr.get().getTenLoKho())
                .loaiHinhKho(dcnbBBNTBQHdr.get().getLoaiHinhKho())
                .tichLuongKhaDung(dcnbBBNTBQHdr.get().getTichLuongKhaDung())
                .slThucNhapDc(dcnbBBNTBQHdr.get().getSlThucNhapDc())
                .phuongThucBaoQuan(dcnbBBNTBQHdr.get().getPhuongThucBaoQuan())
                .hinhThucKeLot(dcnbBBNTBQHdr.get().getHinhThucBaoQuan())
                .hinhThucBaoQuan(dcnbBBNTBQHdr.get().getHinhThucBaoQuan())
                .dinhMucDuocGiao(dcnbBBNTBQHdr.get().getDinhMucDuocGiao())
                .dinhMucTT(dcnbBBNTBQHdr.get().getDinhMucTT())
                .tongKinhPhiDaTh(dcnbBBNTBQHdr.get().getTongKinhPhiDaTh())
                .tongKinhPhiDaThBc(dcnbBBNTBQHdr.get().getTongKinhPhiDaThBc())
                .nhanXet(dcnbBBNTBQHdr.get().getNhanXet())
                .dcnbBBNTBQDtl(dcnbBBNTBQHdr.get().getDcnbBBNTBQDtl())
                .build();
    }
}
