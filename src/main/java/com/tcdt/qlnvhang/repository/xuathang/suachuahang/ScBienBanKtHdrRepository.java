package com.tcdt.qlnvhang.repository.xuathang.suachuahang;

import com.tcdt.qlnvhang.request.suachua.ScBienBanKtReq;
import com.tcdt.qlnvhang.request.suachua.ScPhieuNhapKhoReq;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScBangKeNhapVtDtl;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScBienBanKtHdr;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScPhieuNhapKhoHdr;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ScBienBanKtHdrRepository extends JpaRepository<ScBienBanKtHdr, Long> {

    List<ScBienBanKtHdr> findAllByIdQdNh(Long idQdNh);

    @Query(value = "SELECT qd FROM ScBienBanKtHdr qd WHERE 1 = 1 " +
            "AND (:#{#param.nam} IS NULL OR qd.nam = :#{#param.nam}) " +
            "AND (:#{#param.idQdNh} IS NULL OR qd.idQdNh = :#{#param.idQdNh}) " +
            "AND (:#{#param.soBienBan} IS NULL OR qd.soBienBan = :#{#param.soBienBan}) " +
            "AND ((:#{#param.ngayTu} IS NULL OR qd.ngayKetThuc >= :#{#param.ngayTu}) AND (:#{#param.ngayDen}  IS NULL OR qd.ngayKetThuc <= :#{#param.ngayDen})) " +
            " ORDER BY qd.ngaySua desc , qd.ngayTao desc, qd.id desc "
    )
    List<ScBienBanKtHdr> searchList(@Param("param") ScBienBanKtReq req);
}
