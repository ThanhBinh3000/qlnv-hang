package com.tcdt.qlnvhang.repository.xuathang.xuattheophuongthucdaugia.tochuctrienkhai.thongtin;

import com.tcdt.qlnvhang.table.xuathang.xuattheophuongthucdaugia.tochuctrienkhai.thongtin.XhTcTtinBdgPlo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface XhTcTtinBdgPloRepository extends JpaRepository<XhTcTtinBdgPlo, Long> {
  List<XhTcTtinBdgPlo> findByIdTtinDtl(Long idDtl);

  void deleteAllByIdTtinDtl(Long idTtinDtl);

}