package com.tcdt.qlnvhang.repository.xuatcuutro;

import com.tcdt.qlnvhang.table.XhThCuuTroDtl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TongHopCuuTroDtlRepository extends JpaRepository<XhThCuuTroDtl, Long> {
  List<XhThCuuTroDtl> findByIdTongHop(Long idDeXuat);

  void deleteAllByIdTongHopIn(List<Long> listId);
  void deleteAllByIdIn(List<Long> listId);
}