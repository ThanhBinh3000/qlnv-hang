package com.tcdt.qlnvhang.repository.xuathang.bantructiep.kehoach.pheduyet;

import com.tcdt.qlnvhang.entities.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttHdr;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttHdrReq;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface XhQdPdKhBttHdrRepository extends JpaRepository<XhQdPdKhBttHdr, Long> {

    @Query("SELECT QD from XhQdPdKhBttHdr QD WHERE 1 = 1 " +
            "AND (:#{#param.namKh} IS NULL OR QD.namKh = :#{#param.namKh}) " +
            "AND (:#{#param.soQdPd} IS NULL OR LOWER(QD.soQdPd) LIKE LOWER(CONCAT(CONCAT('%',:#{#param.soQdPd}),'%' ) ) )" +
            "AND (:#{#param.trichYeu} IS NULL OR LOWER(QD.trichYeu) LIKE LOWER(CONCAT(CONCAT('%',:#{#param.trichYeu}),'%'))) " +
            "AND (:#{#param.ngayKyQdTu} IS NULL OR QD.ngayKyQd >= :#{#param.ngayKyQdTu}) " +
            "AND (:#{#param.ngayKyQdDen} IS NULL OR QD.ngayKyQd <= :#{#param.ngayKyQdDen}) " +
            "AND (:#{#param.soTrHdr} IS NULL OR LOWER(QD.soTrHdr) LIKE LOWER(CONCAT(CONCAT('%',:#{#param.soTrHdr}),'%'))) " +
            "AND (:#{#param.loaiVthh} IS NULL OR QD.loaiVthh LIKE CONCAT(:#{#param.loaiVthh},'%')) " +
            "AND (:#{#param.lastest} IS NULL OR LOWER(QD.lastest) LIKE LOWER(CONCAT(CONCAT('%',:#{#param.lastest}),'%'))) " +
            "AND (:#{#param.trangThai} IS NULL OR QD.trangThai = :#{#param.trangThai}) " +
            "AND (:#{#param.maDvi} IS NULL OR QD.maDvi = :#{#param.maDvi})")
    Page<XhQdPdKhBttHdr> searchPage(@Param("param") XhQdPdKhBttHdrReq param, Pageable pageable);

    List<XhQdPdKhBttHdr> findBySoQdPd(String soQdPd);

    List <XhQdPdKhBttHdr> findAllByIdIn(List<Long> ids);

}
