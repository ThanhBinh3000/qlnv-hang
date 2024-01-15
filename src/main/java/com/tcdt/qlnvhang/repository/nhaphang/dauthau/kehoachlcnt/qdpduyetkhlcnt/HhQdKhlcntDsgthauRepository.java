package com.tcdt.qlnvhang.repository.nhaphang.dauthau.kehoachlcnt.qdpduyetkhlcnt;

import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kehoachlcnt.qdpduyetkhlcnt.HhQdKhlcntDsgthau;
import com.tcdt.qlnvhang.repository.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Temporal;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface HhQdKhlcntDsgthauRepository extends BaseRepository<HhQdKhlcntDsgthau, Long> {

    HhQdKhlcntDsgthau findByGoiThauAndIdQdDtl(String gthau, Long IdQdDtl);
    List<HhQdKhlcntDsgthau> findByIdQdDtl(Long IdQdDtl);
    List<HhQdKhlcntDsgthau> findByIdQdDtlOrderByGoiThauAsc(Long IdQdDtl);
    List<HhQdKhlcntDsgthau> findByIdQdPdHsmtOrderByGoiThauAsc(Long idQdPdHsmt);
    List<HhQdKhlcntDsgthau> findAllByIdQdPdHsmtIn(List<Long> idQdPdHsmt);
    List<HhQdKhlcntDsgthau> findByIdQdHdr(Long IdQdHdr);
    List<HhQdKhlcntDsgthau> findByIdQdHdrOrderByGoiThauAsc(Long IdQdHdr);

    @Transactional()
    @Modifying
    @Query(value = "UPDATE HH_QD_KHLCNT_DSGTHAU SET TRANG_THAI =:trangThai , LY_DO_HUY =:lyDoHuy WHERE ID =TO_NUMBER(:idGt) ", nativeQuery = true)
    void updateGoiThau(Long idGt, String trangThai, String lyDoHuy);

    @Transactional()
    @Modifying
    @Query(value = "UPDATE HH_QD_KHLCNT_DSGTHAU SET TGIAN_BDAU_TCHUC = CASE WHEN :tgianBdauTchuc IS NULL THEN NULL ELSE :tgianBdauTchuc END," +
            " TGIAN_MTHAU = CASE WHEN :tgianMthau IS NULL THEN NULL ELSE :tgianMthau END, " +
            " TGIAN_DTHAU = CASE WHEN :tgianDthau IS NULL THEN NULL ELSE :tgianDthau END" +
            " WHERE ID_QD_PD_HSMT = TO_NUMBER(:idQdPdHsmt) ", nativeQuery = true)
    void updateThongTinQdPdHsmt(@Temporal Date tgianBdauTchuc, @Temporal Date tgianMthau, @Temporal Date tgianDthau, Long idQdPdHsmt);

    void deleteByIdQdDtl(Long idQdDtl);
    void deleteByIdQdHdr(Long idQdHdr);

    @Query(value = "SELECT NVL(SUM(DTL.THANH_TIEN),0) FROM HH_QD_KHLCNT_DSGTHAU DTL WHERE DTL.ID_QD_DTL = :idQdDtl ", nativeQuery = true)
    BigDecimal sumTotalPriceByIdQdDtl(Long idQdDtl);

    @Query(value = "SELECT gthau.* FROM HH_QD_KHLCNT_DSGTHAU gthau" +
            " JOIN HH_QD_KHLCNT_DTL dtl ON gthau.ID_QD_DTL = dtl.ID " +
            " JOIN HH_QD_KHLCNT_HDR hdr ON dtl.ID_QD_HDR = hdr.ID " +
            " WHERE 1 = 1 " +
            " AND (:cloaiVthh IS NULL OR gthau.CLOAI_VTHH = :cloaiVthh)" +
            " AND (:loaiVthh IS NULL OR gthau.LOAI_VTHH = :loaiVthh)" +
            " AND (:namKh IS NULL OR hdr.NAM_KHOACH = :namKh)" +
            " AND gthau.TRANG_THAI_DT IN ('41', '36', '84')"
            , nativeQuery = true)
    List<HhQdKhlcntDsgthau> danhSachGthauTruot (String cloaiVthh, String loaiVthh, Integer namKh);

    @Query(value = "SELECT gthau.* FROM HH_QD_KHLCNT_DSGTHAU gthau" +
            " JOIN HH_QD_KHLCNT_HDR hdr ON gthau.ID_QD_HDR = hdr.ID " +
            " WHERE 1 = 1 " +
            " AND (:cloaiVthh IS NULL OR gthau.CLOAI_VTHH = :cloaiVthh)" +
            " AND (:loaiVthh IS NULL OR gthau.LOAI_VTHH = :loaiVthh)" +
            " AND (:namKh IS NULL OR hdr.NAM_KHOACH = :namKh)" +
            " AND gthau.TRANG_THAI_DT IN ('41', '36', '84')"
            , nativeQuery = true)
    List<HhQdKhlcntDsgthau> danhSachGthauTruotVt (String cloaiVthh, String loaiVthh, Integer namKh);

    @Query(value = "SELECT gthau.* FROM HH_QD_KHLCNT_DSGTHAU gthau" +
            " WHERE 1 = 1 " +
            " AND (:idQdDtl IS NULL OR gthau.ID_QD_DTL = :idQdDtl)" +
            " AND gthau.ID_NHA_THAU IS NULL" +
            " AND gthau.ID IN (SELECT nt.ID_DT_GT FROM HH_DTHAU_NTHAU_DUTHAU nt)"
            , nativeQuery = true)
    List<HhQdKhlcntDsgthau> dsGthauDaCoTtNthau(Long idQdDtl);

    @Query(value = "SELECT gthau.* FROM HH_QD_KHLCNT_DSGTHAU gthau" +
            " WHERE 1 = 1 " +
            " AND (:idQdDtl IS NULL OR gthau.ID_QD_HDR = :idQdDtl)" +
            " AND gthau.ID_NHA_THAU IS NULL" +
            " AND gthau.ID IN (SELECT nt.ID_DT_GT FROM HH_DTHAU_NTHAU_DUTHAU nt)"
            , nativeQuery = true)
    List<HhQdKhlcntDsgthau> dsGthauDaCoTtNthauVt(Long idQdDtl);
}
