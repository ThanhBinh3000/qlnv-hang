package com.tcdt.qlnvhang.service.suachuahang;

import com.tcdt.qlnvhang.request.suachua.ScTongHopReq;
import com.tcdt.qlnvhang.request.suachua.ScTrinhThamDinhHdrReq;
import com.tcdt.qlnvhang.service.BaseService;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScTongHopHdr;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScTrinhThamDinhHdr;

import java.util.List;

public interface ScTrinhThamDinhService extends BaseService<ScTrinhThamDinhHdr, ScTrinhThamDinhHdrReq, Long> {
    List<ScTrinhThamDinhHdr> dsQuyetDinhSuaChua(ScTrinhThamDinhHdrReq req) throws Exception;
}
