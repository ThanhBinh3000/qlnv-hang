package com.tcdt.qlnvhang.repository.xuatcuutro;

import com.tcdt.qlnvhang.table.XhDxCuuTroHdr;
import com.tcdt.qlnvhang.table.XhDxCuuTroKho;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeXuatCuuTroKhoRepository extends JpaRepository<XhDxCuuTroKho, Long> {
  List<XhDxCuuTroKho> findByIdDxuat(Long idDeXuat);

  List<XhDxCuuTroKho> findByIdDxuatDtl(Long id);

  void deleteAllByIdDxuatIn(List<Long> listId);

  void deleteAllByIdDxuatDtlIn(List<Long> listRemoveId);
}