package com.tcdt.qlnvhang.repository.xuathang.thanhlytieuhuy.tieuhuy;

import com.tcdt.qlnvhang.request.xuathang.thanhlytieuhuy.thanhly.XhTlDanhSachRequest;
import com.tcdt.qlnvhang.request.xuathang.thanhlytieuhuy.tieuhuy.XhThDanhSachReq;
import com.tcdt.qlnvhang.request.xuathang.thanhlytieuhuy.tieuhuy.XhThTongHopRequest;
import com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.thanhly.XhTlDanhSachHdr;
import com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.tieuhuy.XhThDanhSachHdr;
import feign.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface XhThDanhSachRepository extends JpaRepository<XhThDanhSachHdr, Long> {
  @Query("SELECT c FROM XhThDanhSachHdr c WHERE 1=1 " +
      "AND (:#{#param.dvql} IS NULL OR c.maDvi LIKE CONCAT(:#{#param.dvql},'%')) " +
      "AND (:#{#param.loaiVthh} IS NULL OR c.loaiVthh = :#{#param.loaiVthh}) " +
      "AND ((:#{#param.ngayDeXuatTu}  IS NULL OR c.ngayDeXuat >= :#{#param.ngayDeXuatTu})" +
      "AND (:#{#param.ngayDeXuatDen}  IS NULL OR c.ngayDeXuat <= :#{#param.ngayDeXuatDen}) ) " +
      "AND (:#{#param.type} IS NULL OR ('TH' = :#{#param.type} AND c.maTongHop IS NULL))" +
      "ORDER BY c.ngaySua desc , c.ngayTao desc, c.id desc"
  )
  Page<XhThDanhSachHdr> searchPage(@Param("param") XhThDanhSachReq param, Pageable pageable);


  @Query(" SELECT c FROM XhThDanhSachHdr c  " +
          " LEFT JOIN XhThTongHopDtl th on c.id = th.idDsHdr WHERE 1=1 " +
          " AND th.id is null " +
          " AND (:#{#param.maDviSr} IS NULL OR c.maDvi LIKE CONCAT(:#{#param.maDviSr},'%')) " +
          " AND ((:#{#param.thoiGianThTu}  IS NULL OR c.ngayDeXuat >= :#{#param.thoiGianThTu}) AND (:#{#param.thoiGianThDen}  IS NULL OR c.ngayDeXuat <= :#{#param.thoiGianThDen}) ) " +
          " ORDER BY c.ngaySua desc , c.ngayTao desc, c.id desc"
  )
  List<XhThDanhSachHdr> listTongHop(@Param("param") XhThDanhSachReq param);

  List<XhThDanhSachHdr> findAllByIdTongHop(Long idTongHop);


  void deleteAllByIdIn(List<Long> listId);

  List<XhThDanhSachHdr> findByIdIn(List<Long> ids);
}
