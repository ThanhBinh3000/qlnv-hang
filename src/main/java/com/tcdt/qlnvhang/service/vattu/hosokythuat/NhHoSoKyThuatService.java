package com.tcdt.qlnvhang.service.vattu.hosokythuat;

import com.tcdt.qlnvhang.request.DeleteReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.object.vattu.hosokythuat.NhHoSoKyThuatReq;
import com.tcdt.qlnvhang.request.search.vattu.hosokythuat.NhHoSoKyThuatSearchReq;
import com.tcdt.qlnvhang.response.vattu.hosokythuat.NhHoSoKyThuatRes;
import org.springframework.data.domain.Page;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

public interface NhHoSoKyThuatService {
    NhHoSoKyThuatRes create(NhHoSoKyThuatReq req) throws Exception;

    NhHoSoKyThuatRes update(NhHoSoKyThuatReq req) throws Exception;

    NhHoSoKyThuatRes detail(Long id) throws Exception;

    boolean delete(Long id) throws Exception;


    @Transactional(rollbackOn = Exception.class)
    boolean updateStatusQd(StatusReq req) throws Exception;

    Page<NhHoSoKyThuatRes> search(NhHoSoKyThuatSearchReq req) throws Exception;

    @Transactional(rollbackOn = Exception.class)
    boolean deleteMultiple(DeleteReq req) throws Exception;

    boolean exportToExcel(NhHoSoKyThuatSearchReq objReq, HttpServletResponse response) throws Exception;
}
