package com.tcdt.qlnvhang.service.nhaphang.luongthucmuoi.phieuknghiemcluonghang;

import com.tcdt.qlnvhang.request.DeleteReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.object.phieuknghiemcluonghang.PhieuKnghiemCluongHangReq;
import com.tcdt.qlnvhang.request.search.PhieuKnghiemCluongHangSearchReq;
import com.tcdt.qlnvhang.response.BaseNhapHangCount;
import com.tcdt.qlnvhang.response.phieuknghiemcluonghang.PhieuKnghiemCluongHangRes;
import org.springframework.data.domain.Page;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.util.Set;

public interface PhieuKnghiemCluongHangService {
	Page<PhieuKnghiemCluongHangRes> search(PhieuKnghiemCluongHangSearchReq req) throws Exception;
	PhieuKnghiemCluongHangRes create(PhieuKnghiemCluongHangReq req) throws Exception;
	PhieuKnghiemCluongHangRes update(PhieuKnghiemCluongHangReq req) throws Exception;
	boolean updateStatus(StatusReq req) throws Exception;
	PhieuKnghiemCluongHangRes detail(Long id) throws Exception;
	boolean delete(Long id) throws Exception;

	@Transactional(rollbackOn = Exception.class)
	boolean deleteMultiple(DeleteReq req) throws Exception;

    boolean exportToExcel(PhieuKnghiemCluongHangSearchReq objReq, HttpServletResponse response) throws Exception;

	Integer getSo() throws Exception;

	BaseNhapHangCount count(Set<String> maDvis) throws Exception;
}
