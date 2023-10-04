package com.tcdt.qlnvhang.service.xuathang.thanhlytieuhuy.thanhly;

import com.tcdt.qlnvhang.request.xuathang.thanhlytieuhuy.thanhly.XhTlBangKeReq;
import com.tcdt.qlnvhang.request.xuathang.thanhlytieuhuy.thanhly.XhTlKtraClReq;
import com.tcdt.qlnvhang.service.BaseService;
import com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.thanhly.XhTlBangKeHdr;
import com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.thanhly.XhTlQdGiaoNvHdr;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public interface XhTlBangKeService extends BaseService<XhTlBangKeHdr, XhTlBangKeReq, Long> {
    Page<XhTlQdGiaoNvHdr> searchBangKeCanHang(XhTlBangKeReq req) throws Exception;

}