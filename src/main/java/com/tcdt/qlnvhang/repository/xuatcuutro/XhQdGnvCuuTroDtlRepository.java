package com.tcdt.qlnvhang.repository.xuatcuutro;

import com.tcdt.qlnvhang.table.XhQdGnvCuuTroDtl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface XhQdGnvCuuTroDtlRepository extends JpaRepository<XhQdGnvCuuTroDtl, Long> {
  List<XhQdGnvCuuTroDtl> findByIdHdr(Long idHdr);
  List<XhQdGnvCuuTroDtl> findByIdHdrIn(List<Long> idHdr);
}