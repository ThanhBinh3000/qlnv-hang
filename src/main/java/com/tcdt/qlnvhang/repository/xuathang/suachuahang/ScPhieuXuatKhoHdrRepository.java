package com.tcdt.qlnvhang.repository.xuathang.suachuahang;

import com.tcdt.qlnvhang.request.suachua.ScPhieuXuatKhoReq;
import com.tcdt.qlnvhang.response.suachua.ScPhieuXuatKhoDTO;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScPhieuXuatKhoHdr;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ScPhieuXuatKhoHdrRepository extends JpaRepository<ScPhieuXuatKhoHdr, Long> {

    @Query(value = "SELECT qd FROM ScPhieuXuatKhoHdr qd WHERE 1 = 1 " +
            "AND (:#{#param.nam} IS NULL OR qd.nam = :#{#param.nam}) ")
    Page<ScPhieuXuatKhoHdr> searchPage(@Param("param") ScPhieuXuatKhoReq req, Pageable pageable);


    @Query(value = "SELECT qd FROM ScPhieuXuatKhoHdr qd WHERE 1 = 1 " +
            "AND (:#{#param.nam} IS NULL OR qd.nam = :#{#param.nam}) " +
            "AND (:#{#param.idScDanhSachHdr} IS NULL OR qd.idScDanhSachHdr = :#{#param.idScDanhSachHdr}) " +
            "AND (:#{#param.soPhieuXuatKho} IS NULL OR qd.soPhieuXuatKho LIKE CONCAT(:#{#param.soPhieuXuatKho},'%'))")
    List<ScPhieuXuatKhoHdr> searchList(@Param("param") ScPhieuXuatKhoReq req);
}
