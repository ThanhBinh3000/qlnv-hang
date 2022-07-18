package com.tcdt.qlnvhang.service.kehoachbanhangdaugia;

import com.tcdt.qlnvhang.request.kehoachbanhangdaugia.BhDgKehoachReq;
import com.tcdt.qlnvhang.request.kehoachbanhangdaugia.BhDgKehoachSearchReq;
import com.tcdt.qlnvhang.request.phieuktracluong.QlpktclhPhieuKtChatLuongFilterRequestDto;
import com.tcdt.qlnvhang.response.kehoachbanhangdaugia.BhDgKehoachRes;
import org.springframework.data.domain.Page;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface KeHoachBanDauGiaService {
	BhDgKehoachRes create (BhDgKehoachReq req) throws Exception;
	BhDgKehoachRes update (BhDgKehoachReq req) throws Exception;
	boolean delete (Long id) throws Exception;
	Page<BhDgKehoachRes> search(BhDgKehoachSearchReq req) throws Exception;
	BhDgKehoachRes updateTrangThai(Long id, String trangThaiId) throws Exception;

	boolean exportToExcel(BhDgKehoachSearchReq req, HttpServletResponse response) throws Exception;

	boolean deleteMultiple (List<Long> ids) throws Exception;

}
