package com.tcdt.qlnvhang.repository.xuathang.thanhlytieuhuy.tieuhuy;

import com.tcdt.qlnvhang.request.xuathang.thanhlytieuhuy.tieuhuy.SearchXhThQuyetDinh;
import com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.tieuhuy.XhThThongBaoKq;
import feign.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface XhThThongBaoKqRepository extends JpaRepository<XhThThongBaoKq, Long> {

  @Query("SELECT   c FROM XhThThongBaoKq c " +
      " LEFT JOIN XhThHoSoHdr hdr on c.idHoSo = hdr.id " +
      " LEFT JOIN XhThHoSoDtl dtl on hdr.id = dtl.idHdr " +
      " LEFT JOIN XhThDanhSachHdr ds on dtl.idDsHdr = ds.id " +
      " WHERE 1=1 " +
      "AND (:#{#param.dvql} IS NULL OR ds.maDvi LIKE CONCAT(:#{#param.dvql},'%')) " +
      "AND (:#{#param.nam} IS NULL OR c.nam = :#{#param.nam}) " +
      "AND (:#{#param.soThongBao} IS NULL OR LOWER(c.soThongBao) LIKE CONCAT('%',LOWER(:#{#param.soThongBao}),'%')) " +
      "AND (:#{#param.soHoSo} IS NULL OR LOWER(c.soHoSo) LIKE CONCAT('%',LOWER(:#{#param.soHoSo}),'%')) " +
      "AND (:#{#param.trangThai} IS NULL OR c.trangThai = :#{#param.trangThai}) " +
      "ORDER BY c.ngaySua desc , c.ngayTao desc, c.id desc"
  )
  Page<XhThThongBaoKq> search(@Param("param") SearchXhThQuyetDinh param, Pageable pageable);

  void deleteAllByIdIn(List<Long> listId);

  List<XhThThongBaoKq> findByIdIn(List<Long> ids);

  List<XhThThongBaoKq> findAllByIdIn(List<Long> listId);

  Optional<XhThThongBaoKq> findBySoThongBao(String soQd);
}
