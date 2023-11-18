package com.tcdt.qlnvhang.repository.xuathang.thanhlytieuhuy.thanhly;

import com.tcdt.qlnvhang.request.suachua.ScTrinhThamDinhHdrReq;
import com.tcdt.qlnvhang.request.xuathang.thanhlytieuhuy.thanhly.XhTlHoSoReq;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScTrinhThamDinhHdr;
import com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.thanhly.XhTlHoSoHdr;
import feign.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface XhTlHoSoHdrRepository extends JpaRepository<XhTlHoSoHdr, Long> {

  @Query("SELECT DISTINCT  c FROM XhTlHoSoHdr c " +
      " WHERE 1=1 " +
      "AND (:#{#param.dvql} IS NULL OR c.maDvi LIKE CONCAT(:#{#param.dvql},'%')) " +
      "AND (:#{#param.nam} IS NULL OR c.nam = :#{#param.nam}) " +
      "AND (:#{#param.soQd} IS NULL OR LOWER(c.soQd) LIKE CONCAT('%',LOWER(:#{#param.soQd}),'%')) " +
      "AND (:#{#param.soHoSo} IS NULL OR LOWER(c.soHoSo) LIKE CONCAT('%',LOWER(:#{#param.soHoSo}),'%')) " +
      "AND (:#{#param.soTb} IS NULL OR LOWER(c.soTb) LIKE CONCAT('%',LOWER(:#{#param.soTb}),'%')) " +
      "AND ((:#{#param.ngayTuCuc}  IS NULL OR c.ngayDuyetLdc >= :#{#param.ngayTuCuc})" +
      "AND (:#{#param.ngayDenCuc}  IS NULL OR c.ngayDuyetLdc <= :#{#param.ngayDenCuc}) ) " +
      "AND ((:#{#param.ngayTuTc}  IS NULL OR c.ngayDuyetLdtc >= :#{#param.ngayTuTc})" +
      "AND (:#{#param.ngayDenTc}  IS NULL OR c.ngayDuyetLdtc <= :#{#param.ngayDenTc}) ) " +
      "AND (:#{#param.trangThai} IS NULL OR c.trangThai = :#{#param.trangThai}) " +
      "ORDER BY c.ngaySua desc , c.ngayTao desc, c.id desc"
  )
  Page<XhTlHoSoHdr> searchPage(@Param("param") XhTlHoSoReq param, Pageable pageable);

  @Query(value = "SELECT c FROM XhTlHoSoHdr c " +
          " LEFT JOIN XhTlQuyetDinhHdr qd on c.id = qd.idHoSo " +
          " WHERE 1 = 1 " +
          " AND qd.id is null " +
          " AND (:#{#param.trangThai} IS NULL OR c.trangThai = :#{#param.trangThai}) " +
          " ORDER BY c.ngaySua desc , c.ngayTao desc, c.id desc "
  )
  List<XhTlHoSoHdr> listTaoQuyetDinhThanhLy(@Param("param") XhTlHoSoReq param);

  @Query(value = "SELECT c FROM XhTlHoSoHdr c " +
          " LEFT JOIN XhTlThongBaoKq qd on c.id = qd.idHoSo " +
          " WHERE 1 = 1 " +
          " AND qd.id is null " +
          " AND (:#{#param.trangThai} IS NULL OR c.trangThai = :#{#param.trangThai}) " +
          " ORDER BY c.ngaySua desc , c.ngayTao desc, c.id desc "
  )
  List<XhTlHoSoHdr> listTaoThongBaoThanhLy(@Param("param") XhTlHoSoReq param);

  void deleteAllByIdIn(List<Long> listId);

  List<XhTlHoSoHdr> findByIdIn(List<Long> ids);

  List<XhTlHoSoHdr> findAllByIdIn(List<Long> listId);

  Optional<XhTlHoSoHdr> findBySoHoSo(String soQd);
}
