package com.tcdt.qlnvhang.repository.xuathang.daugia.xuatkho;

import com.tcdt.qlnvhang.entities.xuathang.daugia.xuatkho.XhDgPhieuXuatKho;
import com.tcdt.qlnvhang.request.xuathang.daugia.xuatkho.SearchXhDgPhieuXuatKhoReq;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface XhDgPhieuXuatKhoRepository extends JpaRepository<XhDgPhieuXuatKho, Long> {

  @Query("SELECT c FROM XhDgPhieuXuatKho c WHERE 1=1 " +
      "AND (:#{#param.dvql} IS NULL OR c.maDvi LIKE CONCAT(:#{#param.dvql},'%')) " +
      "AND (:#{#param.nam} IS NULL OR c.nam = :#{#param.nam}) " +
      "AND (:#{#param.loaiVthh} IS NULL OR c.loaiVthh = :#{#param.loaiVthh}) " +
      "AND (:#{#param.soQdGiaoNvXh} IS NULL OR LOWER(c.soQdGiaoNvXh) LIKE CONCAT('%',LOWER(:#{#param.soQdGiaoNvXh}),'%')) " +
      "AND (:#{#param.soPhieuXuatKho} IS NULL OR LOWER(c.soPhieuXuatKho) LIKE CONCAT('%',LOWER(:#{#param.soPhieuXuatKho}),'%')) " +
      "AND ((:#{#param.ngayXuatKhoTu}  IS NULL OR c.ngayXuatKho >= :#{#param.ngayXuatKhoTu})" +
      "AND (:#{#param.ngayXuatKhoDen}  IS NULL OR c.ngayXuatKho <= :#{#param.ngayXuatKhoDen}) ) " +
      "AND (:#{#param.trangThai} IS NULL OR c.trangThai = :#{#param.trangThai}) " +
      "ORDER BY c.ngaySua desc , c.ngayTao desc, c.id desc"
  )
  Page<XhDgPhieuXuatKho> search(@Param("param") SearchXhDgPhieuXuatKhoReq param, Pageable pageable);

  Optional<XhDgPhieuXuatKho> findBySoPhieuXuatKho(String soPhieuXuatKho);

  List<XhDgPhieuXuatKho> findByIdIn(List<Long> ids);

  List<XhDgPhieuXuatKho> findAllByIdIn(List<Long> idList);
}
