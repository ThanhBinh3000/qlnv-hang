package com.tcdt.qlnvhang.repository.xuathang.bangkecanhang;

import com.tcdt.qlnvhang.entities.xuathang.bangkecanhang.XhBangKeCanHang;
import com.tcdt.qlnvhang.entities.xuathang.daugia.xuatkho.phieuxuatkho.XhPhieuXuatKho;
import com.tcdt.qlnvhang.repository.BaseRepository;
import com.tcdt.qlnvhang.request.xuathang.bangkecanhang.XhBangKeCanHangReq;
import com.tcdt.qlnvhang.request.xuathang.xuatkho.XhPhieuXuatKhoReq;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

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
