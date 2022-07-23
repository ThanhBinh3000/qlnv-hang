package com.tcdt.qlnvhang.service.bandaugia.kehoachbanhangdaugia;

import com.tcdt.qlnvhang.request.bandaugia.kehoachbanhangdaugia.KehoachBanDauGiaRequest;
import com.tcdt.qlnvhang.request.bandaugia.kehoachbanhangdaugia.KeHoachBanDauGiaSearchRequest;
import com.tcdt.qlnvhang.response.banhangdaugia.kehoachbanhangdaugia.KeHoachBanDauGiaResponse;
import org.springframework.data.domain.Page;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface KeHoachBanDauGiaService {
	KeHoachBanDauGiaResponse create (KehoachBanDauGiaRequest req) throws Exception;
	KeHoachBanDauGiaResponse update (KehoachBanDauGiaRequest req) throws Exception;
	boolean delete (Long id) throws Exception;
	Page<KeHoachBanDauGiaResponse> search(KeHoachBanDauGiaSearchRequest req) throws Exception;
	KeHoachBanDauGiaResponse updateTrangThai(Long id, String trangThaiId) throws Exception;

	boolean exportToExcel(KeHoachBanDauGiaSearchRequest req, HttpServletResponse response) throws Exception;

	boolean deleteMultiple (List<Long> ids) throws Exception;

}
