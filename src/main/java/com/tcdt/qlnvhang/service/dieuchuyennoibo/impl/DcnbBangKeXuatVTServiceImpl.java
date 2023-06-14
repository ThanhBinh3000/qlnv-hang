package com.tcdt.qlnvhang.service.dieuchuyennoibo.impl;

import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.dieuchuyennoibo.DcnbBangKeXuatVTHdrRepository;
import com.tcdt.qlnvhang.request.dieuchuyennoibo.DcnbBangKeXuatVTReq;
import com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbBangKeXuatVTHdrDTO;
import com.tcdt.qlnvhang.service.dieuchuyennoibo.DcnbBangKeXuatVTService;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBangKeXuatVTHdr;
import com.tcdt.qlnvhang.util.Contains;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Service
public class DcnbBangKeXuatVTServiceImpl implements DcnbBangKeXuatVTService {
    DcnbBangKeXuatVTHdrRepository repository;

    @Override
    public Page<DcnbBangKeXuatVTHdr> searchPage(DcnbBangKeXuatVTReq req) throws Exception {
        return null;
    }

    public Page<DcnbBangKeXuatVTHdrDTO> searchPage(CustomUserDetails currentUser, DcnbBangKeXuatVTReq req) throws Exception {
        String dvql = currentUser.getDvql();
        req.setMaDvi(dvql);
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        Page<DcnbBangKeXuatVTHdrDTO> searchDto = null;
        if (currentUser.getUser().getCapDvi().equals(Contains.CAP_CHI_CUC)) {
            searchDto = repository.searchPageChiCuc(req, pageable);
        }
        if (!currentUser.getUser().getCapDvi().equals(Contains.CAP_CHI_CUC)) {
            searchDto = repository.searchPageCuc(req, pageable);
        }
        return searchDto;
    }

    @Override
    public DcnbBangKeXuatVTHdr create(DcnbBangKeXuatVTReq req) throws Exception {
        return null;
    }

    @Override
    public DcnbBangKeXuatVTHdr update(DcnbBangKeXuatVTReq req) throws Exception {
        return null;
    }

    @Override
    public DcnbBangKeXuatVTHdr detail(Long id) throws Exception {
        return null;
    }

    @Override
    public DcnbBangKeXuatVTHdr approve(DcnbBangKeXuatVTReq req) throws Exception {
        return null;
    }

    @Override
    public void delete(Long id) throws Exception {

    }

    @Override
    public void deleteMulti(List<Long> listMulti) throws Exception {

    }

    @Override
    public void export(DcnbBangKeXuatVTReq req, HttpServletResponse response) throws Exception {

    }
}
