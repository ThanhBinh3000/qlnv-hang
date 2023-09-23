package com.tcdt.qlnvhang.repository.nhaphang.dauthau.kehoachlcnt.qdpduyetkhlcnt;

import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kehoachlcnt.qdpduyetkhlcnt.HhQdKhlcntDtl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;

public interface HhQdKhlcntDtlRepository extends JpaRepository<HhQdKhlcntDtl, Long> {

    void deleteAllByIdQdHdr(Long idQdHdr);

    List<HhQdKhlcntDtl> findAllByIdQdHdr (Long idQdHdr);
    List<HhQdKhlcntDtl> findAllByIdQdHdrIn (List<Long> ids);

    List<HhQdKhlcntDtl> findByIdQdHdr(Long idQdHdr);

    @Query(value = "SELECT HDR.ID,COUNT( DISTINCT GT.ID ) AS C " +
            " FROM HH_QD_KHLCNT_HDR HDR " +
            " JOIN HH_QD_KHLCNT_DTL dtl ON hdr.id = dtl.ID_QD_HDR " +
            " JOIN HH_QD_KHLCNT_DSGTHAU gt ON dtl.id = gt.ID_QD_DTL " +
            "  WHERE 1=1 " +
            "  AND HDR.ID IN (:qdIds) " +
            "  AND HDR.LASTEST = 0 GROUP BY HDR.ID "
            , nativeQuery = true)
    List<Object[]> countAllBySoGthau(Collection<Long> qdIds);

    @Query(value = "SELECT HDR.ID,COUNT( DISTINCT GT.ID ) AS C " +
            " FROM HH_QD_KHLCNT_HDR HDR " +
            " JOIN HH_QD_KHLCNT_DTL dtl ON hdr.id = dtl.ID_QD_HDR " +
            " JOIN HH_QD_KHLCNT_DSGTHAU gt ON dtl.id = gt.ID_QD_DTL " +
            "  WHERE 1=1 " +
            "  AND HDR.ID IN (:qdIds) " +
            "  AND HDR.LASTEST = 0 " +
            "  AND (:trangThai is null or GT.TRANG_THAI = :trangThai) GROUP BY HDR.ID"
            , nativeQuery = true)
    List<Object[]> countAllBySoGthauStatus(Collection<Long> qdIds,String trangThai);

    @Query(value = "SELECT ID_QD_HDR,NVL(SUM(DTL.TONG_TIEN),0) FROM HH_QD_KHLCNT_DTL DTL WHERE ID_QD_HDR in (:qdIds) group by ID_QD_HDR ",
            nativeQuery = true)
    List<Object[]> sumTongTienByIdHdr(Collection<Long> qdIds);

