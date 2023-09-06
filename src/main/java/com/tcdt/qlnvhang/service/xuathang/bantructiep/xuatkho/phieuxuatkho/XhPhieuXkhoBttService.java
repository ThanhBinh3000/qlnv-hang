package com.tcdt.qlnvhang.service.xuathang.bantructiep.xuatkho.phieuxuatkho;

import com.tcdt.qlnvhang.entities.xuathang.bantructiep.xuatkho.phieuxuatkho.XhPhieuXkhoBtt;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.xuatkho.phieuxuatkho.XhPhieuXkhoBttReq;
import com.tcdt.qlnvhang.service.BaseService;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public interface XhPhieuXkhoBttService extends BaseService<XhPhieuXkhoBtt, XhPhieuXkhoBttReq, Long> {

    ReportTemplateResponse preview(HashMap<String, Object> body, CustomUserDetails currentUser) throws Exception;
}
