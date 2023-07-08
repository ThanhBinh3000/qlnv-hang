package com.tcdt.qlnvhang.repository.xuathang.suachuahang;

import com.tcdt.qlnvhang.request.suachua.ScQuyetDinhXuatHangReq;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScQuyetDinhXuatHang;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ScQuyetDinhXuatHangRepository extends JpaRepository<ScQuyetDinhXuatHang,Long> {

    @Query(value = "SELECT a FROM ScQuyetDinhXuatHang a WHERE 1=1 " +
            "AND (:#{#param.nam} IS NULL OR a.nam = :#{#param.nam} )" +
            "AND (:#{#param.soQd} IS NULL OR a.soQd LIKE CONCAT(:#{#param.soQd},'%'))" +
            "AND (:#{#param.trichYeu} IS NULL OR a.trichYeu LIKE CONCAT(:#{#param.trichYeu},'%')) ")
    Page<ScQuyetDinhXuatHang> searchPage(@Param("param") ScQuyetDinhXuatHangReq req, Pageable pageable);
}
