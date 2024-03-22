package com.tcdt.qlnvhang.repository.xuathang.kiemtrachatluong;

import com.tcdt.qlnvhang.request.xuathang.kiemtrachatluong.SearchPhieuKnclReq;
import com.tcdt.qlnvhang.table.xuathang.kiemtrachatluong.phieukncl.XhPhieuKnclHdr;
import feign.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface XhPhieuKnclRepository extends JpaRepository<XhPhieuKnclHdr, Long> {

  @Query("SELECT c FROM XhPhieuKnclHdr c " + " WHERE 1=1 " +
      "AND (:#{#param.dvql} IS NULL OR c.maDvi LIKE CONCAT(:#{#param.dvql},'%')) " +
      "AND (:#{#param.nam} IS NULL OR c.nam = :#{#param.nam}) " +
      "AND (:#{#param.soBbQd} IS NULL OR LOWER(c.soBbQd) LIKE CONCAT('%',LOWER(:#{#param.soBbQd}),'%')) " +
      "AND (:#{#param.loaiVthh} IS NULL OR LOWER(c.loaiVthh) LIKE CONCAT(LOWER(:#{#param.loaiVthh}),'%')) " +
      "AND (:#{#param.idQdGnv} IS NULL OR c.idQdGnv = :#{#param.idQdGnv}) " +
      "AND (:#{#param.soQdGnv} IS NULL OR LOWER(c.soQdGnv) LIKE CONCAT('%',LOWER(:#{#param.idQdGnv}),'%')) " +
      "AND (:#{#param.type} IS NULL OR c.type = :#{#param.type}) " +
//      "AND (:#{#param.soHoSo} IS NULL OR LOWER(c.soHoSo) LIKE CONCAT('%',LOWER(:#{#param.soHoSo}),'%')) " +
//      "AND ((:#{#param.ngayKyTu}  IS NULL OR c.ngayKy >= :#{#param.ngayKyTu})" +
//      "AND (:#{#param.ngayKyDen}  IS NULL OR c.ngayKy <= :#{#param.ngayKyDen}) ) " +
//      "AND (:#{#param.trangThai} IS NULL OR c.trangThai = :#{#param.trangThai}) " +
      "ORDER BY c.ngaySua desc , c.ngayTao desc, c.id desc")
  Page<XhPhieuKnclHdr> search(@Param("param") SearchPhieuKnclReq param, Pageable pageable);


  void deleteAllByIdIn(List<Long> listId);

  List<XhPhieuKnclHdr> findByIdIn(List<Long> ids);

  List<XhPhieuKnclHdr> findAllByIdIn(List<Long> listId);

  Optional<XhPhieuKnclHdr> findBySoBbQd(String soQd);
}
