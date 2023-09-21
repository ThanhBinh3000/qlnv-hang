package com.tcdt.qlnvhang.repository.xuathang.suachuahang;

import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScTrinhThamDinhHdr;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface XhScHoSoRepository extends JpaRepository<ScTrinhThamDinhHdr, Long> {

//  @Query("SELECT DISTINCT  c FROM ScTrinhThamDinhHdr c " +
//      " WHERE 1=1 " +
//      "AND (:#{#param.dvql} IS NULL OR c.maDvi LIKE CONCAT(:#{#param.dvql},'%')) " +
//      "AND (:#{#param.nam} IS NULL OR c.nam = :#{#param.nam}) " +
//      "AND (:#{#param.soQd} IS NULL OR LOWER(c.soQd) LIKE CONCAT('%',LOWER(:#{#param.soQd}),'%')) " +
//      "AND (:#{#param.soHoSo} IS NULL OR LOWER(c.soHoSo) LIKE CONCAT('%',LOWER(:#{#param.soHoSo}),'%')) " +
//      "AND (:#{#param.trangThai} IS NULL OR c.trangThai = :#{#param.trangThai}) " +
//      "ORDER BY c.ngaySua desc , c.ngayTao desc, c.id desc"
//  )
//  Page<ScTrinhThamDinhHdr> searchPage(@Param("param") XhTlHoSoRequest param, Pageable pageable);


  void deleteAllByIdIn(List<Long> listId);

  List<ScTrinhThamDinhHdr> findByIdIn(List<Long> ids);

  List<ScTrinhThamDinhHdr> findAllByIdIn(List<Long> listId);

}
