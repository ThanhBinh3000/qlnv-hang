package com.tcdt.qlnvhang.repository.nhaphangtheoptmtt.hopdong.hopdongphuluc;

import com.tcdt.qlnvhang.request.nhaphangtheoptt.hopdong.hopdongphuluc.HopDongMttHdrReq;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.hopdong.hopdongphuluc.HopDongMttHdr;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface HopDongMttHdrRepository extends JpaRepository<HopDongMttHdr, Long> {
  @Query("SELECT DX from HopDongMttHdr DX WHERE 1 = 1 " +
          "AND (:#{#param.namHd} IS NULL OR DX.namHd = :#{#param.namHd}) " +
          "AND (:#{#param.soHd} IS NULL OR LOWER(DX.soHd) LIKE LOWER(CONCAT(CONCAT('%',:#{#param.soHd}),'%' ) ) )" +
          "AND (:#{#param.tenHd} IS NULL OR LOWER(DX.tenHd) LIKE LOWER(CONCAT(CONCAT('%',:#{#param.tenHd}),'%'))) " +
          "AND (:#{#param.tenDviBan} IS NULL OR LOWER(DX.tenDviBan) LIKE LOWER(CONCAT(CONCAT('%',:#{#param.tenDviBan}),'%'))) " +
          "AND (:#{#param.loaiVthh} IS NULL OR DX.loaiVthh LIKE CONCAT(:#{#param.loaiVthh},'%')) " +
          "AND (:#{#param.trangThai} IS NULL OR DX.trangThai = :#{#param.trangThai}) " +
          "AND (:#{#param.maDvi} IS NULL OR DX.maDvi = :#{#param.maDvi})")
  Page<HopDongMttHdr> searchPage(@Param("param") HopDongMttHdrReq param, Pageable pageable);

  @Query("SELECT DX from HopDongMttHdr DX WHERE 1 = 1 " +
          "AND (:#{#param.namHd} IS NULL OR DX.namHd = :#{#param.namHd}) " +
          "AND (:#{#param.soHd} IS NULL OR LOWER(DX.soHd) LIKE LOWER(CONCAT(CONCAT('%',:#{#param.soHd}),'%' ) ) )" +
          "AND (:#{#param.tenHd} IS NULL OR LOWER(DX.tenHd) LIKE LOWER(CONCAT(CONCAT('%',:#{#param.tenHd}),'%'))) " +
          "AND (:#{#param.tenDviBan} IS NULL OR LOWER(DX.tenDviBan) LIKE LOWER(CONCAT(CONCAT('%',:#{#param.tenDviBan}),'%'))) " +
          "AND (:#{#param.loaiVthh} IS NULL OR DX.loaiVthh LIKE CONCAT(:#{#param.loaiVthh},'%')) " +
          "AND (:#{#param.trangThai} IS NULL OR DX.trangThai = :#{#param.trangThai}) " +
          "AND DX.idQdGiaoNvNh is null " +
          "AND (:#{#param.maDvi} IS NULL OR DX.maDvi = :#{#param.maDvi})")
  Page<HopDongMttHdr> dsTaoQd(@Param("param") HopDongMttHdrReq param, Pageable pageable);

  Optional<HopDongMttHdr> findBySoHd(String soHd);
  Optional<HopDongMttHdr> findByIdKqCgia(Long idDviBan);

  @Transactional
  List<HopDongMttHdr> findAllByIdHd(Long idHd);

  @Transactional
  List<HopDongMttHdr> findAllByIdQdKq(Long idQdKq);

  @Transactional
  List<HopDongMttHdr> findAllByIdQdKqAndTrangThai(Long idQdKq, String trangThai);

  @Transactional
  List<HopDongMttHdr> findAllByIdQdGiaoNvNh(Long idQdGiaoNvuNh);

  @Transactional
  List<HopDongMttHdr> findAllByIdQdGiaoNvNhAndTrangThai(Long idQdKq, String trangThai);

  @Transactional
  List<HopDongMttHdr> findAllByIdQdPdSldd(Long idQdPdKqSldd);

  void deleteAllByIdQdPdSldd(Long idQdPdKqSldd);

  @Transactional
  List<HopDongMttHdr> findByIdIn(Collection<Long> ids);

}