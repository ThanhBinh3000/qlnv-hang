package com.tcdt.qlnvhang.repository.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro;

import com.tcdt.qlnvhang.table.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtvtPhieuKnClDtl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface XhCtvtPhieuKnClDtlRepository extends JpaRepository<XhCtvtPhieuKnClDtl,Long> {
  List<XhCtvtPhieuKnClDtl> findByIdHdr(Long id);

  List<XhCtvtPhieuKnClDtl> findAllByIdHdrIn(List<Long> listId);
}
