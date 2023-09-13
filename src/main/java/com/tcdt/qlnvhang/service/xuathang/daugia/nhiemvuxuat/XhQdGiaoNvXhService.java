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
import com.tcdt.qlnvhang.table.ReportTemplateResponse;
import org.springframework.data.domain.Page;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;

public interface XhQdGiaoNvXhService extends BaseService<XhQdGiaoNvXh,XhQdGiaoNvuXuatReq,Long> {

  ReportTemplateResponse preview(HashMap<String, Object> body) throws Exception;
}
