package com.tcdt.qlnvhang.service.xuathang.thanhlytieuhuy.thanhly;

import com.tcdt.qlnvhang.request.suachua.ScBangKeXuatVatTuReq;
import com.tcdt.qlnvhang.request.xuathang.thanhlytieuhuy.thanhly.XhTlBbLayMauReq;
import com.tcdt.qlnvhang.service.BaseService;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScQuyetDinhXuatHang;
import com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.thanhly.XhTlBbLayMauHdr;
import com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.thanhly.XhTlQdGiaoNvHdr;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface XhTlBbLayMauService extends BaseService<XhTlBbLayMauHdr, XhTlBbLayMauReq, Long> {

    List<XhTlBbLayMauHdr> dsTaoKtraCluong(XhTlBbLayMauReq req) throws Exception;

    Page<XhTlQdGiaoNvHdr> searchBbLm(XhTlBbLayMauReq req) throws Exception;

}