package com.tcdt.qlnvhang.repository.xuathang.suachuahang;

import com.tcdt.qlnvhang.request.xuathang.thanhlytieuhuy.thanhly.XhTlHoSoRequest;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.XhScHoSoHdr;
import feign.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface XhScHoSoRepository extends JpaRepository<XhScHoSoHdr, Long> {

  @Query("SELECT DISTINCT  c FROM XhScHoSoHdr c " +
      " WHERE 1=1 " +
      "AND (:#{#param.dvql} IS NULL OR c.maDvi LIKE CONCAT(:#{#param.dvql},'%')) " +
      "AND (:#{#param.nam} IS NULL OR c.nam = :#{#param.nam}) " +
      "AND (:#{#param.soQd} IS NULL OR LOWER(c.soQd) LIKE CONCAT('%',LOWER(:#{#param.soQd}),'%')) " +
      "AND (:#{#param.soHoSo} IS NULL OR LOWER(c.soHoSo) LIKE CONCAT('%',LOWER(:#{#param.soHoSo}),'%')) " +
      "AND (:#{#param.trangThai} IS NULL OR c.trangThai = :#{#param.trangThai}) " +
      "ORDER BY c.ngaySua desc , c.ngayTao desc, c.id desc"
  )
  Page<XhScHoSoHdr> searchPage(@Param("param") XhTlHoSoRequest param, Pageable pageable);


  void deleteAllByIdIn(List<Long> listId);

  List<XhScHoSoHdr> findByIdIn(List<Long> ids);

  List<XhScHoSoHdr> findAllByIdIn(List<Long> listId);

  Optional<XhScHoSoHdr> findBySoHoSo(String soQd);
}
