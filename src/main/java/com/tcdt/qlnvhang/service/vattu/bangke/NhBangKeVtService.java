package com.tcdt.qlnvhang.service.vattu.bangke;

import com.tcdt.qlnvhang.request.DeleteReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.object.vattu.bangke.NhBangKeVtReq;
import com.tcdt.qlnvhang.request.search.vattu.bangke.NhBangKeVtSearchReq;
import com.tcdt.qlnvhang.response.vattu.bangke.NhBangKeVtRes;
import org.springframework.data.domain.Page;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

public interface NhBangKeVtService {
    NhBangKeVtRes create(NhBangKeVtReq req) throws Exception;

    NhBangKeVtRes update(NhBangKeVtReq req) throws Exception;

    NhBangKeVtRes detail(Long id) throws Exception;

    boolean delete(Long id) throws Exception;


    @Transactional(rollbackOn = Exception.class)
    boolean updateStatusQd(StatusReq req) throws Exception;

    Page<NhBangKeVtRes> search(NhBangKeVtSearchReq req) throws Exception;

    @Transactional(rollbackOn = Exception.class)
    boolean deleteMultiple(DeleteReq req) throws Exception;

    boolean exportToExcel(NhBangKeVtSearchReq objReq, HttpServletResponse response) throws Exception;
}
