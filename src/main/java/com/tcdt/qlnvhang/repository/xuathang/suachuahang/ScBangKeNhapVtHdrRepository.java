package com.tcdt.qlnvhang.repository.xuathang.suachuahang;

import com.tcdt.qlnvhang.request.suachua.ScBangKeNhapVtReq;
import com.tcdt.qlnvhang.request.suachua.ScPhieuNhapKhoReq;
import com.tcdt.qlnvhang.response.suachua.ScBangKeNhapVtDTO;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScBangKeNhapVtHdr;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScPhieuNhapKhoHdr;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ScBangKeNhapVtHdrRepository extends JpaRepository<ScBangKeNhapVtHdr, Long> {


    @Query(value = "SELECT qd FROM ScBangKeNhapVtHdr qd WHERE 1 = 1 " +
            "AND (:#{#param.nam} IS NULL OR qd.nam = :#{#param.nam}) " +
            "AND (:#{#param.idQdNh} IS NULL OR qd.idQdNh = :#{#param.idQdNh}) " +
            "AND (:#{#param.idPhieuNhapKhoList.size() }  = 0 OR qd.idPhieuNhapKho IN (:#{#param.idPhieuNhapKhoList})) " +
            "AND (:#{#param.soPhieuNhapKho} IS NULL OR qd.soPhieuNhapKho LIKE CONCAT(:#{#param.soPhieuNhapKho},'%'))" +
            "AND ((:#{#param.ngayTu} IS NULL OR qd.ngayNhapKho >= :#{#param.ngayTu}) AND (:#{#param.ngayDen}  IS NULL OR qd.ngayNhapKho <= :#{#param.ngayDen})) " +
            " ORDER BY qd.ngaySua desc , qd.ngayTao desc, qd.id desc "
    )
    List<ScBangKeNhapVtHdr> searchList(@Param("param") ScBangKeNhapVtReq req);
    Optional<ScBangKeNhapVtHdr> findFirstBySoBangKe(String soBangKe);

    List<ScBangKeNhapVtHdr> findAllByIdIn(List<Long> id);

    List<ScBangKeNhapVtHdr> findAllByIdPhieuNhapKhoIn(List<Long> idPhieuNhapKho);

}
