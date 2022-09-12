package com.tcdt.qlnvhang.repository.xuathang.bangkecanhang;


import com.tcdt.qlnvhang.request.search.xuathang.XhBangKeCanHangSearchReq;
import com.tcdt.qlnvhang.request.search.xuathang.XhPhieuXuatKhoSearchReq;
import com.tcdt.qlnvhang.response.xuathang.bangkecanhang.XhBangKeCanHangRes;
import com.tcdt.qlnvhang.response.xuathang.phieuxuatkho.XhPhieuXuatKhoRes;

import java.util.List;

public interface XhBangKeCanHangRepositoryCustom {
    List<XhBangKeCanHangRes> search(XhBangKeCanHangSearchReq req);
}
