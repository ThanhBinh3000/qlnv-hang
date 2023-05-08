package com.tcdt.qlnvhang.repository.xuathang.thanhlytieuhuy.thanhly;

import com.tcdt.qlnvhang.request.xuathang.thanhlytieuhuy.thanhly.SearchXhTlQuyetDinhTl;
import com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.thanhly.XhTlQuyetDinhTlHdr;
import feign.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface XhTlQuyetDinhTlHdrReqository extends JpaRepository<XhTlQuyetDinhTlHdr,Long> {

  @Query("SELECT DISTINCT  c FROM XhTlQuyetDinhTlHdr c " +
      " WHERE 1=1 " +
      "AND (:#{#param.dvql} IS NULL OR c.maDvi LIKE CONCAT(:#{#param.dvql},'%')) " +
      "AND (:#{#param.nam} IS NULL OR c.nam = :#{#param.nam}) " +
      "AND (:#{#param.soQd} IS NULL OR LOWER(c.soQd) LIKE CONCAT('%',LOWER(:#{#param.soQd}),'%')) " +
      "AND (:#{#param.soHoSo} IS NULL OR LOWER(c.soHoSo) LIKE CONCAT('%',LOWER(:#{#param.soHoSo}),'%')) " +
      "AND ((:#{#param.ngayKyTu}  IS NULL OR c.ngayKy >= :#{#param.ngayKyTu})" +
      "AND (:#{#param.ngayKyDen}  IS NULL OR c.ngayKy <= :#{#param.ngayKyDen}) ) " +
      "AND (:#{#param.trangThai} IS NULL OR c.trangThai = :#{#param.trangThai}) " +
      "ORDER BY c.ngaySua desc , c.ngayTao desc, c.id desc"
  )
  Page<XhTlQuyetDinhTlHdr> search (@Param("param") SearchXhTlQuyetDinhTl param, Pageable pageable);


  void deleteAllByIdIn(List<Long> listId);

  List<XhTlQuyetDinhTlHdr> findByIdIn(List<Long> ids);

  List<XhTlQuyetDinhTlHdr> findAllByIdIn(List<Long> listId);

  Optional<XhTlQuyetDinhTlHdr> findBySoQd(String soQd);
}
