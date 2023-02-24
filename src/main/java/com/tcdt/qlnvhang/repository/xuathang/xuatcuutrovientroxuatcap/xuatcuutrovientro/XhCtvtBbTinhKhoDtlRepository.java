package com.tcdt.qlnvhang.repository.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro;

import com.tcdt.qlnvhang.table.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtvtBbTinhKhoDtl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface XhCtvtBbTinhKhoDtlRepository extends JpaRepository<XhCtvtBbTinhKhoDtl,Long> {
  List<XhCtvtBbTinhKhoDtl> findByIdHdr(Long id);

  List<XhCtvtBbTinhKhoDtl> findAllByIdHdrIn(List<Long> listId);
}
