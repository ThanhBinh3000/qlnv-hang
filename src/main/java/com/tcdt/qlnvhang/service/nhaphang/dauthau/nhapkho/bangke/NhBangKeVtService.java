package com.tcdt.qlnvhang.service.nhaphang.dauthau.nhapkho.bangke;

import com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhapkho.bangke.NhBangKeVt;
import com.tcdt.qlnvhang.request.object.vattu.bangke.NhBangKeVtReq;
import com.tcdt.qlnvhang.request.object.vattu.phieunhapkhotamgui.NhPhieuNhapKhoTamGuiReq;
import com.tcdt.qlnvhang.service.BaseService;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;

public interface NhBangKeVtService extends BaseService<NhBangKeVt,NhBangKeVtReq,Long> {
//    NhBangKeVtRes create(NhBangKeVtReq req) throws Exception;
//
//    NhBangKeVtRes update(NhBangKeVtReq req) throws Exception;
//
//    NhBangKeVtRes detail(Long id) throws Exception;
//
//    boolean delete(Long id) throws Exception;
//
//
//    @Transactional(rollbackOn = Exception.class)
//    boolean updateStatusQd(StatusReq req) throws Exception;
//
//    Page<NhBangKeVtRes> search(NhBangKeVtSearchReq req) throws Exception;
//
//    @Transactional(rollbackOn = Exception.class)
//    boolean deleteMultiple(DeleteReq req) throws Exception;
//
//    boolean exportToExcel(NhBangKeVtSearchReq objReq, HttpServletResponse response) throws Exception;
//
//    BaseNhapHangCount count(Set<String> maDvis) throws Exception;
ReportTemplateResponse preview(NhBangKeVtReq req) throws Exception;
}
