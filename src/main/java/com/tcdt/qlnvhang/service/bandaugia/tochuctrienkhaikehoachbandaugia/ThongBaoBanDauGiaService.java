package com.tcdt.qlnvhang.service.bandaugia.tochuctrienkhaikehoachbandaugia;

import com.tcdt.qlnvhang.request.bandaugia.tochuctrienkhaikehoachbandaugia.ThongBaoBanDauGiaRequest;
import com.tcdt.qlnvhang.response.banhangdaugia.tochuctrienkhaikehoachbandaugia.ThongBaoBanDauGiaResponse;

import java.util.List;

public interface ThongBaoBanDauGiaService {
	ThongBaoBanDauGiaResponse create (ThongBaoBanDauGiaRequest req) throws Exception;
	ThongBaoBanDauGiaResponse update (ThongBaoBanDauGiaRequest req) throws Exception;
	boolean delete (Long id) throws Exception;
	boolean deleteMultiple (List<Long> ids) throws Exception;
//	Page<ThongBaoBanDauGiaResponse> search(KeHoachBanDauGiaSearchRequest req) throws Exception;
//	KeHoachBanDauGiaResponse updateTrangThai(Long id, String trangThaiId) throws Exception;
//
//	boolean exportToExcel(KeHoachBanDauGiaSearchRequest req, HttpServletResponse response) throws Exception;
//

}
