package com.tcdt.qlnvhang.repository.quanlyphieunhapkholuongthuc;

import com.tcdt.qlnvhang.entities.quanlyphieunhapkholuongthuc.QlPhieuNhapKhoHangHoaLt;
import com.tcdt.qlnvhang.repository.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QlPhieuNhapKhoHangHoaLtRepository extends BaseRepository<QlPhieuNhapKhoHangHoaLt, Long> {
    List<QlPhieuNhapKhoHangHoaLt> findAllByQlPhieuNhapKhoLtId(Long qlPhieuNhapKhoLtId);
}
