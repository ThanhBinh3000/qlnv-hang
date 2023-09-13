package com.tcdt.qlnvhang.service.suachuahang;

import com.tcdt.qlnvhang.request.suachua.ScBienBanKtReq;
import com.tcdt.qlnvhang.request.suachua.ScPhieuNhapKhoReq;
import com.tcdt.qlnvhang.request.suachua.ScQuyetDinhXuatHangReq;
import com.tcdt.qlnvhang.service.BaseService;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScBienBanKtHdr;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScQuyetDinhNhapHang;
import org.springframework.data.domain.Page;

public interface ScBienBanKtService extends BaseService<ScBienBanKtHdr, ScBienBanKtReq, Long> {

    Page<ScQuyetDinhNhapHang> searchBienBanKt(ScBienBanKtReq req) throws Exception;

    ReportTemplateResponse preview(ScBienBanKtReq objReq) throws Exception;

}
