package com.tcdt.qlnvhang.service.suachuahang.impl;

import com.google.common.collect.Lists;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.xuathang.suachuahang.ScPhieuXuatKhoDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.suachuahang.ScPhieuXuatKhoHdrRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.dieuchuyennoibo.SearchPhieuXuatKho;
import com.tcdt.qlnvhang.request.suachua.ScPhieuXuatKhoReq;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBBNTBQHdr;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbPhieuNhapKhoDtl;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbPhieuNhapKhoHdr;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScPhieuXuatKhoDtl;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScPhieuXuatKhoHdr;
import com.tcdt.qlnvhang.util.Contains;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.persistence.Transient;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ScPhieuXuatKhoServiceImpl extends BaseServiceImpl {

    @Autowired
    private ScPhieuXuatKhoHdrRepository scPhieuXuatKhoHdrRepository;

    @Autowired
    private ScPhieuXuatKhoDtlRepository scPhieuXuatKhoDtlRepository;

    @Autowired
    private FileDinhKemService fileDinhKemService;

    public Page<ScPhieuXuatKhoHdr> searchPage(CustomUserDetails currentUser, ScPhieuXuatKhoReq req) throws Exception {
        String dvql = currentUser.getDvql();
        req.setMaDvi(dvql);
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        Page<ScPhieuXuatKhoHdr> searchDto = scPhieuXuatKhoHdrRepository.searchPage(req, pageable);

        return searchDto;
    }

    @Transactional
    public ScPhieuXuatKhoHdr save(CustomUserDetails currentUser, ScPhieuXuatKhoReq objReq) throws Exception {
        if (currentUser == null) {
            throw new Exception("Bad request.");
        }
        if (!currentUser.getUser().getCapDvi().equals(Contains.CAP_CHI_CUC)) {
            throw new Exception("Chức năng chỉ dành cho cấp chi cục");
        }
        Optional<ScPhieuXuatKhoHdr> soDxuat = scPhieuXuatKhoHdrRepository.findBySoPhieuXuatKho(objReq.getSoPhieuXuatKho());
        if (soDxuat.isPresent()) {
            throw new Exception("Số đề xuất đã tồn tại");
        }
        ScPhieuXuatKhoHdr data = new ScPhieuXuatKhoHdr();
        BeanUtils.copyProperties(objReq, data);
        data.setMaDvi(currentUser.getDvql());
        objReq.getChildren().forEach(e -> {
            e.setParent(data);
        });
        ScPhieuXuatKhoHdr created = scPhieuXuatKhoHdrRepository.save(data);
        saveFileDinhKem(objReq.getFileDinhKems(), created.getId(), ScPhieuXuatKhoHdr.TABLE_NAME);
        return created;
    }

    @Transactional
    public ScPhieuXuatKhoHdr update(CustomUserDetails currentUser, ScPhieuXuatKhoReq objReq) throws Exception {
        if (currentUser == null) {
            throw new Exception("Bad request.");
        }
        Optional<ScPhieuXuatKhoHdr> optional = scPhieuXuatKhoHdrRepository.findById(objReq.getId());
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu cần sửa");
        }
        if (!currentUser.getUser().getCapDvi().equals(Contains.CAP_CHI_CUC)) {
            throw new Exception("Chức năng chỉ dành cho cấp chi cục");
        }

        ScPhieuXuatKhoHdr data = optional.get();
        BeanUtils.copyProperties(objReq, data);
        data.setChildren(objReq.getChildren());
        ScPhieuXuatKhoHdr update = scPhieuXuatKhoHdrRepository.save(data);
        fileDinhKemService.delete(update.getId(), Lists.newArrayList(ScPhieuXuatKhoHdr.TABLE_NAME));
        List<FileDinhKem> canCu = fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKemReq(), update.getId(), ScPhieuXuatKhoHdr.TABLE_NAME);
        update.setFileDinhKems(canCu);
        return update;
    }

    public ScPhieuXuatKhoHdr detail(Long id) throws Exception {
        if (ObjectUtils.isEmpty(id)) {
            throw new Exception("Tham số không hợp lệ.");
        }
        Optional<ScPhieuXuatKhoHdr> optional = scPhieuXuatKhoHdrRepository.findById(id);
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        ScPhieuXuatKhoHdr data = optional.get();
        data.setFileDinhKems(fileDinhKemService.search(id, Collections.singletonList(ScPhieuXuatKhoHdr.TABLE_NAME)));
        return data;
    }

    @Transient
    public void delete(Long id) throws Exception {
        Optional<ScPhieuXuatKhoHdr> optional = scPhieuXuatKhoHdrRepository.findById(id);
        if (!optional.isPresent()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        ScPhieuXuatKhoHdr data = optional.get();
        List<ScPhieuXuatKhoDtl> list = scPhieuXuatKhoDtlRepository.findByHdrId(id);
        scPhieuXuatKhoDtlRepository.deleteAll(list);
        scPhieuXuatKhoHdrRepository.delete(data);
    }

//    @Transient
//    public void deleteMulti(IdSearchReq idSearchReq) throws Exception {
//        List<ScPhieuXuatKho> list = scPhieuXuatKhoRepository.findAllByIdIn(idSearchReq.getIdList());
//
//        if (list.isEmpty()) {
//            throw new Exception("Bản ghi không tồn tại");
//        }
//        List<Long> listId = list.stream().map(ScPhieuXuatKho::getId).collect(Collectors.toList());
//        List<DcnbBangKeCanHangDtl> listBangKe = dcnbBangKeCanHangDtlRepository.findByHdrIdIn(listId);
//        dcnbBangKeCanHangDtlRepository.deleteAll(listBangKe);
//    }

    @Transactional
    public void approve(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {
        if (StringUtils.isEmpty(statusReq.getId())) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        ScPhieuXuatKhoHdr details = detail(statusReq.getId());
        Optional<ScPhieuXuatKhoHdr> optional = Optional.of(details);
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        optional.get().setTrangThai(statusReq.getTrangThai());
        scPhieuXuatKhoHdrRepository.save(optional.get());
    }

    public void export(CustomUserDetails currentUser, SearchPhieuXuatKho objReq, HttpServletResponse response) throws Exception {

    }
}
