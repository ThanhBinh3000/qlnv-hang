package com.tcdt.qlnvhang.repository.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro;

import com.tcdt.qlnvhang.table.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtvtBbHaoDoiDtl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface XhCtvtBbHaoDoiDtlRepository extends JpaRepository<XhCtvtBbHaoDoiDtl,Long> {
  List<XhCtvtBbHaoDoiDtl> findByIdHdr(Long id);

  List<XhCtvtBbHaoDoiDtl> findAllByIdHdrIn(List<Long> listId);
}
