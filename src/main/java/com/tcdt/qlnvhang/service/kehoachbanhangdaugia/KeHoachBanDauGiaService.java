package com.tcdt.qlnvhang.service.kehoachbanhangdaugia;

import com.tcdt.qlnvhang.request.kehoachbanhangdaugia.BhDgKehoachReq;
import com.tcdt.qlnvhang.request.kehoachbanhangdaugia.BhDgKehoachSearchReq;
import com.tcdt.qlnvhang.response.kehoachbanhangdaugia.BhDgKehoachRes;
import org.springframework.data.domain.Page;

import java.util.List;

public interface KeHoachBanDauGiaService {
	BhDgKehoachRes create (BhDgKehoachReq req) throws Exception;
	BhDgKehoachRes update (BhDgKehoachReq req) throws Exception;
	boolean delete (Long id) throws Exception;
	Page<BhDgKehoachRes> search(BhDgKehoachSearchReq req) throws Exception;
}
