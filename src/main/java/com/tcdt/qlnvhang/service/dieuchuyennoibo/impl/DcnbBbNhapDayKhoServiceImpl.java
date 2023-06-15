package com.tcdt.qlnvhang.service.dieuchuyennoibo.impl;

import com.google.common.collect.Lists;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.dieuchuyennoibo.DcnbBbNhapDayKhoDtlRepository;
import com.tcdt.qlnvhang.repository.dieuchuyennoibo.DcnbBbNhapDayKhoHdrRepository;
import com.tcdt.qlnvhang.repository.dieuchuyennoibo.DcnbDataLinkHdrRepository;
import com.tcdt.qlnvhang.request.dieuchuyennoibo.DcnbBbNhapDayKhoHdrReq;
import com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbBbNhapDayKhoHdrDTO;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.dieuchuyennoibo.DcnbBbNhapDayKhoService;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.*;
import com.tcdt.qlnvhang.util.Contains;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class DcnbBbNhapDayKhoServiceImpl implements DcnbBbNhapDayKhoService {

    @Autowired
    private DcnbBbNhapDayKhoHdrRepository hdrRepository;

    @Autowired
    private DcnbBbNhapDayKhoDtlRepository dtlRepository;

    @Autowired
    private FileDinhKemService fileDinhKemService;

    @Autowired
    private DcnbDataLinkHdrRepository dcnbDataLinkHdrRepository;

    @Override
    public Page<DcnbBbNhapDayKhoHdr> searchPage(DcnbBbNhapDayKhoHdrReq req) throws Exception {
        return null;
    }

    @Override
    public DcnbBbNhapDayKhoHdr create(DcnbBbNhapDayKhoHdrReq req) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null){
            throw new Exception("Access denied.");
        }
        if(!userInfo.getCapDvi().equals(Contains.CAP_CHI_CUC)){
            throw new Exception("Văn bản này chỉ có thêm ở cấp chi cục");
        }
        Optional<DcnbBbNhapDayKhoHdr> optional = hdrRepository.findBySoBb(req.getSoBb());
        if (optional.isPresent()) {
            throw new Exception("Số biên bản đã tồn tại");
        }

        DcnbBbNhapDayKhoHdr data = new DcnbBbNhapDayKhoHdr();
        BeanUtils.copyProperties(req, data);
        data.setMaDvi(userInfo.getDvql());
        data.setId(Long.parseLong(req.getSoBb().split("/")[0]));
        req.getChildren().forEach(e -> {
            e.setParent(data);
        });
        DcnbBbNhapDayKhoHdr created = hdrRepository.save(data);
        List<FileDinhKem> canCu = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKemReq(), created.getId(), DcnbBBNTBQHdr.TABLE_NAME);
        created.setFileDinhKems(canCu);
//        DcnbKeHoachDcDtlTT kh = new DcnbKeHoachDcDtlTT();
//        kh.setIdHdr(created.getId());
//        kh.setTableName(DcnbBbNhapDayKhoHdr.TABLE_NAME);
//        kh.setIdKhDcDtl(data.getIdKeHoachDtl());
//        dcnbKeHoachNhapXuatService.saveOrUpdate(kh);
        return created;
    }

    @Override
    public DcnbBbNhapDayKhoHdr update(DcnbBbNhapDayKhoHdrReq req) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null){
            throw new Exception("Access denied.");
        }
        if(!userInfo.getCapDvi().equals(Contains.CAP_CHI_CUC)){
            throw new Exception("Văn bản này chỉ có thêm ở cấp chi cục");
        }
        Optional<DcnbBbNhapDayKhoHdr> optional = hdrRepository.findById(req.getId());
        if (!optional.isPresent()) {
            throw new Exception("Số biên bản không tồn tại");
        }
        DcnbBbNhapDayKhoHdr data = optional.get();
        BeanUtils.copyProperties(req,data);
        data.setChildren(req.getChildren());
        DcnbBbNhapDayKhoHdr update = hdrRepository.save(data);
        fileDinhKemService.delete(update.getId(), Lists.newArrayList(DcnbBBNTBQHdr.TABLE_NAME));
        List<FileDinhKem> canCu = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKemReq(), update.getId(), DcnbBBNTBQHdr.TABLE_NAME);
        update.setFileDinhKems(canCu);
        return update;
    }

    @Override
    public DcnbBbNhapDayKhoHdr detail(Long id) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null){
            throw new Exception("Access denied.");
        }
        if(Objects.isNull(id)){
            throw new Exception("Id is null");
        }
        Optional<DcnbBbNhapDayKhoHdr> optional = hdrRepository.findById(id);
        if (!optional.isPresent()) {
            throw new Exception("Số biên bản không tồn tại");
        }
        DcnbBbNhapDayKhoHdr data = optional.get();
        data.setFileDinhKems(fileDinhKemService.search(id, Collections.singleton(DcnbBBNTBQHdr.TABLE_NAME)));
        return data;
    }

    @Override
    public DcnbBbNhapDayKhoHdr approve(DcnbBbNhapDayKhoHdrReq req) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null){
            throw new Exception("Access denied.");
        }
        DcnbBbNhapDayKhoHdr hdr = detail(req.getId());
        String status = hdr.getTrangThai() + req.getTrangThai();
        switch (status) {
            // Arena các roll back approve
            case Contains.CHODUYET_KTVBQ + Contains.DUTHAO:
            case Contains.TUCHOI_KT + Contains.DUTHAO:
            case Contains.TUCHOI_LDCC + Contains.DUTHAO:
                break;
            // Arena các cấp duuyệt
            case Contains.DUTHAO + Contains.CHODUYET_KTVBQ:
                break;
            case Contains.CHODUYET_KTVBQ + Contains.CHODUYET_KT:
                hdr.setIdKyThuatVien(userInfo.getId());
                break;
            case Contains.CHODUYET_KT + Contains.CHODUYET_LDCC:
                hdr.setIdKeToan(userInfo.getId());
                break;
            case Contains.CHODUYET_LDCC + Contains.DADUYET_LDCC:
                hdr.setIdLanhDao(userInfo.getId());
                break;
            // Arena từ chối
            case Contains.CHODUYET_KTVBQ + Contains.CHODUYET_KTVBQ:
            case Contains.CHODUYET_KT + Contains.TUCHOI_KT:
            case Contains.CHODUYET_LDCC + Contains.TUCHOI_LDCC:
                hdr.setLyDoTuChoi(req.getLyDoTuChoi());
                break;
            default:
                throw new Exception("Phê duyệt không thành công");
        }
        hdr.setTrangThai(req.getTrangThai());
        DcnbBbNhapDayKhoHdr created = hdrRepository.save(hdr);
        return created;
    }

    @Override
    public void delete(Long id) throws Exception {
        DcnbBbNhapDayKhoHdr detail = detail(id);
        hdrRepository.delete(detail);
        dtlRepository.deleteAllByHdrId(id);
    }

    @Override
    public void deleteMulti(List<Long> listMulti) throws Exception {
        if(listMulti != null && !listMulti.isEmpty()){
            listMulti.forEach( i -> {
                try {
                    delete(i);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        }else{
            throw new Exception("List id is null");
        }
    }

    @Override
    public void export(DcnbBbNhapDayKhoHdrReq req, HttpServletResponse response) throws Exception {

    }

    @Override
    public List<DcnbBbNhapDayKhoHdrDTO> searchList(CustomUserDetails currentUser, DcnbBbNhapDayKhoHdrReq param) {
        param.setMaDvi(currentUser.getDvql());
        return hdrRepository.searchList(param);
    }
}