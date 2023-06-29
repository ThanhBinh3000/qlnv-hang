package com.tcdt.qlnvhang.repository.xuathang.suachuahang;

import com.tcdt.qlnvhang.request.suachua.ScPhieuXuatKhoReq;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScPhieuXuatKhoHdr;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ScPhieuXuatKhoHdrRepository extends JpaRepository<ScPhieuXuatKhoHdr, Long> {
    Optional<ScPhieuXuatKhoHdr> findBySoPhieuXuatKho(String soPhieuXuatKho);

    @Query(value = "SELECT a FROM ScPhieuXuatKhoHdr a WHERE 1=1 " +
            "AND (:#{#param.nam} IS NULL OR a.nam = :#{#param.nam})" +
            "AND (:#{#param.soQdGiaoNv} IS NULL OR a.soQdGiaoNv = :#{#param.soQdGiaoNv})" +
            "AND (:#{#param.soPhieuXuatKho} IS NULL OR a.soPhieuXuatKho = :#{#param.soPhieuXuatKho})")
    Page<ScPhieuXuatKhoHdr> searchPage(@Param("param") ScPhieuXuatKhoReq req, Pageable pageable);
}
