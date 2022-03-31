package com.tcdt.qlnvhang.repository.quanlybienbannhapdaykholuongthuc;

import com.tcdt.qlnvhang.entities.quanlybienbannhapdaykholuongthuc.QlBienBanNhapDayKhoLt;
import com.tcdt.qlnvhang.repository.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QlBienBanNhapDayKhoLtRepository extends BaseRepository<QlBienBanNhapDayKhoLt, Long>, QlBienBanNhapDayKhoLtRepositoryCustom {
}
