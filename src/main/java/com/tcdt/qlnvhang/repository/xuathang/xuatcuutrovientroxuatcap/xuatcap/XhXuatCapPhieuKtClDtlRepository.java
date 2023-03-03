package com.tcdt.qlnvhang.repository.xuathang.xuatcuutrovientroxuatcap.xuatcap;

import com.tcdt.qlnvhang.table.xuathang.xuatcuutrovientroxuatcap.xuatcap.XhXuatCapPhieuKtClDtl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface XhXuatCapPhieuKtClDtlRepository extends JpaRepository<XhXuatCapPhieuKtClDtl,Long> {
  List<XhXuatCapPhieuKtClDtl> findAllByIdHdrIn(List<Long> listId);

  List<XhXuatCapPhieuKtClDtl> findByIdHdr(Long id);
}
