package com.tcdt.qlnvhang.repository.xuathang.xuatkhac.vattubaohanh;

import com.tcdt.qlnvhang.request.xuathang.xuatkhac.ktvattubaohanh.XhXkVtBhPhieuKtclRequest;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.ktvattubaohanh.XhXkVtBhPhieuKtclHdr;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface XhXkVtBhPhieuKtclRepository extends JpaRepository<XhXkVtBhPhieuKtclHdr, Long> {

  @Query("SELECT c FROM XhXkVtBhPhieuKtclHdr c WHERE 1=1 " +
      "AND (:#{#param.dvql} IS NULL OR c.maDvi LIKE CONCAT(:#{#param.dvql},'%')) " +
      "AND (:#{#param.nam} IS NULL OR c.nam = :#{#param.nam}) " +
      "AND (:#{#param.loaiVthh} IS NULL OR c.loaiVthh = :#{#param.loaiVthh}) " +
      "AND (:#{#param.loai} IS NULL OR c.loai = :#{#param.loai}) " +
      "AND (:#{#param.soPhieu} IS NULL OR LOWER(c.soPhieu) LIKE CONCAT('%',LOWER(:#{#param.soPhieu}),'%')) " +
      "AND (:#{#param.soQdGiaoNvXh} IS NULL OR LOWER(c.soQdGiaoNvXh) LIKE CONCAT('%',LOWER(:#{#param.soQdGiaoNvXh}),'%')) " +
      "AND (:#{#param.soQdGiaoNvNh} IS NULL OR LOWER(c.soQdGiaoNvNh) LIKE CONCAT('%',LOWER(:#{#param.soQdGiaoNvNh}),'%')) " +
      "AND ((:#{#param.ngayKiemTraTu}  IS NULL OR c.ngayKiemTra >= :#{#param.ngayKiemTraTu})" +
      "AND (:#{#param.ngayKiemTraDen}  IS NULL OR c.ngayKiemTra <= :#{#param.ngayKiemTraDen}) ) " +
      "AND (:#{#param.trangThai} IS NULL OR c.trangThai = :#{#param.trangThai}) " +
      "ORDER BY c.ngaySua desc , c.ngayTao desc, c.id desc"
  )
  Page<XhXkVtBhPhieuKtclHdr> search(@Param("param") XhXkVtBhPhieuKtclRequest param, Pageable pageable);

  Optional<XhXkVtBhPhieuKtclHdr> findBySoPhieu(String soPhieu);

  List<XhXkVtBhPhieuKtclHdr> findByIdIn(List<Long> ids);

  List<XhXkVtBhPhieuKtclHdr> findAllByIdIn(List<Long> idList);

  List<XhXkVtBhPhieuKtclHdr> findByIdQdGiaoNvXh(Long ids);

}
