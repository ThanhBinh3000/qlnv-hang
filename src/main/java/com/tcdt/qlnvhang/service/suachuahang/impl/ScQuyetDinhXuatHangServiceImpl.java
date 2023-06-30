package com.tcdt.qlnvhang.service.suachuahang.impl;

import com.google.common.collect.Lists;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.FileDinhKemRepository;
import com.tcdt.qlnvhang.repository.xuathang.suachuahang.ScQuyetDinhScRepository;
import com.tcdt.qlnvhang.repository.xuathang.suachuahang.ScQuyetDinhXuatHangRepository;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.suachua.ScQuyetDinhReq;
import com.tcdt.qlnvhang.request.suachua.ScQuyetDinhXuatHangReq;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScQuyetDinhSc;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScQuyetDinhXuatHang;
import com.tcdt.qlnvhang.util.Contains;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.Transient;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ScQuyetDinhXuatHangServiceImpl extends BaseServiceImpl {
    @Autowired
    private ScQuyetDinhXuatHangRepository scQuyetDinhXuatHangRepository;

    @Autowired
    private FileDinhKemService fileDinhKemService;

    @Autowired
    private FileDinhKemRepository fileDinhKemRepository;

    public Page<ScQuyetDinhXuatHang> searchPage(CustomUserDetails currentUser, ScQuyetDinhXuatHangReq req) throws Exception {
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        Page<ScQuyetDinhXuatHang> search = scQuyetDinhXuatHangRepository.searchPage(req, pageable);
        return search;
    }

    public ScQuyetDinhXuatHang create(CustomUserDetails currentUser, ScQuyetDinhXuatHangReq req) throws Exception {
        ScQuyetDinhXuatHang hdr = new ScQuyetDinhXuatHang();
        BeanUtils.copyProperties(req, hdr);
        hdr.setTrangThai(NhapXuatHangTrangThaiEnum.DUTHAO.getId());
        ScQuyetDinhXuatHang created = scQuyetDinhXuatHangRepository.save(hdr);
        List<FileDinhKem> canCu = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKemReq(), created.getId(), ScQuyetDinhXuatHang.TABLE_NAME + "_CAN_CU");
        created.setCanCu(canCu);
        List<FileDinhKem> fileDinhKem = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKemReq(), created.getId(), ScQuyetDinhXuatHang.TABLE_NAME + "_TAI_LIEU_DINH_KEM");
        created.setFileDinhKem(fileDinhKem);
        return created;
    }


    public ScQuyetDinhXuatHang update(CustomUserDetails currentUser, ScQuyetDinhXuatHangReq req) throws Exception {
        Optional<ScQuyetDinhXuatHang> optional = scQuyetDinhXuatHangRepository.findById(req.getId());
        if (!optional.isPresent()){
            throw new Exception("Bản ghi không tồn tại");
        }
        ScQuyetDinhXuatHang hdr = optional.get();
        BeanUtils.copyProperties(req, hdr);
        ScQuyetDinhXuatHang created = scQuyetDinhXuatHangRepository.save(hdr);
        fileDinhKemService.delete(req.getId(), Lists.newArrayList(ScQuyetDinhXuatHang.TABLE_NAME + "_CAN_CU"));
        List<FileDinhKem> canCu = fileDinhKemService.saveListFileDinhKem(req.getCanCu(), created.getId(), ScQuyetDinhXuatHang.TABLE_NAME + "_CAN_CU");
        created.setCanCu(canCu);
        fileDinhKemService.delete(req.getId(), Lists.newArrayList(ScQuyetDinhXuatHang.TABLE_NAME + "_TAI_LIEU_DINH_KEM"));
        List<FileDinhKem> fileDinhKemList = fileDinhKemService.saveListFileDinhKem(req.getCanCu(), created.getId(), ScQuyetDinhXuatHang.TABLE_NAME + "_TAI_LIEU_DINH_KEM");
        created.setFileDinhKem(fileDinhKemList);

        return created;
    }

    public ScQuyetDinhXuatHang detail(Long id) throws Exception {
        Optional<ScQuyetDinhXuatHang> optional = scQuyetDinhXuatHangRepository.findById(id);
        if (!optional.isPresent()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        ScQuyetDinhXuatHang data = optional.get();
        List<FileDinhKem> canCu = fileDinhKemRepository.findByDataIdAndDataTypeIn(data.getId(), Collections.singleton(ScQuyetDinhXuatHang.TABLE_NAME + "_CAN_CU"));
        data.setCanCu(canCu);

        List<FileDinhKem> fileDinhKemList = fileDinhKemRepository.findByDataIdAndDataTypeIn(data.getId(), Collections.singleton(ScQuyetDinhXuatHang.TABLE_NAME + "_TAI_LIEU_DINH_KEM"));
        data.setFileDinhKem(fileDinhKemList);
        return data;
    }

    @Transient
    public void delete(Long id) throws Exception {
        Optional<ScQuyetDinhXuatHang> optional = scQuyetDinhXuatHangRepository.findById(id);
        if (!optional.isPresent()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        fileDinhKemService.delete(id, Lists.newArrayList(ScQuyetDinhXuatHang.TABLE_NAME + "_CAN_CU"));
        fileDinhKemService.delete(id, Lists.newArrayList(ScQuyetDinhXuatHang.TABLE_NAME + "_TAI_LIEU_DINH_KEM"));
        scQuyetDinhXuatHangRepository.delete(optional.get());
    }

    public void deleteMulti(List<Long> listMulti) throws Exception {
        if (CollectionUtils.isNotEmpty(listMulti)) {
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

    public void approve(StatusReq req) throws Exception {
        Optional<ScQuyetDinhXuatHang> optional = scQuyetDinhXuatHangRepository.findById(req.getId());
        if (!optional.isPresent()){
            throw new Exception("Bản ghi không tồn tại");
        }
        ScQuyetDinhXuatHang data = optional.get();
        data.setTrangThai(req.getTrangThai());
        scQuyetDinhXuatHangRepository.save(data);
    }

    public ScQuyetDinhXuatHang export(CustomUserDetails currentUser, ScQuyetDinhReq req) throws Exception {
        return null;
    }


}
