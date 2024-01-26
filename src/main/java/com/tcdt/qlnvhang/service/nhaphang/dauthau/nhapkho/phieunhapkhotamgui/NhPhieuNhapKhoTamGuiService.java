package com.tcdt.qlnvhang.service.nhaphang.dauthau.nhapkho.phieunhapkhotamgui;

import com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhapkho.phieunhapkhotamgui.NhPhieuNhapKhoTamGui;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.request.object.vattu.bienbanchuanbikho.NhBienBanChuanBiKhoReq;
import com.tcdt.qlnvhang.request.object.vattu.phieunhapkhotamgui.NhPhieuNhapKhoTamGuiReq;
import com.tcdt.qlnvhang.request.search.HhQdNhapxuatSearchReq;
import com.tcdt.qlnvhang.service.BaseService;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

public interface NhPhieuNhapKhoTamGuiService extends BaseService<NhPhieuNhapKhoTamGui,NhPhieuNhapKhoTamGuiReq,Long> {
//    ReportTemplateResponse preview(NhPhieuNhapKhoTamGuiReq req) throws Exception;
    void exportPnktg(HhQdNhapxuatSearchReq searchReq, HttpServletResponse response) throws Exception;
    ReportTemplateResponse preview(HashMap<String, Object> body, CustomUserDetails currentUser) throws Exception;
}
