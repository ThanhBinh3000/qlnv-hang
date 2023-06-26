package com.tcdt.qlnvhang.service.suachuahang.impl;

import com.google.common.collect.Lists;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.FileDinhKemRepository;
import com.tcdt.qlnvhang.repository.xuathang.suachuahang.ScQuyetDinhScRepository;
import com.tcdt.qlnvhang.request.suachua.ScQuyetDinhReq;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScQuyetDinhSc;
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
public class ScQuyetDinhScImpl extends BaseServiceImpl {
    @Autowired
    private ScQuyetDinhScRepository scQuyetDinhScRepository;

    @Autowired
    private FileDinhKemService fileDinhKemService;

    @Autowired
    private FileDinhKemRepository fileDinhKemRepository;

    public Page<ScQuyetDinhSc> searchPage(CustomUserDetails currentUser, ScQuyetDinhReq req) throws Exception {
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        Page<ScQuyetDinhSc> search = scQuyetDinhScRepository.searchPage(req, pageable);
        return search;
    }

    public ScQuyetDinhSc create(CustomUserDetails currentUser, ScQuyetDinhReq req) throws Exception {
        ScQuyetDinhSc hdr = new ScQuyetDinhSc();
        BeanUtils.copyProperties(req, hdr);
        hdr.setTrangThai(NhapXuatHangTrangThaiEnum.DUTHAO.getId());
        ScQuyetDinhSc created = scQuyetDinhScRepository.save(hdr);
        List<FileDinhKem> canCu = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKemReq(), created.getId(), ScQuyetDinhSc.TABLE_NAME + "_CAN_CU");
        created.setCanCu(canCu);
        List<FileDinhKem> fileDinhKem = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKemReq(), created.getId(), ScQuyetDinhSc.TABLE_NAME + "_TAI_LIEU_DINH_KEM");
        created.setFileDinhKem(fileDinhKem);
        return created;
    }

    public ScQuyetDinhSc update(CustomUserDetails currentUser, ScQuyetDinhReq req) throws Exception {
        Optional<ScQuyetDinhSc> optional = scQuyetDinhScRepository.findById(req.getId());
        if (optional.isEmpty()){
            throw new Exception("Bản ghi không tồn tại");
        }
        ScQuyetDinhSc hdr = optional.get();
        BeanUtils.copyProperties(req, hdr);
        ScQuyetDinhSc created = scQuyetDinhScRepository.save(hdr);
        fileDinhKemService.delete(req.getId(), Lists.newArrayList(ScQuyetDinhSc.TABLE_NAME + "_CAN_CU"));
        List<FileDinhKem> canCu = fileDinhKemService.saveListFileDinhKem(req.getCanCu(), created.getId(), ScQuyetDinhSc.TABLE_NAME + "_CAN_CU");
        created.setCanCu(canCu);
        fileDinhKemService.delete(req.getId(), Lists.newArrayList(ScQuyetDinhSc.TABLE_NAME + "_TAI_LIEU_DINH_KEM"));
        List<FileDinhKem> fileDinhKemList = fileDinhKemService.saveListFileDinhKem(req.getCanCu(), created.getId(), ScQuyetDinhSc.TABLE_NAME + "_TAI_LIEU_DINH_KEM");
        created.setFileDinhKem(fileDinhKemList);

        return created;
    }

    public ScQuyetDinhSc detail(Long id) throws Exception {
        Optional<ScQuyetDinhSc> optional = scQuyetDinhScRepository.findById(id);
        if (optional.isEmpty()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        ScQuyetDinhSc data = optional.get();
        List<FileDinhKem> canCu = fileDinhKemRepository.findByDataIdAndDataTypeIn(data.getId(), Collections.singleton(ScQuyetDinhSc.TABLE_NAME + "_CAN_CU"));
        data.setCanCu(canCu);

        List<FileDinhKem> fileDinhKemList = fileDinhKemRepository.findByDataIdAndDataTypeIn(data.getId(), Collections.singleton(ScQuyetDinhSc.TABLE_NAME + "_TAI_LIEU_DINH_KEM"));
        data.setFileDinhKem(fileDinhKemList);
        return data;
    }

    @Transient
    public void delete(Long id) throws Exception {
        var optional = scQuyetDinhScRepository.findById(id);
        if (optional.isEmpty()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        fileDinhKemService.delete(id, Lists.newArrayList(ScQuyetDinhSc.TABLE_NAME + "_CAN_CU"));
        fileDinhKemService.delete(id, Lists.newArrayList(ScQuyetDinhSc.TABLE_NAME + "_TAI_LIEU_DINH_KEM"));
        scQuyetDinhScRepository.delete(optional.get());
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

    public void approve(Long id) throws Exception {
        Optional<ScQuyetDinhSc> optional = scQuyetDinhScRepository.findById(id);
        if (optional.isEmpty()){
            throw new Exception("Bản ghi không tồn tại");
        }
        ScQuyetDinhSc data = optional.get();
        data.setTrangThai(Contains.BAN_HANH);
        scQuyetDinhScRepository.save(data);
    }

    public ScQuyetDinhSc export(CustomUserDetails currentUser, ScQuyetDinhReq req) throws Exception {
        return null;
    }

}
