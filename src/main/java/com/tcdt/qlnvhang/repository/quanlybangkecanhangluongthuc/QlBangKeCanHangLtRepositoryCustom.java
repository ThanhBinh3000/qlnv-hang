package com.tcdt.qlnvhang.repository.quanlybangkecanhangluongthuc;

import com.tcdt.qlnvhang.entities.quanlybangkecanhangluongthuc.QlBangKeCanHangLt;
import com.tcdt.qlnvhang.request.search.quanlybangkecanhangluongthuc.QlBangKeCanHangLtSearchReq;

import java.util.List;

public interface QlBangKeCanHangLtRepositoryCustom {
    List<QlBangKeCanHangLt> search(QlBangKeCanHangLtSearchReq req);

    int count(QlBangKeCanHangLtSearchReq req);
}
