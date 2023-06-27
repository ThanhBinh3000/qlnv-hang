package com.tcdt.qlnvhang.repository.xuathang.xuatkhac;

import com.tcdt.qlnvhang.request.xuathang.thanhlytieuhuy.thanhly.XhTlDanhSachRequest;
import com.tcdt.qlnvhang.request.xuathang.xuatkhac.XhXkDanhSachRequest;
import com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.thanhly.XhTlDanhSachHdr;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.XhXkDanhSachHdr;
import feign.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface XhXkDanhSachRepository extends JpaRepository<XhXkDanhSachHdr, Long> {
  @Query("SELECT c FROM XhXkDanhSachHdr c WHERE 1=1 " +
      "AND (:#{#param.dvql} IS NULL OR c.maDvi LIKE CONCAT(:#{#param.dvql},'%')) " +
      "AND (:#{#param.loaiVthh} IS NULL OR c.loaiVthh = :#{#param.loaiVthh}) " +
      "AND ((:#{#param.ngayDeXuatTu}  IS NULL OR c.ngayDeXuat >= :#{#param.ngayDeXuatTu})" +
      "AND (:#{#param.ngayDeXuatDen}  IS NULL OR c.ngayDeXuat <= :#{#param.ngayDeXuatDen}) ) " +
      "AND (:#{#param.type} IS NULL OR ('TH' = :#{#param.type} AND c.maTongHop IS NULL))" +
      "ORDER BY c.ngaySua desc , c.ngayTao desc, c.id desc"
  )
  Page<XhXkDanhSachHdr> searchPage(@Param("param") XhXkDanhSachRequest param, Pageable pageable);


  void deleteAllByIdIn(List<Long> listId);

  List<XhXkDanhSachHdr> findByIdIn(List<Long> ids);

  Optional<XhXkDanhSachHdr> findByIdTongHop(Long ids);
}
