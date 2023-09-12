package com.tcdt.qlnvhang.service.nhaphang.nhapkhac;

import com.tcdt.qlnvhang.entities.nhaphang.nhapkhac.kiemtracl.bblaymaubangiaomau.BienBanLayMauKhac;
import com.tcdt.qlnvhang.request.object.BienBanLayMauKhacReq;
import com.tcdt.qlnvhang.service.BaseService;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;

public interface BienBanLayMauKhacService extends BaseService<BienBanLayMauKhac, BienBanLayMauKhacReq,Long> {

    BienBanLayMauKhac create(BienBanLayMauKhacReq req) throws Exception;

    void delete(Long id) throws Exception;

    BienBanLayMauKhac approve(BienBanLayMauKhacReq req) throws Exception;

    ReportTemplateResponse preview(BienBanLayMauKhacReq objReq) throws Exception;
}
