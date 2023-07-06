package com.tcdt.qlnvhang.service.suachuahang;

import com.tcdt.qlnvhang.request.suachua.ScTongHopReq;
import com.tcdt.qlnvhang.service.BaseService;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScTongHopHdr;

import java.util.List;

public interface ScTongHopService extends BaseService<ScTongHopHdr,ScTongHopReq,Long> {

    List<ScTongHopHdr> dsTongHopTrinhVaThamDinh(ScTongHopReq req) throws Exception;
}
