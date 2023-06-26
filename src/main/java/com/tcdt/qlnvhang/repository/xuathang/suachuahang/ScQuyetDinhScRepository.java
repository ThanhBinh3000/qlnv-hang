package com.tcdt.qlnvhang.repository.xuathang.suachuahang;

import com.tcdt.qlnvhang.request.suachua.ScQuyetDinhReq;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScQuyetDinhSc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ScQuyetDinhScRepository extends JpaRepository<ScQuyetDinhSc, Long> {

    @Query(value = "SELECT q FROM ScQuyetDinhSc q WHERE 1 = 1" +
            "AND (:#{#param.soQd} IS NULL OR q.soQd LIKE CONCAT(:#{#param.soQd},'%')) " +
            "AND (:#{#param.soToTrinh} IS NULL OR q.soToTrinh LIKE CONCAT(:#{#param.soToTrinh},'%'))" +
            "AND (:#{#param.trangThai} IS NULL OR q.trangThai = :#{#param.trangThai}) " +
            "AND ((:#{#param.ngayKyTu}  IS NULL OR q.ngayKy >= :#{#param.ngayKyTu})" +
            "AND (:#{#param.ngayKyDen}  IS NULL OR q.ngayKy <= :#{#param.ngayKyDen}) ) ")
    Page<ScQuyetDinhSc> searchPage(@Param("param") ScQuyetDinhReq req, Pageable pageable);
}
