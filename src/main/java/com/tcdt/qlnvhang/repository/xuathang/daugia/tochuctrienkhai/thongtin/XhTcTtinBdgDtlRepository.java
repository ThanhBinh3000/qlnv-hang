package com.tcdt.qlnvhang.repository.xuathang.daugia.tochuctrienkhai.thongtin;

import com.tcdt.qlnvhang.entities.xuathang.daugia.tochuctrienkhai.thongtin.XhTcTtinBdgDtl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface XhTcTtinBdgDtlRepository extends JpaRepository<XhTcTtinBdgDtl, Long> {
  List<XhTcTtinBdgDtl> findByIdTtinHdr(Long idTtin);

  void deleteAllByIdTtinHdr(Long idTtinHdr);
}