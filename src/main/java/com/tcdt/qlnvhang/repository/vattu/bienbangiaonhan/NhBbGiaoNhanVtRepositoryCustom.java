package com.tcdt.qlnvhang.repository.vattu.bienbangiaonhan;

import com.tcdt.qlnvhang.request.search.vattu.bienbangiaonhan.NhBbGiaoNhanVtSearchReq;
import com.tcdt.qlnvhang.request.search.vattu.bienbanguihang.NhBienBanGuiHangSearchReq;

import java.util.List;

public interface NhBbGiaoNhanVtRepositoryCustom {

    List<Object[]> search(NhBbGiaoNhanVtSearchReq req);

    int count(NhBbGiaoNhanVtSearchReq req);
}
