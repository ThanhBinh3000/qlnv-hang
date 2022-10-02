package com.tcdt.qlnvhang.repository.xuatcuutro;

import com.tcdt.qlnvhang.request.xuatcuutro.XhDxCuuTroHdrSearchReq;
import com.tcdt.qlnvhang.table.XhDxCuuTroHdr;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeXuatCuuTroRepository extends JpaRepository<XhDxCuuTroHdr, Long> {
  @Query("SELECT c FROM XhDxCuuTroHdr c WHERE 1=1 " +
   /*    "AND (:#{#param.pagTypeLT} IS NULL OR c.loaiVthh NOT LIKE CONCAT('02','%')) " +
       "AND (:#{#param.pagTypeVT} IS NULL OR c.loaiVthh LIKE CONCAT('02','%')) " +*/
      "AND (:#{#param.dvql} IS NULL OR c.maDvi LIKE CONCAT(:#{#param.dvql},'%')) " +
//      "AND (:#{#param.maDvi} IS NULL OR c.maDvi = :#{#param.maDvi}) " +
      "AND (:#{#param.nam} IS NULL OR c.nam = :#{#param.nam}) " +
      "AND (:#{#param.soDxuat}  IS NULL OR LOWER(c.soDxuat) LIKE CONCAT('%',LOWER(:#{#param.soDxuat}),'%')) " +
      "AND (:#{#param.loaiVthh}  IS NULL OR LOWER(c.loaiVthh) =:#{#param.loaiVthh}) " +
      "AND (:#{#param.cloaiVthh}  IS NULL OR LOWER(c.cloaiVthh) =:#{#param.cloaiVthh}) " +
      "AND (:#{#param.loaiHinhNhapXuat}  IS NULL OR LOWER(c.loaiHinhNhapXuat) =:#{#param.loaiHinhNhapXuat}) " +
//       "AND (:#{#param.trichYeu}  IS NULL OR LOWER(c.trichYeu) LIKE CONCAT('%',LOWER(:#{#param.trichYeu}),'%')) " +
      "AND ((:#{#param.tuThoiGianThucHien}  IS NULL OR c.thoiGianThucHien >= :#{#param.tuThoiGianThucHien}) AND (:#{#param.denThoiGianThucHien}  IS NULL OR c.thoiGianThucHien <= :#{#param.denThoiGianThucHien}) ) " +
      "AND ((:#{#param.tuNgayDxuat}  IS NULL OR c.ngayDxuat >= :#{#param.tuNgayDxuat}) AND (:#{#param.denNgayDxuat}  IS NULL OR c.ngayDxuat <= :#{#param.denNgayDxuat}) ) " +
      "ORDER BY c.ngaySua desc , c.ngayTao desc, c.id desc"
  )
  Page<XhDxCuuTroHdr> search(@Param("param") XhDxCuuTroHdrSearchReq param, Pageable pageable);

  Optional<XhDxCuuTroHdr> findFirstBySoDxuatAndNam(String soDxuat, int nam);

  void deleteAllByIdIn(List<Long> listId);
}