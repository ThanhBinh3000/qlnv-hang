package com.tcdt.qlnvhang.service.kehoachbanhangdaugia;

import com.tcdt.qlnvhang.request.kehoachbanhangdaugia.BhDgKehoachReq;
import com.tcdt.qlnvhang.response.kehoachbanhangdaugia.BhDgKehoachRes;

public interface KeHoachBanDauGiaService {
	BhDgKehoachRes create (BhDgKehoachReq req) throws Exception;
	BhDgKehoachRes update (BhDgKehoachReq req) throws Exception;
}
