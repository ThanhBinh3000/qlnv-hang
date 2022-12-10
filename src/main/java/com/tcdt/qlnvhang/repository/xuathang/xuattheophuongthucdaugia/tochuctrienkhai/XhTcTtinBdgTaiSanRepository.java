package com.tcdt.qlnvhang.repository.xuathang.xuattheophuongthucdaugia.tochuctrienkhai;

import com.tcdt.qlnvhang.table.xuathang.xuattheophuongthucdaugia.tochuctrienkhai.XhTcTtinBdgTaiSan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface XhTcTtinBdgTaiSanRepository extends JpaRepository<XhTcTtinBdgTaiSan, Long> {
  List<XhTcTtinBdgTaiSan> findByIdTtinDtl(Long idDtl);

  void deleteByIdTtinHdr(Long id);
}