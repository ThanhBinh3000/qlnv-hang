package com.tcdt.qlnvhang.service.nhaphang.nhapkhac;

import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kiemtracl.hosokythuat.NhHoSoKyThuat;
import com.tcdt.qlnvhang.entities.nhaphang.nhapkhac.hosokythuat.NhHoSoKyThuatNk;
import com.tcdt.qlnvhang.request.object.vattu.hosokythuat.NhHoSoKyThuatReq;
import com.tcdt.qlnvhang.request.xuathang.phieukiemnghiemchatluong.XhPhieuKnghiemCluongReq;
import com.tcdt.qlnvhang.service.BaseService;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;
import org.springframework.data.domain.Page;

public interface NhHoSoKyThuatNkService extends BaseService<NhHoSoKyThuatNk,NhHoSoKyThuatReq,Long> {
//    NhHoSoKyThuatNk create(NhHoSoKyThuatReq req) throws Exception;
//
//    NhHoSoKyThuatNk update(NhHoSoKyThuatReq req) throws Exception;
//
    NhHoSoKyThuatNk detail(Long id) throws Exception;

    ReportTemplateResponse preview(NhHoSoKyThuatReq objReq) throws Exception;
//
//    boolean delete(Long id) throws Exception;
//
//
//    @Transactional(rollbackOn = Exception.class)
//    boolean updateStatusQd(StatusReq req) throws Exception;
//
//    Page<NhHoSoKyThuatNk> search(NhHoSoKyThuatReq req) throws Exception;
//
//    @Transactional(rollbackOn = Exception.class)
//    boolean deleteMultiple(DeleteReq req) throws Exception;
//
//    boolean exportToExcel(NhHoSoKyThuatSearchReq objReq, HttpServletResponse response) throws Exception;
//
//    Integer getSo() throws Exception;
//
//    BaseNhapHangCount count(Set<String> maDvis) throws Exception;
}
