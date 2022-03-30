package com.tcdt.qlnvhang.repository.quanlybangkecanhangluongthuc;

import com.tcdt.qlnvhang.request.search.quanlybangkecanhangluongthuc.QlBangKeCanHangLtSearchReq;
import com.tcdt.qlnvhang.response.quanlybangkecanhangluongthuc.QlBangKeCanHangLtRes;
import org.springframework.data.domain.Page;

public interface QlBangKeCanHangLtRepositoryCustom {
    Page<QlBangKeCanHangLtRes> search(QlBangKeCanHangLtSearchReq req);
}
