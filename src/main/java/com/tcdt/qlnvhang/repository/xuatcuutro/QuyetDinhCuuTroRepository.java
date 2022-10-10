package com.tcdt.qlnvhang.repository.xuatcuutro;

import com.tcdt.qlnvhang.request.xuatcuutro.XhQdCuuTroHdrSearchReq;
import com.tcdt.qlnvhang.table.XhQdCuuTroHdr;
import com.tcdt.qlnvhang.table.XhThCuuTroHdr;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuyetDinhCuuTroRepository extends JpaRepository<XhQdCuuTroHdr, Long> {
  @Query("SELECT c FROM XhQdCuuTroHdr c WHERE 1=1 " +
   /*    "AND (:#{#param.pagTypeLT} IS NULL OR c.loaiVthh NOT LIKE CONCAT('02','%')) " +
       "AND (:#{#param.pagTypeVT} IS NULL OR c.loaiVthh LIKE CONCAT('02','%')) " +*/
      "AND (:#{#param.dvql} IS NULL OR c.maDvi LIKE CONCAT(:#{#param.dvql},'%')) " +
      "AND (:#{#param.maDvi} IS NULL OR c.maDvi = :#{#param.maDvi}) " +
      "AND (:#{#param.nam} IS NULL OR c.nam = :#{#param.nam}) " +
      "AND ((:#{#param.tuNgayKy}  IS NULL OR c.ngayKy >= :#{#param.tuNgayKy}) AND (:#{#param.denNgayKy}  IS NULL OR c.ngayKy <= :#{#param.denNgayKy}) ) " +
      "ORDER BY c.ngaySua desc , c.ngayTao desc, c.id desc"
  )
  Page<XhQdCuuTroHdr> search(@Param("param") XhQdCuuTroHdrSearchReq param, Pageable pageable);

  void deleteAllByIdIn(List<Long> listId);

  Optional<XhThCuuTroHdr> findFirstByMaTongHopAndNam(String maTongHop, int nam);

  Optional<XhQdCuuTroHdr> findFirstBySoQdAndNam(String soQd, int nam);
}