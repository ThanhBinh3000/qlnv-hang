package com.tcdt.qlnvhang.repository.xuathang.bbtinhkho;


import com.tcdt.qlnvhang.request.search.xuathang.XhBienBanTinhKhoSearchReq;

import java.util.List;

public interface XhBienBanTinhKhoRepositoryCustom {
    List<Object[]> search(XhBienBanTinhKhoSearchReq req);

}
