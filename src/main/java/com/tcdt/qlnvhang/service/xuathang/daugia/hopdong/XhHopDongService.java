package com.tcdt.qlnvhang.service.xuathang.daugia.hopdong;

import com.tcdt.qlnvhang.entities.xuathang.daugia.hopdong.XhHopDongHdr;
import com.tcdt.qlnvhang.request.xuathang.daugia.hopdong.XhHopDongHdrReq;
import com.tcdt.qlnvhang.service.BaseService;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public interface XhHopDongService extends BaseService<XhHopDongHdr, XhHopDongHdrReq, Long> {
  ReportTemplateResponse preview(HashMap<String, Object> body) throws Exception;

}
