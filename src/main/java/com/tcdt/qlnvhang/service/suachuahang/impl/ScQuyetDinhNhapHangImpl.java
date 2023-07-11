package com.tcdt.qlnvhang.service.suachuahang.impl;

import com.google.common.collect.Lists;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.xuathang.suachuahang.ScQuyetDinhNhapHangRepository;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.suachua.ScQuyetDinhNhapHangReq;
import com.tcdt.qlnvhang.request.suachua.ScQuyetDinhXuatHangReq;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScQuyetDinhNhapHang;
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
public class ScQuyetDinhNhapHangImpl extends BaseServiceImpl {
    @Autowired
    private ScQuyetDinhNhapHangRepository scQuyetDinhNhapHangRepository;

    @Autowired
    private FileDinhKemService fileDinhKemService;

    public Page<ScQuyetDinhNhapHang> searchPage(CustomUserDetails currentUser, ScQuyetDinhNhapHangReq req) throws Exception {
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        Page<ScQuyetDinhNhapHang> searchDto = scQuyetDinhNhapHangRepository.searchPage(req, pageable);

        return searchDto;
    }

    public ScQuyetDinhNhapHang create(CustomUserDetails currentUser, ScQuyetDinhNhapHangReq req) throws Exception {
        ScQuyetDinhNhapHang hdr = new ScQuyetDinhNhapHang();
        BeanUtils.copyProperties(req, hdr);
        hdr.setTrangThai(NhapXuatHangTrangThaiEnum.DUTHAO.getId());
        ScQuyetDinhNhapHang created = scQuyetDinhNhapHangRepository.save(hdr);
        List<FileDinhKem> canCu = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKemReq(), created.getId(), ScQuyetDinhNhapHang.TABLE_NAME + "_CAN_CU");
        created.setCanCu(canCu);
        List<FileDinhKem> fileDinhKem = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKemReq(), created.getId(), ScQuyetDinhNhapHang.TABLE_NAME + "_TAI_LIEU_DINH_KEM");
        created.setFileDinhKems(fileDinhKem);
        return created;
    }

    public ScQuyetDinhNhapHang update(CustomUserDetails currentUser, ScQuyetDinhNhapHangReq req) throws Exception {
        Optional<ScQuyetDinhNhapHang> optional = scQuyetDinhNhapHangRepository.findById(req.getId());
        if (!optional.isPresent()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        ScQuyetDinhNhapHang hdr = optional.get();
        BeanUtils.copyProperties(req, hdr);
        ScQuyetDinhNhapHang created = scQuyetDinhNhapHangRepository.save(hdr);
        fileDinhKemService.delete(req.getId(), Lists.newArrayList(ScQuyetDinhNhapHang.TABLE_NAME + "_CAN_CU"));
        List<FileDinhKem> canCu = fileDinhKemService.saveListFileDinhKem(req.getCanCu(), created.getId(), ScQuyetDinhNhapHang.TABLE_NAME + "_CAN_CU");
        created.setCanCu(canCu);
        fileDinhKemService.delete(req.getId(), Lists.newArrayList(ScQuyetDinhNhapHang.TABLE_NAME + "_TAI_LIEU_DINH_KEM"));
        List<FileDinhKem> fileDinhKemList = fileDinhKemService.saveListFileDinhKem(req.getCanCu(), created.getId(), ScQuyetDinhNhapHang.TABLE_NAME + "_TAI_LIEU_DINH_KEM");
        created.setFileDinhKems(fileDinhKemList);

        return created;
    }

    public ScQuyetDinhNhapHang detail(Long id) throws Exception {
        if (ObjectUtils.isEmpty(id)) {
            throw new Exception("Tham số không hợp lệ.");
        }
        Optional<ScQuyetDinhNhapHang> optional = scQuyetDinhNhapHangRepository.findById(id);
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        ScQuyetDinhNhapHang data = optional.get();
        data.setFileDinhKems(fileDinhKemService.search(id, Collections.singletonList(ScQuyetDinhNhapHang.TABLE_NAME)));
        return data;
    }

    @Transient
    public void delete(Long id) throws Exception {
        Optional<ScQuyetDinhNhapHang> optional = scQuyetDinhNhapHangRepository.findById(id);
        if (!optional.isPresent()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        ScQuyetDinhNhapHang data = optional.get();
        scQuyetDinhNhapHangRepository.delete(data);
    }

    @Transactional
    public void approve(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {
        if (ObjectUtils.isEmpty(statusReq.getId())) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        ScQuyetDinhNhapHang details = detail(statusReq.getId());
        Optional<ScQuyetDinhNhapHang> optional = Optional.of(details);
        ScQuyetDinhNhapHang data = optional.get();
        data.setTrangThai(statusReq.getTrangThai());
        scQuyetDinhNhapHangRepository.save(data);
    }
}
