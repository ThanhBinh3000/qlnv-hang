package com.tcdt.qlnvhang.repository.xuathang.daugia.kehoach.pheduyet;

import com.tcdt.qlnvhang.entities.xuathang.daugia.kehoach.pheduyet.XhQdPdKhBdgDtl;
import com.tcdt.qlnvhang.request.xuathang.daugia.kehoachbdg.pheduyet.XhQdPdKhBdgDtlReq;
import feign.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface XhQdPdKhBdgDtlRepository extends JpaRepository<XhQdPdKhBdgDtl,Long> {

    List<XhQdPdKhBdgDtl> findAllByIdQdHdr (Long idQdHdr);

    void deleteAllByIdQdHdr(Long idQdHdr);

    @Query(value = " SELECT dtl.GIA_QD FROM KH_PAG_TONG_HOP_CTIET dtl " +
            "JOIN KH_PAG_GCT_QD_TCDTNN hdr ON dtl.QD_TCDTNN_ID = hdr.ID " +
            " WHERE hdr.TRANG_THAI = '29'   AND hdr.CLOAI_VTHH = :cloaiVthh AND dtl.MA_DVI = :maDvi AND hdr.NAM_KE_HOACH = :namKhoach AND hdr.NGAY_HIEU_LUC <= SYSDATE " +
            " FETCH FIRST 1 ROWS ONLY ",
            nativeQuery = true)
    BigDecimal getDonGiaVatLt(String cloaiVthh, String maDvi, Integer namKhoach);

    @Query(value = " SELECT dtl.GIA_QD FROM KH_PAG_TT_CHUNG dtl " +
            "JOIN KH_PAG_GCT_QD_TCDTNN hdr ON dtl.QD_TCDTNN_ID = hdr.ID " +
            " WHERE hdr.TRANG_THAI = '29'   AND dtl.CLOAI_VTHH = :cloaiVthh AND hdr.NAM_KE_HOACH = :namKhoach AND hdr.NGAY_HIEU_LUC <= SYSDATE " +
            " FETCH FIRST 1 ROWS ONLY ",
            nativeQuery = true)
    BigDecimal getDonGiaVatVt(String cloaiVthh, Integer namKhoach);

    @Query("SELECT DISTINCT dtl FROM XhQdPdKhBdgDtl dtl " +
            " left join XhQdPdKhBdg hdr on hdr.id = dtl.idQdHdr WHERE 1=1 " +
            "AND (:#{#param.nam} IS NULL OR hdr.nam = :#{#param.nam}) " +
            "AND (:#{#param.maDvi} IS NULL OR dtl.maDvi = :#{#param.maDvi}) " +
            "AND (:#{#param.soDxuat} IS NULL OR LOWER(dtl.soDxuat) LIKE LOWER(CONCAT(CONCAT('%',:#{#param.soDxuat}),'%' ) ) )" +
            "AND (:#{#param.soQdPd} IS NULL OR LOWER(hdr.soQdPd) LIKE LOWER(CONCAT(CONCAT('%',:#{#param.soQdPd}),'%' ) ) )" +
            "AND (:#{#param.soQdPdKqBdg} IS NULL OR LOWER(dtl.soQdPdKqBdg) LIKE LOWER(CONCAT(CONCAT('%',:#{#param.soQdPdKqBdg}),'%' ) ) )" +
            "AND (:#{#param.ngayKyQdPdKqBdgTu} IS NULL OR dtl.ngayKyQdPdKqBdg >= :#{#param.ngayKyQdPdKqBdgTu}) " +
            "AND (:#{#param.ngayKyQdPdKqBdgDen} IS NULL OR dtl.ngayKyQdPdKqBdg <= :#{#param.ngayKyQdPdKqBdgDen}) " +
            "AND (:#{#param.trangThai} IS NULL OR dtl.trangThai = :#{#param.trangThai}) " +
            "AND (:#{#param.lastest} IS NULL OR LOWER(hdr.lastest) LIKE LOWER(CONCAT(CONCAT('%',:#{#param.lastest}),'%'))) " +
            "AND (:#{#param.loaiVthh } IS NULL OR LOWER(hdr.loaiVthh) LIKE CONCAT(:#{#param.loaiVthh},'%')) "
    )
    Page<XhQdPdKhBdgDtl> searchDtl(@Param("param") XhQdPdKhBdgDtlReq param, Pageable pageable);

}
