package com.tcdt.qlnvhang.service.dieuchuyennoibo.impl;

import com.google.common.collect.Lists;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.FileDinhKemRepository;
import com.tcdt.qlnvhang.repository.QlnvDmDonviRepository;
import com.tcdt.qlnvhang.repository.dieuchuyennoibo.DcnbBbThuaThieuDtlRepository;
import com.tcdt.qlnvhang.repository.dieuchuyennoibo.DcnbBbThuaThieuHdrRepository;
import com.tcdt.qlnvhang.repository.dieuchuyennoibo.DcnbBbThuaThieuKiemKeDtlRepository;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.dieuchuyennoibo.DcnbBbKqDcSearch;
import com.tcdt.qlnvhang.request.dieuchuyennoibo.DcnbBbThuaThieuHdrReq;
import com.tcdt.qlnvhang.service.dieuchuyennoibo.DcnbBbThuaThieuService;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.catalog.QlnvDmDonvi;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.*;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.UserUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class DcnbBbThuaThieuServiceImpl implements DcnbBbThuaThieuService {
    @Autowired
    private DcnbBbThuaThieuHdrRepository hdrRepository;
    @Autowired
    private DcnbBbThuaThieuDtlRepository dtlRepository;
    @Autowired
    private DcnbBbThuaThieuKiemKeDtlRepository kiemKeDtlRepository;
    @Autowired
    private QlnvDmDonviRepository qlnvDmDonviRepository;
    @Autowired
    private FileDinhKemService fileDinhKemService;
    @Autowired
    private FileDinhKemRepository fileDinhKemRepository;

    @Override
    public Page<DcnbBbThuaThieuHdr> searchPage(DcnbBbThuaThieuHdrReq req) throws Exception {
        CustomUserDetails currentUser = UserUtils.getUserLoginInfo();
        String dvql = currentUser.getDvql();
        req.setMaDvi(dvql);
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        Page<DcnbBbThuaThieuHdr> searchDto = hdrRepository.searchPage(req, pageable);
        return searchDto;
    }

    @Override
    public DcnbBbThuaThieuHdr create(DcnbBbThuaThieuHdrReq objReq) throws Exception {
        CustomUserDetails currentUser = UserUtils.getUserLoginInfo();
        QlnvDmDonvi cqt = qlnvDmDonviRepository.findByMaDvi(currentUser.getDvql());
        if (currentUser == null) {
            throw new Exception("Bad request.");
        }
//        Optional<DcnbBbKqDcHdr> optional = hdrRepository.findFirstBySoBc(objReq.getSoBc());
//        if (optional.isPresent() && objReq.getSoBc() != null && objReq.getSoBc().split("/").length == 1) {
//            throw new Exception("số biên bản lấy mẫu đã tồn tại");
//        }
        DcnbBbThuaThieuHdr data = new DcnbBbThuaThieuHdr();
        BeanUtils.copyProperties(objReq, data);
        data.setMaDvi(cqt.getMaDvi());
        data.setTenDvi(cqt.getTenDvi());
        // Biên bản lấy mẫu thì auto thay đổi thủ kho
        data.setTrangThai(Contains.DUTHAO);
        data.setNgayTao(LocalDateTime.now());
        data.setNguoiTaoId(currentUser.getUser().getId());
        objReq.getChiTiet().forEach(e -> {
            e.setDcnbBbThuaThieuHdr(data);
        });
        objReq.getBanKiemKe().forEach(e -> {
            e.setDcnbBbThuaThieuHdr(data);
        });
        DcnbBbThuaThieuHdr created = hdrRepository.save(data);
        String so = created.getId() + "/" + (new Date().getYear() + 1900) + "/BBTT-" + currentUser.getUser().getDvqlTenVietTat();
        created.setSoBb(so);
        hdrRepository.save(created);
        List<FileDinhKem> canCu = fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKems(), created.getId(), DcnbBbThuaThieuHdr.TABLE_NAME + "_DINH_KEM");
        created.setFileDinhKems(canCu);
        List<FileDinhKem> bbHaoDoi = fileDinhKemService.saveListFileDinhKem(objReq.getFileBienBanHaoDois(), created.getId(), DcnbBbThuaThieuHdr.TABLE_NAME + "_HAO_DOI");
        created.setFileBienBanHaoDois(bbHaoDoi);
        return created;
    }

    @Override
    public DcnbBbThuaThieuHdr update(DcnbBbThuaThieuHdrReq objReq) throws Exception {
        CustomUserDetails currentUser = UserUtils.getUserLoginInfo();
        if (currentUser == null) {
            throw new Exception("Bad request.");
        }
        Optional<DcnbBbThuaThieuHdr> optional = hdrRepository.findById(objReq.getId());
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu cần sửa");
        }
