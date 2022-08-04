package com.tcdt.qlnvhang.repository.xuathang.quyetdinhgiaonhiemvuxuat;

import com.tcdt.qlnvhang.entities.xuathang.XhQdGiaoNvuXuat;
import com.tcdt.qlnvhang.request.search.xuathang.XhQdGiaoNvuXuatSearchReq;

import java.util.List;

public interface XhQdGiaoNvuXuatRepositoryCustom {
    List<XhQdGiaoNvuXuat> search(XhQdGiaoNvuXuatSearchReq req);

    int count(XhQdGiaoNvuXuatSearchReq req);
}
