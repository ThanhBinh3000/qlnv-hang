package com.tcdt.qlnvhang.repository.dieuchuyennoibo;

import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBangKeCanHangDtl;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbPhieuNhapKhoDtl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DcnbPhieuNhapKhoDtlRepository extends JpaRepository<DcnbPhieuNhapKhoDtl, Long> {
    List<DcnbBangKeCanHangDtl> findByHdrId(Long id);

    List<DcnbBangKeCanHangDtl> findByHdrIdIn(List<Long> listId);

    void deleteAllByHdrId(long hdrId);
}
