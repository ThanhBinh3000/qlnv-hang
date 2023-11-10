package com.tcdt.qlnvhang.service;

import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kehoachlcnt.HhSlNhapHang;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kehoachlcnt.dexuatkhlcnt.HhDxuatKhLcntHdr;
import com.tcdt.qlnvhang.request.CountKhlcntSlReq;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.object.HhDxuatKhLcntHdrReq;
import com.tcdt.qlnvhang.request.object.HhSlNhapHangReq;
import com.tcdt.qlnvhang.request.search.HhDxuatKhLcntSearchReq;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;
import org.springframework.data.domain.Page;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.math.BigDecimal;

public interface HhSlNhapHangService {

	@Transactional(rollbackOn = Exception.class)
	HhSlNhapHang create(HhSlNhapHangReq objReq) throws Exception;

	@Transactional(rollbackOn = Exception.class)
	HhSlNhapHang update(HhSlNhapHangReq objReq) throws Exception;

	HhSlNhapHang detail(Long id) throws Exception;

}
