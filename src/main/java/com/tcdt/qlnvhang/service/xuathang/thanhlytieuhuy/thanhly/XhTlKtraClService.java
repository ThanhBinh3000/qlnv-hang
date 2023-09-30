package com.tcdt.qlnvhang.service.xuathang.thanhlytieuhuy.thanhly;

import com.tcdt.qlnvhang.request.xuathang.thanhlytieuhuy.thanhly.XhTlKtraClReq;
import com.tcdt.qlnvhang.service.BaseService;
import com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.thanhly.XhTlKtraClHdr;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface XhTlKtraClService extends BaseService<XhTlKtraClHdr, XhTlKtraClReq, Long> {

    List<XhTlKtraClHdr> dsTaoPhieuXuatKho(XhTlKtraClReq req) throws Exception;
}