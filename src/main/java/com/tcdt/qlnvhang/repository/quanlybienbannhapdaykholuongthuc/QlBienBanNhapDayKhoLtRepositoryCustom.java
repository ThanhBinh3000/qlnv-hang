package com.tcdt.qlnvhang.repository.quanlybienbannhapdaykholuongthuc;

import com.tcdt.qlnvhang.entities.nhaphang.quanlybienbannhapdaykholuongthuc.QlBienBanNhapDayKhoLt;
import com.tcdt.qlnvhang.request.search.quanlybienbannhapdaykholuongthuc.QlBienBanNhapDayKhoLtSearchReq;

import java.util.List;

public interface QlBienBanNhapDayKhoLtRepositoryCustom {
    List<Object[]> search(QlBienBanNhapDayKhoLtSearchReq req);

    int count(QlBienBanNhapDayKhoLtSearchReq req);
}
