package com.tcdt.qlnvhang.repository.xuathang.suachuahang;

import com.tcdt.qlnvhang.request.suachua.ScQuyetDinhScReq;
import com.tcdt.qlnvhang.request.suachua.ScTongHopReq;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScQuyetDinhSc;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScTongHopHdr;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ScQuyetDinhScRepository extends JpaRepository<ScQuyetDinhSc, Long> {

    @Query(value = "SELECT q FROM ScQuyetDinhSc q WHERE 1 = 1" +
            "AND (:#{#param.soQd} IS NULL OR q.soQd LIKE CONCAT(:#{#param.soQd},'%')) " +
            "AND (:#{#param.trangThai} IS NULL OR q.trangThai = :#{#param.trangThai})" +
            " ORDER BY q.ngaySua desc , q.ngayTao desc, q.id desc "
    )
    Page<ScQuyetDinhSc> searchPage(@Param("param") ScQuyetDinhScReq req, Pageable pageable);

    @Query(value = "SELECT c FROM ScQuyetDinhSc c " +
            " LEFT JOIN ScQuyetDinhXuatHang qd on c.id = qd.idQdSc " +
            " WHERE 1 = 1 " +
            " AND qd.id is null " +
            " AND (:#{#param.trangThai} IS NULL OR c.trangThai = :#{#param.trangThai}) " +
            " ORDER BY c.ngaySua desc , c.ngayTao desc, c.id desc "
    )
    List<ScQuyetDinhSc> listQuyetDinhXuatHang(@Param("param") ScQuyetDinhScReq param);

}
