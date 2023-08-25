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
public interface XhQdPdKhBdgDtlRepository extends JpaRepository<XhQdPdKhBdgDtl, Long> {

    @Query("SELECT DISTINCT dtl FROM XhQdPdKhBdgDtl dtl " +
            " left join XhQdPdKhBdg hdr on hdr.id = dtl.idQdHdr WHERE 1=1 " +
            "AND (:#{#param.dvql} IS NULL OR dtl.maDvi LIKE CONCAT(:#{#param.dvql},'%')) " +
            "AND (:#{#param.nam} IS NULL OR hdr.nam = :#{#param.nam}) " +
            "AND (:#{#param.soDxuat} IS NULL OR LOWER(dtl.soDxuat) LIKE LOWER(CONCAT(CONCAT('%',:#{#param.soDxuat}),'%' ) ) )" +
            "AND (:#{#param.soQdPd} IS NULL OR LOWER(hdr.soQdPd) LIKE LOWER(CONCAT(CONCAT('%',:#{#param.soQdPd}),'%' ) ) )" +
            "AND (:#{#param.soQdPdKqBdg} IS NULL OR LOWER(dtl.soQdPdKqBdg) LIKE LOWER(CONCAT(CONCAT('%',:#{#param.soQdPdKqBdg}),'%' ) ) )" +
            "AND (:#{#param.ngayKyQdPdKqBdgTu} IS NULL OR dtl.ngayKyQdPdKqBdg >= :#{#param.ngayKyQdPdKqBdgTu}) " +
            "AND (:#{#param.ngayKyQdPdKqBdgDen} IS NULL OR dtl.ngayKyQdPdKqBdg <= :#{#param.ngayKyQdPdKqBdgDen}) " +
            "AND (:#{#param.trangThai} IS NULL OR dtl.trangThai = :#{#param.trangThai}) " +
            "AND (:#{#param.lastest} IS NULL OR LOWER(hdr.lastest) LIKE LOWER(CONCAT(CONCAT('%',:#{#param.lastest}),'%'))) " +
            "AND (:#{#param.loaiVthh } IS NULL OR LOWER(hdr.loaiVthh) LIKE CONCAT(:#{#param.loaiVthh},'%'))")
    Page<XhQdPdKhBdgDtl> searchDtl(@Param("param") XhQdPdKhBdgDtlReq param, Pageable pageable);

    @Query(value = " SELECT NVL(SUM(TTHDR.SO_DVI_TSAN),0) FROM XH_QD_PD_KH_BDG_DTL DTL" +
            " INNER JOIN XH_TC_TTIN_BDG_HDR TTHDR on DTL.ID = TTHDR.ID_QD_PD_DTL" +
            " WHERE TTHDR.KET_QUA = 1 AND DTL.ID = :idQdPdDtl AND DTL.MA_DVI = :maDvi AND TTHDR.TRANG_THAI='45'",
            nativeQuery = true)
    BigDecimal countSlDviTsanThanhCong(Long idQdPdDtl, String maDvi);

    void deleteAllByIdQdHdr(Long idQdHdr);

    List<XhQdPdKhBdgDtl> findAllByIdQdHdr(Long idQdHdr);

    List<XhQdPdKhBdgDtl> findByIdQdHdrIn(List<Long> listId);

    List<XhQdPdKhBdgDtl> findByIdIn(List<Long> idDtlList);

}
