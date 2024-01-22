package com.tcdt.qlnvhang.repository.xuathang.thanhlytieuhuy.tieuhuy;

import com.tcdt.qlnvhang.request.xuathang.thanhlytieuhuy.thanhly.XhTlHoSoReq;
import com.tcdt.qlnvhang.request.xuathang.thanhlytieuhuy.tieuhuy.XhThHoSoRequest;
import com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.thanhly.XhTlHoSoHdr;
import com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.tieuhuy.XhThHoSoHdr;
import feign.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface XhThHoSoHdrRepository extends JpaRepository<XhThHoSoHdr, Long> {

  @Query("SELECT DISTINCT  c FROM XhThHoSoHdr c " +
      " LEFT JOIN XhThHoSoDtl dtl on c.id = dtl.idHdr " +
      " LEFT JOIN XhThDanhSachHdr ds on dtl.idDsHdr = ds.id " +
      " WHERE 1=1 " +
      "AND (:#{#param.maDviSr} IS NULL OR ds.maDvi LIKE CONCAT(:#{#param.maDviSr},'%')) " +
      "AND (:#{#param.nam} IS NULL OR c.nam = :#{#param.nam}) " +
      "AND (:#{#param.soQdSr} IS NULL OR LOWER(c.soQd) LIKE CONCAT('%',LOWER(:#{#param.soQdSr}),'%')) " +
      "AND (:#{#param.soHoSo} IS NULL OR LOWER(c.soHoSo) LIKE CONCAT('%',LOWER(:#{#param.soHoSo}),'%')) " +
      "AND (:#{#param.soTbSr} IS NULL OR LOWER(c.soTb) LIKE CONCAT('%',LOWER(:#{#param.soTbSr}),'%')) " +
      "AND (:#{#param.trangThai} IS NULL OR c.trangThai = :#{#param.trangThai}) " +
      "AND (:#{#param.trangThaiTc} IS NULL OR c.trangThaiTc = :#{#param.trangThaiTc}) " +
      "ORDER BY c.ngaySua desc , c.ngayTao desc, c.id desc"
  )
  Page<XhThHoSoHdr> searchPage(@Param("param") XhThHoSoRequest param, Pageable pageable);

  @Query(value = "SELECT c FROM XhThHoSoHdr c " +
          " LEFT JOIN XhThQuyetDinhHdr qd on c.id = qd.idHoSo " +
          " WHERE 1 = 1 " +
          " AND qd.id is null " +
          " AND (:#{#param.trangThai} IS NULL OR c.trangThai = :#{#param.trangThai}) " +
          " ORDER BY c.ngaySua desc , c.ngayTao desc, c.id desc "
  )
  List<XhThHoSoHdr> listTaoQuyetDinhTieuHuy(@Param("param") XhThHoSoRequest param);

  @Query(value = "SELECT c FROM XhThHoSoHdr c " +
          " LEFT JOIN XhThThongBaoKq qd on c.id = qd.idHoSo " +
          " WHERE 1 = 1 " +
          " AND qd.id is null " +
          " AND (:#{#param.trangThai} IS NULL OR c.trangThai = :#{#param.trangThai}) " +
          " ORDER BY c.ngaySua desc , c.ngayTao desc, c.id desc "
  )
  List<XhThHoSoHdr> listTaoThongBaoTieuHuy(@Param("param") XhThHoSoRequest param);


  void deleteAllByIdIn(List<Long> listId);

  List<XhThHoSoHdr> findByIdIn( List<Long> ids);

  List<XhThHoSoHdr> findAllByIdIn(List<Long> listId);

  Optional<XhThHoSoHdr> findBySoHoSo(String soQd);
}
