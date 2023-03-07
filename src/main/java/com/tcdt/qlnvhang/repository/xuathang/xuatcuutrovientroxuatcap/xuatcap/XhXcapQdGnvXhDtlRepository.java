package com.tcdt.qlnvhang.repository.xuathang.xuatcuutrovientroxuatcap.xuatcap;

import com.tcdt.qlnvhang.table.xuathang.xuatcuutrovientroxuatcap.xuatcap.XhXcapQdGnvXhDtl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface XhXcapQdGnvXhDtlRepository extends JpaRepository<XhXcapQdGnvXhDtl, Long> {
  List<XhXcapQdGnvXhDtl> findByIdHdr(Long id);

  List<XhXcapQdGnvXhDtl> findAllByIdHdrIn(List<Long> listId);

  List<XhXcapQdGnvXhDtl> findAllByIdHdr(Long id);
}
