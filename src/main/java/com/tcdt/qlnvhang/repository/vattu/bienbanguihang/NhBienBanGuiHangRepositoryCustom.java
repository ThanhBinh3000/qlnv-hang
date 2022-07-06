package com.tcdt.qlnvhang.repository.vattu.bienbanguihang;

import com.tcdt.qlnvhang.request.search.vattu.bienbanguihang.NhBienBanGuiHangSearchReq;

import java.util.List;

public interface NhBienBanGuiHangRepositoryCustom {

    List<Object[]> search(NhBienBanGuiHangSearchReq req);

    int count(NhBienBanGuiHangSearchReq req);
}
