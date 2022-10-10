package com.tcdt.qlnvhang.repository.xuatcuutro;

import com.tcdt.qlnvhang.table.XhDxCuuTroDtl;
import com.tcdt.qlnvhang.table.XhDxCuuTroHdr;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeXuatCuuTroDtlRepository extends JpaRepository<XhDxCuuTroDtl, Long> {
  List<XhDxCuuTroDtl> findByIdDxuat(Long idDeXuat);

  void deleteAllByIdDxuatIn(List<Long> listId);

  void deleteAllByIdIn(List<Long> listId);

  List<XhDxCuuTroDtl> findByIdDxuatIn(List<Long> idDeXuat);

  @Query("SELECT c FROM XhDxCuuTroDtl c WHERE 1=1 " +
      "AND (c.idDxuat IN (SELECT hdr.id FROM XhDxCuuTroHdr hdr where hdr.idTongHop = :idTongHop))"
  )
  List<XhDxCuuTroDtl> findByIdTongHop(Long idTongHop);
}