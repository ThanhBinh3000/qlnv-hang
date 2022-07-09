package com.tcdt.qlnvhang.repository.vattu.bienbanchuanbikho;

import com.tcdt.qlnvhang.request.search.vattu.bienbanchuanbikho.NhBienBanChuanBiKhoSearchReq;

import java.util.List;

public interface NhBienBanChuanBiKhoRepositoryCustom {
    List<Object[]> search(NhBienBanChuanBiKhoSearchReq req);

    int count(NhBienBanChuanBiKhoSearchReq req);
}
