package com.tcdt.qlnvhang.service.xuathang.thanhlytieuhuy.thanhly;

import com.tcdt.qlnvhang.request.xuathang.thanhlytieuhuy.thanhly.XhTlBbLayMauReq;
import com.tcdt.qlnvhang.service.BaseService;
import com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.thanhly.XhTlBbLayMauHdr;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface XhTlBbLayMauService extends BaseService<XhTlBbLayMauHdr, XhTlBbLayMauReq, Long> {

    List<XhTlBbLayMauHdr> dsTaoKtraCluong(XhTlBbLayMauReq req) throws Exception;
}