package com.tcdt.qlnvhang.repository.xuathang.bbtinhkho;


import com.tcdt.qlnvhang.entities.xuathang.bbtinhkho.XhBienBanTinhKho;
import com.tcdt.qlnvhang.request.search.xuathang.XhBienBanTinhKhoSearchReq;

import java.util.List;

public interface XhBienBanTinhKhoRepositoryCustom {
    List<XhBienBanTinhKho> search(XhBienBanTinhKhoSearchReq req);

    int count(XhBienBanTinhKhoSearchReq req);
}
