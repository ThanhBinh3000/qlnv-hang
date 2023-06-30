package com.tcdt.qlnvhang.repository.xuathang.suachuahang;

import com.tcdt.qlnvhang.request.suachua.ScBangKeXuatVatTuReq;
import com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbBangKeNhapVTHdrDTO;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScBangKeXuatVatTuHdr;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ScBangKeXuatVatTuHdrRepository extends JpaRepository<ScBangKeXuatVatTuHdr,Long> {
    Optional<ScBangKeXuatVatTuHdr> findFirstBySoBangKe(String soBangKe);

    List<ScBangKeXuatVatTuHdr> findAllByIdIn(List<Long> ids);

    @Query(value = "SELECT a FROM ScBangKeXuatVatTuHdr a WHERE 1=1" +
            "AND (:#{#param.nam} IS NULL OR a.nam = :#{#param.nam})" +
            "AND (:#{#param.soQdGiaoNv} IS NULL OR a.soQdGiaoNv = :#{#param.soQdGiaoNv})" +
            "AND (:#{#param.soBangKe} IS NULL OR a.soBangKe = :#{#param.soBangKe})"
    )
    Page<ScBangKeXuatVatTuHdr> searchPage(@Param("param") ScBangKeXuatVatTuReq req, Pageable pageable);

}
