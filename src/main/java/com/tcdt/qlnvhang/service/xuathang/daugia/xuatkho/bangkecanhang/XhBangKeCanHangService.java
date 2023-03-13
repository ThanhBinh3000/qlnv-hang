package com.tcdt.qlnvhang.service.xuathang.daugia.xuatkho.bangkecanhang;

import com.tcdt.qlnvhang.entities.xuathang.bangkecanhang.XhBangKeCanHang;
import com.tcdt.qlnvhang.entities.xuathang.daugia.xuatkho.phieuxuatkho.XhPhieuXuatKho;
import com.tcdt.qlnvhang.request.DeleteReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.search.xuathang.XhBangKeCanHangSearchReq;
import com.tcdt.qlnvhang.request.xuathang.bangkecanhang.XhBangKeCanHangReq;
import com.tcdt.qlnvhang.request.xuathang.xuatkho.XhPhieuXuatKhoReq;
import com.tcdt.qlnvhang.response.xuathang.bangkecanhang.XhBangKeCanHangRes;
import com.tcdt.qlnvhang.service.BaseService;
import org.springframework.data.domain.Page;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

public interface XhBangKeCanHangService extends BaseService<XhBangKeCanHang, XhBangKeCanHangReq,Long> {
//    @Transactional(rollbackOn = Exception.class)
//    XhBangKeCanHangRes create(XhBangKeCanHangReq req) throws Exception;
//
//    @Transactional(rollbackOn = Exception.class)
//    XhBangKeCanHangRes update(XhBangKeCanHangReq req) throws Exception;
//
//    XhBangKeCanHangRes detail(Long id) throws Exception;
//
//    @Transactional(rollbackOn = Exception.class)
//    boolean delete(Long id) throws Exception;
//
//    @Transactional(rollbackOn = Exception.class)
//    boolean deleteMultiple(DeleteReq req) throws Exception;
//
//    @Transactional(rollbackOn = Exception.class)
//    boolean updateStatus(StatusReq statusReq) throws Exception;
//
//    Page<XhBangKeCanHangRes> search(XhBangKeCanHangSearchReq req) throws Exception;
//
//    boolean exportToExcel(XhBangKeCanHangSearchReq objReq, HttpServletResponse response) throws Exception;
}
