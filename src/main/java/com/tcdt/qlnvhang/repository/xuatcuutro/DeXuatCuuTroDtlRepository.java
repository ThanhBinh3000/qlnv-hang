package com.tcdt.qlnvhang.repository.xuatcuutro;

import com.tcdt.qlnvhang.table.XhDxCuuTroDtl;
import com.tcdt.qlnvhang.table.XhDxCuuTroHdr;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeXuatCuuTroDtlRepository extends JpaRepository<XhDxCuuTroDtl, Long> {
  List<XhDxCuuTroDtl> findByIdDxuat(Long idDeXuat);
}