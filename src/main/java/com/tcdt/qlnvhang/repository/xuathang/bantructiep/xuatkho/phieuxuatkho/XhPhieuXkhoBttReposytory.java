package com.tcdt.qlnvhang.repository.xuathang.bantructiep.xuatkho.phieuxuatkho;

import com.tcdt.qlnvhang.entities.xuathang.bantructiep.xuatkho.phieuxuatkho.XhPhieuXkhoBtt;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.xuatkho.phieuxuatkho.XhPhieuXkhoBttReq;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface XhPhieuXkhoBttReposytory extends JpaRepository<XhPhieuXkhoBtt, Long> {

    @Query("SELECT XK from XhPhieuXkhoBtt XK WHERE 1 = 1 " +
            "AND (:#{#param.namKh} IS NULL OR XK.namKh = :#{#param.namKh}) " +
            "AND (:#{#param.soQdNv} IS NULL OR LOWER(XK.soQdNv) LIKE LOWER(CONCAT(CONCAT('%',:#{#param.soQdNv}),'%' ) ) )" +
            "AND (:#{#param.soPhieuXuat} IS NULL OR LOWER(XK.soPhieuXuat) LIKE LOWER(CONCAT(CONCAT('%',:#{#param.soPhieuXuat}),'%' ) ) )" +
            "AND (:#{#param.loaiVthh} IS NULL OR XK.loaiVthh LIKE CONCAT(:#{#param.loaiVthh},'%')) " +
            "AND (:#{#param.trangThai} IS NULL OR XK.trangThai = :#{#param.trangThai}) " +
            "AND (:#{#param.maDvi} IS NULL OR XK.maDvi = :#{#param.maDvi})")
    Page<XhPhieuXkhoBtt> searchPage(@Param("param") XhPhieuXkhoBttReq param, Pageable pageable);

}
