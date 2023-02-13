package com.tcdt.qlnvhang.repository.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro;
import com.tcdt.qlnvhang.table.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtvtQdGiaoNvXhDtl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface XhCtvtQdGiaoNvXhDtlRepository extends JpaRepository<XhCtvtQdGiaoNvXhDtl,Long> {
  List<XhCtvtQdGiaoNvXhDtl> findByIdHdr(Long id);

  List<XhCtvtQdGiaoNvXhDtl> findAllByIdHdrIn(List<Long> listId);

  List<XhCtvtQdGiaoNvXhDtl> findAllByIdHdr(Long id);
}
