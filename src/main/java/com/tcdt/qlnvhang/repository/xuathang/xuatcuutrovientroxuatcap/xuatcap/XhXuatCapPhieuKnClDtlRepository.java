package com.tcdt.qlnvhang.repository.xuathang.xuatcuutrovientroxuatcap.xuatcap;

import com.tcdt.qlnvhang.table.xuathang.xuatcuutrovientroxuatcap.xuatcap.XhXuatCapPhieuKnClDtl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface XhXuatCapPhieuKnClDtlRepository extends JpaRepository<XhXuatCapPhieuKnClDtl,Long> {
  List<XhXuatCapPhieuKnClDtl> findAllByIdHdrIn(List<Long> listId);

  List<XhXuatCapPhieuKnClDtl> findByIdHdr(Long id);
}
