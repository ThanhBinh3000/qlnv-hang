package com.tcdt.qlnvhang.repository.xuathang.bangkecanhang;

import com.tcdt.qlnvhang.entities.xuathang.bangkecanhang.XhBangKeCanHang;
import com.tcdt.qlnvhang.repository.BaseRepository;
import com.tcdt.qlnvhang.request.xuathang.bangkecanhang.XhBangKeCanHangReq;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface XhBangKeCanHangRepository extends BaseRepository<XhBangKeCanHang, Long>, XhBangKeCanHangRepositoryCustom {
  @Query("SELECT c FROM XhBbLayMau c " +
      " WHERE 1 = 1 " +
      "AND (:#{#param.maDvi} IS NULL OR c.maDvi LIKE CONCAT(:#{#param.maDvi},'%')) " +
      "AND (:#{#param.nam} IS NULL OR c.nam = :#{#param.nam}) " +
      "AND (:#{#param.loaiVthh } IS NULL OR LOWER(c.loaiVthh) LIKE CONCAT(:#{#param.loaiVthh},'%')) " +
      "AND (:#{#param.trangThai} IS NULL OR c.trangThai = :#{#param.trangThai}) "
  )
  Page<XhBangKeCanHang> searchPage(@Param("param") XhBangKeCanHangReq param, Pageable pageable);
}
