package com.tcdt.qlnvhang.service.xuathang.daugia.nhiemvuxuat;

import com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhiemvunhap.NhQdGiaoNvuNhapxuatHdr;
import com.tcdt.qlnvhang.entities.xuathang.daugia.nhiemvuxuat.XhQdGiaoNvXh;
import com.tcdt.qlnvhang.request.DeleteReq;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.object.HhQdGiaoNvuNhapxuatHdrReq;
import com.tcdt.qlnvhang.request.search.HhQdNhapxuatSearchReq;
import com.tcdt.qlnvhang.request.search.xuathang.XhQdGiaoNvuXuatSearchReq;
import com.tcdt.qlnvhang.request.xuathang.quyetdinhgiaonhiemvuxuat.XhQdGiaoNvuXuatReq;
import com.tcdt.qlnvhang.service.BaseService;
import org.springframework.data.domain.Page;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.util.List;

public interface XhQdGiaoNvXhService extends BaseService<XhQdGiaoNvXh,XhQdGiaoNvuXuatReq,Long> {
//    @Transactional(rollbackOn = Exception.class)
//    XhQdGiaoNvXh create(XhQdGiaoNvuXuatReq req) throws Exception;
//
//    @Transactional(rollbackOn = Exception.class)
//    XhQdGiaoNvXh update(XhQdGiaoNvuXuatReq objReq) throws Exception;
//
//    XhQdGiaoNvXh detail(String ids) throws Exception;
//
//    @Transactional(rollbackOn = Exception.class)
//    void delete(IdSearchReq idSearchReq) throws Exception;
//
//    @Transactional(rollbackOn = Exception.class)
//    void deleteMulti(IdSearchReq idSearchReq) throws Exception;
//
//    @Transactional(rollbackOn = Exception.class)
//    boolean updateStatus(StatusReq req) throws Exception;
//
//    Page<XhQdGiaoNvXh> searchPage(XhQdGiaoNvuXuatSearchReq req) throws Exception;
//
//    void exportToExcel(XhQdGiaoNvuXuatSearchReq searchReq, HttpServletResponse response) throws Exception;
}
