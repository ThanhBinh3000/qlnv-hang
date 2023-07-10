package com.tcdt.qlnvhang.repository.xuathang.suachuahang;

import com.tcdt.qlnvhang.request.suachua.ScQuyetDinhNhapHangReq;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScQuyetDinhNhapHang;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ScQuyetDinhNhapHangRepository extends JpaRepository<ScQuyetDinhNhapHang, Long> {

    @Query(value = "SELECT a FROM ScQuyetDinhNhapHang a WHERE 1=1 " +
            "AND (:#{#param.nam} IS NULL OR a.nam = :#{#param.nam}) " +
            "AND (:#{#param.soQdNhapHang} IS NULL OR a.soQd = :#{#param.soQdNhapHang}) " +
            " AND (:#{#param.trichYeu} IS NULL OR a.trichYeu LIKE CONCAT(:#{#param.trichYeu},'%')) ")
    Page<ScQuyetDinhNhapHang> searchPage(@Param("param") ScQuyetDinhNhapHangReq req, Pageable pageable);

}
