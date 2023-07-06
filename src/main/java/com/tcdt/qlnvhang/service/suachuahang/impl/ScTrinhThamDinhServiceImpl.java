package com.tcdt.qlnvhang.service.suachuahang.impl;

import com.google.common.collect.Lists;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.repository.FileDinhKemRepository;
import com.tcdt.qlnvhang.repository.xuathang.suachuahang.ScDanhSachRepository;
import com.tcdt.qlnvhang.repository.xuathang.suachuahang.ScTrinhThamDinhRepository;
import com.tcdt.qlnvhang.request.suachua.ScTrinhThamDinhDtlReq;
import com.tcdt.qlnvhang.request.suachua.ScTrinhThamDinhHdrReq;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.service.suachuahang.ScTrinhThamDinhService;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScDanhSachHdr;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScTrinhThamDinhDtl;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScTrinhThamDinhHdr;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ScTrinhThamDinhServiceImpl extends BaseServiceImpl implements ScTrinhThamDinhService {

    @Autowired
    private ScTrinhThamDinhRepository hdrRepository;

    @Autowired
    private FileDinhKemService fileDinhKemService;

    @Autowired
    private ScDanhSachRepository scDanhSachRepository;

    @Override
    public Page<ScTrinhThamDinhHdr> searchPage(ScTrinhThamDinhHdrReq req) throws Exception {
        UserInfo currentUser = SecurityContextService.getUser();
        if (currentUser == null) {
            throw new Exception("Access denied.");
        }
        String dvql = currentUser.getDvql();
        if (!currentUser.getCapDvi().equals(Contains.CAP_TONG_CUC)) {
            req.setMaDvi(dvql.substring(0, 6));
        }
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        Page<ScTrinhThamDinhHdr> search = hdrRepository.searchPage(req, pageable);
        return search;
    }

    @Transactional
    @Override
    public ScTrinhThamDinhHdr create(ScTrinhThamDinhHdrReq req) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        ScTrinhThamDinhHdr hdr = new ScTrinhThamDinhHdr();
        BeanUtils.copyProperties(req, hdr);
        hdr.setNam(LocalDate.now().getYear());
        hdr.setMaDvi(userInfo.getDvql());
        hdr.setTrangThai(NhapXuatHangTrangThaiEnum.DUTHAO.getId());
        ScTrinhThamDinhHdr created = hdrRepository.save(hdr);
        List<FileDinhKem> canCu = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKemReq(), created.getId(), ScTrinhThamDinhHdr.TABLE_NAME + "_CAN_CU");
        created.setFileCanCu(canCu);
        List<FileDinhKem> fileDinhKem = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKemReq(), created.getId(), ScTrinhThamDinhHdr.TABLE_NAME + "_DINH_KEM");
        created.setFileDinhKem(fileDinhKem);
        List<ScTrinhThamDinhDtl> scTrinhThamDinhDtls = this.saveDtl(req, created.getId());
        created.setChildren(scTrinhThamDinhDtls);
        return created;
    }

    private List<ScTrinhThamDinhDtl> saveDtl(ScTrinhThamDinhHdrReq req,Long idHdr) throws Exception{
        List<ScTrinhThamDinhDtlReq> children = req.getChildren();
        List<ScTrinhThamDinhDtl> listDtl = new ArrayList<>();
        req.getChildren().forEach( item -> {
            ScTrinhThamDinhDtl dtl = new ScTrinhThamDinhDtl();
            dtl.setIdHdr(idHdr);
            dtl.setIdDsHdr(item.getId());
            // Update lại data vào danh sách gốc
            Optional<ScDanhSachHdr> dsHdr = scDanhSachRepository.findById(item.getId());
            if(dsHdr.isPresent()){
                dsHdr.get().setKetQua(item.getKetQua());
                dsHdr.get().setDonGiaDk(item.getDonGiaDk());
                ScDanhSachHdr save = scDanhSachRepository.save(dsHdr.get());
                dtl.setScDanhSachHdr(save);
            }else{
                throw new RuntimeException("Không tìm thấy danh sách hàng cần sửa chữa");
            }
            listDtl.add(dtl);
        });
        return listDtl;
    }

    @Transactional
    @Override
    public ScTrinhThamDinhHdr update(ScTrinhThamDinhHdrReq req) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        Optional<ScTrinhThamDinhHdr> optional = hdrRepository.findById(req.getId());
        if (!optional.isPresent()){
            throw new Exception("Bản ghi không tồn tại");
        }
        ScTrinhThamDinhHdr hdr = optional.get();
        BeanUtils.copyProperties(req, hdr);

        hdr.setNam(LocalDate.now().getYear());
        hdr.setMaDvi(userInfo.getDvql());
        hdr.setTrangThai(NhapXuatHangTrangThaiEnum.DUTHAO.getId());
        ScTrinhThamDinhHdr created = hdrRepository.save(hdr);
        fileDinhKemService.delete(req.getId(), Lists.newArrayList(ScTrinhThamDinhHdr.TABLE_NAME + "_CAN_CU"));
        List<FileDinhKem> canCu = fileDinhKemService.saveListFileDinhKem(req.getFileCanCuReq(), created.getId(), ScTrinhThamDinhHdr.TABLE_NAME + "_CAN_CU");
        created.setFileCanCu(canCu);
        fileDinhKemService.delete(req.getId(), Lists.newArrayList(ScTrinhThamDinhHdr.TABLE_NAME + "_TAI_LIEU_DINH_KEM"));
        List<FileDinhKem> fileDinhKemList = fileDinhKemService.saveListFileDinhKem(req.getFileCanCuReq(), created.getId(), ScTrinhThamDinhHdr.TABLE_NAME + "_TAI_LIEU_DINH_KEM");
        created.setFileDinhKem(fileDinhKemList);
        List<ScTrinhThamDinhDtl> scTrinhThamDinhDtls = this.saveDtl(req, created.getId());
        created.setChildren(scTrinhThamDinhDtls);
        return created;
    }

    @Override
    public ScTrinhThamDinhHdr detail(Long id) throws Exception {
        Optional<ScTrinhThamDinhHdr> optional = hdrRepository.findById(id);
        if (!optional.isPresent()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        ScTrinhThamDinhHdr data = optional.get();
        return data;
    }

    @Override
    public ScTrinhThamDinhHdr approve(ScTrinhThamDinhHdrReq req) throws Exception {
        Optional<ScTrinhThamDinhHdr> optional = hdrRepository.findById(req.getId());
        if (!optional.isPresent()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        ScTrinhThamDinhHdr hdr = optional.get();
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
        ScTrinhThamDinhHdr save = hdrRepository.save(hdr);
        return save;
    }
    @Transient
    @Override
    public void delete(Long id) throws Exception {
        Optional<ScTrinhThamDinhHdr> optional = hdrRepository.findById(id);
        if (!optional.isPresent()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        fileDinhKemService.delete(id, Lists.newArrayList(ScTrinhThamDinhHdr.TABLE_NAME + "_CAN_CU"));
        fileDinhKemService.delete(id, Lists.newArrayList(ScTrinhThamDinhHdr.TABLE_NAME + "_TAI_LIEU_DINH_KEM"));
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
    public void export(ScTrinhThamDinhHdrReq req, HttpServletResponse response) throws Exception {

    }
}
