package com.tcdt.qlnvhang.service.suachuahang;

import com.tcdt.qlnvhang.request.suachua.ScQuyetDinhScReq;
import com.tcdt.qlnvhang.request.suachua.ScQuyetDinhXuatHangReq;
import com.tcdt.qlnvhang.request.suachua.ScTongHopReq;
import com.tcdt.qlnvhang.service.BaseService;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScQuyetDinhSc;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScQuyetDinhXuatHang;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScTongHopHdr;

import java.util.List;

public interface ScQuyetDinhXuatHangService extends BaseService<ScQuyetDinhXuatHang, ScQuyetDinhXuatHangReq, Long> {

    List<ScQuyetDinhXuatHang> dsTaoPhieuXuatKho(ScQuyetDinhXuatHangReq req) throws Exception;
}
