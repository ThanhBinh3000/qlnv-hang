package com.tcdt.qlnvhang.service.nhaphang.nhapkhac.impl;

import com.google.common.collect.Lists;
import com.tcdt.qlnvhang.entities.nhaphang.nhapkhac.HhNkBangKeCanHangHdr;
import com.tcdt.qlnvhang.entities.nhaphang.nhapkhac.HhNkBbNhapDayKhoHdr;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.dieuchuyennoibo.DcnbDataLinkHdrRepository;
import com.tcdt.qlnvhang.repository.nhaphang.nhapkhac.HhNkBbNhapDayKhoDtlRepository;
import com.tcdt.qlnvhang.repository.nhaphang.nhapkhac.HhNkBbNhapDayKhoHdrRepository;
import com.tcdt.qlnvhang.request.nhaphang.nhapkhac.HhNkBangKeCanHangHdrReq;
import com.tcdt.qlnvhang.request.nhaphang.nhapkhac.HhNkBbNhapDayKhoHdrReq;
import com.tcdt.qlnvhang.response.nhaphang.nhapkhac.HhNkBbNhapDayKhoHdrDTO;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.service.nhaphang.nhapkhac.HhNkBbNhapDayKhoService;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;
import com.tcdt.qlnvhang.table.UserInfo;
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
import java.time.LocalDate;
import java.util.*;

@Service
public class HhNkBbNhapDayKhoServiceImpl extends BaseServiceImpl implements HhNkBbNhapDayKhoService {

    @Autowired
    private HhNkBbNhapDayKhoHdrRepository hdrRepository;

    @Autowired
    private HhNkBbNhapDayKhoDtlRepository dtlRepository;

    @Autowired
    private FileDinhKemService fileDinhKemService;

    @Autowired
    private DcnbDataLinkHdrRepository dcnbDataLinkHdrRepository;

    @Override
    public Page<HhNkBbNhapDayKhoHdr> searchPage(HhNkBbNhapDayKhoHdrReq req) throws Exception {
        return null;
    }

    public Page<HhNkBbNhapDayKhoHdrDTO> search(HhNkBbNhapDayKhoHdrReq req) throws Exception {
        CustomUserDetails currentUser = UserUtils.getUserLoginInfo();
        req.setMaDvi(currentUser.getDvql());
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        Page<HhNkBbNhapDayKhoHdrDTO> searchDto = null;
        searchDto = hdrRepository.searchPage(req, pageable);

        return searchDto;
    }

    @Override
    public HhNkBbNhapDayKhoHdr create(HhNkBbNhapDayKhoHdrReq req) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null) {
            throw new Exception("Access denied.");
        }
        if (!userInfo.getCapDvi().equals(Contains.CAP_CHI_CUC)) {
            throw new Exception("Văn bản này chỉ có thêm ở cấp chi cục");
        }
