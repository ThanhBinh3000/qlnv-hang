package com.tcdt.qlnvhang.repository.xuathang.daugia.kehoach.pheduyet;

import com.tcdt.qlnvhang.entities.xuathang.daugia.kehoach.pheduyet.XhQdPdKhBdgPlDtl;
import com.tcdt.qlnvhang.util.Contains;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface XhQdPdKhBdgPlDtlRepository extends JpaRepository<XhQdPdKhBdgPlDtl, Long> {

    void deleteAllByIdPhanLo(Long idPhanLo);

    List<XhQdPdKhBdgPlDtl> findAllByIdPhanLo(Long idPhanLo);

    List<XhQdPdKhBdgPlDtl> findByIdPhanLoIn(List<Long> listId);

    @Query(value = "SELECT COALESCE(NULLIF(dtl.GIA_QD_DC_TCDTNN, 0), dtl.GIA_QD_TCDTNN) " +
            "FROM KH_PAG_TONG_HOP_CTIET dtl " +
            "JOIN KH_PAG_GCT_QD_TCDTNN hdr ON dtl.QD_TCDTNN_ID = hdr.ID " +
            "WHERE hdr.CLOAI_VTHH = :cloaiVthh " +
            "AND hdr.LOAI_VTHH = :loaiVthh " +
            "AND hdr.NAM_KE_HOACH = :namKh " +
            "AND hdr.LOAI_GIA = 'LG04' " +
            "AND dtl.MA_CHI_CUC = :maDvi " +
            "AND hdr.TRANG_THAI = '" + Contains.BAN_HANH + "'" +
            "AND hdr.NGAY_KY <= CURRENT_DATE " +
            "ORDER BY hdr.NGAY_KY DESC " +
            "FETCH FIRST 1 ROWS ONLY",
            nativeQuery = true)
    BigDecimal getGiaDuocDuyetLuongThuc(
            @Param("cloaiVthh") String cloaiVthh,
            @Param("loaiVthh") String loaiVthh,
            @Param("namKh") Long namKh,
            @Param("maDvi") String maDvi);

    @Query(value = "SELECT COALESCE(NULLIF(dtl.GIA_QD_DC_TCDT, 0), dtl.GIA_QD_TCDT) " +
            "FROM KH_PAG_TT_CHUNG dtl " +
            "JOIN KH_PAG_GCT_QD_TCDTNN hdr ON dtl.QD_TCDTNN_ID = hdr.ID " +
            "WHERE (dtl.CLOAI_VTHH = :cloaiVthh OR dtl.CLOAI_VTHH IS NULL) " +
            "AND dtl.LOAI_VTHH = :loaiVthh " +
            "AND hdr.NAM_KE_HOACH = :namKh " +
            "AND hdr.LOAI_GIA = 'LG04' " +
            "AND hdr.TRANG_THAI = '" + Contains.BAN_HANH + "'" +
            "AND hdr.NGAY_KY <= CURRENT_DATE " +
            "ORDER BY hdr.NGAY_KY DESC " +
            "FETCH FIRST 1 ROWS ONLY",
            nativeQuery = true)
    BigDecimal getGiaDuocDuyetVatTu(
            @Param("cloaiVthh") String cloaiVthh,
            @Param("loaiVthh") String loaiVthh,
            @Param("namKh") Long namKh);
}
