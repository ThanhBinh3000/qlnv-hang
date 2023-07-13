package com.tcdt.qlnvhang.repository.xuathang.suachuahang;

import com.tcdt.qlnvhang.request.suachua.ScQuyetDinhNhapHangReq;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScQuyetDinhNhapHang;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ScQuyetDinhNhapHangRepository extends JpaRepository<ScQuyetDinhNhapHang, Long> {

    @Query(value = "SELECT c FROM ScQuyetDinhNhapHang c WHERE 1=1 " +
            " AND (:#{#param.maDviSr} IS NULL OR c.maDvi LIKE CONCAT(:#{#param.maDviSr},'%')) " +
            "AND (:#{#param.nam} IS NULL OR c.nam = :#{#param.nam}) " +
            " AND (:#{#param.trichYeu} IS NULL OR c.trichYeu LIKE CONCAT(:#{#param.trichYeu},'%')) ")
    Page<ScQuyetDinhNhapHang> searchPage(@Param("param") ScQuyetDinhNhapHangReq req, Pageable pageable);

}
