package com.tcdt.qlnvhang.repository.xuathang.xuatkhac.vattubaohanh;

import com.tcdt.qlnvhang.request.xuathang.xuatkhac.ktvattubaohanh.XhXkVtBhPhieuKdclRequest;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.ktvattubaohanh.XhXkVtBhPhieuKdclHdr;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface XhXkVtBhPhieuKdclRepository extends JpaRepository<XhXkVtBhPhieuKdclHdr, Long> {

  @Query("SELECT c FROM XhXkVtBhPhieuKdclHdr c WHERE 1=1 " +
      "AND (:#{#param.dvql} IS NULL OR c.maDvi LIKE CONCAT(:#{#param.dvql},'%')) " +
      "AND (:#{#param.nam} IS NULL OR c.nam = :#{#param.nam}) " +
      "AND (:#{#param.loaiVthh} IS NULL OR c.loaiVthh = :#{#param.loaiVthh}) " +
      "AND (:#{#param.soPhieu} IS NULL OR LOWER(c.soPhieu) LIKE CONCAT('%',LOWER(:#{#param.soPhieu}),'%')) " +
      "AND (:#{#param.soQdGiaoNvXh} IS NULL OR LOWER(c.soQdGiaoNvXh) LIKE CONCAT('%',LOWER(:#{#param.soQdGiaoNvXh}),'%')) " +
      "AND (:#{#param.soQdGiaoNvNh} IS NULL OR LOWER(c.soQdGiaoNvNh) LIKE CONCAT('%',LOWER(:#{#param.soQdGiaoNvNh}),'%')) " +
      "AND (:#{#param.soBbLayMau} IS NULL OR LOWER(c.soBbLayMau) LIKE CONCAT('%',LOWER(:#{#param.soBbLayMau}),'%')) " +
      "AND (:#{#param.dviKiemDinh} IS NULL OR LOWER(c.dviKiemDinh) LIKE CONCAT('%',LOWER(:#{#param.dviKiemDinh}),'%')) " +
      "AND ((:#{#param.ngayKiemDinhTu}  IS NULL OR c.ngayKiemDinh >= :#{#param.ngayKiemDinhTu})" +
      "AND (:#{#param.ngayKiemDinhDen}  IS NULL OR c.ngayKiemDinh <= :#{#param.ngayKiemDinhDen}) ) " +
      "AND (:#{#param.trangThai} IS NULL OR c.trangThai = :#{#param.trangThai}) " +
      "ORDER BY c.ngaySua desc , c.ngayTao desc, c.id desc"
  )
  Page<XhXkVtBhPhieuKdclHdr> search(@Param("param") XhXkVtBhPhieuKdclRequest param, Pageable pageable);

  Optional<XhXkVtBhPhieuKdclHdr> findBySoPhieu(String soPhieu);

  List<XhXkVtBhPhieuKdclHdr> findByIdIn(List<Long> ids);

  List<XhXkVtBhPhieuKdclHdr> findAllByIdIn(List<Long> idList);

  List<XhXkVtBhPhieuKdclHdr> findByIdQdGiaoNvXh(Long ids);

  List<XhXkVtBhPhieuKdclHdr> findByIdQdGiaoNvXhIn(List<Long> list);

  List<XhXkVtBhPhieuKdclHdr> findAllByIdBaoCaoKdm(Long ids);
}
