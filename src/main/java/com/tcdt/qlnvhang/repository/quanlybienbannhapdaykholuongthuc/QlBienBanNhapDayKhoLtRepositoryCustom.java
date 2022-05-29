package com.tcdt.qlnvhang.repository.quanlybienbannhapdaykholuongthuc;

import com.tcdt.qlnvhang.entities.quanlybienbannhapdaykholuongthuc.QlBienBanNhapDayKhoLt;
import com.tcdt.qlnvhang.request.search.quanlybienbannhapdaykholuongthuc.QlBienBanNhapDayKhoLtSearchReq;

import java.util.List;

public interface QlBienBanNhapDayKhoLtRepositoryCustom {
    List<QlBienBanNhapDayKhoLt> search(QlBienBanNhapDayKhoLtSearchReq req);

    int count(QlBienBanNhapDayKhoLtSearchReq req);
}
