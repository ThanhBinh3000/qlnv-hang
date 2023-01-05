package com.tcdt.qlnvhang.repository.xuathang.xuattheophuongthucdaugia.tochuctrienkhai;

import com.tcdt.qlnvhang.table.xuathang.xuattheophuongthucdaugia.tochuctrienkhai.thongtin.XhTcTtinBdgNlq;
import org.springframework.data.jpa.repository.JpaRepository;

public interface XhTcTtinBdgNlqRepository extends JpaRepository<XhTcTtinBdgNlq, Long> {
//  List<XhTcTtinBdgNlq> findByIdTtinDtl(Long idDtl);

  void deleteByIdTtinHdr(Long id);
}