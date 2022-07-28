package com.tcdt.qlnvhang.service.bandaugia.bienbanbandaugia;

import com.tcdt.qlnvhang.request.DeleteReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.bandaugia.bienbanbandaugia.BhBbBanDauGiaReq;
import com.tcdt.qlnvhang.request.bandaugia.bienbanbandaugia.BhBbBanDauGiaSearchReq;
import com.tcdt.qlnvhang.response.banhangdaugia.bienbanbandaugia.BhBbBanDauGiaRes;
import org.springframework.data.domain.Page;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

public interface BhBbBanDauGiaService {
    @Transactional(rollbackOn = Exception.class)
    BhBbBanDauGiaRes create(BhBbBanDauGiaReq req) throws Exception;

    @Transactional(rollbackOn = Exception.class)
    BhBbBanDauGiaRes update(BhBbBanDauGiaReq req) throws Exception;

    BhBbBanDauGiaRes detail(Long id) throws Exception;

    @Transactional(rollbackOn = Exception.class)
    boolean delete(Long id) throws Exception;

    @Transactional(rollbackOn = Exception.class)
    boolean updateStatusQd(StatusReq stReq) throws Exception;

   //Page<BhBbBanDauGiaRes> search(BhBbBanDauGiaSearchReq req) throws Exception;

    Page<BhBbBanDauGiaRes> search(BhBbBanDauGiaSearchReq req) throws Exception;

    @Transactional(rollbackOn = Exception.class)
    boolean deleteMultiple(DeleteReq req) throws Exception;

    boolean exportToExcel(BhBbBanDauGiaSearchReq objReq, HttpServletResponse response) throws Exception;

    //boolean exportToExcel(BhBbBanDauGiaSearchReq objReq, HttpServletResponse response) throws Exception;
}
