package com.tcdt.qlnvhang.repository.xuathang.xuattheophuongthucdaugia.quyetdinhdieuchinhbdg;

import com.tcdt.qlnvhang.table.xuathang.xuattheophuongthucdaugia.quyetdinhdieuchinhbdg.XhQdDchinhKhBdgDtl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface XhQdDchinhKhBdgDtlRepository extends JpaRepository<XhQdDchinhKhBdgDtl, Long> {
  List<XhQdDchinhKhBdgDtl> findAllByIdQdHdrIn(List<Long> ids);

  List<XhQdDchinhKhBdgDtl> findAllByIdQdHdr(Long idQdHdr);

  void deleteAllByIdQdHdr(Long idQdHdr);
}