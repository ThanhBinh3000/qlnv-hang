package com.tcdt.qlnvhang.service.nhaphang.bbanlaymau;

import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kiemtracl.bblaymaubangiaomau.BienBanLayMau;
import com.tcdt.qlnvhang.request.object.bbanlaymau.BienBanLayMauReq;
import com.tcdt.qlnvhang.request.object.vattu.bienbanguihang.NhBienBanGuiHangReq;
import com.tcdt.qlnvhang.service.BaseService;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface BienBanLayMauService extends BaseService<BienBanLayMau,BienBanLayMauReq,Long> {
    ReportTemplateResponse preview(BienBanLayMauReq req) throws Exception;

    List<BienBanLayMau> searchList(BienBanLayMauReq objReq);
}