    @Query(value = " SELECT * FROM HH_QD_KHLCNT_DTL DTL " +
            " LEFT JOIN HH_QD_KHLCNT_HDR HDR ON HDR.ID = DTL.ID_QD_HDR " +
            " LEFT JOIN HH_QD_PDUYET_KQLCNT_HDR PD_HDR ON PD_HDR.ID_QD_PD_KHLCNT_DTL = DTL.ID " +
            " WHERE (:namKh IS NULL OR HDR.NAM_KHOACH = TO_NUMBER(:namKh)) " +
            " AND (:soQd IS NULL OR LOWER(DTL.SO_DXUAT) LIKE LOWER(CONCAT(CONCAT('%', :soQd),'%')))" +
            " AND (:soQdPdKqlcnt IS NULL OR LOWER(PD_HDR.SO_QD) LIKE LOWER(CONCAT(CONCAT('%', :soQdPdKqlcnt),'%')))" +
            " AND (:soQdPdKhlcnt IS NULL OR LOWER(HDR.SO_QD) LIKE LOWER(CONCAT(CONCAT('%', :soQdPdKhlcnt),'%')))" +
            " AND (:loaiVthh IS NULL OR LOWER(HDR.LOAI_VTHH) LIKE LOWER(CONCAT(CONCAT('%', :loaiVthh),'%')))" +
            " AND (:maDvi IS NULL OR LOWER(DTL.MA_DVI) LIKE LOWER(CONCAT(CONCAT('%', :maDvi),'%')))" +
            " AND (:trangThaiCuc IS NULL OR DTL.TRANG_THAI = :trangThaiCuc)" +
            " AND HDR.TRANG_THAI = :trangThai " +
            " AND (:trangThaiDt IS NULL OR HDR.TRANG_THAI_DT = :trangThaiDt )" +
            " AND (:tuNgayQd IS NULL OR PD_HDR.NGAY_KY >= TO_DATE(:tuNgayQd, 'yyyy-MM-dd')) " +
            " AND (:denNgayQd IS NULL OR PD_HDR.NGAY_KY <= TO_DATE(:denNgayQd, 'yyyy-MM-dd')) " +
            " AND HDR.LASTEST = 1 " ,
//            " GROUP BY DTL.ID, DTL.ID_QD_HDR, DTL.MA_DVI, DTL.SO_DXUAT, DTL.NGAY_TAO, DTL.TEN_DU_AN, DTL.SO_LUONG, DTL.DON_GIA_VAT, DTL.SO_GTHAU, DTL.NAM_KHOACH, DTL.ID_DX_HDR, DTL.TRANG_THAI, DTL.NGAY_PDUYET, DTL.DIA_CHI_DVI, DTL.TRICH_YEU, DTL.SO_QD_PD_KQ_LCNT, DTL.DON_GIA_TAM_TINH, DTL.GOI_THAU, DTL.CLOAI_VTHH, DTL.LOAI_VTHH, DTL.TEN_NHA_THAU, DTL.DON_GIA_NHA_THAU, DTL.ID_NHA_THAU, DTL.ID_DC_DX_HDR",
            countQuery = "SELECT COUNT(*) FROM (" +
                    " SELECT * FROM HH_QD_KHLCNT_DTL DTL " +
                    " LEFT JOIN HH_QD_KHLCNT_HDR HDR ON HDR.ID = DTL.ID_QD_HDR " +
                    " LEFT JOIN HH_QD_PDUYET_KQLCNT_HDR PD_HDR ON PD_HDR.ID_QD_PD_KHLCNT_DTL = DTL.ID " +
                    " WHERE (:namKh IS NULL OR HDR.NAM_KHOACH = TO_NUMBER(:namKh)) " +
                    " AND (:soQd IS NULL OR LOWER(DTL.SO_DXUAT) LIKE LOWER(CONCAT(CONCAT('%', :soQd),'%')))" +
                    " AND (:soQdPdKqlcnt IS NULL OR LOWER(PD_HDR.SO_QD) LIKE LOWER(CONCAT(CONCAT('%', :soQdPdKqlcnt),'%')))" +
                    " AND (:soQdPdKhlcnt IS NULL OR LOWER(HDR.SO_QD) LIKE LOWER(CONCAT(CONCAT('%', :soQdPdKhlcnt),'%')))" +
                    " AND (:loaiVthh IS NULL OR LOWER(HDR.LOAI_VTHH) LIKE LOWER(CONCAT(CONCAT('%', :loaiVthh),'%')))" +
                    " AND (:maDvi IS NULL OR LOWER(DTL.MA_DVI) LIKE LOWER(CONCAT(CONCAT('%', :maDvi),'%')))" +
                    " AND (:trangThaiCuc IS NULL OR DTL.TRANG_THAI = :trangThaiCuc)" +
                    " AND HDR.TRANG_THAI = :trangThai " +
                    " AND (:trangThaiDt IS NULL OR HDR.TRANG_THAI_DT = :trangThaiDt )" +
                    " AND (:tuNgayQd IS NULL OR PD_HDR.NGAY_KY >= TO_DATE(:tuNgayQd, 'yyyy-MM-dd')) " +
                    " AND (:denNgayQd IS NULL OR PD_HDR.NGAY_KY <= TO_DATE(:denNgayQd, 'yyyy-MM-dd')) " +
                    " AND HDR.LASTEST = 1 " +
//                    " GROUP BY DTL.ID, DTL.ID_QD_HDR, DTL.MA_DVI, DTL.SO_DXUAT, DTL.NGAY_TAO, DTL.TEN_DU_AN, DTL.SO_LUONG, DTL.DON_GIA_VAT, DTL.SO_GTHAU, DTL.NAM_KHOACH, DTL.ID_DX_HDR, DTL.TRANG_THAI, DTL.NGAY_PDUYET, DTL.DIA_CHI_DVI, DTL.TRICH_YEU, DTL.SO_QD_PD_KQ_LCNT, DTL.DON_GIA_TAM_TINH, DTL.GOI_THAU, DTL.CLOAI_VTHH, DTL.LOAI_VTHH, DTL.TEN_NHA_THAU, DTL.DON_GIA_NHA_THAU, DTL.ID_NHA_THAU, DTL.ID_DC_DX_HDR" +
                    ")",
            nativeQuery = true )
    Page<HhQdKhlcntDtl> selectPage(Integer namKh , String loaiVthh,
                                   String maDvi, String trangThai,String trangThaiCuc,String trangThaiDt,
                                   String soQd,
                                   String tuNgayQd, String denNgayQd,
                                   String soQdPdKhlcnt, String soQdPdKqlcnt, Pageable pageable);

}
