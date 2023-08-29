package com.tcdt.qlnvhang.service.nhaphang.dauthau.nhapkho.bienbaogiaonhan;

import com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhapkho.bienbangiaonhan.NhBbGiaoNhanVt;
import com.tcdt.qlnvhang.request.object.vattu.bienbangiaonhan.NhBbGiaoNhanVtReq;
import com.tcdt.qlnvhang.service.BaseService;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;

public interface NhBbGiaoNhanVtService extends BaseService<NhBbGiaoNhanVt,NhBbGiaoNhanVtReq,Long> {

//    NhBbGiaoNhanVtRes create(NhBbGiaoNhanVtReq req) throws Exception;
//
//    NhBbGiaoNhanVtRes update(NhBbGiaoNhanVtReq req) throws Exception;
//
//    NhBbGiaoNhanVtRes detail(Long id) throws Exception;
//
//    boolean delete(Long id) throws Exception;
//
//
//    @Transactional(rollbackOn = Exception.class)
//    boolean updateStatusQd(StatusReq req) throws Exception;
//
//    Page<NhBbGiaoNhanVtRes> search(NhBbGiaoNhanVtSearchReq req) throws Exception;
//
//    @Transactional(rollbackOn = Exception.class)
//    boolean deleteMultiple(DeleteReq req) throws Exception;
//
//    boolean exportToExcel(NhBbGiaoNhanVtSearchReq objReq, HttpServletResponse response) throws Exception;
//
//    BaseNhapHangCount count(Set<String> maDvis) throws Exception;
ReportTemplateResponse preview(NhBbGiaoNhanVtReq req) throws Exception;
}
