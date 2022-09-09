package com.tcdt.qlnvhang.repository.xuathang.phieuxuatkho;


import com.tcdt.qlnvhang.request.search.xuathang.XhPhieuXuatKhoSearchReq;
import com.tcdt.qlnvhang.response.xuathang.phieuxuatkho.XhPhieuXuatKhoRes;

import java.util.List;

public interface XhPhieuXuatKhoRepositoryCustom {
    List<XhPhieuXuatKhoRes> search(XhPhieuXuatKhoSearchReq req);
}
