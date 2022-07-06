package com.tcdt.qlnvhang.repository.quanlyphieunhapkholuongthuc;

import com.tcdt.qlnvhang.entities.quanlyphieunhapkholuongthuc.QlPhieuNhapKhoHangHoaLt;
import com.tcdt.qlnvhang.repository.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;

@Repository
public interface QlPhieuNhapKhoHangHoaLtRepository extends BaseRepository<QlPhieuNhapKhoHangHoaLt, Long> {
    List<QlPhieuNhapKhoHangHoaLt> findAllByQlPhieuNhapKhoLtId(Long qlPhieuNhapKhoLtId);

    @Transactional
    @Modifying
    void deleteByQlPhieuNhapKhoLtIdIn(Collection<Long> phieuNhapKhoIds);
}
