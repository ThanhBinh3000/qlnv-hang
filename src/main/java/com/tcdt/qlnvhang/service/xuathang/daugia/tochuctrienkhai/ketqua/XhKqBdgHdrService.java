package com.tcdt.qlnvhang.service.xuathang.daugia.tochuctrienkhai.ketqua;

import com.tcdt.qlnvhang.request.xuathang.daugia.tochuctrienkhai.ketqua.XhKqBdgHdrReq;
import com.tcdt.qlnvhang.service.BaseService;
import com.tcdt.qlnvhang.entities.xuathang.daugia.tochuctrienkhai.ketqua.XhKqBdgHdr;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

@Service
public interface XhKqBdgHdrService extends BaseService<XhKqBdgHdr, XhKqBdgHdrReq,Long> {

  void exportQdHd(XhKqBdgHdrReq objReq, HttpServletResponse response) throws Exception;

  ReportTemplateResponse preview(HashMap<String, Object> body) throws Exception;
}

