package com.tcdt.qlnvhang.service.bandaugia.quyetdinhpheduyetkehoachbandaugia;


import com.tcdt.qlnvhang.request.bandaugia.quyetdinhpheduyetkehochbandaugia.BhQdPheDuyetKhbdgRequest;
import com.tcdt.qlnvhang.request.bandaugia.quyetdinhpheduyetkehochbandaugia.BhQdPheDuyetKhbdgSearchRequest;
import com.tcdt.qlnvhang.response.banhangdaugia.quyetdinhpheduyetkehoachbandaugia.BhQdPheDuyetKhbdgResponse;
import com.tcdt.qlnvhang.response.banhangdaugia.quyetdinhpheduyetkehoachbandaugia.BhQdPheDuyetKhbdgSearchResponse;
import org.springframework.data.domain.Page;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface BhQdPheDuyetKhbdgService {
	BhQdPheDuyetKhbdgResponse create(BhQdPheDuyetKhbdgRequest req) throws Exception;

	BhQdPheDuyetKhbdgResponse update(BhQdPheDuyetKhbdgRequest req) throws Exception;

	boolean delete(Long id) throws Exception;

	Page<BhQdPheDuyetKhbdgSearchResponse> search(BhQdPheDuyetKhbdgSearchRequest req) throws Exception;

	BhQdPheDuyetKhbdgResponse updateTrangThai(Long id, String trangThaiId) throws Exception;

	boolean exportToExcel(BhQdPheDuyetKhbdgSearchRequest req, HttpServletResponse response) throws Exception;

	boolean deleteMultiple(List<Long> ids) throws Exception;

	BhQdPheDuyetKhbdgResponse detail(Long id) throws Exception;


}
