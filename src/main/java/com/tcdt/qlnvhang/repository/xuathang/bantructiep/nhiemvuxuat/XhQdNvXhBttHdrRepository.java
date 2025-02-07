package com.tcdt.qlnvhang.repository.xuathang.bantructiep.nhiemvuxuat;

import com.tcdt.qlnvhang.entities.xuathang.bantructiep.nhiemvuxuat.XhQdNvXhBttHdr;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.nhiemvuxuat.XhQdNvXhBttHdrReq;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface XhQdNvXhBttHdrRepository extends JpaRepository<XhQdNvXhBttHdr, Long> {

    @Query("SELECT DISTINCT QD FROM XhQdNvXhBttHdr QD " +
            "LEFT JOIN XhQdNvXhBttDtl DTL ON QD.id = DTL.idHdr " +
            "WHERE (:#{#param.dvql} IS NULL OR QD.maDvi LIKE CONCAT(:#{#param.dvql}, '%')) " +
            "AND (:#{#param.namKh} IS NULL OR QD.namKh = :#{#param.namKh}) " +
            "AND (:#{#param.soQdNv} IS NULL OR LOWER(QD.soQdNv) LIKE LOWER(CONCAT('%', :#{#param.soQdNv}, '%'))) " +
            "AND (:#{#param.loaiVthh } IS NULL OR LOWER(QD.loaiVthh) LIKE CONCAT(:#{#param.loaiVthh}, '%')) " +
            "AND (:#{#param.trichYeu} IS NULL OR LOWER(QD.trichYeu) LIKE LOWER(CONCAT('%', :#{#param.trichYeu}, '%'))) " +
            "AND (:#{#param.ngayKyQdNvTu} IS NULL OR QD.ngayKyQdNv >= :#{#param.ngayKyQdNvTu}) " +
            "AND (:#{#param.ngayKyQdNvDen} IS NULL OR QD.ngayKyQdNv <= :#{#param.ngayKyQdNvDen}) " +
            "AND (:#{#param.trangThai} IS NULL OR QD.trangThai = :#{#param.trangThai}) " +
            "AND (:#{#param.maDviCon} IS NULL OR DTL.maDvi LIKE CONCAT(:#{#param.maDviCon}, '%')) " +
            "AND (:#{#param.pthucBanTrucTiep} IS NULL OR LOWER(QD.pthucBanTrucTiep) LIKE LOWER(CONCAT('%', :#{#param.pthucBanTrucTiep}, '%'))) " +
            "ORDER BY QD.namKh DESC, QD.ngaySua DESC, QD.ngayTao DESC, QD.id DESC")
    Page<XhQdNvXhBttHdr> searchPage(@Param("param") XhQdNvXhBttHdrReq param, Pageable pageable);

    boolean existsBySoQdNv(String soQdNv);

    boolean existsBySoQdNvAndIdNot(String soQdNv, Long id);

    List<XhQdNvXhBttHdr> findByIdIn(List<Long> idQdList);

    List<XhQdNvXhBttHdr> findAllByIdIn(List<Long> listId);

    @Modifying
    @Query(value = "UPDATE XH_QD_NV_XH_BTT_HDR QD " +
            "SET QD.SO_HOP_DONG = CONCAT(COALESCE(QD.SO_HOP_DONG, ''), :newSoHopDong) WHERE QD.ID = :idQdNv", nativeQuery = true)
    void updateSoHopDong(@Param("newSoHopDong") String newSoHopDong, @Param("idQdNv") Long idQdNv);

    @Modifying
    @Query(value = "UPDATE XH_QD_NV_XH_BTT_HDR QD " +
            "SET QD.SO_BANG_KE_BAN_LE = CONCAT(COALESCE(QD.SO_BANG_KE_BAN_LE, ''), :newBangKeBanLe) WHERE QD.ID = :idQdNv", nativeQuery = true)
    void updateSoBangKeBanLe(@Param("newBangKeBanLe") String newBangKeBanLe, @Param("idQdNv") Long idQdNv);
}