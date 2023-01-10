package com.tcdt.qlnvhang.repository.xuatcuutro;

import com.tcdt.qlnvhang.request.xuatcuutro.XhQdGnvCuuTroHdrSearchReq;
import com.tcdt.qlnvhang.table.XhQdGnvCuuTroHdr;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface XhQdGnvCuuTroHdrRepository extends JpaRepository<XhQdGnvCuuTroHdr, Long> {
  @Query("SELECT c FROM XhQdGnvCuuTroHdr c WHERE 1=1 " +
      "AND (:#{#param.dvql} IS NULL OR c.maDvi LIKE CONCAT(:#{#param.dvql},'%')) " +
      "AND (:#{#param.trangThai} IS NULL OR c.trangThai = :#{#param.trangThai}) " +
      "AND (:#{#param.nam} IS NULL OR c.nam = :#{#param.nam}) " +
      "AND (:#{#param.soQd} IS NULL OR LOWER(c.soQd) LIKE CONCAT('%',LOWER(:#{#param.soQd}),'%')) " +
      "AND (:#{#param.loaiVthh} IS NULL OR LOWER(c.loaiVthh) =:#{#param.loaiVthh}) " +
      "AND (:#{#param.trichYeu} IS NULL OR LOWER(c.trichYeu) LIKE CONCAT('%',LOWER(:#{#param.trichYeu}),'%')) " +
      "AND ((:#{#param.ngayKy} IS NULL OR c.ngayKy >= :#{#param.ngayKy}) AND (:#{#param.ngayKy} IS NULL OR c.ngayKy <= :#{#param.ngayKy}) ) " +
      "ORDER BY c.ngaySua desc , c.ngayTao desc, c.id desc")
  Page<XhQdGnvCuuTroHdr> search(@Param("param") XhQdGnvCuuTroHdrSearchReq param, Pageable pageable);

  void deleteAllByIdIn(List<Long> listId);

  List<XhQdGnvCuuTroHdr> findByIdIn(List<Long> idDxuat);

  Optional<XhQdGnvCuuTroHdr> findFirstBySoQdAndNam(String soQd, int nam);
}