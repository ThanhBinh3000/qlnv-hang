package com.tcdt.qlnvhang.repository.xuathang.xuattheophuongthucdaugia.tochuctrienkhai.thongtin;

import com.tcdt.qlnvhang.table.xuathang.xuattheophuongthucdaugia.tochuctrienkhai.thongtin.XhTcTtinBdgNlq;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface XhTcTtinBdgNlqRepository extends JpaRepository<XhTcTtinBdgNlq, Long> {

  List<XhTcTtinBdgNlq> findByIdTtinHdr(Long idTtinHdr);

  void deleteByIdTtinHdr(Long id);
}