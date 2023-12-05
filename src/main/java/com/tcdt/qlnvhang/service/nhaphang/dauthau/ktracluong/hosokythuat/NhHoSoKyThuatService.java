package com.tcdt.qlnvhang.service.nhaphang.dauthau.ktracluong.hosokythuat;

import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kiemtracl.hosokythuat.NhHoSoKyThuat;
import com.tcdt.qlnvhang.request.object.vattu.bienbanguihang.NhBienBanGuiHangReq;
import com.tcdt.qlnvhang.request.object.vattu.hosokythuat.NhHoSoKyThuatReq;
import com.tcdt.qlnvhang.service.BaseService;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;

public interface NhHoSoKyThuatService extends BaseService<NhHoSoKyThuat,NhHoSoKyThuatReq,Long> {
//    NhHoSoKyThuatRes create(NhHoSoKyThuatReq req) throws Exception;
//
//    NhHoSoKyThuatRes update(NhHoSoKyThuatReq req) throws Exception;
//
//    NhHoSoKyThuatRes detail(Long id) throws Exception;
//
//    boolean delete(Long id) throws Exception;
//
//
//    @Transactional(rollbackOn = Exception.class)
//    boolean updateStatusQd(StatusReq req) throws Exception;
//
//    Page<NhHoSoKyThuatRes> search(NhHoSoKyThuatSearchReq req) throws Exception;
//
//    @Transactional(rollbackOn = Exception.class)
//    boolean deleteMultiple(DeleteReq req) throws Exception;
//
//    boolean exportToExcel(NhHoSoKyThuatSearchReq objReq, HttpServletResponse response) throws Exception;
//
//    Integer getSo() throws Exception;
//
//    BaseNhapHangCount count(Set<String> maDvis) throws Exception;
    ReportTemplateResponse preview(NhHoSoKyThuatReq req) throws Exception;
}
