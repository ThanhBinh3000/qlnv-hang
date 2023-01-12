package com.tcdt.qlnvhang.repository.xuathang.xuattheophuongthucdaugia.tochuctrienkhai.ketqua;

import com.tcdt.qlnvhang.request.xuathang.xuattheophuongthucdaugia.tochuctrienkhai.ketqua.XhKqBdgHdrReq;
import com.tcdt.qlnvhang.table.xuathang.xuattheophuongthucdaugia.tochuctrienkhai.ketqua.XhKqBdgHdr;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface XhKqBdgHdrRepository extends JpaRepository<XhKqBdgHdr, Long> {

    @Query("SELECT c FROM XhKqBdgHdr c where 1 = 1" +
            "AND (:#{#param.maDvi} IS NULL OR c.maDvi = :#{#param.maDvi}) " +
            "AND (:#{#param.nam} IS NULL OR c.nam = :#{#param.nam}) " +
            "AND (:#{#param.loaiVthh } IS NULL OR LOWER(c.loaiVthh) LIKE CONCAT(:#{#param.loaiVthh},'%')) "
    )
    Page<XhKqBdgHdr> search(@Param("param") XhKqBdgHdrReq param, Pageable pageable);

    XhKqBdgHdr findByMaThongBao(String maThongBao);

}