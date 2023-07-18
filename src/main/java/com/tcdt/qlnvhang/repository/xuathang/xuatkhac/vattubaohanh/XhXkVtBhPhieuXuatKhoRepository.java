package com.tcdt.qlnvhang.repository.xuathang.xuatkhac.vattubaohanh;

import com.tcdt.qlnvhang.request.xuathang.xuatkhac.ktvattubaohanh.XhXkVtBhPhieuXuatKhoRequest;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.ktvattubaohanh.XhXkVtBhPhieuXuatKho;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface XhXkVtBhPhieuXuatKhoRepository extends JpaRepository<XhXkVtBhPhieuXuatKho, Long> {

  @Query("SELECT c FROM XhXkVtBhPhieuXuatKho c WHERE 1=1 " +
      "AND (:#{#param.dvql} IS NULL OR c.maDvi LIKE CONCAT(:#{#param.dvql},'%')) " +
      "AND (:#{#param.loai} IS NULL OR c.loai = :#{#param.loai}) " +
      "AND (:#{#param.namKeHoach} IS NULL OR c.namKeHoach = :#{#param.namKeHoach}) " +
      "AND (:#{#param.loaiVthh} IS NULL OR c.loaiVthh = :#{#param.loaiVthh}) " +
      "AND (:#{#param.soCanCu} IS NULL OR LOWER(c.soCanCu) LIKE CONCAT('%',LOWER(:#{#param.soCanCu}),'%')) " +
      "AND (:#{#param.soPhieu} IS NULL OR LOWER(c.soPhieu) LIKE CONCAT('%',LOWER(:#{#param.soPhieu}),'%')) " +
      "AND ((:#{#param.ngayXuatTu}  IS NULL OR c.ngayXuat >= :#{#param.ngayXuatTu})" +
      "AND (:#{#param.ngayXuatDen}  IS NULL OR c.ngayXuat <= :#{#param.ngayXuatDen}) ) " +
      "AND (:#{#param.trangThai} IS NULL OR c.trangThai = :#{#param.trangThai}) " +
      "ORDER BY c.ngaySua desc , c.ngayTao desc, c.id desc"
  )
  Page<XhXkVtBhPhieuXuatKho> search(@Param("param") XhXkVtBhPhieuXuatKhoRequest param, Pageable pageable);

  Optional<XhXkVtBhPhieuXuatKho> findBySoPhieu(String soPhieu);

  List<XhXkVtBhPhieuXuatKho> findByIdIn(List<Long> ids);

  List<XhXkVtBhPhieuXuatKho> findAllByIdIn(List<Long> idList);
}
