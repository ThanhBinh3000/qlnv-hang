package com.tcdt.qlnvhang.repository.quanlyphieunhapkholuongthuc;

import com.tcdt.qlnvhang.entities.quanlyphieunhapkholuongthuc.QlPhieuNhapKhoLt;
import com.tcdt.qlnvhang.repository.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QlPhieuNhapKhoLtRepository extends BaseRepository<QlPhieuNhapKhoLt, Long>, QlPhieuNhapKhoLtRepositoryCustom {
}
