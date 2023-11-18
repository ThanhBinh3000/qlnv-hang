package com.tcdt.qlnvhang.repository.xuathang.thanhlytieuhuy.thanhly;

import com.tcdt.qlnvhang.request.xuathang.thanhlytieuhuy.thanhly.SearchXhTlQuyetDinh;
import com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.thanhly.XhTlQuyetDinhHdr;
import com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.thanhly.XhTlThongBaoKq;
import feign.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface XhTlThongBaoKqRepository extends JpaRepository<XhTlThongBaoKq, Long> {

  @Query("SELECT   c FROM XhTlThongBaoKq c " +
      " WHERE 1=1 " +
      "AND (:#{#param.dvql} IS NULL OR c.maDvi LIKE CONCAT(:#{#param.dvql},'%')) " +
      "AND (:#{#param.nam} IS NULL OR c.nam = :#{#param.nam}) " +
      "AND (:#{#param.soThongBao} IS NULL OR LOWER(c.soThongBao) LIKE CONCAT('%',LOWER(:#{#param.soThongBao}),'%')) " +
      "AND (:#{#param.soHoSo} IS NULL OR LOWER(c.soHoSo) LIKE CONCAT('%',LOWER(:#{#param.soHoSo}),'%')) " +
      "AND ((:#{#param.ngayTu}  IS NULL OR c.ngayThongBao >= :#{#param.ngayTu})" +
      "AND (:#{#param.ngayDen}  IS NULL OR c.ngayThongBao <= :#{#param.ngayDen}) ) " +
      "AND (:#{#param.trangThai} IS NULL OR c.trangThai = :#{#param.trangThai}) " +
      "ORDER BY c.ngaySua desc , c.ngayTao desc, c.id desc"
  )
  Page<XhTlThongBaoKq> search(@Param("param") SearchXhTlQuyetDinh param, Pageable pageable);


  void deleteAllByIdIn(List<Long> listId);

  List<XhTlThongBaoKq> findByIdIn(List<Long> ids);

  List<XhTlThongBaoKq> findAllByIdIn(List<Long> listId);

  Optional<XhTlThongBaoKq> findBySoThongBao(String soQd);

  Optional<XhTlThongBaoKq> findByIdHoSo(Long idHoSo);
}
