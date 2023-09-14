package com.tcdt.qlnvhang.service.suachuahang;

import com.tcdt.qlnvhang.request.suachua.ScPhieuXuatKhoReq;
import com.tcdt.qlnvhang.request.suachua.ScQuyetDinhScReq;
import com.tcdt.qlnvhang.request.suachua.ScQuyetDinhXuatHangReq;
import com.tcdt.qlnvhang.request.suachua.ScTongHopReq;
import com.tcdt.qlnvhang.service.BaseService;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.*;

import java.util.List;

public interface ScQuyetDinhXuatHangService extends BaseService<ScQuyetDinhXuatHang, ScQuyetDinhXuatHangReq, Long> {

    List<ScQuyetDinhXuatHang> dsTaoPhieuXuatKho(ScQuyetDinhXuatHangReq req) throws Exception;

    List<ScQuyetDinhXuatHang> searchDanhSachTaoBaoCao(ScQuyetDinhXuatHangReq req) throws Exception;

    List<ScDanhSachHdr> getDetailBaoCao(Long idQdXh) throws Exception;

    ReportTemplateResponse preview(ScQuyetDinhXuatHangReq objReq) throws Exception;
}
