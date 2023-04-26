package com.tcdt.qlnvhang.repository.xuathang.xuatcuutrovientroxuatcap.xuatcap;

import com.tcdt.qlnvhang.request.xuathang.xuatcuutrovientroxuatcap.xuatcap.SearchXhCtvtQdXuatCap;
import com.tcdt.qlnvhang.table.xuathang.xuatcuutrovientroxuatcap.xuatcap.XhXcapQuyetDinhPdHdr;
import feign.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface XhXcapQuyetDinhPdHdrRepository extends JpaRepository<XhXcapQuyetDinhPdHdr,Long> {

  @Query("SELECT DISTINCT  c FROM XhXcapQuyetDinhPdHdr c " +
      " WHERE 1=1 " +
      "AND (:#{#param.dvql} IS NULL OR c.maDvi LIKE CONCAT(:#{#param.dvql},'%')) " +
      "AND (:#{#param.nam} IS NULL OR c.nam = :#{#param.nam}) " +
      "AND (:#{#param.soQdXc} IS NULL OR LOWER(c.soQd) LIKE CONCAT('%',LOWER(:#{#param.soQdXc}),'%')) " +
      "AND ((:#{#param.ngayXuatCtvtTu}  IS NULL OR c.thoiHanXuat >= :#{#param.ngayXuatCtvtTu})" +
      "AND (:#{#param.ngayXuatCtvtDen}  IS NULL OR c.thoiHanXuat <= :#{#param.ngayXuatCtvtDen}) ) " +
      "AND ((:#{#param.ngayHieuLucTu}  IS NULL OR c.ngayHluc >= :#{#param.ngayHieuLucTu})" +
      "AND (:#{#param.ngayHieuLucDen}  IS NULL OR c.ngayHluc <= :#{#param.ngayHieuLucDen}) ) " +
      "AND (:#{#param.trangThai} IS NULL OR c.trangThai = :#{#param.trangThai}) " +
      "ORDER BY c.ngaySua desc , c.ngayTao desc, c.id desc"
  )
  Page<XhXcapQuyetDinhPdHdr> search (@Param("param") SearchXhCtvtQdXuatCap param, Pageable pageable);


  void deleteAllByIdIn(List<Long> listId);

  List<XhXcapQuyetDinhPdHdr> findByIdIn(List<Long> ids);

  List<XhXcapQuyetDinhPdHdr> findAllByIdIn(List<Long> listId);

  Optional<XhXcapQuyetDinhPdHdr> findBySoQd(String soQd);
}
