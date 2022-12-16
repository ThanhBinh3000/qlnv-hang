package com.tcdt.qlnvhang.repository.xuathang.xuattheophuongthucdaugia.quyetdinhdieuchinhbdg;

import com.tcdt.qlnvhang.table.xuathang.xuattheophuongthucdaugia.quyetdinhdieuchinhbdg.XhQdDchinhKhBdgPlDtl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface XhQdDchinhKhBdgPlDtlRepository extends JpaRepository<XhQdDchinhKhBdgPlDtl, Long> {
  List<XhQdDchinhKhBdgPlDtl> findByIdPhanLo(Long idPhanLo);

  List<XhQdDchinhKhBdgPlDtl> findByIdQdHdr(Long id);


  void deleteAllByIdPhanLo(Long idPhanLo);
}