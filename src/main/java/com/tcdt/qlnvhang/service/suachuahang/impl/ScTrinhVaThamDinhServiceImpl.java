package com.tcdt.qlnvhang.service.suachuahang.impl;

import com.google.common.collect.Lists;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.repository.FileDinhKemRepository;
import com.tcdt.qlnvhang.repository.xuathang.suachuahang.ScTrinhThamDinhRepository;
import com.tcdt.qlnvhang.request.suachua.ScTrinhVaThamDinhReq;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.service.suachuahang.ScTrinhVaThamDinhService;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBienBanLayMauHdr;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScTrinhThamDinh;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.UserUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.Transient;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ScTrinhVaThamDinhServiceImpl extends BaseServiceImpl implements ScTrinhVaThamDinhService {

    @Autowired
    private ScTrinhThamDinhRepository hdrRepository;

    @Autowired
    private FileDinhKemService fileDinhKemService;

    @Autowired
    private FileDinhKemRepository fileDinhKemRepository;

    @Override
    public Page<ScTrinhThamDinh> searchPage(ScTrinhVaThamDinhReq req) throws Exception {
        UserInfo currentUser = SecurityContextService.getUser();
        if (currentUser == null) {
            throw new Exception("Access denied.");
        }
        String dvql = currentUser.getDvql();
        if (!currentUser.getCapDvi().equals(Contains.CAP_TONG_CUC)) {
            req.setMaDvi(dvql.substring(0, 6));
        }
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        Page<ScTrinhThamDinh> search = hdrRepository.searchPage(req, pageable);
        return search;
    }

    @Transactional
    @Override
    public ScTrinhThamDinh create(ScTrinhVaThamDinhReq req) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        ScTrinhThamDinh hdr = new ScTrinhThamDinh();
        BeanUtils.copyProperties(req, hdr);

        hdr.setNam(LocalDate.now().getYear());
        hdr.setMaDvi(userInfo.getDvql());
        hdr.setTrangThai(NhapXuatHangTrangThaiEnum.DUTHAO.getId());
        ScTrinhThamDinh created = hdrRepository.save(hdr);
        List<FileDinhKem> canCu = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKemReq(), created.getId(), ScTrinhThamDinh.TABLE_NAME + "_CAN_CU");
        created.setCanCu(canCu);
        List<FileDinhKem> fileDinhKem = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKemReq(), created.getId(), ScTrinhThamDinh.TABLE_NAME + "_TAI_LIEU_DINH_KEM");
        created.setFileDinhKem(fileDinhKem);
        return created;
    }
    @Transactional
    @Override
    public ScTrinhThamDinh update(ScTrinhVaThamDinhReq req) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        Optional<ScTrinhThamDinh> optional = hdrRepository.findById(req.getId());
        if (optional.isEmpty()){
            throw new Exception("Bản ghi không tồn tại");
        }
        ScTrinhThamDinh hdr = optional.get();
        BeanUtils.copyProperties(req, hdr);

        hdr.setNam(LocalDate.now().getYear());
        hdr.setMaDvi(userInfo.getDvql());
        hdr.setTrangThai(NhapXuatHangTrangThaiEnum.DUTHAO.getId());
        ScTrinhThamDinh created = hdrRepository.save(hdr);
        fileDinhKemService.delete(req.getId(), Lists.newArrayList(ScTrinhThamDinh.TABLE_NAME + "_CAN_CU"));
        List<FileDinhKem> canCu = fileDinhKemService.saveListFileDinhKem(req.getCanCu(), created.getId(), ScTrinhThamDinh.TABLE_NAME + "_CAN_CU");
        created.setCanCu(canCu);
        fileDinhKemService.delete(req.getId(), Lists.newArrayList(ScTrinhThamDinh.TABLE_NAME + "_TAI_LIEU_DINH_KEM"));
        List<FileDinhKem> fileDinhKemList = fileDinhKemService.saveListFileDinhKem(req.getCanCu(), created.getId(), ScTrinhThamDinh.TABLE_NAME + "_TAI_LIEU_DINH_KEM");
        created.setFileDinhKem(fileDinhKemList);

        return created;
    }

    @Override
    public ScTrinhThamDinh detail(Long id) throws Exception {
        Optional<ScTrinhThamDinh> optional = hdrRepository.findById(id);
        if (optional.isEmpty()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        ScTrinhThamDinh data = optional.get();
        List<FileDinhKem> canCu = fileDinhKemRepository.findByDataIdAndDataTypeIn(data.getId(), Collections.singleton(ScTrinhThamDinh.TABLE_NAME + "_CAN_CU"));
        data.setCanCu(canCu);

        List<FileDinhKem> fileDinhKemList = fileDinhKemRepository.findByDataIdAndDataTypeIn(data.getId(), Collections.singleton(ScTrinhThamDinh.TABLE_NAME + "_TAI_LIEU_DINH_KEM"));
        data.setFileDinhKem(fileDinhKemList);
        return data;
    }

    @Override
    public ScTrinhThamDinh approve(ScTrinhVaThamDinhReq req) throws Exception {
        Optional<ScTrinhThamDinh> optional = hdrRepository.findById(req.getId());
        if (optional.isEmpty()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        ScTrinhThamDinh hdr = optional.get();
        String status = hdr.getTrangThai() + req.getTrangThai();
        switch (status) {
            // Arena các roll back approve
            case Contains.TUCHOI_TK + Contains.DUTHAO:
            case Contains.TUCHOI_KT + Contains.DUTHAO:
            case Contains.TUCHOI_LDCC + Contains.DUTHAO:
                break;
            // Arena các cấp duuyệt
            case Contains.DUTHAO + Contains.CHODUYET_TK:
                break;
            case Contains.CHODUYET_TK + Contains.CHODUYET_KT:
                break;
            case Contains.CHODUYET_KT + Contains.CHODUYET_LDCC:
                break;
            case Contains.CHODUYET_LDCC + Contains.DADUYET_LDCC:
                break;
            // Arena từ chối
            case Contains.CHODUYET_TK + Contains.TUCHOI_TK:
            case Contains.CHODUYET_KT + Contains.TUCHOI_KT:
            case Contains.CHODUYET_LDCC + Contains.TUCHOI_LDCC:
                break;
            default:
                throw new Exception("Phê duyệt không thành công");
        }
        hdr.setTrangThai(req.getTrangThai());
        ScTrinhThamDinh save = hdrRepository.save(hdr);
        return save;
    }
    @Transient
    @Override
    public void delete(Long id) throws Exception {
        var optional = hdrRepository.findById(id);
        if (optional.isEmpty()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        fileDinhKemService.delete(id, Lists.newArrayList(ScTrinhThamDinh.TABLE_NAME + "_CAN_CU"));
        fileDinhKemService.delete(id, Lists.newArrayList(ScTrinhThamDinh.TABLE_NAME + "_TAI_LIEU_DINH_KEM"));
        hdrRepository.delete(optional.get());

    }

    @Override
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

    @Override
    public void export(ScTrinhVaThamDinhReq req, HttpServletResponse response) throws Exception {

    }
}
