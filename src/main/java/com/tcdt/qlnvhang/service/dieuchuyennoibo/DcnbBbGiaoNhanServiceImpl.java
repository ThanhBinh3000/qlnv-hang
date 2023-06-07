package com.tcdt.qlnvhang.service.dieuchuyennoibo;

import com.tcdt.qlnvhang.request.dieuchuyennoibo.DcnbBbGiaoNhanHdrReq;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBbGiaoNhanHdr;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Service
public class DcnbBbGiaoNhanServiceImpl implements DcnbBbGiaoNhanService {

    @Override
    public Page<DcnbBbGiaoNhanHdr> searchPage(DcnbBbGiaoNhanHdrReq req) throws Exception {
        return null;
    }

    @Override
    public DcnbBbGiaoNhanHdr create(DcnbBbGiaoNhanHdrReq req) throws Exception {
        return null;
    }

    @Override
    public DcnbBbGiaoNhanHdr update(DcnbBbGiaoNhanHdrReq req) throws Exception {
        return null;
    }

    @Override
    public DcnbBbGiaoNhanHdr detail(Long id) throws Exception {
        return null;
    }

    @Override
    public DcnbBbGiaoNhanHdr approve(DcnbBbGiaoNhanHdrReq req) throws Exception {
        return null;
    }

    @Override
    public void delete(Long id) throws Exception {

    }

    @Override
    public void deleteMulti(List<Long> listMulti) throws Exception {

    }

    @Override
    public void export(DcnbBbGiaoNhanHdrReq req, HttpServletResponse response) throws Exception {

    }
}