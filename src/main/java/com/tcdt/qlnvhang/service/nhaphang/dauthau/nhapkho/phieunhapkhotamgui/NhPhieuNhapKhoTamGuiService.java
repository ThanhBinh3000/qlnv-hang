package com.tcdt.qlnvhang.service.nhaphang.dauthau.nhapkho.phieunhapkhotamgui;

import com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhapkho.phieunhapkhotamgui.NhPhieuNhapKhoTamGui;
import com.tcdt.qlnvhang.request.object.vattu.bienbanchuanbikho.NhBienBanChuanBiKhoReq;
import com.tcdt.qlnvhang.request.object.vattu.phieunhapkhotamgui.NhPhieuNhapKhoTamGuiReq;
import com.tcdt.qlnvhang.service.BaseService;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;

public interface NhPhieuNhapKhoTamGuiService extends BaseService<NhPhieuNhapKhoTamGui,NhPhieuNhapKhoTamGuiReq,Long> {
    ReportTemplateResponse preview(NhPhieuNhapKhoTamGuiReq req) throws Exception;

}
