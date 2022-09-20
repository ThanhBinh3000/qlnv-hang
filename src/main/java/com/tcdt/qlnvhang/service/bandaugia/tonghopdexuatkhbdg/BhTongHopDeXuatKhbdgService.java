package com.tcdt.qlnvhang.service.bandaugia.tonghopdexuatkhbdg;

import com.tcdt.qlnvhang.entities.bandaugia.tonghopdexuatkhbdg.BhTongHopDeXuatKhbdg;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.bandaugia.tonghopdexuatkhbdg.BhTongHopDeXuatKhbdgRequest;
import com.tcdt.qlnvhang.request.bandaugia.tonghopdexuatkhbdg.BhTongHopDeXuatKhbdgSearchRequest;
import com.tcdt.qlnvhang.request.search.HhDxKhLcntThopSearchReq;
import com.tcdt.qlnvhang.response.banhangdaugia.tonghopdexuatkhbdg.BhTongHopDeXuatKhbdgResponse;
import com.tcdt.qlnvhang.response.banhangdaugia.tonghopdexuatkhbdg.BhTongHopDeXuatKhbdgSearchResponse;
import com.tcdt.qlnvhang.table.HhDxKhLcntThopHdr;
import org.springframework.data.domain.Page;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface BhTongHopDeXuatKhbdgService {
	BhTongHopDeXuatKhbdgResponse create(BhTongHopDeXuatKhbdgRequest req) throws Exception;

	BhTongHopDeXuatKhbdgResponse update(BhTongHopDeXuatKhbdgRequest req) throws Exception;

	boolean delete(Long id) throws Exception;

	Page<BhTongHopDeXuatKhbdgSearchResponse> search(BhTongHopDeXuatKhbdgSearchRequest req) throws Exception;

	boolean exportToExcel(BhTongHopDeXuatKhbdgSearchRequest req, HttpServletResponse response) throws Exception;

	boolean deleteMultiple(List<Long> ids) throws Exception;

	BhTongHopDeXuatKhbdgResponse detail(Long id) throws Exception;
	boolean updateStatusQd(StatusReq stReq) throws Exception;

	Page<BhTongHopDeXuatKhbdg> searchPage(HttpServletRequest request, BhTongHopDeXuatKhbdgSearchRequest req) throws Exception;
}