//        Optional<DcnbBbKqDcHdr> soDxuat = hdrRepository.findFirstBySoBc(objReq.getSoBc());
//        if (org.apache.commons.lang3.StringUtils.isNotEmpty(objReq.getSoBc())) {
//            if (soDxuat.isPresent() && objReq.getSoBc().split("/").length == 1) {
//                if (!soDxuat.get().getId().equals(objReq.getId())) {
//                    throw new Exception("số biên bản lấy mẫu đã tồn tại");
//                }
//            }
//        }
        DcnbBbThuaThieuHdr data = optional.get();
        BeanUtils.copyProperties(objReq, data);
        objReq.getChiTiet().forEach(e -> {
            e.setDcnbBbThuaThieuHdr(data);
        });
        objReq.getBanKiemKe().forEach(e -> {
            e.setDcnbBbThuaThieuHdr(data);
        });
        DcnbBbThuaThieuHdr created = hdrRepository.save(data);
        String so = created.getId() + "/" + (new Date().getYear() + 1900) + "/BBTT-" + currentUser.getUser().getDvqlTenVietTat();
        created.setSoBb(so);
        hdrRepository.save(created);
        fileDinhKemService.delete(objReq.getId(), Lists.newArrayList(DcnbBbThuaThieuHdr.TABLE_NAME + "_DINH_KEM"));
        List<FileDinhKem> canCu = fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKems(), created.getId(), DcnbBbThuaThieuHdr.TABLE_NAME + "_DINH_KEM");
        created.setFileDinhKems(canCu);
        fileDinhKemService.delete(objReq.getId(), Lists.newArrayList(DcnbBbThuaThieuHdr.TABLE_NAME + "_HAO_DOI"));
        List<FileDinhKem> bbHaoDoi = fileDinhKemService.saveListFileDinhKem(objReq.getFileBienBanHaoDois(), created.getId(), DcnbBbThuaThieuHdr.TABLE_NAME + "_HAO_DOI");
        created.setFileBienBanHaoDois(bbHaoDoi);
        return created;
    }

    @Override
    public DcnbBbThuaThieuHdr detail(Long id) throws Exception {
        Optional<DcnbBbThuaThieuHdr> optional = hdrRepository.findById(id);
        if (!optional.isPresent()) {
            throw new Exception("Dữ liệu không tồn tại");
        }
        List<FileDinhKem> canCu = fileDinhKemRepository.findByDataIdAndDataTypeIn(id, Collections.singleton(DcnbBbThuaThieuHdr.TABLE_NAME + "_DINH_KEM"));
        optional.get().setFileDinhKems(canCu);
        List<FileDinhKem> bbHaoDoi = fileDinhKemRepository.findByDataIdAndDataTypeIn(id, Collections.singleton(DcnbBbThuaThieuHdr.TABLE_NAME + "_HAO_DOI"));
        optional.get().setFileBienBanHaoDois(bbHaoDoi);
        return optional.get();
    }

    @Override
    public DcnbBbThuaThieuHdr approve(DcnbBbThuaThieuHdrReq req) throws Exception {
        return null;
    }

    @Override
    public void delete(Long id) throws Exception {
        Optional<DcnbBbThuaThieuHdr> optional = hdrRepository.findById(id);
        if (!optional.isPresent()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        DcnbBbThuaThieuHdr data = optional.get();
        List<DcnbBbThuaThieuDtl> list = dtlRepository.findByHdrId(data.getId());
        dtlRepository.deleteAll(list);

        List<DcnbBbThuaThieuKiemKeDtl> byHdrId = kiemKeDtlRepository.findByHdrId(data.getId());
        kiemKeDtlRepository.deleteAll(byHdrId);

        fileDinhKemService.delete(id, Lists.newArrayList(DcnbBbThuaThieuDtl.TABLE_NAME));
        hdrRepository.delete(data);
    }

    @Override
    public void deleteMulti(List<Long> listMulti) throws Exception {

    }

    @Override
    public void export(DcnbBbThuaThieuHdrReq req, HttpServletResponse response) throws Exception {

    }

    public void finish(StatusReq statusReq) throws Exception {
        CustomUserDetails currentUser = UserUtils.getUserLoginInfo();
        if (StringUtils.isEmpty(statusReq.getId())) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        DcnbBbThuaThieuHdr details = detail(Long.valueOf(statusReq.getId()));
        Optional<DcnbBbThuaThieuHdr> optional = Optional.of(details);
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        optional.get().setTrangThai(Contains.DA_HOAN_THANH);
        hdrRepository.save(optional.get());
    }
}
