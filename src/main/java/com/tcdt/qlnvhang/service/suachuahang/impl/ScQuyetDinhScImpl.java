package com.tcdt.qlnvhang.service.suachuahang.impl;

import com.google.common.collect.Lists;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.repository.FileDinhKemRepository;
import com.tcdt.qlnvhang.repository.xuathang.suachuahang.ScQuyetDinhScRepository;
import com.tcdt.qlnvhang.request.suachua.ScQuyetDinhScReq;
import com.tcdt.qlnvhang.request.suachua.ScTongHopReq;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.service.suachuahang.ScQuyetDinhScService;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScQuyetDinhSc;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScTongHopHdr;
import com.tcdt.qlnvhang.util.Contains;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.Transient;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ScQuyetDinhScImpl extends BaseServiceImpl implements ScQuyetDinhScService {
    @Autowired
    private ScQuyetDinhScRepository scQuyetDinhScRepository;

    @Autowired
    private FileDinhKemService fileDinhKemService;

    @Autowired
    private ScTrinhThamDinhServiceImpl scTrinhThamDinhServiceImpl;

    @Override
    public Page<ScQuyetDinhSc> searchPage(ScQuyetDinhScReq req) throws Exception {
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        Page<ScQuyetDinhSc> search = scQuyetDinhScRepository.searchPage(req, pageable);
        return search;
    }

    @Override
    public ScQuyetDinhSc create(ScQuyetDinhScReq req) throws Exception {
        ScQuyetDinhSc hdr = new ScQuyetDinhSc();
        BeanUtils.copyProperties(req, hdr);
        hdr.setTrangThai(NhapXuatHangTrangThaiEnum.DUTHAO.getId());
        ScQuyetDinhSc created = scQuyetDinhScRepository.save(hdr);
        List<FileDinhKem> canCu = fileDinhKemService.saveListFileDinhKem(req.getFileCanCuReq(), created.getId(), ScQuyetDinhSc.TABLE_NAME + "_CAN_CU");
        created.setFileCanCu(canCu);
        List<FileDinhKem> fileDinhKem = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKemReq(), created.getId(), ScQuyetDinhSc.TABLE_NAME + "_DINH_KEM");
        created.setFileDinhKem(fileDinhKem);
        return created;
    }

    @Override
    public ScQuyetDinhSc update(ScQuyetDinhScReq req) throws Exception {
        Optional<ScQuyetDinhSc> optional = scQuyetDinhScRepository.findById(req.getId());
        if (!optional.isPresent()){
            throw new Exception("Bản ghi không tồn tại");
        }
        ScQuyetDinhSc hdr = optional.get();
        BeanUtils.copyProperties(req, hdr);
        ScQuyetDinhSc created = scQuyetDinhScRepository.save(hdr);
        fileDinhKemService.delete(req.getId(), Lists.newArrayList(ScQuyetDinhSc.TABLE_NAME + "_CAN_CU"));
        List<FileDinhKem> canCu = fileDinhKemService.saveListFileDinhKem(req.getFileCanCuReq(), created.getId(), ScQuyetDinhSc.TABLE_NAME + "_CAN_CU");
        created.setFileCanCu(canCu);
        fileDinhKemService.delete(req.getId(), Lists.newArrayList(ScQuyetDinhSc.TABLE_NAME + "_TAI_LIEU_DINH_KEM"));
        List<FileDinhKem> fileDinhKemList = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKemReq(), created.getId(), ScQuyetDinhSc.TABLE_NAME + "_DINH_KEM");
        created.setFileDinhKem(fileDinhKemList);

        return created;
    }

    public ScQuyetDinhSc detail(Long id) throws Exception {
        Optional<ScQuyetDinhSc> optional = scQuyetDinhScRepository.findById(id);
        if (!optional.isPresent()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        ScQuyetDinhSc data = optional.get();
        List<FileDinhKem> canCu = fileDinhKemService.search(data.getId(), Collections.singleton(ScQuyetDinhSc.TABLE_NAME + "_CAN_CU"));
        data.setFileCanCu(canCu);

        List<FileDinhKem> fileDinhKemList = fileDinhKemService.search(data.getId(), Collections.singleton(ScQuyetDinhSc.TABLE_NAME + "_DINH_KEM"));
        data.setFileDinhKem(fileDinhKemList);

        data.setScTrinhThamDinhHdr(scTrinhThamDinhServiceImpl.detail(data.getIdTtr()));
        return data;
    }

    @Override
    public ScQuyetDinhSc approve(ScQuyetDinhScReq req) throws Exception {
        Optional<ScQuyetDinhSc> optional = scQuyetDinhScRepository.findById(req.getId());
        if(!optional.isPresent()){
            throw new Exception("Thông tin tổng hợp không tồn tại");
        }
        String status = req.getTrangThai() + optional.get().getTrangThai();
        if ((TrangThaiAllEnum.BAN_HANH.getId() + TrangThaiAllEnum.DU_THAO.getId()).equals(status)) {
            optional.get().setTrangThai(req.getTrangThai());
            optional.get().setNgayKy(LocalDate.now());
        } else {
            throw new Exception("Gửi duyệt không thành công");
        }
        scQuyetDinhScRepository.save(optional.get());

        return optional.get();
    }

    @Transient
    public void delete(Long id) throws Exception {
        Optional<ScQuyetDinhSc> optional = scQuyetDinhScRepository.findById(id);
        if (!optional.isPresent()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        fileDinhKemService.delete(id, Lists.newArrayList(ScQuyetDinhSc.TABLE_NAME + "_CAN_CU"));
        fileDinhKemService.delete(id, Lists.newArrayList(ScQuyetDinhSc.TABLE_NAME + "_DINH_KEM"));
        scQuyetDinhScRepository.delete(optional.get());
    }

    @Override
    public void deleteMulti(List<Long> listMulti) throws Exception {

    }

    @Override
    public void export(ScQuyetDinhScReq req, HttpServletResponse response) throws Exception {

    }

    @Override
    public List<ScQuyetDinhSc> dsQuyetDinhXuatHang(ScQuyetDinhScReq req) throws Exception {
        UserInfo currentUser = SecurityContextService.getUser();
        if (currentUser == null){
            throw new Exception("Access denied.");
        }
        String dvql = currentUser.getDvql();
//        if (currentUser.getCapDvi().equals(Contains.CAP_CUC)) {
//            req.setMaDviSr(dvql);
//        }
        req.setTrangThai(TrangThaiAllEnum.BAN_HANH.getId());
        List<ScQuyetDinhSc> list = scQuyetDinhScRepository.listQuyetDinhXuatHang(req);
        return list;
    }
}
