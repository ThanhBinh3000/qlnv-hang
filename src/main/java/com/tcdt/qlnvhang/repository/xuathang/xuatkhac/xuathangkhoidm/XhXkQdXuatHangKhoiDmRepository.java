package com.tcdt.qlnvhang.repository.xuathang.xuatkhac.xuathangkhoidm;

import com.tcdt.qlnvhang.request.xuathang.xuatkhac.xuathangkhoidm.XhXkDsHangDtqgRequest;
import com.tcdt.qlnvhang.request.xuathang.xuatkhac.xuathangkhoidm.XhXkQdXuatHangKhoiDmRequest;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.xuathangkhoidm.XhXkDsHangDtqgHdr;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.xuathangkhoidm.XhXkQdXuatHangKhoiDm;
import feign.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface XhXkQdXuatHangKhoiDmRepository extends JpaRepository<XhXkQdXuatHangKhoiDm, Long> {
    @Query("SELECT distinct c FROM XhXkQdXuatHangKhoiDm c WHERE 1=1 " +
            "AND (:#{#param.soQd} IS NULL OR c.soQd LIKE CONCAT(:#{#param.soQd},'%')) " +
            "AND ((:#{#param.ngayKyTu}  IS NULL OR c.ngayKy >= :#{#param.ngayKyTu})" +
            "AND  (:#{#param.ngayKyDen}  IS NULL OR c.ngayKy <= :#{#param.ngayKyDen})) " +
            "AND ((:#{#param.ngayHieuLucTu}  IS NULL OR c.ngayHieuLuc >= :#{#param.ngayHieuLucTu})" +
            "AND  (:#{#param.ngayHieuLucDen}  IS NULL OR c.ngayHieuLuc <= :#{#param.ngayHieuLucDen})) " +
            "ORDER BY c.ngaySua desc , c.ngayTao desc, c.id desc"
    )
    Page<XhXkQdXuatHangKhoiDm> searchPage(@Param("param") XhXkQdXuatHangKhoiDmRequest param, Pageable pageable);

    void deleteAllByIdIn(List<Long> listId);

    List<XhXkQdXuatHangKhoiDm> findByIdIn(List<Long> ids);

    Optional<XhXkQdXuatHangKhoiDm> findBySoQd(String soQd);
}
