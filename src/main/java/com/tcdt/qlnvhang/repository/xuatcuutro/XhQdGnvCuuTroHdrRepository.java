package com.tcdt.qlnvhang.repository.xuatcuutro;

import com.tcdt.qlnvhang.request.xuatcuutro.XhQdGnvCuuTroHdrSearchReq;
import com.tcdt.qlnvhang.table.XhQdGnvCuuTroHdr;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface XhQdGnvCuuTroHdrRepository extends JpaRepository<XhQdGnvCuuTroHdr, Long> {
  @Query("SELECT c FROM XhQdGnvCuuTroHdr c WHERE 1=1 " +
      "ORDER BY c.ngaySua desc , c.ngayTao desc, c.id desc"
  )
  Page<XhQdGnvCuuTroHdr> search(@Param("param") XhQdGnvCuuTroHdrSearchReq param, Pageable pageable);

  void deleteAllByIdIn(List<Long> listId);

  List<XhQdGnvCuuTroHdr> findByIdIn(List<Long> idDxuat);

  Optional<XhQdGnvCuuTroHdr> findFirstBySoQdAndNam(String soQd, int nam);
}