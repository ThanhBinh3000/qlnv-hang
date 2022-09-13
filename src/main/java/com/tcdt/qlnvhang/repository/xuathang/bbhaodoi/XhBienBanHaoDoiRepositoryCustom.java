package com.tcdt.qlnvhang.repository.xuathang.bbhaodoi;


import com.tcdt.qlnvhang.entities.xuathang.bbhaodoi.XhBienBanHaoDoi;
import com.tcdt.qlnvhang.request.search.xuathang.XhBienBanHaoDoiSearchReq;
import com.tcdt.qlnvhang.request.search.xuathang.XhBienBanTinhKhoSearchReq;

import java.util.List;

public interface XhBienBanHaoDoiRepositoryCustom {
    List<XhBienBanHaoDoi> search(XhBienBanHaoDoiSearchReq req);

    int count(XhBienBanHaoDoiSearchReq req);

}
