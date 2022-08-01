package com.tcdt.qlnvhang.repository.quanlybangkecanhangluongthuc;

import com.tcdt.qlnvhang.entities.nhaphang.quanlybangkecanhangluongthuc.QlBangKeCanHangLt;
import com.tcdt.qlnvhang.request.search.quanlybangkecanhangluongthuc.QlBangKeCanHangLtSearchReq;

import java.util.List;

public interface QlBangKeCanHangLtRepositoryCustom {
    List<Object[]> search(QlBangKeCanHangLtSearchReq req);

    int count(QlBangKeCanHangLtSearchReq req);
}
