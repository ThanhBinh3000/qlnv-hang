package com.tcdt.qlnvhang.repository.quyetdinhgiaonhiemvunhapxuat;

import com.tcdt.qlnvhang.request.search.HhQdNhapxuatSearchReq;
import com.tcdt.qlnvhang.table.HhQdGiaoNvuNhapxuatHdr;

import java.util.List;

public interface HhQdGiaoNvuNhapxuatRepositoryCustom {
    List<Object[]> search(HhQdNhapxuatSearchReq req, String capDvi);

    int count(HhQdNhapxuatSearchReq req, String capDvi);
}
