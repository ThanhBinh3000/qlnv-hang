package com.tcdt.qlnvhang.request.dieuchuyennoibo;

import com.tcdt.qlnvhang.service.dieuchuyennoibo.DcnbBBKetThucNKService;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBBKetThucNKHdr;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Service
public class DcnbBBKetThucNKServiceImpl implements DcnbBBKetThucNKService {
    @Override
    public Page<DcnbBBKetThucNKHdr> searchPage(DcnbBBKetThucNKReq req) throws Exception {
        return null;
    }

    @Override
    public DcnbBBKetThucNKHdr create(DcnbBBKetThucNKReq req) throws Exception {
        return null;
    }

    @Override
    public DcnbBBKetThucNKHdr update(DcnbBBKetThucNKReq req) throws Exception {
        return null;
    }

    @Override
    public DcnbBBKetThucNKHdr detail(Long id) throws Exception {
        return null;
    }

    @Override
    public DcnbBBKetThucNKHdr approve(DcnbBBKetThucNKReq req) throws Exception {
        return null;
    }

    @Override
    public void delete(Long id) throws Exception {

    }

    @Override
    public void deleteMulti(List<Long> listMulti) throws Exception {

    }

    @Override
    public void export(DcnbBBKetThucNKReq req, HttpServletResponse response) throws Exception {

    }
}
