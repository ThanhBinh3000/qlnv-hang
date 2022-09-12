package com.tcdt.qlnvhang.repository.xuathang.bangkecanhang;


import com.tcdt.qlnvhang.request.search.xuathang.XhBangKeCanHangSearchReq;
import com.tcdt.qlnvhang.response.xuathang.bangkecanhang.XhBangKeCanHangRes;

import java.util.List;

public interface XhBangKeCanHangRepositoryCustom {
    List<XhBangKeCanHangRes> search(XhBangKeCanHangSearchReq req);
}
