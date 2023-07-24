package com.tcdt.qlnvhang.service.dieuchuyennoibo.impl;

import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.dieuchuyennoibo.DcnbBbThuaThieuDtlRepository;
import com.tcdt.qlnvhang.repository.dieuchuyennoibo.DcnbBbThuaThieuHdrRepository;
import com.tcdt.qlnvhang.request.dieuchuyennoibo.DcnbBbKqDcSearch;
import com.tcdt.qlnvhang.request.dieuchuyennoibo.DcnbBbThuaThieuHdrReq;
import com.tcdt.qlnvhang.service.dieuchuyennoibo.DcnbBbThuaThieuService;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBbThuaThieuDtl;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBbThuaThieuHdr;
import com.tcdt.qlnvhang.util.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Service
public class DcnbBbThuaThieuServiceImpl implements DcnbBbThuaThieuService {
    @Autowired
    private DcnbBbThuaThieuHdrRepository hdrRepository;

    @Autowired
    private DcnbBbThuaThieuDtlRepository dtlRepository;

    @Override
    public Page<DcnbBbThuaThieuHdr> searchPage(DcnbBbThuaThieuHdrReq req) throws Exception {
        CustomUserDetails currentUser = UserUtils.getUserLoginInfo();
        String dvql = currentUser.getDvql();
        req.setMaDvi(dvql);
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
//        Page<DcnbBbThuaThieuHdr> searchDto = hdrRepository.searchPage(req, pageable);
//        return searchDto;
        return null;
    }

    @Override
    public DcnbBbThuaThieuHdr create(DcnbBbThuaThieuHdrReq req) throws Exception {
        return null;
    }

    @Override
    public DcnbBbThuaThieuHdr update(DcnbBbThuaThieuHdrReq req) throws Exception {
        return null;
    }

    @Override
    public DcnbBbThuaThieuHdr detail(Long id) throws Exception {
        return null;
    }

    @Override
    public DcnbBbThuaThieuHdr approve(DcnbBbThuaThieuHdrReq req) throws Exception {
        return null;
    }

    @Override
    public void delete(Long id) throws Exception {

    }

    @Override
    public void deleteMulti(List<Long> listMulti) throws Exception {

    }

    @Override
    public void export(DcnbBbThuaThieuHdrReq req, HttpServletResponse response) throws Exception {

    }

    @Override
    public List<DcnbBbThuaThieuDtl> thongTinNhapXuatHang(DcnbBbKqDcSearch objReq) throws Exception {
        return null;
    }
}
