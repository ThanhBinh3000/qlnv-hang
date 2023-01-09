package com.tcdt.qlnvhang.repository.nhaphangtheoptmtt.hopdong.hopdongphuluc;

import com.tcdt.qlnvhang.request.nhaphangtheoptt.hopdong.hopdongphuluc.HopDongMttHdrReq;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.hopdong.hopdongphuluc.HopDongMttHdr;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface HopDongMttHdrRepository extends JpaRepository<HopDongMttHdr, Long> {
  @Query("SELECT c FROM HopDongMttHdr c WHERE 1=1 " +
      "AND (:#{#param.maDvi} IS NULL OR c.maDvi = :#{#param.maDvi}) " +
      "AND (:#{#param.dvql} IS NULL OR c.maDvi LIKE CONCAT(:#{#param.dvql},'%')) " +
      "AND (:#{#param.nam} IS NULL OR c.nam = :#{#param.nam}) " +
      "AND (:#{#param.soHd} IS NULL OR  LOWER(c.soHd) LIKE CONCAT('%',LOWER(:#{#param.soHd}),'%')) " +
      "AND (:#{#param.tenHd} IS NULL OR  LOWER(c.tenHd) LIKE CONCAT('%',LOWER(:#{#param.tenHd}),'%')) " +
      "AND ((:#{#param.ngayKyTu}  IS NULL OR c.ngayKy >= :#{#param.ngayKyTu})" +
      "AND (:#{#param.ngayKyDen}  IS NULL OR c.ngayKy <= :#{#param.ngayKyDen}) ) " +
      "AND (:#{#param.soQdPdKh}  IS NULL OR LOWER(c.soQdPdKh) LIKE CONCAT('%',LOWER(:#{#param.soQdPdKh}),'%')) " +
      "AND (:#{#param.soQdPdKq}  IS NULL OR LOWER(c.soQdPdKq) LIKE CONCAT('%',LOWER(:#{#param.soQdPdKq}),'%')) " +
      "AND (:#{#param.loaiVthh}  IS NULL OR LOWER(c.loaiVthh) =:#{#param.loaiVthh}) " +
      "AND (:#{#param.cloaiVthh}  IS NULL OR LOWER(c.cloaiVthh) =:#{#param.cloaiVthh}) " +
      "AND (:#{#param.trangThai == null} = true OR c.trangThai in :#{#param.trangThai}) " +
//      "AND ((:#{#param.ngayQdPdKqBdg}  IS NULL OR c.ngayQdPdKqBdg >= :#{#param.ngayQdPdKqBdg}) AND (:#{#param.ngayQdPdKqBdg}  IS NULL OR c.ngayQdPdKqBdg <= :#{#param.ngayQdPdKqBdg}) ) " +
      "ORDER BY c.ngaySua desc , c.ngayTao desc, c.id desc"
  )
  Page<HopDongMttHdr> search(@Param("param") HopDongMttHdrReq param, Pageable pageable);

  void deleteAllByIdIn(List<Long> listId);

  List<HopDongMttHdr> findByIdIn(List<Long> ids);

  List<HopDongMttHdr> findByIdHd(Long idHdr);

  List<HopDongMttHdr> findByIdHdIn(List<Long> idHdr);

  Optional<HopDongMttHdr> findBySoHd(String soHd);

  List<HopDongMttHdr> findAllByIdIn(List<Long> listId);

}