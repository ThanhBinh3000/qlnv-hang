package com.tcdt.qlnvhang.repository.xuathang.daugia.xuatkho;


import com.tcdt.qlnvhang.entities.xuathang.daugia.xuatkho.XhDgBbHaoDoiHdr;
import com.tcdt.qlnvhang.request.xuathang.daugia.xuatkho.SearchXhDgBbHaoDoi;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface XhDgBbHaoDoiHdrRepository extends JpaRepository<XhDgBbHaoDoiHdr,Long> {

  @Query("SELECT DISTINCT c FROM XhDgBbHaoDoiHdr c  WHERE 1=1 " +
      "AND (:#{#param.dvql} IS NULL OR c.maDvi LIKE CONCAT(:#{#param.dvql},'%')) " +
      "AND (:#{#param.type} IS NULL OR c.type = :#{#param.type}) " +
      "AND (:#{#param.nam} IS NULL OR c.nam = :#{#param.nam}) " +
      "AND (:#{#param.loaiVthh} IS NULL OR c.loaiVthh = :#{#param.loaiVthh}) " +
      "AND (:#{#param.soQdGiaoNvXh} IS NULL OR LOWER(c.soQdGiaoNvXh) LIKE CONCAT('%',LOWER(:#{#param.soQdGiaoNvXh}),'%')) " +
      "AND ((:#{#param.ngayTaoBbTu}  IS NULL OR c.ngayTaoBb >= :#{#param.ngayTaoBbTu})" +
      "AND (:#{#param.ngayTaoBbDen}  IS NULL OR c.ngayTaoBb <= :#{#param.ngayTaoBbDen}) ) " +
      "AND ((:#{#param.ngayBatDauXuatTu}  IS NULL OR c.ngayBatDauXuat >= :#{#param.ngayBatDauXuatTu})" +
      "AND (:#{#param.ngayBatDauXuatDen}  IS NULL OR c.ngayBatDauXuat <= :#{#param.ngayBatDauXuatDen}) ) " +
      "AND ((:#{#param.ngayKetThucXuatTu}  IS NULL OR c.ngayKetThucXuat >= :#{#param.ngayKetThucXuatTu})" +
      "AND (:#{#param.ngayKetThucXuatDen}  IS NULL OR c.ngayKetThucXuat <= :#{#param.ngayKetThucXuatDen}) ) " +
      "AND ((:#{#param.ngayQdGiaoNvXhTu}  IS NULL OR c.ngayQdGiaoNvXh >= :#{#param.ngayQdGiaoNvXhTu})" +
      "AND (:#{#param.ngayQdGiaoNvXhDen}  IS NULL OR c.ngayQdGiaoNvXh <= :#{#param.ngayQdGiaoNvXhDen}) ) " +
      "AND (:#{#param.trangThai} IS NULL OR c.trangThai = :#{#param.trangThai}) " +
      "ORDER BY c.ngaySua desc , c.ngayTao desc, c.id desc"
  )
  Page<XhDgBbHaoDoiHdr> search(@Param("param") SearchXhDgBbHaoDoi param, Pageable pageable);

  
  List<XhDgBbHaoDoiHdr> findByIdIn(List<Long> ids);

  Optional<XhDgBbHaoDoiHdr> findBySoBbHaoDoi(String soBbHaoDoi);

  List<XhDgBbHaoDoiHdr> findAllByIdIn(List<Long> idList);
}
