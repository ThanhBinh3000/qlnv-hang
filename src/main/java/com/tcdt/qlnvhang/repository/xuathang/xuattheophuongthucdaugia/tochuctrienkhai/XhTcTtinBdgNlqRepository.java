package com.tcdt.qlnvhang.repository.xuathang.xuattheophuongthucdaugia.tochuctrienkhai;

import com.tcdt.qlnvhang.table.xuathang.xuattheophuongthucdaugia.tochuctrienkhai.XhTcTtinBdgKetQua;
import com.tcdt.qlnvhang.table.xuathang.xuattheophuongthucdaugia.tochuctrienkhai.XhTcTtinBdgNlq;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface XhTcTtinBdgNlqRepository extends JpaRepository<XhTcTtinBdgNlq, Long> {
  List<XhTcTtinBdgNlq> findByIdTtinDtl(Long idDtl);

  void deleteByIdTtin(Long id);
}