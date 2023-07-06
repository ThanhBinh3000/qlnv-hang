package com.tcdt.qlnvhang.repository.xuathang.suachuahang;

import com.tcdt.qlnvhang.request.suachua.ScTrinhThamDinhHdrReq;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScTrinhThamDinhHdr;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ScTrinhThamDinhRepository extends JpaRepository<ScTrinhThamDinhHdr, Long> {
    @Query(value = "SELECT t FROM ScTrinhThamDinhHdr t WHERE 1 = 1 "
//            "AND (:#{#param.soQdSc} IS NULL OR t.soQdSc LIKE CONCAT(:#{#param.soQdSc},'%')) " +
//            "AND (:#{#param.trangThai} IS NULL OR t.trangThai = :#{#param.trangThai}) " +
//            "AND ((:#{#param.ngayDuyetTu}  IS NULL OR t.ngayDuyetLdc >= :#{#param.ngayDuyetTu})" +
//            "AND (:#{#param.ngayDuyetDen}  IS NULL OR t.ngayDuyetLdc <= :#{#param.ngayDuyetDen})) "
    )
    Page<ScTrinhThamDinhHdr> searchPage(@Param("param") ScTrinhThamDinhHdrReq req,
                                     Pageable pageable);
}
