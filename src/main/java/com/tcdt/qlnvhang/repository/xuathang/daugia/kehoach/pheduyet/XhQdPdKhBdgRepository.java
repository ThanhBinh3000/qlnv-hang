package com.tcdt.qlnvhang.repository.xuathang.daugia.kehoach.pheduyet;
import com.tcdt.qlnvhang.entities.xuathang.daugia.kehoach.pheduyet.XhQdPdKhBdg;
import com.tcdt.qlnvhang.request.xuathang.daugia.kehoachbdg.pheduyet.XhQdPdKhBdgReq;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface XhQdPdKhBdgRepository extends JpaRepository<XhQdPdKhBdg,Long> {

    @Query("SELECT DISTINCT QD from XhQdPdKhBdg QD WHERE 1 = 1 " +
            "AND (:#{#param.dvql} IS NULL OR QD.maDvi LIKE CONCAT(:#{#param.dvql},'%')) " +
            "AND (:#{#param.nam} IS NULL OR QD.nam = :#{#param.nam}) " +
            "AND (:#{#param.soQdPd} IS NULL OR LOWER(QD.soQdPd) LIKE LOWER(CONCAT(CONCAT('%',:#{#param.soQdPd}),'%' ) ) )" +
            "AND (:#{#param.trichYeu} IS NULL OR LOWER(QD.trichYeu) LIKE LOWER(CONCAT(CONCAT('%',:#{#param.trichYeu}),'%'))) " +
            "AND (:#{#param.ngayKyQdTu} IS NULL OR QD.ngayKyQd >= :#{#param.ngayKyQdTu}) " +
            "AND (:#{#param.ngayKyQdDen} IS NULL OR QD.ngayKyQd <= :#{#param.ngayKyQdDen}) " +
            "AND (:#{#param.soTrHdr} IS NULL OR LOWER(QD.soTrHdr) LIKE LOWER(CONCAT(CONCAT('%',:#{#param.soTrHdr}),'%'))) " +
            "AND (:#{#param.loaiVthh} IS NULL OR QD.loaiVthh LIKE CONCAT(:#{#param.loaiVthh},'%')) " +
            "AND (:#{#param.lastest} IS NULL OR LOWER(QD.lastest) LIKE LOWER(CONCAT(CONCAT('%',:#{#param.lastest}),'%'))) " +
            "AND (:#{#param.trangThai} IS NULL OR QD.trangThai = :#{#param.trangThai}) " +
            "AND (:#{#param.maDvi} IS NULL OR QD.maDvi = :#{#param.maDvi})")
    Page<XhQdPdKhBdg> searchPage(@Param("param") XhQdPdKhBdgReq param, Pageable pageable);

    Optional<XhQdPdKhBdg> findBySoQdPd(String soQdPd);

    List<XhQdPdKhBdg> findByIdIn(List<Long> idDxList);

    List<XhQdPdKhBdg> findAllByIdIn(List<Long> listId);
}
