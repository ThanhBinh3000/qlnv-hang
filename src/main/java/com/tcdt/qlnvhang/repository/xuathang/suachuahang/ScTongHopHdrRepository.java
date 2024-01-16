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
  @Query(value = "SELECT DISTINCT c FROM ScTongHopHdr c " +
      " LEFT JOIN ScTongHopDtl dtl on c.id = dtl.idHdr " +
      " LEFT JOIN ScDanhSachHdr ds on dtl.idDsHdr = ds.id " +
      " WHERE 1 = 1 " +
      " AND (:#{#param.namSr} IS NULL OR c.nam = :#{#param.namSr}) " +
      "AND ((:#{#param.ngayTu} IS NULL OR c.ngayTao >= :#{#param.ngayTu}) AND (:#{#param.ngayDen}  IS NULL OR c.ngayTao <= :#{#param.ngayDen})) " +
      " AND (:#{#param.dvql} IS NULL OR c.maDvi LIKE CONCAT(:#{#param.dvql},'%')) " +
      " AND (:#{#param.maDviSr} IS NULL OR ds.maDvi LIKE CONCAT(:#{#param.maDviSr},'%')) " +
      " ORDER BY c.ngaySua desc , c.ngayTao desc, c.id desc "
  )
  Page<ScTongHopHdr> searchPage(@Param("param") ScTongHopReq param, Pageable pageable);

  @Query(value = "SELECT c FROM ScTongHopHdr c " +
          " LEFT JOIN ScTrinhThamDinhHdr tr on c.id = tr.idThHdr " +
          " WHERE 1 = 1 " +
          " AND tr.id is null " +
          " AND (:#{#param.trangThai} IS NULL OR c.trangThai = :#{#param.trangThai}) " +
          " AND (:#{#param.maDviSr} IS NULL OR c.maDvi LIKE CONCAT(:#{#param.maDviSr},'%')) " +
          " ORDER BY c.ngaySua desc , c.ngayTao desc, c.id desc "
  )
  List<ScTongHopHdr> listTongHopTrinhThamDinh(@Param("param") ScTongHopReq param);


  void deleteAllByIdIn(List<Long> listId);

  List<ScTongHopHdr> findByIdIn(List<Long> ids);

  Optional<ScTongHopHdr> findByMaDanhSach(String maDs);
}
