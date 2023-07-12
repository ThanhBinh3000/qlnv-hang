package com.tcdt.qlnvhang.service.suachuahang;

import com.tcdt.qlnvhang.request.suachua.ScQuyetDinhScReq;
import com.tcdt.qlnvhang.request.suachua.ScTongHopReq;
import com.tcdt.qlnvhang.service.BaseService;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScQuyetDinhSc;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScTongHopHdr;

import java.util.List;

public interface ScQuyetDinhScService extends BaseService<ScQuyetDinhSc, ScQuyetDinhScReq, Long> {

    List<ScQuyetDinhSc> dsQuyetDinhXuatHang(ScQuyetDinhScReq req) throws Exception;
}
