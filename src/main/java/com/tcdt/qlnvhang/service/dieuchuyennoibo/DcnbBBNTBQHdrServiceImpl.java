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
    private DcnbBBNTBQHdrRepository dcnbBBNTBQHdrRepository;

    @Autowired
    private DcnbBBNTBQDtlRepository dcnbBBNTBQDtlRepository;

    @Autowired
    private FileDinhKemService fileDinhKemService;


    @Override
    public Page<DcnbBBNTBQHdr> searchPage(DcnbBBNTBQHdrReq req) throws Exception {
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        Page<DcnbBBNTBQHdr> search = dcnbBBNTBQHdrRepository.search(req, pageable);
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
        Optional<DcnbBBNTBQHdr> optional = dcnbBBNTBQHdrRepository.findBySoBban(req.getSoBban());
        if (optional.isPresent()) {
            throw new Exception("Số biên bản đã tồn tại");
        }

        DcnbBBNTBQHdr data = new DcnbBBNTBQHdr();
        BeanUtils.copyProperties(req, data);
        data.setMaDvi(userInfo.getDvql());
        data.setId(Long.parseLong(req.getSoBban().split("/")[0]));
        req.getDcnbBBNTBQDtlDtl().forEach(e -> {
            e.setDcnbBBNTBQHdr(data);
        });
        DcnbBBNTBQHdr created = dcnbBBNTBQHdrRepository.save(data);
        List<FileDinhKem> canCu = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKemReq(), created.getId(), DcnbBBNTBQHdr.TABLE_NAME);
        created.setFileDinhKems(canCu);
        return null;
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
        Optional<DcnbBBNTBQHdr> optional = dcnbBBNTBQHdrRepository.findById(req.getId());
        if (!optional.isPresent()) {
            throw new Exception("Số biên bản không tồn tại");
        }
        DcnbBBNTBQHdr data = optional.get();
        BeanUtils.copyProperties(req,data);
        data.setDcnbBBNTBQDtl(req.getDcnbBBNTBQDtlDtl());
        DcnbBBNTBQHdr created = dcnbBBNTBQHdrRepository.save(data);
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
        Optional<DcnbBBNTBQHdr> optional = dcnbBBNTBQHdrRepository.findById(id);
        if (!optional.isPresent()) {
            throw new Exception("Số biên bản không tồn tại");
        }
        DcnbBBNTBQHdr data = optional.get();
        data.setFileDinhKems(fileDinhKemService.search(id, Collections.singleton(DcnbBBNTBQHdr.TABLE_NAME)));
        return data;
    }

    @Override
    public DcnbBBNTBQHdr approve(DcnbBBNTBQHdrReq req) throws Exception {
        return null;
    }

    @Override
    public void delete(Long id) throws Exception {

    }

    @Override
    public void deleteMulti(List<Long> listMulti) throws Exception {

    }

    @Override
    public void export(DcnbBBNTBQHdrReq req, HttpServletResponse response) throws Exception {

    }
}
