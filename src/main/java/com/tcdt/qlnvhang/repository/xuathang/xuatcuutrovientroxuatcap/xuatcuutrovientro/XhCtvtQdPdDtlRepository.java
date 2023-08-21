package com.tcdt.qlnvhang.repository.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro;


import com.tcdt.qlnvhang.table.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtvtQuyetDinhPdDtl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface XhCtvtQdPdDtlRepository extends JpaRepository<XhCtvtQuyetDinhPdDtl, Long> {
  List<XhCtvtQuyetDinhPdDtl> findByXhCtvtQuyetDinhPdHdrId(Long id);

  List<XhCtvtQuyetDinhPdDtl> findByXhCtvtQuyetDinhPdHdrIdIn(List<Long> listId);
}
