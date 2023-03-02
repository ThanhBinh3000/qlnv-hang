package com.tcdt.qlnvhang.repository.xuathang.bantructiep.nhiemvuxuat;

import com.tcdt.qlnvhang.entities.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttHdr;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.nhiemvuxuat.XhQdNvXhBttHdr;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.nhiemvuxuat.XhQdNvXhBttHdrReq;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface XhQdNvXhBttHdrRepository extends JpaRepository<XhQdNvXhBttHdr, Long> {


    @Query("SELECT DISTINCT QD FROM XhQdNvXhBttHdr QD " +
            " LEFT JOIN XhQdNvXhBttDtl DTL on QD.id = DTL.idQdHdr " +
            " WHERE 1 = 1 " +
            "AND (:#{#param.maDvi} IS NULL OR QD.maDvi = :#{#param.maDvi}) " +
            "AND (:#{#param.namKh} IS NULL OR QD.namKh = :#{#param.namKh}) " +
            "AND (:#{#param.soQd} IS NULL OR LOWER(QD.soQd) LIKE LOWER(CONCAT(CONCAT('%',:#{#param.soQd}),'%' ) ) )" +
            "AND (:#{#param.loaiVthh } IS NULL OR LOWER(QD.loaiVthh) LIKE CONCAT(:#{#param.loaiVthh},'%')) " +
            "AND (:#{#param.trichYeu} IS NULL OR LOWER(QD.trichYeu) LIKE LOWER(CONCAT(CONCAT('%',:#{#param.trichYeu}),'%'))) " +
            "AND (:#{#param.ngayTaoTu} IS NULL OR QD.ngayTao >= :#{#param.ngayTaoTu}) " +
            "AND (:#{#param.ngayTaoDen} IS NULL OR QD.ngayTao <= :#{#param.ngayTaoDen}) " +
            "AND (:#{#param.trangThai} IS NULL OR QD.trangThai = :#{#param.trangThai}) " +
            "AND (:#{#param.maChiCuc} IS NULL OR DTL.maDvi = :#{#param.maChiCuc}) "
    )
    Page<XhQdNvXhBttHdr> searchPage(@Param("param") XhQdNvXhBttHdrReq param, Pageable pageable);
    List<XhQdNvXhBttHdr> findBySoQd(String soQd);

}
