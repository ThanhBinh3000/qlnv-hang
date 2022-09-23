package com.tcdt.qlnvhang.repository.xuatcuutro;

import com.tcdt.qlnvhang.request.xuatcuutro.XhDxCuuTroHdrSearchReq;
import com.tcdt.qlnvhang.table.XhDxCuuTroHdr;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DeXuatCuuTroRepository extends JpaRepository<XhDxCuuTroHdr, Long> {
  @Query("SELECT c FROM XhDxCuuTroHdr c WHERE 1=1 "+
      /* "AND (:#{#param.pagTypeLT} IS NULL OR c.loaiVthh NOT LIKE CONCAT('02','%')) " +
       "AND (:#{#param.pagTypeVT} IS NULL OR c.loaiVthh LIKE CONCAT('02','%')) " +
       "AND (:#{#param.dvql} IS NULL OR c.maDvi LIKE CONCAT(:#{#param.dvql},'%')) " +
       "AND (:#{#param.namKeHoach} IS NULL OR function('TO_CHAR',c.namKeHoach) = :#{#param.namKeHoach}) " +
       "AND (:#{#param.soQd}  IS NULL OR LOWER(c.soQd) LIKE CONCAT('%',LOWER(:#{#param.soQd}),'%')) " +
       "AND (:#{#param.trichYeu}  IS NULL OR LOWER(c.trichYeu) LIKE CONCAT('%',LOWER(:#{#param.trichYeu}),'%')) " +
       "AND ((:#{#param.ngayKyTu}  IS NULL OR c.ngayKy >= :#{#param.ngayKyTu}) AND (:#{#param.ngayKyDen}  IS NULL OR c.ngayKy <= :#{#param.ngayKyDen}) ) " +*/
      "ORDER BY c.ngaySua desc , c.ngayTao desc, c.id desc"
  )
  Page<XhDxCuuTroHdr> search(@Param("param") XhDxCuuTroHdrSearchReq param, Pageable pageable);

  Optional<XhDxCuuTroHdr> findFirstBySoDxuatAndNam(String soDxuat, int nam);
}