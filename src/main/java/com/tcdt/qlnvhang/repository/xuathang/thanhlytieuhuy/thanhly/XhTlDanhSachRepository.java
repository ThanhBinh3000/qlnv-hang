package com.tcdt.qlnvhang.repository.xuathang.thanhlytieuhuy.thanhly;

import com.tcdt.qlnvhang.request.xuathang.thanhlytieuhuy.thanhly.XhTlDanhSachRequest;
import com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.thanhly.XhTlDanhSachHdr;
import feign.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface XhTlDanhSachRepository extends JpaRepository<XhTlDanhSachHdr,Long> {
  @Query("SELECT c FROM XhCtvtQdGiaoNvXhHdr c WHERE 1=1 " +
      "AND (:#{#param.dvql} IS NULL OR c.maDvi LIKE CONCAT(:#{#param.dvql},'%')) " +
      "AND (:#{#param.type} IS NULL OR c.type = :#{#param.type}) " +
      "AND (:#{#param.nam} IS NULL OR c.nam = :#{#param.nam}) " +
      "AND (:#{#param.loaiVthh} IS NULL OR c.loaiVthh = :#{#param.loaiVthh}) " +
      "AND (:#{#param.soQd} IS NULL OR LOWER(c.soQd) LIKE CONCAT('%',LOWER(:#{#param.soQd}),'%')) " +
      "AND (:#{#param.trichYeu} IS NULL OR LOWER(c.trichYeu) LIKE CONCAT('%',LOWER(:#{#param.trichYeu}),'%')) " +
      "AND ((:#{#param.ngayKyTu}  IS NULL OR c.ngayKy >= :#{#param.ngayKyTu})" +
      "AND (:#{#param.ngayKyDen}  IS NULL OR c.ngayKy <= :#{#param.ngayKyDen}) ) " +
      "AND (:#{#param.trangThai} IS NULL OR c.trangThai = :#{#param.trangThai}) " +
      "AND (:#{#param.listTrangThaiXh.size() }  = 0 OR c.trangThaiXh in :#{#param.listTrangThaiXh}) " +
//      "AND (:#{#param.listTrangThai.size() } = 0 OR c.trangThai in :#{#param.listTrangThai}) " +
      "ORDER BY c.ngaySua desc , c.ngayTao desc, c.id desc"
  )
  Page<XhTlDanhSachHdr> searchPage (@Param("param") XhTlDanhSachRequest param, Pageable pageable);


  void deleteAllByIdIn(List<Long> listId);

  List<XhTlDanhSachHdr> findByIdIn(List<Long> ids);

  Optional<XhTlDanhSachHdr> findByMaDanhSach(String maDs);
}
