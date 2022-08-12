package com.tcdt.qlnvhang.service.bandaugia.tochuctrienkhaikehoachbandaugia;

import com.tcdt.qlnvhang.request.bandaugia.tochuctrienkhaikehoachbandaugia.ThongBaoBanDauGiaRequest;
import com.tcdt.qlnvhang.request.bandaugia.tochuctrienkhaikehoachbandaugia.ThongBaoBanDauGiaSearchRequest;
import com.tcdt.qlnvhang.response.banhangdaugia.tochuctrienkhaikehoachbandaugia.ThongBaoBanDauGiaResponse;
import com.tcdt.qlnvhang.response.banhangdaugia.tochuctrienkhaikehoachbandaugia.ThongBaoBanDauGiaSearchResponse;
import org.springframework.data.domain.Page;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface ThongBaoBanDauGiaService {
	ThongBaoBanDauGiaResponse create(ThongBaoBanDauGiaRequest req) throws Exception;

	ThongBaoBanDauGiaResponse update(ThongBaoBanDauGiaRequest req) throws Exception;

	boolean delete(Long id) throws Exception;

	boolean deleteMultiple(List<Long> ids) throws Exception;

	Page<ThongBaoBanDauGiaSearchResponse> search(ThongBaoBanDauGiaSearchRequest req) throws Exception;

	ThongBaoBanDauGiaResponse detail(Long id) throws Exception;

	ThongBaoBanDauGiaResponse updateTrangThai(Long id, String trangThaiId) throws Exception;

	boolean exportToExcel(ThongBaoBanDauGiaSearchRequest req, HttpServletResponse response) throws Exception;


}
