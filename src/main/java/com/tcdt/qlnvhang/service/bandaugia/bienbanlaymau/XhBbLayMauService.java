package com.tcdt.qlnvhang.service.bandaugia.bienbanlaymau;


import com.tcdt.qlnvhang.request.bandaugia.bienbanlaymau.XhBbLayMauRequest;
import com.tcdt.qlnvhang.request.bandaugia.bienbanlaymau.XhBbLayMauSearchRequest;
import com.tcdt.qlnvhang.response.banhangdaugia.bienbanlaymau.XhBbLayMauResponse;
import com.tcdt.qlnvhang.response.banhangdaugia.bienbanlaymau.XhBbLayMauSearchResponse;
import org.springframework.data.domain.Page;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface XhBbLayMauService {
	XhBbLayMauResponse create(XhBbLayMauRequest req) throws Exception;

	XhBbLayMauResponse update(XhBbLayMauRequest req) throws Exception;

	boolean delete(Long id) throws Exception;

	boolean deleteMultiple(List<Long> ids) throws Exception;

	Page<XhBbLayMauSearchResponse> search(XhBbLayMauSearchRequest req) throws Exception;

	XhBbLayMauResponse detail(Long id) throws Exception;

	XhBbLayMauResponse updateTrangThai(Long id, String trangThaiId) throws Exception;

	boolean exportToExcel(XhBbLayMauSearchRequest req, HttpServletResponse response) throws Exception;

	Integer getSo() throws Exception;



}
