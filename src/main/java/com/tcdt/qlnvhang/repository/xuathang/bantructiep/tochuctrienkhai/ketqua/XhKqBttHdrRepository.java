package com.tcdt.qlnvhang.repository.xuathang.bantructiep.tochuctrienkhai.ketqua;

import com.tcdt.qlnvhang.entities.xuathang.bantructiep.tochuctrienkhai.ketqua.XhKqBttHdr;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.tochuctrienkhai.ketqua.XhKqBttHdrReq;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface XhKqBttHdrRepository extends JpaRepository<XhKqBttHdr, Long> {

    @Query("SELECT QD FROM XhKqBttHdr QD " +
            " LEFT JOIN XhQdPdKhBttDtl DTL on QD.id = DTL.idQdKq " +
            " WHERE 1=1 " +
            "AND (:#{#param.dvql} IS NULL OR QD.maDvi LIKE CONCAT(:#{#param.dvql},'%')) " +
            "AND (:#{#param.namKh} IS NULL OR QD.namKh = :#{#param.namKh}) " +
            "AND (:#{#param.soQdKq} IS NULL OR LOWER(QD.soQdKq) LIKE LOWER(CONCAT(CONCAT('%',:#{#param.soQdKq}),'%' ) ) )" +
            "AND (:#{#param.ngayCgiaTu} IS NULL OR DTL.ngayNhanCgia >= :#{#param.ngayCgiaTu}) " +
            "AND (:#{#param.ngayCgiaDen} IS NULL OR DTL.ngayNhanCgia <= :#{#param.ngayCgiaDen}) " +
            "AND (:#{#param.loaiVthh} IS NULL OR QD.loaiVthh LIKE CONCAT(:#{#param.loaiVthh},'%')) " +
            "AND (:#{#param.trichYeu} IS NULL OR LOWER(QD.trichYeu) LIKE LOWER(CONCAT(CONCAT('%',:#{#param.trichYeu}),'%'))) " +
            "AND (:#{#param.trangThai} IS NULL OR QD.trangThai = :#{#param.trangThai}) " +
            "ORDER BY QD.ngaySua desc , QD.ngayTao desc, QD.id desc")
    Page<XhKqBttHdr> searchPage(@Param("param") XhKqBttHdrReq param, Pageable pageable);

    Optional<XhKqBttHdr> findBySoQdKq(String soQdKq);

    List<XhKqBttHdr> findByIdIn(List<Long> idQdList);

    List<XhKqBttHdr> findAllByIdIn(List<Long> listId);
}
