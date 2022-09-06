package com.tcdt.qlnvhang.service.xuathang.xuatkho;

import com.tcdt.qlnvhang.request.DeleteReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.search.xuathang.XhBienBanTinhKhoSearchReq;
import com.tcdt.qlnvhang.request.search.xuathang.XhPhieuXuatKhoSearchReq;
import com.tcdt.qlnvhang.request.xuathang.bbtinhkho.XhBienBanTinhKhoReq;
import com.tcdt.qlnvhang.request.xuathang.xuatkho.XhPhieuXuatKhoReq;
import com.tcdt.qlnvhang.response.xuathang.bbtinhkho.XhBienBanTinhKhoRes;
import com.tcdt.qlnvhang.response.xuathang.phieuxuatkho.XhPhieuXuatKhoRes;
import org.springframework.data.domain.Page;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

public interface XhPhieuXuatKhoService {
    @Transactional(rollbackOn = Exception.class)
    XhPhieuXuatKhoRes create(XhPhieuXuatKhoReq xhPhieuXuatKhoReq) throws Exception;

    @Transactional(rollbackOn = Exception.class)
    XhPhieuXuatKhoRes update(XhPhieuXuatKhoReq xhPhieuXuatKhoReq) throws Exception;

    XhPhieuXuatKhoRes detail(Long id) throws Exception;

    @Transactional(rollbackOn = Exception.class)
    boolean delete(Long id) throws Exception;

    @Transactional(rollbackOn = Exception.class)
    boolean deleteMultiple(DeleteReq req) throws Exception;

    @Transactional(rollbackOn = Exception.class)
    boolean updateStatus(StatusReq statusReq) throws Exception;

    Page<XhPhieuXuatKhoRes> search(XhPhieuXuatKhoSearchReq req) throws Exception;

    boolean exportToExcel(XhPhieuXuatKhoSearchReq objReq, HttpServletResponse response) throws Exception;
}
