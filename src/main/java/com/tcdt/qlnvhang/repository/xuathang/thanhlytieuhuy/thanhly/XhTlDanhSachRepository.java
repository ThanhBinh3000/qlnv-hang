package com.tcdt.qlnvhang.repository.xuathang.thanhlytieuhuy.thanhly;

import com.tcdt.qlnvhang.request.xuathang.thanhlytieuhuy.thanhly.XhTlDanhSachRequest;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScDanhSachHdr;
import com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.thanhly.XhTlDanhSachHdr;
import feign.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface XhTlDanhSachRepository extends JpaRepository<XhTlDanhSachHdr, Long> {
  @Query("SELECT c FROM XhTlDanhSachHdr c WHERE 1=1 " +
      "AND (:#{#param.dvql} IS NULL OR c.maDvi LIKE CONCAT(:#{#param.dvql},'%')) " +
      "AND (:#{#param.loaiVthh} IS NULL OR c.loaiVthh LIKE CONCAT(:#{#param.loaiVthh},'%')) " +
      "AND (:#{#param.cloaiVthh} IS NULL OR c.cloaiVthh LIKE CONCAT(:#{#param.cloaiVthh},'%')) " +
      "AND ((:#{#param.ngayDeXuatTu}  IS NULL OR c.ngayDeXuat >= :#{#param.ngayDeXuatTu})" +
      "AND (:#{#param.ngayDeXuatDen}  IS NULL OR c.ngayDeXuat <= :#{#param.ngayDeXuatDen}) ) " +
      "AND (:#{#param.type} IS NULL OR ('TH' = :#{#param.type} AND c.maTongHop IS NULL))" +
      "ORDER BY c.ngaySua desc , c.ngayTao desc, c.id desc"
  )
  Page<XhTlDanhSachHdr> searchPage(@Param("param") XhTlDanhSachRequest param, Pageable pageable);


  @Query("SELECT c FROM XhTlDanhSachHdr c  " +
          " LEFT JOIN XhTlTongHopDtl th on c.id = th.idDsHdr WHERE 1=1 " +
          " AND th.id is null " +
          " AND (:#{#param.dvql} IS NULL OR c.maDvi LIKE CONCAT(:#{#param.dvql},'%')) " +
          "AND ((:#{#param.thoiGianTlTu}  IS NULL OR c.ngayDeXuat >= :#{#param.thoiGianTlTu}) AND (:#{#param.thoiGianTlDen}  IS NULL OR c.ngayDeXuat <= :#{#param.thoiGianTlDen}) ) " +
          "ORDER BY c.ngaySua desc , c.ngayTao desc, c.id desc"
  )
  List<XhTlDanhSachHdr> listTongHop(@Param("param") XhTlDanhSachRequest param);

  void deleteAllByIdIn(List<Long> listId);

  List<XhTlDanhSachHdr> findByIdIn(List<Long> ids);

  List<XhTlDanhSachHdr> findAllByIdTongHop (Long idTongHop);
}
