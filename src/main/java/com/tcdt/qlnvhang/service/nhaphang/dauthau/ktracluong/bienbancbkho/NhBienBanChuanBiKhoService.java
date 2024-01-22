package com.tcdt.qlnvhang.service.nhaphang.dauthau.ktracluong.bienbancbkho;

import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kiemtracl.bienbanchuanbikho.NhBienBanChuanBiKho;
import com.tcdt.qlnvhang.request.object.HhQdKhlcntHdrReq;
import com.tcdt.qlnvhang.request.object.vattu.bienbanchuanbikho.NhBienBanChuanBiKhoReq;
import com.tcdt.qlnvhang.request.search.HhQdNhapxuatSearchReq;
import com.tcdt.qlnvhang.service.BaseService;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;

import javax.servlet.http.HttpServletResponse;

public interface NhBienBanChuanBiKhoService extends BaseService<NhBienBanChuanBiKho,NhBienBanChuanBiKhoReq,Long> {
    ReportTemplateResponse preview(NhBienBanChuanBiKhoReq req) throws Exception;
    void exportBbcbk(HhQdNhapxuatSearchReq searchReq, HttpServletResponse response) throws Exception;
}
