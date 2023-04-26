package com.tcdt.qlnvhang.repository.dieuchuyennoibo;

import com.tcdt.qlnvhang.table.catalog.QlnvDmDonvi;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbKeHoachDcDtl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DcnbKeHoachDcDtlRepository extends JpaRepository<DcnbKeHoachDcDtl,Long> {

    List<DcnbKeHoachDcDtl> findByDcnbKeHoachDcHdrId(Long idHdr);

    List<DcnbKeHoachDcDtl> findByDcnbKeHoachDcHdrIdIn(List<Long> ids);

    @Query(nativeQuery = true,value ="SELECT * FROM DCNB_KE_HOACH_DC_DTL KHDTL\n" +
            "LEFT JOIN DCNB_KE_HOACH_DC_HDR KHHDR ON KHHDR.ID = KHDTL.HDR_ID\n" +
            "WHERE KHHDR.MA_DVI = ?1 AND KHHDR.TRANG_THAI = ?2 AND KHHDR.TYPE = ?3 AND KHHDR.LOAI_DC = ?4 AND (TO_DATE(TO_CHAR(KHHDR.NGAY_TAO,'YYYY-MM-DD HH24:MI:SS'),'YYYY-MM-DD HH24:MI:SS') <= TO_DATE(?5,'YYYY-MM-DD HH24:MI:SS'))")
    List<DcnbKeHoachDcDtl> findByDonViAndTrangThaiChiCuc(String maDvi, String trangThai,String type,String loaiDieuChuyen, String thoiGianTongHop);

    @Query(nativeQuery = true,value ="SELECT * FROM DCNB_KE_HOACH_DC_DTL KHDTL\n" +
            "LEFT JOIN DCNB_KE_HOACH_DC_HDR KHHDR ON KHHDR.ID = KHDTL.HDR_ID\n" +
            "LEFT JOIN DM_DONVI dv ON dv.MA_DVI = hdr.MA_DVI \n" +
            "WHERE dv.MA_DVI_CHA = ?1 AND KHHDR.TRANG_THAI = ?2 AND (TO_DATE(TO_CHAR(KHHDR.NGAY_TAO,'YYYY-MM-DD'),'YYYY-MM-DD') <= TO_DATE(?3,'YYYY-MM-DD')) AND KHHDR.LOAI_DC = 'CHI_CUC' AND KHHDR.TYPE = 'DC'")
    List<DcnbKeHoachDcDtl> findByDonViChaAndTrangThaiChiCuc(String maDvi, String trangThai, String thoiGianTongHop);

    @Query(nativeQuery = true,value ="SELECT * FROM DCNB_KE_HOACH_DC_DTL KHDTL\n" +
            "LEFT JOIN DCNB_KE_HOACH_DC_HDR KHHDR ON KHHDR.ID = KHDTL.HDR_ID\n" +
            "WHERE KHHDR.MA_DVI = ?1 AND KHHDR.TRANG_THAI = ?2 AND (TO_DATE(TO_CHAR(KHHDR.NGAY_TAO,'YYYY-MM-DD'),'YYYY-MM-DD') <= TO_DATE(?3,'YYYY-MM-DD')) AND KHHDR.LOAI_DC = 'CUC' AND KHHDR.TYPE = 'DC'")
    List<DcnbKeHoachDcDtl> findByDonViChaAndTrangThaiCuc(String maDvi,String trangThai, String thoiGianTongHop);

    List<DcnbKeHoachDcDtl> findByDcnbKeHoachDcHdrIdAndId(Long hdrId, Long id);
    @Query(nativeQuery = true,value ="SELECT SUM(d.DU_TOAN_KPHI) FROM DCNB_KE_HOACH_DC_DTL d " +
            "LEFT JOIN DCNB_KE_HOACH_DC_HDR h ON h.ID = d.HDR_ID " +
            "WHERE h.MA_DVI = ?1 AND h.MA_DVI_CUC = ?2 AND h.MA_CUC_NHAN = ?3 AND h.TYPE = 'DC' AND h.LOAI_DC = 'CUC' AND h.TRANG_THAI = '17' AND (TO_DATE(TO_CHAR(h.NGAY_TAO,'YYYY-MM-DD HH24:MI:SS'),'YYYY-MM-DD HH24:MI:SS') <= TO_DATE(?4,'YYYY-MM-DD HH24:MI:SS'))")
    Long findByMaDviCucAndTypeAndLoaiDc(String maDVi, String dvql, String maCucNhan, String thoigianTongHop);

    @Query(nativeQuery = true,value ="SELECT SUM(d.DU_TOAN_KPHI) FROM DCNB_KE_HOACH_DC_DTL d " +
            "LEFT JOIN DCNB_KE_HOACH_DC_HDR h ON h.ID = d.HDR_ID " +
            "WHERE h.MA_DVI_CUC = ?1 AND h.TYPE = ?2 AND h.LOAI_DC = ?3 AND h.TRANG_THAI = ?4 " +
            "AND (d.LOAI_VTHH IS NULL OR d.LOAI_VTHH = ?5 )" +
            "AND (d.CLOAI_VTHH IS NULL OR d.CLOAI_VTHH = ?6)"+
            "AND (TO_DATE(TO_CHAR(h.NGAY_TAO,'YYYY-MM-DD HH24:MI:SS'),'YYYY-MM-DD HH24:MI:SS') <= TO_DATE(?7,'YYYY-MM-DD HH24:MI:SS'))")
    Long findByMaDviCucAndTypeAndLoaiDcTongCucChiCuc(String maDVi, String type, String loaiDieuChuyen, String trangThai,String loaiHH, String chungLoaiHH , String thoigianTongHop);

    @Query(nativeQuery = true,value ="SELECT SUM(d.DU_TOAN_KPHI) FROM DCNB_KE_HOACH_DC_DTL d " +
            "LEFT JOIN DCNB_KE_HOACH_DC_HDR h ON h.ID = d.HDR_ID " +
            "WHERE h.MA_DVI_CUC = ?1 AND h.MA_CUC_NHAN = ?2 AND h.TYPE = ?2 AND h.LOAI_DC = ?3 AND h.TRANG_THAI = ?4 " +
            "AND (d.LOAI_VTHH IS NULL OR d.LOAI_VTHH = ?5 )" +
            "AND (d.CLOAI_VTHH IS NULL OR d.CLOAI_VTHH = ?6)"+
            "AND (TO_DATE(TO_CHAR(h.NGAY_TAO,'YYYY-MM-DD HH24:MI:SS'),'YYYY-MM-DD HH24:MI:SS') <= TO_DATE(?7,'YYYY-MM-DD HH24:MI:SS'))")
    Long findByMaDviCucAndTypeAndLoaiDcTongCucCuc(String maDVi,String maDviCucNhan, String type, String loaiDieuChuyen, String trangThai,String loaiHH, String chungLoaiHH , String thoigianTongHop);

    @Query(nativeQuery = true,value = "SELECT * FROM DCNB_KE_HOACH_DC_DTL d " +
            "WHERE d.HDR_ID = ?1 AND (d.LOAI_VTHH IS NULL OR d.LOAI_VTHH = ?2) AND (d.CLOAI_VTHH IS NULL OR d.CLOAI_VTHH = ?3)")
    List<DcnbKeHoachDcDtl> findByDcnbKeHoachDcHdrIdAndLoaiHhAndCLoaiHh(Long keHoachDcHdrId,String trangThai,String loaiHH);
}