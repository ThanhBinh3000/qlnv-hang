package com.tcdt.qlnvhang.repository.xuathang.xuattheophuongthucdaugia.tochuctrienkhai;

import com.tcdt.qlnvhang.table.xuathang.xuattheophuongthucdaugia.tochuctrienkhai.XhTcTtinBdgKetQua;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface XhTcTtinBdgKetQuaRepository extends JpaRepository<XhTcTtinBdgKetQua, Long> {
  List<XhTcTtinBdgKetQua> findByIdTtin(Long idTtin);
}