package com.tcdt.qlnvhang.repository.xuathang.xuatcuutrovientroxuatcap.xuatcap;

import com.tcdt.qlnvhang.table.xuathang.xuatcuutrovientroxuatcap.xuatcap.XhXuatCapQdGiaoNvXhDtl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface XhXuatCapQdGiaoNvXhDtlRepository extends JpaRepository<XhXuatCapQdGiaoNvXhDtl, Long> {
  List<XhXuatCapQdGiaoNvXhDtl> findByIdHdr(Long id);

  List<XhXuatCapQdGiaoNvXhDtl> findAllByIdHdrIn(List<Long> listId);

  List<XhXuatCapQdGiaoNvXhDtl> findAllByIdHdr(Long id);
}
