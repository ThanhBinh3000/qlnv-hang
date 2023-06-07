package com.tcdt.qlnvhang.service.dieuchuyennoibo;

import com.google.common.collect.Lists;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.dieuchuyennoibo.DcnbBBNTBQDtlRepository;
import com.tcdt.qlnvhang.repository.dieuchuyennoibo.DcnbBBNTBQHdrRepository;
import com.tcdt.qlnvhang.repository.dieuchuyennoibo.DcnbBangKeCanHangDtlRepository;
import com.tcdt.qlnvhang.repository.dieuchuyennoibo.DcnbBangKeCanHangHdrRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.dieuchuyennoibo.DcnbBBNTBQHdrReq;
import com.tcdt.qlnvhang.request.dieuchuyennoibo.DcnbBangKeCanHangHdrReq;
import com.tcdt.qlnvhang.request.dieuchuyennoibo.SearchBangKeCanHang;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.*;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.DataUtils;
import com.tcdt.qlnvhang.util.ExportExcel;
import org.hibernate.Hibernate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.Transient;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DcnbBBNTBQHdrServiceImpl implements DcnbBBNTBQHdrService {

    @Autowired
    private DcnbBBNTBQHdrRepository hdrRepository;

    @Autowired
    private DcnbBBNTBQDtlRepository dtlRepository;

    @Autowired
    private FileDinhKemService fileDinhKemService;


    @Override
    public Page<DcnbBBNTBQHdr> searchPage(DcnbBBNTBQHdrReq req) throws Exception {
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        Page<DcnbBBNTBQHdr> search = hdrRepository.search(req, pageable);
        return search;
    }

    @Override
    public DcnbBBNTBQHdr create(DcnbBBNTBQHdrReq req) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null){
            throw new Exception("Access denied.");
        }
        if(!userInfo.getCapDvi().equals(Contains.CAP_CHI_CUC)){
            throw new Exception("Văn bản này chỉ có thêm ở cấp chi cục");
        }
        Optional<DcnbBBNTBQHdr> optional = hdrRepository.findBySoBban(req.getSoBban());
        if (optional.isPresent()) {
            throw new Exception("Số biên bản đã tồn tại");
        }

        DcnbBBNTBQHdr data = new DcnbBBNTBQHdr();
        BeanUtils.copyProperties(req, data);
        data.setMaDvi(userInfo.getDvql());
        data.setId(Long.parseLong(req.getSoBban().split("/")[0]));
        req.getDcnbBBNTBQDtlList().forEach(e -> {
            e.setDcnbBBNTBQHdr(data);
        });
        DcnbBBNTBQHdr created = hdrRepository.save(data);
        List<FileDinhKem> canCu = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKemReq(), created.getId(), DcnbBBNTBQHdr.TABLE_NAME);
        created.setFileDinhKems(canCu);
        return created;
    }

    @Override
    public DcnbBBNTBQHdr update(DcnbBBNTBQHdrReq req) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null){
            throw new Exception("Access denied.");
        }
        if(!userInfo.getCapDvi().equals(Contains.CAP_CHI_CUC)){
            throw new Exception("Văn bản này chỉ có thêm ở cấp chi cục");
        }
        Optional<DcnbBBNTBQHdr> optional = hdrRepository.findById(req.getId());
        if (!optional.isPresent()) {
            throw new Exception("Số biên bản không tồn tại");
        }
        DcnbBBNTBQHdr data = optional.get();
        BeanUtils.copyProperties(req,data);
        data.setDcnbBBNTBQDtl(req.getDcnbBBNTBQDtlList());
        DcnbBBNTBQHdr created = hdrRepository.save(data);
        fileDinhKemService.delete(created.getId(), Lists.newArrayList(DcnbBBNTBQHdr.TABLE_NAME));
        List<FileDinhKem> canCu = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKemReq(), created.getId(), DcnbBBNTBQHdr.TABLE_NAME);
        created.setFileDinhKems(canCu);
        return created;
    }

    @Override
    public DcnbBBNTBQHdr detail(Long id) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null){
            throw new Exception("Access denied.");
        }
        if(Objects.isNull(id)){
            throw new Exception("Id is null");
        }
        Optional<DcnbBBNTBQHdr> optional = hdrRepository.findById(id);
        if (!optional.isPresent()) {
            throw new Exception("Số biên bản không tồn tại");
        }
        DcnbBBNTBQHdr data = optional.get();
        data.setFileDinhKems(fileDinhKemService.search(id, Collections.singleton(DcnbBBNTBQHdr.TABLE_NAME)));
        return data;
    }

    @Override
    public DcnbBBNTBQHdr approve(DcnbBBNTBQHdrReq req) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null){
            throw new Exception("Access denied.");
        }
        DcnbBBNTBQHdr hdr = detail(req.getId());
        String status = hdr.getTrangThai() + req.getTrangThai();
        switch (status) {
            case Contains.TUCHOI_TK + Contains.DUTHAO:
            case Contains.TUCHOI_KT + Contains.DUTHAO:
            case Contains.TUCHOI_LDCC + Contains.DUTHAO:
            case Contains.DUTHAO + Contains.CHODUYET_TK:
                break;
            case Contains.CHODUYET_TK + Contains.CHODUYET_KT:
                hdr.setThuKho(userInfo.getFullName());
                break;
            case Contains.CHODUYET_KT + Contains.CHODUYET_LDCC:
                hdr.setKeToan(userInfo.getFullName());
                break;
            case Contains.CHODUYET_LDCC + Contains.DADUYET_LDCC:
                hdr.setLdChiCuc(userInfo.getFullName());
                break;
            case Contains.CHODUYET_TK + Contains.TUCHOI_TK:
            case Contains.CHODUYET_KT + Contains.TUCHOI_KT:
            case Contains.CHODUYET_LDCC + Contains.TUCHOI_LDCC:
                hdr.setLyDoTuChoi(req.getLyDoTuChoi());
                break;
            default:
                throw new Exception("Phê duyệt không thành công");
        }
        hdr.setTrangThai(req.getTrangThai());
        DcnbBBNTBQHdr created = hdrRepository.save(hdr);
        return created;
    }

    @Override
    public void delete(Long id) throws Exception {
        DcnbBBNTBQHdr detail = detail(id);
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
    public void export(DcnbBBNTBQHdrReq req, HttpServletResponse response) throws Exception {

    }
}
