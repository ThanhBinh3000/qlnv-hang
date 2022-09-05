package com.tcdt.qlnvhang.service.xuathang.bbtinhkho;

import com.tcdt.qlnvhang.request.DeleteReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.search.xuathang.XhBienBanTinhKhoSearchReq;
import com.tcdt.qlnvhang.request.xuathang.bbtinhkho.XhBienBanTinhKhoReq;
import com.tcdt.qlnvhang.response.xuathang.bbtinhkho.XhBienBanTinhKhoRes;
import org.springframework.data.domain.Page;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

public interface XhBienBanTinhKhoService {
    @Transactional(rollbackOn = Exception.class)
    XhBienBanTinhKhoRes create(XhBienBanTinhKhoReq xhBienBanTinhKhoReq) throws Exception;

    @Transactional(rollbackOn = Exception.class)
    XhBienBanTinhKhoRes update(XhBienBanTinhKhoReq xhBienBanTinhKhoReq) throws Exception;

    XhBienBanTinhKhoRes detail(Long id) throws Exception;

    @Transactional(rollbackOn = Exception.class)
    boolean delete(Long id) throws Exception;

    @Transactional(rollbackOn = Exception.class)
    boolean deleteMultiple(DeleteReq req) throws Exception;

    @Transactional(rollbackOn = Exception.class)
    boolean updateStatus(StatusReq statusReq) throws Exception;

    Page<XhBienBanTinhKhoRes> search(XhBienBanTinhKhoSearchReq req) throws Exception;

    boolean exportToExcel(XhBienBanTinhKhoSearchReq objReq, HttpServletResponse response) throws Exception;
}
