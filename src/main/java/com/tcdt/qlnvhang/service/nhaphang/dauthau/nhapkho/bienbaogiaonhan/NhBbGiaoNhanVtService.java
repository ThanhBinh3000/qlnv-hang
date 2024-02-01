package com.tcdt.qlnvhang.service.nhaphang.dauthau.nhapkho.bienbaogiaonhan;

import com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhapkho.bienbangiaonhan.NhBbGiaoNhanVt;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhiemvunhap.NhQdGiaoNvuNhapxuatHdr;
import com.tcdt.qlnvhang.request.object.vattu.bienbangiaonhan.NhBbGiaoNhanVtReq;
import com.tcdt.qlnvhang.request.search.HhQdNhapxuatSearchReq;
import com.tcdt.qlnvhang.request.search.vattu.bienbangiaonhan.NhBbGiaoNhanVtSearchReq;
import com.tcdt.qlnvhang.service.BaseService;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;
import org.springframework.data.domain.Page;

import javax.servlet.http.HttpServletResponse;

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
    Page<NhQdGiaoNvuNhapxuatHdr> search(NhBbGiaoNhanVtSearchReq req) throws Exception;
//
//    @Transactional(rollbackOn = Exception.class)
//    boolean deleteMultiple(DeleteReq req) throws Exception;
//
//    boolean exportToExcel(NhBbGiaoNhanVtSearchReq objReq, HttpServletResponse response) throws Exception;
//
//    BaseNhapHangCount count(Set<String> maDvis) throws Exception;
void exportBbgn(NhBbGiaoNhanVtSearchReq searchReq, HttpServletResponse response) throws Exception;
ReportTemplateResponse preview(NhBbGiaoNhanVtReq req) throws Exception;
}
