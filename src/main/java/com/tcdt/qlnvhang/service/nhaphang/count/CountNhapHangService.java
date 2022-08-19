package com.tcdt.qlnvhang.service.nhaphang.count;

import com.tcdt.qlnvhang.request.CountReq;
import com.tcdt.qlnvhang.response.BaseNhapHangCount;

public interface CountNhapHangService {
    BaseNhapHangCount countKtcl(CountReq req) throws Exception;

    BaseNhapHangCount countNhapKho(CountReq req) throws Exception;
}
