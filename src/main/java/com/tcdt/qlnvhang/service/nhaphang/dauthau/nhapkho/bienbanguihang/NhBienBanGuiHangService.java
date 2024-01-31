package com.tcdt.qlnvhang.service.nhaphang.dauthau.nhapkho.bienbanguihang;

import com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhapkho.bienbanguihang.NhBienBanGuiHang;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.request.object.vattu.bienbanguihang.NhBienBanGuiHangReq;
import com.tcdt.qlnvhang.request.search.HhQdNhapxuatSearchReq;
import com.tcdt.qlnvhang.service.BaseService;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

public interface NhBienBanGuiHangService extends BaseService<NhBienBanGuiHang, NhBienBanGuiHangReq, Long> {
  //    NhBienBanGuiHangRes create(NhBienBanGuiHangReq req) throws Exception;
//
//    NhBienBanGuiHangRes update(NhBienBanGuiHangReq req) throws Exception;
//
//    NhBienBanGuiHangRes detail(Long id) throws Exception;
//
//    boolean delete(Long id) throws Exception;
//
//
//    @Transactional(rollbackOn = Exception.class)
//    boolean updateStatusQd(StatusReq req) throws Exception;
//
//    Page<NhBienBanGuiHangRes> search(NhBienBanGuiHangSearchReq req) throws Exception;
//
//    @Transactional(rollbackOn = Exception.class)
//    boolean deleteMultiple(DeleteReq req) throws Exception;
//
//    boolean exportToExcel(NhBienBanGuiHangSearchReq objReq, HttpServletResponse response) throws Exception;
//
//    Integer getSo() throws Exception;
//
//    BaseNhapHangCount count(Set<String> maDvis) throws Exception;
//    ReportTemplateResponse preview(NhBienBanGuiHangReq req) throws Exception;
  void exportBbgh(HhQdNhapxuatSearchReq searchReq, HttpServletResponse response) throws Exception;
  ReportTemplateResponse preview(HashMap<String, Object> body, CustomUserDetails currentUser) throws Exception;
}
