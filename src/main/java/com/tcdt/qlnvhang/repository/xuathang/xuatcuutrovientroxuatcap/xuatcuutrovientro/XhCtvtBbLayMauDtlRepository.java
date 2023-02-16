package com.tcdt.qlnvhang.repository.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro;

import com.tcdt.qlnvhang.table.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtvtBbLayMauDtl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface XhCtvtBbLayMauDtlRepository extends JpaRepository<XhCtvtBbLayMauDtl,Long> {
  List<XhCtvtBbLayMauDtl> findByIdHdr(Long id);

  List<XhCtvtBbLayMauDtl> findAllByIdHdrIn(List<Long> listId);
}
