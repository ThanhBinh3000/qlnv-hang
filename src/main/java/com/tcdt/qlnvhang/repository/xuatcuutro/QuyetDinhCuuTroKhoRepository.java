package com.tcdt.qlnvhang.repository.xuatcuutro;

import com.tcdt.qlnvhang.table.XhQdCuuTroKho;
import com.tcdt.qlnvhang.table.XhThCuuTroKho;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuyetDinhCuuTroKhoRepository extends JpaRepository<XhQdCuuTroKho, Long> {
/*  List<XhThCuuTroKho> findByIdTongHop(Long idDeXuat);

  List<XhThCuuTroKho> findByIdTongHopDtl(Long id);

  void deleteAllByIdTongHopIn(List<Long> listId);

  void deleteAllByIdTongHopDtlIn(List<Long> listRemoveId);*/
}