package com.tcdt.qlnvhang.repository.xuathang.suachuahang;

import com.tcdt.qlnvhang.request.suachua.ScTongHopReq;
import com.tcdt.qlnvhang.request.xuathang.thanhlytieuhuy.thanhly.XhTlTongHopRequest;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScTongHopHdr;
import feign.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ScTongHopHdrRepository extends JpaRepository<ScTongHopHdr, Long> {
  @Query(value = "SELECT c FROM ScTongHopHdr c " +
      " WHERE 1 = 1 " +
      " AND (:#{#param.maDviSr} IS NULL OR c.maDvi LIKE CONCAT(:#{#param.maDviSr},'%')) " +
//      " AND (:#{#param.namSr} IS NULL OR c.nam = :#{#param.namSr}) " +
//      " AND ((:#{#param.ngayTu}  IS NULL OR c.ngayTao >= :#{#param.ngayTu}) " +
//      " AND (:#{#param.ngayDen}  IS NULL OR c.ngayTao <= :#{#param.ngayDen})) " +
//      " AND (:#{#param.maDanhSachSr} IS NULL OR LOWER(c.maDanhSach) LIKE CONCAT('%',LOWER(:#{#param.maDanhSachSr}),'%')) " +
//      " AND (:#{#param.trangThai} IS NULL OR c.trangThai = :#{#param.trangThai}) " +
      " ORDER BY c.ngaySua desc , c.ngayTao desc, c.id desc "
  )
  Page<ScTongHopHdr> searchPage(@Param("param") ScTongHopReq param, Pageable pageable);

  void deleteAllByIdIn(List<Long> listId);

  List<ScTongHopHdr> findByIdIn(List<Long> ids);

  Optional<ScTongHopHdr> findByMaDanhSach(String maDs);
}
