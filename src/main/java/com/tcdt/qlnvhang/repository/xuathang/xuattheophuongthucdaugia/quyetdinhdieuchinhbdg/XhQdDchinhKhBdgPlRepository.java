package com.tcdt.qlnvhang.repository.xuathang.xuattheophuongthucdaugia.quyetdinhdieuchinhbdg;

import com.tcdt.qlnvhang.table.xuathang.xuattheophuongthucdaugia.quyetdinhdieuchinhbdg.XhQdDchinhKhBdgPl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface XhQdDchinhKhBdgPlRepository extends JpaRepository<XhQdDchinhKhBdgPl, Long> {
  List<XhQdDchinhKhBdgPl> findAllByIdQdDtlIn(List<Long> ids);

  List<XhQdDchinhKhBdgPl> findByIdQdDtl(Long idQdDtl);

  void deleteByIdQdDtl(Long idQdDtl);
}