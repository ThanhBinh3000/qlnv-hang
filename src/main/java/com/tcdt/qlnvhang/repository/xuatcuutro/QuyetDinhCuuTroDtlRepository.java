package com.tcdt.qlnvhang.repository.xuatcuutro;

import com.tcdt.qlnvhang.table.XhQdCuuTroDtl;
import com.tcdt.qlnvhang.table.XhThCuuTroDtl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuyetDinhCuuTroDtlRepository extends JpaRepository<XhQdCuuTroDtl, Long> {
  List<XhThCuuTroDtl> findByIdTongHop(Long idDeXuat);

  void deleteAllByIdTongHopIn(List<Long> listId);

  void deleteAllByIdIn(List<Long> listId);


  List<XhQdCuuTroDtl> findByIdQd(Long id);

  void deleteAllByIdQdIn(List<Long> listId);
}