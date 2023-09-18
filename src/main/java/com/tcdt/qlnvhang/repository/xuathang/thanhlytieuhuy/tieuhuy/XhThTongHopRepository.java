package com.tcdt.qlnvhang.repository.xuathang.thanhlytieuhuy.tieuhuy;

import com.tcdt.qlnvhang.request.suachua.ScTrinhThamDinhHdrReq;
import com.tcdt.qlnvhang.request.xuathang.thanhlytieuhuy.tieuhuy.XhThTongHopRequest;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScTrinhThamDinhHdr;
import com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.tieuhuy.XhThTongHopHdr;
import feign.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface XhThTongHopRepository extends JpaRepository<XhThTongHopHdr, Long> {
  @Query("SELECT distinct c FROM XhThTongHopHdr c left join c.tongHopDtl h WHERE 1=1 " +
      "AND (:#{#param.dvql} IS NULL OR c.maDvi LIKE CONCAT(:#{#param.dvql},'%')) " +
      "AND (:#{#param.nam} IS NULL OR c.nam = :#{#param.nam}) " +
      "AND (:#{#param.maDanhSach} IS NULL OR LOWER(c.maDanhSach) LIKE CONCAT('%',LOWER(:#{#param.maDanhSach}),'%')) " +
      "AND (:#{#param.maCuc} IS NULL OR substring(h.maDiaDiem,0,6) = :#{#param.maCuc}) " +
      "AND (:#{#param.maChiCuc} IS NULL OR substring(h.maDiaDiem,0,8) = :#{#param.maChiCuc}) " +
      "AND ((:#{#param.ngayTaoTu}  IS NULL OR c.ngayTao >= :#{#param.ngayTaoTu})" +
      "AND  (:#{#param.ngayTaoDen}  IS NULL OR c.ngayTao <= :#{#param.ngayTaoDen})) " +
      "AND (:#{#param.trangThai} IS NULL OR c.trangThai = :#{#param.trangThai}) " +
      "ORDER BY c.ngaySua desc , c.ngayTao desc, c.id desc"
  )
  Page<XhThTongHopHdr> searchPage(@Param("param") XhThTongHopRequest param, Pageable pageable);

  @Query(value = "SELECT c FROM XhThTongHopHdr c " +
          " LEFT JOIN XhThHoSoHdr sc on c.id = sc.idDanhSach " +
          " WHERE 1 = 1 " +
          " AND sc.id is null " +
          " AND (:#{#param.trangThai} IS NULL OR c.trangThai = :#{#param.trangThai}) " +
          " AND (:#{#param.dvql} IS NULL OR c.maDvi LIKE CONCAT(:#{#param.dvql},'%')) " +
          " ORDER BY c.ngaySua desc , c.ngayTao desc, c.id desc "
  )
  List<XhThTongHopHdr> listTongHongTaoHoSo(@Param("param") XhThTongHopRequest param);


  void deleteAllByIdIn(List<Long> listId);

  List<XhThTongHopHdr> findByIdIn(List<Long> ids);

  Optional<XhThTongHopHdr> findByMaDanhSach(String maDs);
}
