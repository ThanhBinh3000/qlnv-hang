package com.tcdt.qlnvhang.service.xuathang.bantructiep.xuatkho.bangkecanhang;

import com.tcdt.qlnvhang.entities.xuathang.bantructiep.xuatkho.bangkecanhang.XhBkeCanHangBttHdr;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.xuatkho.bangkecanhang.XhBkeCanHangBttHdrReq;
import com.tcdt.qlnvhang.service.BaseService;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public interface XhBkeCanHangBttService extends BaseService<XhBkeCanHangBttHdr, XhBkeCanHangBttHdrReq, Long> {
    ReportTemplateResponse preview(HashMap<String, Object> body, CustomUserDetails currentUser) throws Exception;
}