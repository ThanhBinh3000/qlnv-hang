package com.tcdt.qlnvhang.repository.xuathang.bantructiep.xuatkho.bienbantinhkho;

import com.tcdt.qlnvhang.entities.xuathang.bantructiep.xuatkho.bienbantinhkho.XhBbTinhkBttHdr;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.xuatkho.bienbantinhkho.XhBbTinhkBttHdrReq;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface XhBbTinhkBttHdrRepository extends JpaRepository<XhBbTinhkBttHdr, Long> {

    @Query("SELECT DX from XhBbTinhkBttHdr DX WHERE 1 = 1 " +
            "AND (:#{#param.namKh} IS NULL OR DX.namKh = :#{#param.namKh}) " +
            "AND (:#{#param.soBbTinhKho} IS NULL OR LOWER(DX.soBbTinhKho) LIKE LOWER(CONCAT(CONCAT('%',:#{#param.soBbTinhKho}),'%' ) ) )" +
            "AND (:#{#param.trangThai} IS NULL OR DX.trangThai = :#{#param.trangThai}) " +
            "AND (:#{#param.maDvi} IS NULL OR DX.maDvi = :#{#param.maDvi})")
    Page<XhBbTinhkBttHdr> searchPage(@Param("param") XhBbTinhkBttHdrReq param, Pageable pageable);

    List<XhBbTinhkBttHdr> findAllByIdQd(Long idQd);



}
