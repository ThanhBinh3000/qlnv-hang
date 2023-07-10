package com.tcdt.qlnvhang.service.suachuahang.impl;

import com.google.common.collect.Lists;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.xuathang.suachuahang.ScPhieuNhapKhoDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.suachuahang.ScPhieuNhapKhoHdrRepository;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.dieuchuyennoibo.DcnbPhieuNhapKhoHdrReq;
import com.tcdt.qlnvhang.request.suachua.ScPhieuNhapKhoReq;
import com.tcdt.qlnvhang.request.suachua.ScQuyetDinhNhapHangReq;
import com.tcdt.qlnvhang.response.suachua.ScPhieuNhapKhoDTO;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbDataLinkDtl;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbDataLinkHdr;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbPhieuNhapKhoDtl;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbPhieuNhapKhoHdr;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScPhieuNhapKhoDtl;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScPhieuNhapKhoHdr;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScQuyetDinhNhapHang;
import com.tcdt.qlnvhang.util.Contains;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ScPhieuNhapKhoServiceImpl extends BaseServiceImpl {
    @Autowired
    private ScPhieuNhapKhoHdrRepository hdrRepository;
    @Autowired
    private ScPhieuNhapKhoDtlRepository dtlRepository;
    @Autowired
    private FileDinhKemService fileDinhKemService;

    public Page<ScPhieuNhapKhoDTO> searchPage(CustomUserDetails currentUser, ScPhieuNhapKhoReq req) throws Exception {
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        Page<ScPhieuNhapKhoDTO> searchDto = hdrRepository.searchPage(req, pageable);

        return searchDto;
    }

    public ScPhieuNhapKhoHdr create(ScPhieuNhapKhoReq req) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null) {
            throw new Exception("Access denied.");
        }
        if (!userInfo.getCapDvi().equals(Contains.CAP_CHI_CUC)) {
            throw new Exception("Văn bản này chỉ có thêm ở cấp chi cục");
        }
        Optional<ScPhieuNhapKhoHdr> optional = hdrRepository.findBySoPhieuNhapKho(req.getSoPhieuNhapKho());
        if (optional.isPresent()) {
            throw new Exception("Số biên bản đã tồn tại");
        }

        ScPhieuNhapKhoHdr data = new ScPhieuNhapKhoHdr();
        BeanUtils.copyProperties(req, data);
        data.setMaDvi(userInfo.getDvql());
        if (req.getSoPhieuNhapKho() != null) {
            data.setId(Long.parseLong(req.getSoPhieuNhapKho().split("/")[0]));
        }
        req.getChildren().forEach(e -> {
            e.setParent(data);
        });
        ScPhieuNhapKhoHdr created = hdrRepository.save(data);
        List<FileDinhKem> canCu = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKemReq(), created.getId(), ScPhieuNhapKhoHdr.TABLE_NAME);
        created.setFileDinhKems(canCu);
        return created;
    }

    public ScPhieuNhapKhoHdr update(ScPhieuNhapKhoReq req) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null) {
            throw new Exception("Access denied.");
        }
        if (!userInfo.getCapDvi().equals(Contains.CAP_CHI_CUC)) {
            throw new Exception("Văn bản này chỉ có thêm ở cấp chi cục");
        }
        Optional<ScPhieuNhapKhoHdr> optional = hdrRepository.findById(req.getId());
        if (!optional.isPresent()) {
            throw new Exception("Số biên bản không tồn tại");
        }
        ScPhieuNhapKhoHdr data = optional.get();
        BeanUtils.copyProperties(req, data);
        data.setChildren(req.getChildren());
        ScPhieuNhapKhoHdr update = hdrRepository.save(data);
        fileDinhKemService.delete(update.getId(), Lists.newArrayList(ScPhieuNhapKhoHdr.TABLE_NAME));
        List<FileDinhKem> canCu = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKemReq(), update.getId(), ScPhieuNhapKhoHdr.TABLE_NAME);
        update.setFileDinhKems(canCu);
        return update;
    }

    public ScPhieuNhapKhoHdr detail(Long id) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null) {
            throw new Exception("Access denied.");
        }
        if (Objects.isNull(id)) {
            throw new Exception("Id is null");
        }
        Optional<ScPhieuNhapKhoHdr> optional = hdrRepository.findById(id);
        if (!optional.isPresent()) {
            throw new Exception("Số biên bản không tồn tại");
        }
        ScPhieuNhapKhoHdr data = optional.get();
        data.setFileDinhKems(fileDinhKemService.search(id, Collections.singleton(ScPhieuNhapKhoHdr.TABLE_NAME)));
        return data;
    }


    public ScPhieuNhapKhoHdr approve(StatusReq req) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null) {
            throw new Exception("Access denied.");
        }
        ScPhieuNhapKhoHdr hdr = detail(req.getId());
        hdr.setTrangThai(req.getTrangThai());
        ScPhieuNhapKhoHdr created = hdrRepository.save(hdr);
        return created;
    }


    public void delete(Long id) throws Exception {
        Optional<ScPhieuNhapKhoHdr> optional = hdrRepository.findById(id);
        if (!optional.isPresent()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        ScPhieuNhapKhoHdr data = optional.get();
        List<ScPhieuNhapKhoDtl> list = dtlRepository.findByHdrId(data.getId());
        dtlRepository.deleteAll(list);
        hdrRepository.delete(data);
    }


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


}
