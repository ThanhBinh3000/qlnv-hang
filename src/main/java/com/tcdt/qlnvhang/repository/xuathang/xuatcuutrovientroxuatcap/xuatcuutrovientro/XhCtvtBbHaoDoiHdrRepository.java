package com.tcdt.qlnvhang.repository.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro;


import com.tcdt.qlnvhang.request.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.SearchXhCtvtBbHaoDoi;
import com.tcdt.qlnvhang.table.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtvtBbHaoDoiHdr;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface XhCtvtBbHaoDoiHdrRepository extends JpaRepository<XhCtvtBbHaoDoiHdr,Long> {

  @Query("SELECT DISTINCT c FROM XhCtvtBbHaoDoiHdr c  WHERE 1=1 " +
      "AND (:#{#param.dvql} IS NULL OR c.maDvi LIKE CONCAT(:#{#param.dvql},'%')) " +
      "AND (:#{#param.type} IS NULL OR c.type = :#{#param.type}) " +
      "AND (:#{#param.nam} IS NULL OR c.nam = :#{#param.nam}) " +
//      "AND (:#{#param.loaiVthh} IS NULL OR c.loaiVthh = :#{#param.loaiVthh}) " +
      "AND (:#{#param.loaiVthh} IS NULL OR c.loaiVthh LIKE CONCAT(:#{#param.loaiVthh}, '%')) " +
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
  Page<XhCtvtBbHaoDoiHdr> search(@Param("param") SearchXhCtvtBbHaoDoi param, Pageable pageable);

  
  List<XhCtvtBbHaoDoiHdr> findByIdIn(List<Long> ids);

  Optional<XhCtvtBbHaoDoiHdr> findBySoBbHaoDoi(String soBbHaoDoi);

  List<XhCtvtBbHaoDoiHdr> findAllByIdIn(List<Long> idList);
}
