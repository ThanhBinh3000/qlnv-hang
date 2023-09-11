package com.tcdt.qlnvhang.repository.xuathang.kiemtrachatluong;

import com.tcdt.qlnvhang.request.xuathang.kiemtrachatluong.SearchBienBanLayMauReq;
import com.tcdt.qlnvhang.request.xuathang.kiemtrachatluong.XhBienBanLayMauReq;
import com.tcdt.qlnvhang.table.xuathang.kiemtrachatluong.bienbanlaymau.XhBienBanLayMauHdr;
import feign.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface XhBienBanLayMauRepository extends JpaRepository<XhBienBanLayMauHdr, Long> {

  @Query("SELECT c FROM XhBienBanLayMauHdr c " + " WHERE 1=1 " +
      "AND (:#{#param.dvql} IS NULL OR c.maDvi LIKE CONCAT(:#{#param.dvql},'%')) " +
      "AND (:#{#param.nam} IS NULL OR c.nam = :#{#param.nam}) " +
      "AND (:#{#param.soBbQd} IS NULL OR LOWER(c.soBbQd) LIKE CONCAT('%',LOWER(:#{#param.soBbQd}),'%')) " +
      "AND (:#{#param.loaiVthh} IS NULL OR LOWER(c.loaiVthh) LIKE CONCAT(LOWER(:#{#param.loaiVthh}),'%')) " +
      "AND (:#{#param.type} IS NULL OR c.type = :#{#param.type}) " +
//      "AND (:#{#param.soHoSo} IS NULL OR LOWER(c.soHoSo) LIKE CONCAT('%',LOWER(:#{#param.soHoSo}),'%')) " +
//      "AND ((:#{#param.ngayKyTu}  IS NULL OR c.ngayKy >= :#{#param.ngayKyTu})" +
//      "AND (:#{#param.ngayKyDen}  IS NULL OR c.ngayKy <= :#{#param.ngayKyDen}) ) " +
//      "AND (:#{#param.trangThai} IS NULL OR c.trangThai = :#{#param.trangThai}) " +
      "ORDER BY c.ngaySua desc , c.ngayTao desc, c.id desc")
  Page<XhBienBanLayMauHdr> search(@Param("param") SearchBienBanLayMauReq param, Pageable pageable);


  void deleteAllByIdIn(List<Long> listId);

  List<XhBienBanLayMauHdr> findByIdIn(List<Long> ids);

  List<XhBienBanLayMauHdr> findAllByIdIn(List<Long> listId);

  Optional<XhBienBanLayMauHdr> findFirstBySoBbQd(String soQd);
}
