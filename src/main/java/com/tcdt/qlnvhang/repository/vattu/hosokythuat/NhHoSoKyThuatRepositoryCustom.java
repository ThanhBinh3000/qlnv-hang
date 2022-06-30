package com.tcdt.qlnvhang.repository.vattu.hosokythuat;

import com.tcdt.qlnvhang.request.search.vattu.bienbanguihang.NhBienBanGuiHangSearchReq;
import com.tcdt.qlnvhang.request.search.vattu.hosokythuat.NhHoSoKyThuatSearchReq;

import java.util.List;

public interface NhHoSoKyThuatRepositoryCustom {
    List<Object[]> search(NhHoSoKyThuatSearchReq req);

    int count(NhHoSoKyThuatSearchReq req);
}
