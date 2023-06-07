package com.tcdt.qlnvhang.service.dieuchuyennoibo;

import com.tcdt.qlnvhang.request.dieuchuyennoibo.DcnbPhieuNhapKhoHdrReq;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbPhieuNhapKhoHdr;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Service
public class DcnbPhieuNhapKhoServiceImpl implements DcnbPhieuNhapKhoService {

    @Override
    public Page<DcnbPhieuNhapKhoHdr> searchPage(DcnbPhieuNhapKhoHdrReq req) throws Exception {
        return null;
    }

    @Override
    public DcnbPhieuNhapKhoHdr create(DcnbPhieuNhapKhoHdrReq req) throws Exception {
        return null;
    }

    @Override
    public DcnbPhieuNhapKhoHdr update(DcnbPhieuNhapKhoHdrReq req) throws Exception {
        return null;
    }

    @Override
    public DcnbPhieuNhapKhoHdr detail(Long id) throws Exception {
        return null;
    }

    @Override
    public DcnbPhieuNhapKhoHdr approve(DcnbPhieuNhapKhoHdrReq req) throws Exception {
        return null;
    }

    @Override
    public void delete(Long id) throws Exception {

    }

    @Override
    public void deleteMulti(List<Long> listMulti) throws Exception {

    }

    @Override
    public void export(DcnbPhieuNhapKhoHdrReq req, HttpServletResponse response) throws Exception {

    }
}