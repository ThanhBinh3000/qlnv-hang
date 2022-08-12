package com.tcdt.qlnvhang.service.xuathang.quyetdinhgiaonhiemvuxuat;

import com.tcdt.qlnvhang.request.DeleteReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.search.xuathang.XhQdGiaoNvuXuatSearchReq;
import com.tcdt.qlnvhang.request.xuathang.quyetdinhgiaonhiemvuxuat.XhQdGiaoNvuXuatReq;
import com.tcdt.qlnvhang.response.xuathang.quyetdinhgiaonhiemvuxuat.XhQdGiaoNvuXuatRes;
import org.springframework.data.domain.Page;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

public interface XhQdGiaoNvuXuatService {
    @Transactional(rollbackOn = Exception.class)
    XhQdGiaoNvuXuatRes create(XhQdGiaoNvuXuatReq req) throws Exception;

    @Transactional(rollbackOn = Exception.class)
    XhQdGiaoNvuXuatRes update(XhQdGiaoNvuXuatReq req) throws Exception;

    XhQdGiaoNvuXuatRes detail(Long id) throws Exception;

    @Transactional(rollbackOn = Exception.class)
    boolean delete(Long id) throws Exception;

    @Transactional(rollbackOn = Exception.class)
    boolean updateStatusQd(StatusReq stReq) throws Exception;

    Page<XhQdGiaoNvuXuatRes> search(XhQdGiaoNvuXuatSearchReq req) throws Exception;

    @Transactional(rollbackOn = Exception.class)
    boolean deleteMultiple(DeleteReq req) throws Exception;

    boolean exportToExcel(XhQdGiaoNvuXuatSearchReq objReq, HttpServletResponse response) throws Exception;
}
