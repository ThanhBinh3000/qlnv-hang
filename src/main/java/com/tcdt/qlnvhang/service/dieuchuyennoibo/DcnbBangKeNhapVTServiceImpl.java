package com.tcdt.qlnvhang.service.dieuchuyennoibo;

import com.tcdt.qlnvhang.request.dieuchuyennoibo.DcnbBangKeNhapVTReq;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBangKeNhapVTHdr;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Service
public class DcnbBangKeNhapVTServiceImpl implements DcnbBangKeNhapVTService{
    @Override
    public Page<DcnbBangKeNhapVTHdr> searchPage(DcnbBangKeNhapVTReq req) throws Exception {
        return null;
    }

    @Override
    public DcnbBangKeNhapVTHdr create(DcnbBangKeNhapVTReq req) throws Exception {
        return null;
    }

    @Override
    public DcnbBangKeNhapVTHdr update(DcnbBangKeNhapVTReq req) throws Exception {
        return null;
    }

    @Override
    public DcnbBangKeNhapVTHdr detail(Long id) throws Exception {
        return null;
    }

    @Override
    public DcnbBangKeNhapVTHdr approve(DcnbBangKeNhapVTReq req) throws Exception {
        return null;
    }

    @Override
    public void delete(Long id) throws Exception {

    }

    @Override
    public void deleteMulti(List<Long> listMulti) throws Exception {

    }

    @Override
    public void export(DcnbBangKeNhapVTReq req, HttpServletResponse response) throws Exception {

    }
}
