package com.tcdt.qlnvhang.service.kehoachbanhangdaugia;

import com.tcdt.qlnvhang.request.bandaugia.kehoachbanhangdaugia.KehoachBanDauGiaRequest;
import com.tcdt.qlnvhang.request.bandaugia.kehoachbanhangdaugia.KeHoachBanDauGiaSearchRequest;
import com.tcdt.qlnvhang.response.banhangdaugia.kehoachbanhangdaugia.BhDgKehoachResponse;
import org.springframework.data.domain.Page;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface KeHoachBanDauGiaService {
	BhDgKehoachResponse create (KehoachBanDauGiaRequest req) throws Exception;
	BhDgKehoachResponse update (KehoachBanDauGiaRequest req) throws Exception;
	boolean delete (Long id) throws Exception;
	Page<BhDgKehoachResponse> search(KeHoachBanDauGiaSearchRequest req) throws Exception;
	BhDgKehoachResponse updateTrangThai(Long id, String trangThaiId) throws Exception;

	boolean exportToExcel(KeHoachBanDauGiaSearchRequest req, HttpServletResponse response) throws Exception;

	boolean deleteMultiple (List<Long> ids) throws Exception;

}
