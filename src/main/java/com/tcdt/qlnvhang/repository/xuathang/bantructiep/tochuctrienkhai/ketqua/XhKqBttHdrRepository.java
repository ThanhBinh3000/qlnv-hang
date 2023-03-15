package com.tcdt.qlnvhang.repository.xuathang.bantructiep.tochuctrienkhai.ketqua;

import com.tcdt.qlnvhang.entities.xuathang.bantructiep.tochuctrienkhai.ketqua.XhKqBttHdr;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.tochuctrienkhai.ketqua.XhKqBttHdrReq;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface XhKqBttHdrRepository  extends JpaRepository<XhKqBttHdr, Long> {

    @Query("SELECT DISTINCT QD FROM XhKqBttHdr QD " +
            " LEFT JOIN XhKqBttDtl DTL on QD.id = DTL.idHdr " +
            " WHERE 1 = 1 " +
            "AND (:#{#param.maDvi} IS NULL OR QD.maDvi = :#{#param.maDvi}) " +
            "AND (:#{#param.namKh} IS NULL OR QD.namKh = :#{#param.namKh}) " +
            "AND (:#{#param.loaiVthh } IS NULL OR LOWER(QD.loaiVthh) LIKE CONCAT(:#{#param.loaiVthh},'%')) " +
            "AND (:#{#param.maChiCuc} IS NULL OR DTL.maDvi = :#{#param.maChiCuc}) "
    )
    Page<XhKqBttHdr> searchPage(@Param("param") XhKqBttHdrReq param, Pageable pageable);
    XhKqBttHdr findBySoQdPd(String soQdPd);

    Optional<XhKqBttHdr> findBySoQdKq(String soQdKq);


}
