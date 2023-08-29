package com.tcdt.qlnvhang.service.nhaphang.dauthau.ktracluong.bienbancbkho;

import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kiemtracl.bienbanchuanbikho.NhBienBanChuanBiKho;
import com.tcdt.qlnvhang.request.object.HhQdKhlcntHdrReq;
import com.tcdt.qlnvhang.request.object.vattu.bienbanchuanbikho.NhBienBanChuanBiKhoReq;
import com.tcdt.qlnvhang.service.BaseService;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;

public interface NhBienBanChuanBiKhoService extends BaseService<NhBienBanChuanBiKho,NhBienBanChuanBiKhoReq,Long> {
    ReportTemplateResponse preview(NhBienBanChuanBiKhoReq req) throws Exception;
}
