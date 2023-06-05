package com.tcdt.qlnvhang.repository.dieuchuyennoibo;

import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBangKeCanHangDtl;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbPhieuXuatKhoDtl;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbPhieuXuatKhoHdr;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DcnbPhieuXuatKhoDtlRepository extends JpaRepository<DcnbPhieuXuatKhoDtl, Long> {

    List<DcnbPhieuXuatKhoDtl> findAllByHdrId(Long hdrId);

    void deleteByHdrId(Long hdrId);

}
