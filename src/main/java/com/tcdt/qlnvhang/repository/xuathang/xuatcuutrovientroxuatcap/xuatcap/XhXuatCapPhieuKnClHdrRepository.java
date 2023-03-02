package com.tcdt.qlnvhang.repository.xuathang.xuatcuutrovientroxuatcap.xuatcap;

import com.tcdt.qlnvhang.request.xuathang.xuatcuutrovientroxuatcap.xuatcap.SearchXhXuatCapPhieuKnCl;
import com.tcdt.qlnvhang.table.xuathang.xuatcuutrovientroxuatcap.xuatcap.XhXuatCapPhieuKnClHdr;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface XhXuatCapPhieuKnClHdrRepository extends JpaRepository<XhXuatCapPhieuKnClHdr,Long> {

  @Query("SELECT c FROM XhXuatCapPhieuKnClHdr c WHERE 1=1 " +
      "AND (:#{#param.dvql} IS NULL OR c.maDvi LIKE CONCAT(:#{#param.dvql},'%')) " +
      "AND (:#{#param.type} IS NULL OR c.type = :#{#param.type}) " +
      "AND (:#{#param.loaiVthh} IS NULL OR c.loaiVthh = :#{#param.loaiVthh}) " +
      "AND (:#{#param.nam} IS NULL OR c.nam = :#{#param.nam}) " +
      "AND (:#{#param.soQdGiaoNvXh} IS NULL OR LOWER(c.soQdGiaoNvXh) LIKE CONCAT('%',LOWER(:#{#param.soQdGiaoNvXh}),'%')) " +
      "AND (:#{#param.soPhieu} IS NULL OR LOWER(c.soPhieuKnCl) LIKE CONCAT('%',LOWER(:#{#param.soPhieu}),'%')) " +
      "AND ((:#{#param.ngayKnTu}  IS NULL OR c.ngayLapPhieu >= :#{#param.ngayKnTu})" +
      "AND (:#{#param.ngayKnDen}  IS NULL OR c.ngayLapPhieu <= :#{#param.ngayKnDen}) ) " +
      "AND (:#{#param.trangThai} IS NULL OR c.trangThai = :#{#param.trangThai}) " +
      "ORDER BY c.ngaySua desc , c.ngayTao desc, c.id desc"
  )
  Page<XhXuatCapPhieuKnClHdr> search(@Param("param") SearchXhXuatCapPhieuKnCl param, Pageable pageable);

  List<XhXuatCapPhieuKnClHdr> findAllByIdIn(List<Long> idList);


  List<XhXuatCapPhieuKnClHdr> findByIdIn(List<Long> ids);

  Optional<XhXuatCapPhieuKnClHdr> findBySoPhieuKnCl(String soPhieuKnCl);
}
