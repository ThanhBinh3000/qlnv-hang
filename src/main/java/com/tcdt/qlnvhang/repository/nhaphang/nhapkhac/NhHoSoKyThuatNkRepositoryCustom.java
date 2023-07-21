package com.tcdt.qlnvhang.repository.nhaphang.nhapkhac;

import com.tcdt.qlnvhang.request.search.vattu.hosokythuat.NhHoSoKyThuatSearchReq;

import java.util.List;

public interface NhHoSoKyThuatNkRepositoryCustom {
    List<Object[]> search(NhHoSoKyThuatSearchReq req);

    int count(NhHoSoKyThuatSearchReq req);
}
