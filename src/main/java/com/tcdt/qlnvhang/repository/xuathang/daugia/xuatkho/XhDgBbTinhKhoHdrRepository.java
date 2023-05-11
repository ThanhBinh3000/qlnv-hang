package com.tcdt.qlnvhang.repository.xuathang.daugia.xuatkho;

import com.tcdt.qlnvhang.entities.xuathang.daugia.xuatkho.XhDgBbTinhKhoHdr;
import com.tcdt.qlnvhang.request.xuathang.daugia.xuatkho.SearchXhDgBbTinhKho;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface XhDgBbTinhKhoHdrRepository extends JpaRepository<XhDgBbTinhKhoHdr,Long> {
  
  @Query("SELECT DISTINCT c FROM XhDgBbTinhKhoHdr c  LEFT JOIN c.listPhieuXuatKho e" +
      " WHERE 1=1 " +
      "AND (:#{#param.dvql} IS NULL OR c.maDvi LIKE CONCAT(:#{#param.dvql},'%')) " +
      "AND (:#{#param.type} IS NULL OR c.type = :#{#param.type}) " +
      "AND (:#{#param.nam} IS NULL OR c.nam = :#{#param.nam}) " +
      "AND (:#{#param.loaiVthh} IS NULL OR c.loaiVthh = :#{#param.loaiVthh}) " +
      "AND (:#{#param.soQdGiaoNvXh} IS NULL OR LOWER(c.soQdGiaoNvXh) LIKE CONCAT('%',LOWER(:#{#param.soQdGiaoNvXh}),'%')) " +
      "AND (:#{#param.soBbTinhKho} IS NULL OR LOWER(e.soPhieuXuatKho) LIKE CONCAT('%',LOWER(:#{#param.soBbTinhKho}),'%')) " +
      "AND ((:#{#param.ngayBatDauXuatTu}  IS NULL OR c.ngayBatDauXuat >= :#{#param.ngayBatDauXuatTu})" +
      "AND (:#{#param.ngayBatDauXuatDen}  IS NULL OR c.ngayBatDauXuat <= :#{#param.ngayBatDauXuatDen}) ) " +
      "AND ((:#{#param.ngayKetThucXuatTu}  IS NULL OR c.ngayKetThucXuat >= :#{#param.ngayKetThucXuatTu})" +
      "AND (:#{#param.ngayKetThucXuatDen}  IS NULL OR c.ngayKetThucXuat <= :#{#param.ngayKetThucXuatDen}) ) " +
      "AND (:#{#param.trangThai} IS NULL OR c.trangThai = :#{#param.trangThai}) " +
      "ORDER BY c.ngaySua desc , c.ngayTao desc, c.id desc"
  )
  Page<XhDgBbTinhKhoHdr> search(@Param("param") SearchXhDgBbTinhKho param, Pageable pageable);

  Optional<XhDgBbTinhKhoHdr> findBySoBbTinhKho(String soBbTinhKho);

  List<XhDgBbTinhKhoHdr> findByIdIn(List<Long> ids);

  List<XhDgBbTinhKhoHdr> findAllByIdIn(List<Long> idList);
}
