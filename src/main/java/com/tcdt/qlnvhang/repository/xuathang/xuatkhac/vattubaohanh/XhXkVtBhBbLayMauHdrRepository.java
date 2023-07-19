package com.tcdt.qlnvhang.repository.xuathang.xuatkhac.vattubaohanh;


import com.tcdt.qlnvhang.request.xuathang.xuatkhac.ktvattubaohanh.XhXkVtBhBbLayMauRequest;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.ktvattubaohanh.XhXkVtBhBbLayMauHdr;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

;


@Repository
public interface XhXkVtBhBbLayMauHdrRepository extends JpaRepository<XhXkVtBhBbLayMauHdr, Long> {

  @Query("SELECT c FROM XhXkVtBhBbLayMauHdr c WHERE 1=1 " +
      "AND (:#{#param.dvql} IS NULL OR c.maDvi LIKE CONCAT(:#{#param.dvql},'%')) " +
      "AND (:#{#param.type} IS NULL OR c.type = :#{#param.type}) " +
      "AND (:#{#param.nam} IS NULL OR c.nam = :#{#param.nam}) " +
      "AND (:#{#param.loaiVthh} IS NULL OR c.loaiVthh = :#{#param.loaiVthh}) " +
      "AND (:#{#param.soBienBan} IS NULL OR LOWER(c.soBienBan) LIKE CONCAT('%',LOWER(:#{#param.soBienBan}),'%')) " +
      "AND (:#{#param.soQdGiaoNvXh} IS NULL OR LOWER(c.soQdGiaoNvXh) LIKE CONCAT('%',LOWER(:#{#param.soQdGiaoNvXh}),'%')) " +
      "AND (:#{#param.dviKiemNghiem} IS NULL OR LOWER(c.dviKiemNghiem) LIKE CONCAT('%',LOWER(:#{#param.dviKiemNghiem}),'%')) " +
      "AND ((:#{#param.ngayLayMauTu}  IS NULL OR c.ngayLayMau >= :#{#param.ngayLayMauTu})" +
      "AND (:#{#param.ngayLayMauDen}  IS NULL OR c.ngayLayMau <= :#{#param.ngayLayMauDen}) ) " +
      "AND (:#{#param.trangThai} IS NULL OR c.trangThai = :#{#param.trangThai}) " +
      "ORDER BY c.ngaySua desc , c.ngayTao desc, c.id desc"
  )
  Page<XhXkVtBhBbLayMauHdr> search(@Param("param") XhXkVtBhBbLayMauRequest param, Pageable pageable);

  Optional<XhXkVtBhBbLayMauHdr> findBySoBienBan(String soBienBan);

  List<XhXkVtBhBbLayMauHdr> findByIdIn(List<Long> ids);

  List<XhXkVtBhBbLayMauHdr> findAllByIdIn(List<Long> idList);

  List<XhXkVtBhBbLayMauHdr> findByIdQdGiaoNvXh(Long ids);

}
