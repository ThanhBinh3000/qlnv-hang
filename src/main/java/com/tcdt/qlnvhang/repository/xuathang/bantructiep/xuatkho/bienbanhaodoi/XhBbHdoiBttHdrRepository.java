package com.tcdt.qlnvhang.repository.xuathang.bantructiep.xuatkho.bienbanhaodoi;

import com.tcdt.qlnvhang.entities.xuathang.bantructiep.xuatkho.bienbanhaodoi.XhBbHdoiBttHdr;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.xuatkho.bienbanhaodoi.XhBbHdoiBttHdrReq;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface XhBbHdoiBttHdrRepository extends JpaRepository<XhBbHdoiBttHdr, Long> {
    @Query("SELECT HD from XhBbHdoiBttHdr HD WHERE 1 = 1 " +
            "AND (:#{#param.namKh} IS NULL OR HD.namKh = :#{#param.namKh}) " +
            "AND (:#{#param.soBbHaoDoi} IS NULL OR LOWER(HD.soBbHaoDoi) LIKE LOWER(CONCAT(CONCAT('%',:#{#param.soBbHaoDoi}),'%' ) ) )" +
            "AND (:#{#param.trangThai} IS NULL OR HD.trangThai = :#{#param.trangThai}) " +
            "AND (:#{#param.maDvi} IS NULL OR HD.maDvi = :#{#param.maDvi})")
    Page<XhBbHdoiBttHdr> searchPage(@Param("param") XhBbHdoiBttHdrReq param, Pageable pageable);

    List<XhBbHdoiBttHdr> findAllByIdQd(Long idQd);
}
