package com.tcdt.qlnvhang.repository.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro;

import com.tcdt.qlnvhang.request.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.SearchXhCtvtBangKeReq;
import com.tcdt.qlnvhang.table.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtvtBangKeHdr;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface XhCtvtBangKeHdrRepository extends JpaRepository<XhCtvtBangKeHdr, Long> {

  @Query("SELECT c FROM XhCtvtBangKeHdr c WHERE 1=1 " +
      "AND (:#{#param.dvql} IS NULL OR c.maDvi LIKE CONCAT(:#{#param.dvql},'%')) " +
      "AND (:#{#param.type} IS NULL OR c.type = :#{#param.type}) " +
      "AND (:#{#param.nam} IS NULL OR c.nam = :#{#param.nam}) " +
//      "AND (:#{#param.loaiVthh} IS NULL OR c.loaiVthh = :#{#param.loaiVthh}) " +
      "AND (:#{#param.loaiVthh} IS NULL OR c.loaiVthh LIKE CONCAT(:#{#param.loaiVthh}, '%')) " +
      "AND (:#{#param.soQdGiaoNvXh} IS NULL OR LOWER(c.soQdGiaoNvXh) LIKE CONCAT('%',LOWER(:#{#param.soQdGiaoNvXh}),'%')) " +
      "AND (:#{#param.soBangKe} IS NULL OR LOWER(c.soBangKe) LIKE CONCAT('%',LOWER(:#{#param.soBangKe}),'%')) " +
      "AND ((:#{#param.ngayXuatTu}  IS NULL OR c.ngayXuat >= :#{#param.ngayXuatTu})" +
      "AND (:#{#param.ngayXuatDen}  IS NULL OR c.ngayXuat <= :#{#param.ngayXuatDen}) ) " +
      "AND ((:#{#param.thoiGianGiaoNhanTu}  IS NULL OR c.thoiGianGiaoNhan >= :#{#param.thoiGianGiaoNhanTu})" +
      "AND (:#{#param.thoiGianGiaoNhanDen}  IS NULL OR c.thoiGianGiaoNhan <= :#{#param.thoiGianGiaoNhanDen}) ) " +
      "AND (:#{#param.trangThai} IS NULL OR c.trangThai = :#{#param.trangThai}) " +
      "ORDER BY c.ngaySua desc , c.ngayTao desc, c.id desc"
  )
  Page<XhCtvtBangKeHdr> search(@Param("param") SearchXhCtvtBangKeReq param, Pageable pageable);

  Optional<XhCtvtBangKeHdr> findBySoBangKe(String str);

  List<XhCtvtBangKeHdr> findByIdIn(List<Long> ids);

  List<XhCtvtBangKeHdr> findAllByIdIn(List<Long> idList);
}
