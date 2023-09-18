package com.tcdt.qlnvhang.service.suachuahang;

import com.tcdt.qlnvhang.request.suachua.ScQuyetDinhNhapHangReq;
import com.tcdt.qlnvhang.request.suachua.ScQuyetDinhXuatHangReq;
import com.tcdt.qlnvhang.service.BaseService;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScQuyetDinhNhapHang;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScQuyetDinhXuatHang;

import java.util.List;

public interface ScQuyetDinhNhapHangService extends BaseService<ScQuyetDinhNhapHang, ScQuyetDinhNhapHangReq, Long> {

    List<ScQuyetDinhNhapHang> dsTaoPhieuNhapKho(ScQuyetDinhNhapHangReq req) throws Exception;

    ReportTemplateResponse preview(ScQuyetDinhNhapHangReq objReq) throws Exception;
}
