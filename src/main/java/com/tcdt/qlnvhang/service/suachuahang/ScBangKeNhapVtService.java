package com.tcdt.qlnvhang.service.suachuahang;

import com.tcdt.qlnvhang.request.suachua.ScBangKeNhapVtReq;
import com.tcdt.qlnvhang.request.suachua.ScBangKeXuatVatTuReq;
import com.tcdt.qlnvhang.service.BaseService;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScBangKeNhapVtHdr;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScQuyetDinhNhapHang;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScQuyetDinhXuatHang;
import org.springframework.data.domain.Page;

public interface ScBangKeNhapVtService extends BaseService<ScBangKeNhapVtHdr, ScBangKeNhapVtReq, Long> {

    Page<ScQuyetDinhNhapHang> searchBangKeNhapVt(ScBangKeNhapVtReq req) throws Exception;

}
