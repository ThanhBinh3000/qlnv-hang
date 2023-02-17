package com.tcdt.qlnvhang.repository.xuathang.bantructiep.tochuctrienkhai.ketqua;

import com.tcdt.qlnvhang.entities.xuathang.bantructiep.tochuctrienkhai.ketqua.XhKqBttHdr;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.tochuctrienkhai.ketqua.XhKqBttHdrReq;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface XhKqBttHdrRepository  extends JpaRepository<XhKqBttHdr, Long> {

    @Query("SELECT c FROM XhKqBttHdr c where 1 = 1" +
            "AND (:#{#param.maDvi} IS NULL OR c.maDvi = :#{#param.maDvi}) " +
            "AND (:#{#param.namKh} IS NULL OR c.namKh = :#{#param.namKh}) " +
            "AND (:#{#param.loaiVthh } IS NULL OR LOWER(c.loaiVthh) LIKE CONCAT(:#{#param.loaiVthh},'%')) " +
            "AND (:#{#param.trangThai} IS NULL OR c.trangThai = :#{#param.trangThai}) "
    )
    Page<XhKqBttHdr> search(@Param("param") XhKqBttHdrReq param, Pageable pageable);

    XhKqBttHdr findBySoQdPd(String soQdPd);


}
