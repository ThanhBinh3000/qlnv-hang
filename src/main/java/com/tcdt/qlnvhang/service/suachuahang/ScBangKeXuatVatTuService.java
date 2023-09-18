package com.tcdt.qlnvhang.service.suachuahang;

import com.tcdt.qlnvhang.request.suachua.ScBangKeXuatVatTuReq;
import com.tcdt.qlnvhang.request.suachua.ScPhieuXuatKhoReq;
import com.tcdt.qlnvhang.request.suachua.ScQuyetDinhXuatHangReq;
import com.tcdt.qlnvhang.service.BaseService;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScBangKeXuatVatTuHdr;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScPhieuXuatKhoHdr;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScQuyetDinhXuatHang;
import org.springframework.data.domain.Page;

public interface ScBangKeXuatVatTuService extends BaseService<ScBangKeXuatVatTuHdr, ScBangKeXuatVatTuReq, Long> {

    Page<ScQuyetDinhXuatHang> searchBangKeCanHang(ScBangKeXuatVatTuReq req) throws Exception;

    ReportTemplateResponse preview(ScBangKeXuatVatTuReq objReq) throws Exception;
}
