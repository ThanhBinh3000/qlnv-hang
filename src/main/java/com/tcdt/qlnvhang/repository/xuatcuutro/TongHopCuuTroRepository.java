package com.tcdt.qlnvhang.repository.xuatcuutro;

import com.tcdt.qlnvhang.request.xuatcuutro.XhThCuuTroHdrSearchReq;
import com.tcdt.qlnvhang.table.XhThCuuTroDtl;
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
public interface TongHopCuuTroRepository extends JpaRepository<XhThCuuTroHdr, Long> {
  @Query("SELECT c FROM XhThCuuTroHdr c WHERE 1=1 " +
   /*    "AND (:#{#param.pagTypeLT} IS NULL OR c.loaiVthh NOT LIKE CONCAT('02','%')) " +
       "AND (:#{#param.pagTypeVT} IS NULL OR c.loaiVthh LIKE CONCAT('02','%')) " +*/
      "AND (:#{#param.dvql} IS NULL OR c.maDvi LIKE CONCAT(:#{#param.dvql},'%')) " +
      "AND (:#{#param.maDvi} IS NULL OR c.maDvi = :#{#param.maDvi}) " +
      "AND (:#{#param.nam} IS NULL OR c.nam = :#{#param.nam}) " +
//      "AND (:#{#param.soDxuat}  IS NULL OR LOWER(c.soDxuat) LIKE CONCAT('%',LOWER(:#{#param.soDxuat}),'%')) " +
      "AND (:#{#param.maDviDxuat}  IS NULL OR LOWER(c.maDviDxuat) LIKE CONCAT('%',LOWER(:#{#param.maDviDxuat}),'%')) " +
      "AND (:#{#param.loaiVthh}  IS NULL OR LOWER(c.loaiVthh) =:#{#param.loaiVthh}) " +
      "AND (:#{#param.cloaiVthh}  IS NULL OR LOWER(c.cloaiVthh) =:#{#param.cloaiVthh}) " +
      "AND (:#{#param.listTrangThai == null} = true OR c.trangThai in :#{#param.listTrangThai}) " +
      "AND ((:#{#param.tuNgayTongHop}  IS NULL OR c.ngayTongHop >= :#{#param.tuNgayTongHop}) AND (:#{#param.denNgayTongHop}  IS NULL OR c.ngayTongHop <= :#{#param.denNgayTongHop}) ) " +
      "OR (c.id = :#{#param.idTongHop}) " +
      "ORDER BY c.ngaySua desc , c.ngayTao desc, c.id desc"
  )
  Page<XhThCuuTroHdr> search(@Param("param") XhThCuuTroHdrSearchReq param, Pageable pageable);

  void deleteAllByIdIn(List<Long> listId);

  Optional<XhThCuuTroHdr> findFirstByMaTongHopAndNam(String maTongHop, int nam);

}