//        Optional<HhNkBbNhapDayKhoHdr> optional = hdrRepository.findBySoBb(req.getSoBb());
//        if (optional.isPresent()) {
//            throw new Exception("Số biên bản đã tồn tại");
//        }

        HhNkBbNhapDayKhoHdr data = new HhNkBbNhapDayKhoHdr();
        BeanUtils.copyProperties(req, data);
        data.setMaDvi(userInfo.getDvql());
        data.setId(null);
        req.getChildren().forEach(e -> {
            e.setParent(data);
        });
        HhNkBbNhapDayKhoHdr created = hdrRepository.save(data);
        String so = created.getId() + "/" + (new Date().getYear() + 1900) + "/BBNDK-" + userInfo.getDvqlTenVietTat();
        created.setSoBb(so);
        hdrRepository.save(created);
        List<FileDinhKem> canCu = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKemReq(), created.getId(), HhNkBbNhapDayKhoHdr.TABLE_NAME);
        created.setFileDinhKems(canCu);
        return created;
    }

    @Override
    public HhNkBbNhapDayKhoHdr update(HhNkBbNhapDayKhoHdrReq req) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null) {
            throw new Exception("Access denied.");
        }
        if (!userInfo.getCapDvi().equals(Contains.CAP_CHI_CUC)) {
            throw new Exception("Văn bản này chỉ có thêm ở cấp chi cục");
        }
        Optional<HhNkBbNhapDayKhoHdr> optional = hdrRepository.findById(req.getId());
        if (!optional.isPresent()) {
            throw new Exception("Số biên bản không tồn tại");
        }
        HhNkBbNhapDayKhoHdr data = optional.get();
        req.setMaDvi(userInfo.getDvql());
        BeanUtils.copyProperties(req, data);
        data.setChildren(req.getChildren());
        HhNkBbNhapDayKhoHdr update = hdrRepository.save(data);
        String so = update.getId() + "/" + (new Date().getYear() + 1900) + "/BBNDK-" + userInfo.getDvqlTenVietTat();
        update.setSoBb(so);
        hdrRepository.save(update);
        fileDinhKemService.delete(update.getId(), Lists.newArrayList(HhNkBbNhapDayKhoHdr.TABLE_NAME));
        List<FileDinhKem> canCu = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKemReq(), update.getId(), HhNkBbNhapDayKhoHdr.TABLE_NAME);
        update.setFileDinhKems(canCu);
        return update;
    }

    @Override
    public HhNkBbNhapDayKhoHdr detail(Long id) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null) {
            throw new Exception("Access denied.");
        }
        if (Objects.isNull(id)) {
            throw new Exception("Id is null");
        }
        Optional<HhNkBbNhapDayKhoHdr> optional = hdrRepository.findById(id);
        if (!optional.isPresent()) {
            throw new Exception("Số biên bản không tồn tại");
        }
        HhNkBbNhapDayKhoHdr data = optional.get();
        data.setFileDinhKems(fileDinhKemService.search(id, Collections.singleton(HhNkBbNhapDayKhoHdr.TABLE_NAME)));
        return data;
    }

    @Override
    public HhNkBbNhapDayKhoHdr approve(HhNkBbNhapDayKhoHdrReq req) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null) {
            throw new Exception("Access denied.");
        }
        HhNkBbNhapDayKhoHdr hdr = detail(req.getId());
        String status = hdr.getTrangThai() + req.getTrangThai();
        switch (status) {
            case Contains.TUCHOI_KT + Contains.DUTHAO:
            case Contains.TUCHOI_LDCC + Contains.DUTHAO:
            case Contains.DUTHAO + Contains.CHODUYET_KTVBQ:
                hdr.setNguoiGDuyet(userInfo.getId());
                hdr.setNgayGDuyet(LocalDate.now());
                break;
            case Contains.CHODUYET_KTVBQ + Contains.CHODUYET_KT:
                hdr.setIdKyThuatVien(userInfo.getId());
                hdr.setTenKyThuatVien(userInfo.getFullName());
                hdr.setNguoiGDuyetKtvbq(userInfo.getId());
                hdr.setNgayGDuyetKtvbq(LocalDate.now());
                break;
            case Contains.CHODUYET_KT + Contains.CHODUYET_LDCC:
                hdr.setIdKeToan(userInfo.getId());
                hdr.setTenKeToan(userInfo.getFullName());
                hdr.setNguoiGDuyetKt(userInfo.getId());
                hdr.setNgayGDuyetKt(LocalDate.now());
                break;
            case Contains.CHODUYET_LDCC + Contains.DADUYET_LDCC:
                hdr.setIdLanhDao(userInfo.getId());
                hdr.setTenLanhDao(userInfo.getFullName());
                hdr.setNguoiPDuyet(userInfo.getId());
                hdr.setNgayPDuyet(LocalDate.now());
                break;
            case Contains.CHODUYET_KT + Contains.TUCHOI_KT:
                hdr.setLyDoTuChoi(req.getLyDoTuChoi());
                hdr.setIdKeToan(userInfo.getId());
                hdr.setTenKeToan(userInfo.getFullName());
                hdr.setNguoiGDuyetKt(userInfo.getId());
                hdr.setNgayGDuyetKt(LocalDate.now());
                break;
            case Contains.CHODUYET_LDCC + Contains.TUCHOI_LDCC:
                hdr.setLyDoTuChoi(req.getLyDoTuChoi());
                hdr.setIdLanhDao(userInfo.getId());
                hdr.setTenLanhDao(userInfo.getFullName());
                hdr.setNguoiPDuyet(userInfo.getId());
                hdr.setNgayPDuyet(LocalDate.now());
                break;
            default:
                throw new Exception("Phê duyệt không thành công");
        }
        hdr.setTrangThai(req.getTrangThai());
        HhNkBbNhapDayKhoHdr created = hdrRepository.save(hdr);
        return created;
    }

    @Override
    public void delete(Long id) throws Exception {
        HhNkBbNhapDayKhoHdr detail = detail(id);
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
    public void export(HhNkBbNhapDayKhoHdrReq req, HttpServletResponse response) throws Exception {

    }

    @Override
    public List<HhNkBbNhapDayKhoHdrDTO> searchList(CustomUserDetails currentUser, HhNkBbNhapDayKhoHdrReq param) {
        param.setMaDvi(currentUser.getDvql());
        return hdrRepository.searchList(param);
    }

    public ReportTemplateResponse preview(HhNkBbNhapDayKhoHdrReq objReq) throws Exception {
        HhNkBbNhapDayKhoHdr optional = detail(objReq.getId());
        ReportTemplate model = findByTenFile(objReq.getReportTemplateRequest());
        byte[] byteArray = Base64.getDecoder().decode(model.getFileUpload());
        ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArray);
        return docxToPdfConverter.convertDocxToPdf(inputStream, optional);
    }
}