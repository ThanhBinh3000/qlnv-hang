package com.tcdt.qlnvhang.service.nhaphang.nhapkhac.impl;

import com.tcdt.qlnvhang.entities.nhaphang.nhapkhac.HhNkPhieuNhapKhoHdr;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.nhaphang.nhapkhac.HhNkPhieuNhapKhoHdrRepository;
import com.tcdt.qlnvhang.request.nhaphang.nhapkhac.HhNkPhieuNhapKhoHdrReq;
import com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbPhieuNhapKhoHdrDTO;
import com.tcdt.qlnvhang.response.nhaphang.nhapkhac.HhNkPhieuNhapKhoHdrDTO;
import com.tcdt.qlnvhang.response.nhaphang.nhapkhac.HhNkPhieuNhapKhoHdrListDTO;
import com.tcdt.qlnvhang.service.nhaphang.nhapkhac.HhNkPhieuNhapKhoService;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Service
public class HhNkPhieuNhapKhoServiceImpl implements HhNkPhieuNhapKhoService {
    @Autowired
    private HhNkPhieuNhapKhoHdrRepository hdrRepository;

    public Page<HhNkPhieuNhapKhoHdrDTO> search(HhNkPhieuNhapKhoHdrReq req) throws Exception {
        CustomUserDetails currentUser = UserUtils.getUserLoginInfo();
        req.setMaDvi(currentUser.getDvql());
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        Page<HhNkPhieuNhapKhoHdrDTO> searchDto = null;
//        if (currentUser.getUser().getCapDvi().equals(Contains.CAP_CHI_CUC)) {
//            searchDto = hdrRepository.searchPageChiCuc(req, pageable);
//        } else {
//            searchDto = hdrRepository.searchPage(req, pageable);
//        }

        return searchDto;
    }

    @Override
    public Page<HhNkPhieuNhapKhoHdr> searchPage(HhNkPhieuNhapKhoHdrReq req) throws Exception {
        return null;
    }

    @Override
    public HhNkPhieuNhapKhoHdr create(HhNkPhieuNhapKhoHdrReq req) throws Exception {
        return null;
    }

    @Override
    public HhNkPhieuNhapKhoHdr update(HhNkPhieuNhapKhoHdrReq req) throws Exception {
        return null;
    }

    @Override
    public HhNkPhieuNhapKhoHdr detail(Long id) throws Exception {
        return null;
    }

    @Override
    public HhNkPhieuNhapKhoHdr approve(HhNkPhieuNhapKhoHdrReq req) throws Exception {
        return null;
    }

    @Override
    public void delete(Long id) throws Exception {

    }

    @Override
    public void deleteMulti(List<Long> listMulti) throws Exception {

    }

    @Override
    public void export(HhNkPhieuNhapKhoHdrReq req, HttpServletResponse response) throws Exception {

    }

    @Override
    public List<HhNkPhieuNhapKhoHdrListDTO> searchList(HhNkPhieuNhapKhoHdrReq objReq) throws Exception {
        return null;
    }

    @Override
    public List<HhNkPhieuNhapKhoHdrListDTO> searchListChung(HhNkPhieuNhapKhoHdrReq objReq) throws Exception {
        return null;
    }
}
