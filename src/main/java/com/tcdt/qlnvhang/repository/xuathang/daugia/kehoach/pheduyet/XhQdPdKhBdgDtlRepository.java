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
            "LEFT JOIN XhQdPdKhBdg hdr ON hdr.id = dtl.idHdr " +
            "WHERE (:#{#param.dvql} IS NULL OR dtl.maDvi LIKE CONCAT(:#{#param.dvql},'%')) " +
            "AND (:#{#param.nam} IS NULL OR dtl.nam = :#{#param.nam}) " +
            "AND (:#{#param.soDxuat} IS NULL OR LOWER(dtl.soDxuat) LIKE LOWER(CONCAT(:#{#param.soDxuat},'%'))) " +
            "AND (:#{#param.soQdPd} IS NULL OR LOWER(dtl.soQdPd) LIKE LOWER(CONCAT(:#{#param.soQdPd},'%'))) " +
            "AND (:#{#param.soQdDc} IS NULL OR LOWER(dtl.soQdDc) LIKE LOWER(CONCAT(:#{#param.soQdDc},'%'))) " +
            "AND (:#{#param.soQdKq} IS NULL OR LOWER(dtl.soQdKq) LIKE LOWER(CONCAT(:#{#param.soQdKq},'%'))) " +
            "AND (:#{#param.ngayKyQdKqTu} IS NULL OR dtl.ngayKyQdKq >= :#{#param.ngayKyQdKqTu}) " +
            "AND (:#{#param.ngayKyQdKqDen} IS NULL OR dtl.ngayKyQdKq <= :#{#param.ngayKyQdKqDen}) " +
            "AND (:#{#param.trangThai} IS NULL OR dtl.trangThai = :#{#param.trangThai}) " +
            "AND (:#{#param.trangThaiHdr} IS NULL OR hdr.trangThai = :#{#param.trangThaiHdr}) " +
            "AND (:#{#param.lastest} IS NULL OR LOWER(hdr.lastest) LIKE LOWER(CONCAT(:#{#param.lastest},'%'))) " +
            "AND (:#{#param.loaiVthh} IS NULL OR LOWER(dtl.loaiVthh) LIKE CONCAT(:#{#param.loaiVthh},'%')) " +
            "ORDER BY dtl.nam DESC, dtl.id DESC")
    Page<XhQdPdKhBdgDtl> searchDtl(@Param("param") XhQdPdKhBdgDtlReq param, Pageable pageable);

    @Query(value = "SELECT COALESCE(SUM(TTHDR.SO_DVI_TSAN), 0) " +
            "FROM XH_QD_PD_KH_BDG_DTL DTL " +
            "INNER JOIN XH_TC_TTIN_BDG_HDR TTHDR ON DTL.ID = TTHDR.ID_QD_PD_DTL" +
            " WHERE TTHDR.KET_QUA = 1" +
            " AND TTHDR.LOAI_VTHH = :loaiVthh " +
            " AND TTHDR.MA_DVI = :maDvi " +
            " AND TTHDR.ID_QD_PD_DTL = :idQdPdDtl " +
            " AND TTHDR.TRANG_THAI = '45' ",
            nativeQuery = true)
    BigDecimal countSlDviTsanThanhCong(@Param("idQdPdDtl") Long idQdPdDtl,
                                       @Param("loaiVthh") String loaiVthh,
                                       @Param("maDvi") String maDvi);

    void deleteAllByIdHdr(Long idHdr);

    List<XhQdPdKhBdgDtl> findAllByIdHdr(Long idHdr);

    List<XhQdPdKhBdgDtl> findByIdHdrIn(List<Long> listId);

    List<XhQdPdKhBdgDtl> findByIdIn(List<Long> idDtlList);
}