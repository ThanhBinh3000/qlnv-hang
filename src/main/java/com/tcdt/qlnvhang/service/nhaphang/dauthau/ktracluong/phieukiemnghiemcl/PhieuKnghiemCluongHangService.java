package com.tcdt.qlnvhang.service.nhaphang.dauthau.ktracluong.phieukiemnghiemcl;

import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kiemtracl.phieuknghiemcl.PhieuKnghiemCluongHang;
import com.tcdt.qlnvhang.request.object.phieuknghiemcluonghang.PhieuKnghiemCluongHangReq;
import com.tcdt.qlnvhang.request.search.PhieuKnghiemCluongHangSearchReq;
import com.tcdt.qlnvhang.service.BaseService;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;

public interface PhieuKnghiemCluongHangService extends BaseService<PhieuKnghiemCluongHang,PhieuKnghiemCluongHangReq,Long> {
//	Page<PhieuKnghiemCluongHangRes> search(PhieuKnghiemCluongHangSearchReq req) throws Exception;
//	PhieuKnghiemCluongHangRes create(PhieuKnghiemCluongHangReq req) throws Exception;
//	PhieuKnghiemCluongHangRes update(PhieuKnghiemCluongHangReq req) throws Exception;
//	boolean updateStatus(StatusReq req) throws Exception;
//	PhieuKnghiemCluongHangRes detail(Long id) throws Exception;
//	boolean delete(Long id) throws Exception;
//
//	@Transactional(rollbackOn = Exception.class)
//	boolean deleteMultiple(DeleteReq req) throws Exception;
//
//    boolean exportToExcel(PhieuKnghiemCluongHangSearchReq objReq, HttpServletResponse response) throws Exception;
//
//	Integer getSo() throws Exception;
//
//	BaseNhapHangCount count(Set<String> maDvis) throws Exception;

    ReportTemplateResponse preview(PhieuKnghiemCluongHangSearchReq objReq) throws Exception;
}
