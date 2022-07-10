package com.tcdt.qlnvhang.repository.vattu.bangke;

import com.tcdt.qlnvhang.request.search.vattu.bangke.NhBangKeVtSearchReq;

import java.util.List;

public interface NhBangKeVtRepositoryCustom {
    List<Object[]> search(NhBangKeVtSearchReq req);

    int count(NhBangKeVtSearchReq req);
}
