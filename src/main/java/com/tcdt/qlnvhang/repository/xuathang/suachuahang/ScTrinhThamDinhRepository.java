package com.tcdt.qlnvhang.repository.xuathang.suachuahang;

import com.tcdt.qlnvhang.request.suachua.ScTongHopReq;
import com.tcdt.qlnvhang.request.suachua.ScTrinhThamDinhHdrReq;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScQuyetDinhSc;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScTongHopHdr;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScTrinhThamDinhHdr;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ScTrinhThamDinhRepository extends JpaRepository<ScTrinhThamDinhHdr, Long> {
    @Query(value = "SELECT t FROM ScTrinhThamDinhHdr t WHERE 1 = 1 " +
        "AND (:#{#param.soTtr} IS NULL OR t.soTtr LIKE CONCAT(:#{#param.soTtr},'%')) " +
        "AND (:#{#param.soQdScSr} IS NULL OR t.soQdSc LIKE CONCAT(:#{#param.soQdScSr},'%')) " +
        "AND (:#{#param.trangThai} IS NULL OR t.trangThai = :#{#param.trangThai}) " +
        "AND ((:#{#param.ngayTuCuc}  IS NULL OR t.ngayDuyetLdc >= :#{#param.ngayTuCuc}) AND (:#{#param.ngayDenCuc}  IS NULL OR t.ngayDuyetLdc <= :#{#param.ngayDenCuc})) " +
        "AND ((:#{#param.ngayTuTc}  IS NULL OR t.ngayDuyetLdtc >= :#{#param.ngayTuTc}) AND (:#{#param.ngayDenTc}  IS NULL OR t.ngayDuyetLdtc <= :#{#param.ngayDenTc})) " +
        " ORDER BY t.ngaySua desc , t.ngayTao desc, t.id desc "
    )
    Page<ScTrinhThamDinhHdr> searchPage(@Param("param") ScTrinhThamDinhHdrReq req, Pageable pageable);

    @Query(value = "SELECT c FROM ScTrinhThamDinhHdr c " +
            " LEFT JOIN ScQuyetDinhSc sc on c.id = sc.idTtr " +
            " WHERE 1 = 1 " +
            " AND sc.id is null " +
            " AND (:#{#param.trangThai} IS NULL OR c.trangThai = :#{#param.trangThai}) " +
            " ORDER BY c.ngaySua desc , c.ngayTao desc, c.id desc "
    )
    List<ScTrinhThamDinhHdr> listQuyetDinhSuaChua(@Param("param") ScTrinhThamDinhHdrReq req);

    Optional<ScTrinhThamDinhHdr> findBySoTtr(String soTtr);

}
