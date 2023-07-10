package com.tcdt.qlnvhang.service.suachuahang.impl;

import com.google.common.collect.Lists;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.xuathang.suachuahang.ScKiemTraChatLuongDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.suachuahang.ScKiemTraChatLuongHdrRepository;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.suachua.ScKiemTraChatLuongReq;
import com.tcdt.qlnvhang.response.suachua.ScKiemTraChatLuongDTO;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScKiemTraChatLuongDtl;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScKiemTraChatLuongHdr;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScPhieuXuatKhoHdr;
import com.tcdt.qlnvhang.util.Contains;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.persistence.Transient;
import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
@Service
public class ScKiemTraChatLuongImpl extends BaseServiceImpl {
    @Autowired
    private ScKiemTraChatLuongHdrRepository hdrRepository;

    @Autowired
    private ScKiemTraChatLuongDtlRepository dtlRepository;

    @Autowired
    private FileDinhKemService fileDinhKemService;

    public Page<ScKiemTraChatLuongDTO> searchPage(CustomUserDetails currentUser, ScKiemTraChatLuongReq req) throws Exception {
        String dvql = currentUser.getDvql();
        req.setMaDvi(dvql);
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        Page<ScKiemTraChatLuongDTO> searchDto = hdrRepository.searchPage(req, pageable);

        return searchDto;
    }

    @Transactional
    public ScKiemTraChatLuongHdr save(CustomUserDetails currentUser, ScKiemTraChatLuongReq objReq) throws Exception {
        if (currentUser == null) {
            throw new Exception("Bad request.");
        }
        if (!currentUser.getUser().getCapDvi().equals(Contains.CAP_CHI_CUC)) {
            throw new Exception("Chức năng chỉ dành cho cấp chi cục");
        }
        Optional<ScKiemTraChatLuongHdr> soDxuat = hdrRepository.findBySoPhieuKdcl(objReq.getSoPhieuKdcl());
        if (soDxuat.isPresent()) {
            throw new Exception("Số phiếu đã tồn tại");
        }
        ScKiemTraChatLuongHdr data = new ScKiemTraChatLuongHdr();
        BeanUtils.copyProperties(objReq, data);
        data.setMaDvi(currentUser.getDvql());
        objReq.getScKiemTraChatLuongDtls().forEach(e -> {
            e.setScKiemTraChatLuongHdr(data);
        });
        ScKiemTraChatLuongHdr created = hdrRepository.save(data);
        saveFileDinhKem(objReq.getFileDinhKemReqs(), created.getId(), ScKiemTraChatLuongHdr.TABLE_NAME);
        return created;
    }

    @Transactional
    public ScKiemTraChatLuongHdr update(ScKiemTraChatLuongReq objReq) throws Exception {
        Optional<ScKiemTraChatLuongHdr> optional = hdrRepository.findById(objReq.getId());
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu cần sửa");
        }

        ScKiemTraChatLuongHdr data = optional.get();
        objReq.setMaDvi(data.getMaDvi());
        BeanUtils.copyProperties(objReq, data);
        data.setScKiemTraChatLuongDtls(objReq.getScKiemTraChatLuongDtls());
        ScKiemTraChatLuongHdr update = hdrRepository.save(data);
        fileDinhKemService.delete(update.getId(), Lists.newArrayList(ScKiemTraChatLuongHdr.TABLE_NAME));
        List<FileDinhKem> canCu = fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKemReq(), update.getId(), ScKiemTraChatLuongHdr.TABLE_NAME);
        update.setFileDinhKems(canCu);
        return update;
    }

    public ScKiemTraChatLuongHdr detail(Long id) throws Exception {
        if (ObjectUtils.isEmpty(id)) {
            throw new Exception("Tham số không hợp lệ.");
        }
        Optional<ScKiemTraChatLuongHdr> optional = hdrRepository.findById(id);
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        ScKiemTraChatLuongHdr data = optional.get();
        data.setFileDinhKems(fileDinhKemService.search(id, Collections.singletonList(ScKiemTraChatLuongHdr.TABLE_NAME)));
        return data;
    }

    @Transient
    public void delete(Long id) throws Exception {
        Optional<ScKiemTraChatLuongHdr> optional = hdrRepository.findById(id);
        if (!optional.isPresent()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        ScKiemTraChatLuongHdr data = optional.get();
        List<ScKiemTraChatLuongDtl> list = dtlRepository.findAllByHdrId(id);
        dtlRepository.deleteAll(list);
        hdrRepository.delete(data);
    }

    @Transactional
    public void approve(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {
        if (ObjectUtils.isEmpty(statusReq.getId())) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        ScKiemTraChatLuongHdr details = detail(statusReq.getId());
        Optional<ScKiemTraChatLuongHdr> optional = Optional.of(details);
        ScKiemTraChatLuongHdr data = optional.get();
        data.setTrangThai(statusReq.getTrangThai());
        hdrRepository.save(data);
    }
}
