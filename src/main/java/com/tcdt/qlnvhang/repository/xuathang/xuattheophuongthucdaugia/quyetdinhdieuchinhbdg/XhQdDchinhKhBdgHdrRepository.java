package com.tcdt.qlnvhang.repository.xuathang.xuattheophuongthucdaugia.quyetdinhdieuchinhbdg;

import com.tcdt.qlnvhang.request.xuathang.xuattheophuongthucdaugia.quyetdinhdieuchinh.XhQdDchinhKhBdgReq;
import com.tcdt.qlnvhang.table.xuathang.xuattheophuongthucdaugia.quyetdinhdieuchinhbdg.XhQdDchinhKhBdgHdr;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface XhQdDchinhKhBdgHdrRepository extends JpaRepository<XhQdDchinhKhBdgHdr, Long> {

  @Query("SELECT c FROM XhQdDchinhKhBdgHdr c where 1 = 1" +
          "AND (:#{#param.maDvi} IS NULL OR c.maDvi = :#{#param.maDvi}) " +
          "AND (:#{#param.nam} IS NULL OR c.nam = :#{#param.nam}) " +
          "AND (:#{#param.loaiVthh } IS NULL OR LOWER(c.loaiVthh) LIKE CONCAT(:#{#param.loaiVthh},'%')) "
  )
  Page<XhQdDchinhKhBdgHdr> searchPage(@Param("param") XhQdDchinhKhBdgReq param, Pageable pageable);


  List<XhQdDchinhKhBdgHdr> findBySoQdDc(String soQdDc);
  List <XhQdDchinhKhBdgHdr> findAllByIdIn(List<Long> ids);
}