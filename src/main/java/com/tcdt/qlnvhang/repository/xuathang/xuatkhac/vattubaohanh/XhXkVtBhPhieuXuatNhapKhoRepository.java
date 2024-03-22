package com.tcdt.qlnvhang.repository.xuathang.xuatkhac.vattubaohanh;

import com.tcdt.qlnvhang.request.xuathang.xuatkhac.ktvattubaohanh.XhXkVtBhPhieuXuatNhapKhoRequest;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.ktvattubaohanh.XhXkVtBhPhieuXuatNhapKho;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface XhXkVtBhPhieuXuatNhapKhoRepository extends JpaRepository<XhXkVtBhPhieuXuatNhapKho, Long> {

  @Query("SELECT c FROM XhXkVtBhPhieuXuatNhapKho c WHERE 1=1 " +
      "AND (:#{#param.dvql} IS NULL OR c.maDvi LIKE CONCAT(:#{#param.dvql},'%')) " +
      "AND (:#{#param.loai} IS NULL OR c.loai = :#{#param.loai}) " +
      "AND (:#{#param.loaiPhieu} IS NULL OR c.loaiPhieu = :#{#param.loaiPhieu}) " +
      "AND (:#{#param.namKeHoach} IS NULL OR c.namKeHoach = :#{#param.namKeHoach}) " +
      "AND (:#{#param.soLanLm} IS NULL OR c.soLanLm = :#{#param.soLanLm}) " +
      "AND (:#{#param.loaiVthh} IS NULL OR c.loaiVthh = :#{#param.loaiVthh}) " +
      "AND (:#{#param.canCus.size() }  = 0 OR c.idCanCu IN (:#{#param.canCus})) " +
      "AND (:#{#param.soCanCu} IS NULL OR LOWER(c.soCanCu) LIKE CONCAT('%',LOWER(:#{#param.soCanCu}),'%')) " +
      "AND (:#{#param.soPhieu} IS NULL OR LOWER(c.soPhieu) LIKE CONCAT('%',LOWER(:#{#param.soPhieu}),'%')) " +
      "AND ((:#{#param.ngayXuatNhapTu}  IS NULL OR c.ngayXuatNhap >= :#{#param.ngayXuatNhapTu})" +
      "AND (:#{#param.ngayXuatNhapDen}  IS NULL OR c.ngayXuatNhap <= :#{#param.ngayXuatNhapDen}) ) " +
      "AND (:#{#param.trangThai} IS NULL OR c.trangThai = :#{#param.trangThai}) " +
      "ORDER BY c.ngaySua desc , c.ngayTao desc, c.id desc"
  )
  Page<XhXkVtBhPhieuXuatNhapKho> search(@Param("param") XhXkVtBhPhieuXuatNhapKhoRequest param, Pageable pageable);

  Optional<XhXkVtBhPhieuXuatNhapKho> findBySoPhieu(String soPhieu);

  List<XhXkVtBhPhieuXuatNhapKho> findByIdIn(List<Long> ids);

  List<XhXkVtBhPhieuXuatNhapKho> findAllByIdIn(List<Long> idList);

  List<XhXkVtBhPhieuXuatNhapKho> findAllByIdCanCuIn(List<Long> idList);

  List<XhXkVtBhPhieuXuatNhapKho> findAllByIdBbKtNhapKho(Long idBbKtNhapKho);
  List<XhXkVtBhPhieuXuatNhapKho> findAllByIdBbLayMau(Long idBbLayMau);
}
