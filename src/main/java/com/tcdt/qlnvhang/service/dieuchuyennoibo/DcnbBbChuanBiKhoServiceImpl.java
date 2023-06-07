package com.tcdt.qlnvhang.service.dieuchuyennoibo;

import com.tcdt.qlnvhang.request.dieuchuyennoibo.DcnbBbChuanBiKhoHdrReq;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBbChuanBiKhoHdr;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Service
public class DcnbBbChuanBiKhoServiceImpl implements DcnbBbChuanBiKhoService {

    @Override
    public Page<DcnbBbChuanBiKhoHdr> searchPage(DcnbBbChuanBiKhoHdrReq req) throws Exception {
        return null;
    }

    @Override
    public DcnbBbChuanBiKhoHdr create(DcnbBbChuanBiKhoHdrReq req) throws Exception {
        return null;
    }

    @Override
    public DcnbBbChuanBiKhoHdr update(DcnbBbChuanBiKhoHdrReq req) throws Exception {
        return null;
    }

    @Override
    public DcnbBbChuanBiKhoHdr detail(Long id) throws Exception {
        return null;
    }

    @Override
    public DcnbBbChuanBiKhoHdr approve(DcnbBbChuanBiKhoHdrReq req) throws Exception {
        return null;
    }

    @Override
    public void delete(Long id) throws Exception {

    }

    @Override
    public void deleteMulti(List<Long> listMulti) throws Exception {

    }

    @Override
    public void export(DcnbBbChuanBiKhoHdrReq req, HttpServletResponse response) throws Exception {

    }
}