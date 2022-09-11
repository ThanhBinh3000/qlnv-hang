package com.tcdt.qlnvhang.service.xuathang.bbhaodoi;

import com.tcdt.qlnvhang.request.DeleteReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.search.xuathang.XhBienBanHaoDoiSearchReq;
import com.tcdt.qlnvhang.request.xuathang.bbhaodoi.XhBienBanHaoDoiReq;
import com.tcdt.qlnvhang.response.xuathang.bbhaodoi.XhBienBanHaoDoiRes;
import org.springframework.data.domain.Page;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

public interface XhBienBanHaoDoiService {
    @Transactional(rollbackOn = Exception.class)
    XhBienBanHaoDoiRes create(XhBienBanHaoDoiReq xhBienBanHaoDoiReq) throws Exception;

    @Transactional(rollbackOn = Exception.class)
    XhBienBanHaoDoiRes update(XhBienBanHaoDoiReq xhBienBanHaoDoiReq) throws Exception;

    XhBienBanHaoDoiRes detail(Long id) throws Exception;

    @Transactional(rollbackOn = Exception.class)
    boolean delete(Long id) throws Exception;

    @Transactional(rollbackOn = Exception.class)
    boolean deleteMultiple(DeleteReq req) throws Exception;

    @Transactional(rollbackOn = Exception.class)
    boolean updateStatus(StatusReq statusReq) throws Exception;

    Page<XhBienBanHaoDoiRes> search(XhBienBanHaoDoiSearchReq req) throws Exception;

    boolean exportToExcel(XhBienBanHaoDoiSearchReq objReq, HttpServletResponse response) throws Exception;
}
