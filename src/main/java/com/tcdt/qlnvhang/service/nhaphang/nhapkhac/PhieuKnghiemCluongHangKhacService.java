package com.tcdt.qlnvhang.service.nhaphang.nhapkhac;

import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kiemtracl.phieuknghiemcl.PhieuKnghiemCluongHang;
import com.tcdt.qlnvhang.entities.nhaphang.nhapkhac.phieuknghiemcl.PhieuKnghiemCluongHangKhac;
import com.tcdt.qlnvhang.request.object.PhieuKnghiemCluongHangKhacReq;
import com.tcdt.qlnvhang.request.object.phieuknghiemcluonghang.PhieuKnghiemCluongHangReq;
import com.tcdt.qlnvhang.request.search.PhieuKnghiemCluongHangSearchReq;
import com.tcdt.qlnvhang.service.BaseService;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;

public interface PhieuKnghiemCluongHangKhacService extends BaseService<PhieuKnghiemCluongHangKhac, PhieuKnghiemCluongHangKhacReq,Long> {
//	Page<PhieuKnghiemCluongHangRes> search(PhieuKnghiemCluongHangSearchReq req) throws Exception;
    PhieuKnghiemCluongHangKhac create(PhieuKnghiemCluongHangKhacReq req) throws Exception;
    PhieuKnghiemCluongHangKhac update(PhieuKnghiemCluongHangKhacReq req) throws Exception;

    ReportTemplateResponse preview(PhieuKnghiemCluongHangSearchReq objReq) throws Exception;
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
}
