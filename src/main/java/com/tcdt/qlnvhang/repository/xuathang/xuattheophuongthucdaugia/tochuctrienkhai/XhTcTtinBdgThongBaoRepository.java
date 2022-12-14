package com.tcdt.qlnvhang.repository.xuathang.xuattheophuongthucdaugia.tochuctrienkhai;

import com.tcdt.qlnvhang.table.xuathang.xuattheophuongthucdaugia.tochuctrienkhai.XhTcTtinBdgThongBao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface XhTcTtinBdgThongBaoRepository extends JpaRepository<XhTcTtinBdgThongBao, Long> {
  List<XhTcTtinBdgThongBao> findByIdTtin(Long idTtin);
}