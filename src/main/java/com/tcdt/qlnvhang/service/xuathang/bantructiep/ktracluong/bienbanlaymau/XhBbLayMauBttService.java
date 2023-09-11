package com.tcdt.qlnvhang.service.xuathang.bantructiep.ktracluong.bienbanlaymau;

import com.tcdt.qlnvhang.entities.xuathang.bantructiep.ktracluong.bienbanlaymau.XhBbLayMauBttHdr;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.ktracluong.bienbanlaymau.XhBbLayMauBttHdrReq;
import com.tcdt.qlnvhang.service.BaseService;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public interface XhBbLayMauBttService extends BaseService<XhBbLayMauBttHdr, XhBbLayMauBttHdrReq, Long> {

    ReportTemplateResponse preview(HashMap<String, Object> body, CustomUserDetails currentUser) throws Exception;
}
