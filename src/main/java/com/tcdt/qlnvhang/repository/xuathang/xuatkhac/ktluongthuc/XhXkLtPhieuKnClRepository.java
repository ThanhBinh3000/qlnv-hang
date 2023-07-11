package com.tcdt.qlnvhang.repository.xuathang.xuatkhac.ktluongthuc;

import com.tcdt.qlnvhang.request.xuathang.xuatkhac.ktluongthuc.XhXkLtPhieuKnClRequest;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.ktluongthuc.XhXkLtPhieuKnClHdr;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface XhXkLtPhieuKnClRepository extends JpaRepository<XhXkLtPhieuKnClHdr,Long> {

  @Query("SELECT c FROM XhXkLtPhieuKnClHdr c WHERE 1=1 " +
      "AND (:#{#param.dvql} IS NULL OR c.maDvi LIKE CONCAT(:#{#param.dvql},'%')) " +
      "AND (:#{#param.type} IS NULL OR c.type = :#{#param.type}) " +
      "AND (:#{#param.nam} IS NULL OR c.nam = :#{#param.nam}) " +
      "AND (:#{#param.loaiVthh} IS NULL OR c.loaiVthh = :#{#param.loaiVthh}) " +
      "AND (:#{#param.soBienBan} IS NULL OR LOWER(c.soBienBan) LIKE CONCAT('%',LOWER(:#{#param.soBienBan}),'%')) " +
      "AND (:#{#param.maDanhSach} IS NULL OR LOWER(c.maDanhSach) LIKE CONCAT('%',LOWER(:#{#param.maDanhSach}),'%')) " +
      "AND (:#{#param.soBienBan} IS NULL OR LOWER(c.soBienBan) LIKE CONCAT('%',LOWER(:#{#param.soBienBan}),'%')) " +
      "AND ((:#{#param.ngayKnMauTu}  IS NULL OR c.ngayKnMau >= :#{#param.ngayKnMauTu})" +
      "AND (:#{#param.ngayKnMauDen}  IS NULL OR c.ngayKnMau <= :#{#param.ngayKnMauDen}) ) " +
      "AND (:#{#param.trangThai} IS NULL OR c.trangThai = :#{#param.trangThai}) " +
      "ORDER BY c.ngaySua desc , c.ngayTao desc, c.id desc"
  )
  Page<XhXkLtPhieuKnClHdr> search(@Param("param") XhXkLtPhieuKnClRequest param, Pageable pageable);

  Optional<XhXkLtPhieuKnClHdr> findBySoPhieu(String soPhieu);

  List<XhXkLtPhieuKnClHdr> findByIdIn(List<Long> ids);

  List<XhXkLtPhieuKnClHdr> findAllByIdIn(List<Long> idList);

}
