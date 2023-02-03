package com.tcdt.qlnvhang.repository.xuathang.daugia.tochuctrienkhai.ketqua;

import com.tcdt.qlnvhang.request.xuathang.daugia.tochuctrienkhai.ketqua.XhKqBdgHdrReq;
import com.tcdt.qlnvhang.entities.xuathang.daugia.tochuctrienkhai.ketqua.XhKqBdgHdr;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface XhKqBdgHdrRepository extends JpaRepository<XhKqBdgHdr, Long> {

    @Query("SELECT c FROM XhKqBdgHdr c where 1 = 1" +
            "AND (:#{#param.maDvi} IS NULL OR c.maDvi = :#{#param.maDvi}) " +
            "AND (:#{#param.nam} IS NULL OR c.nam = :#{#param.nam}) " +
            "AND (:#{#param.loaiVthh } IS NULL OR LOWER(c.loaiVthh) LIKE CONCAT(:#{#param.loaiVthh},'%')) " +
            "AND (:#{#param.trangThai} IS NULL OR c.trangThai = :#{#param.trangThai}) " +
            "AND (:#{#param.trangThaiHdSr} IS NULL OR c.trangThaiHd = :#{#param.trangThaiHdSr}) "
    )
    Page<XhKqBdgHdr> search(@Param("param") XhKqBdgHdrReq param, Pageable pageable);

    XhKqBdgHdr findByMaThongBao(String maThongBao);

    Optional<XhKqBdgHdr> findBySoQdKq(String soQdKq);

}