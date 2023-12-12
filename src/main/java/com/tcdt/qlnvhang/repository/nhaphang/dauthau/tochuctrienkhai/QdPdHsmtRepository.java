package com.tcdt.qlnvhang.repository.nhaphang.dauthau.tochuctrienkhai;

import com.tcdt.qlnvhang.entities.nhaphang.dauthau.tochuctrienkhai.QdPdHsmt;
import com.tcdt.qlnvhang.repository.BaseRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface QdPdHsmtRepository extends BaseRepository<QdPdHsmt, Long> {
    Optional<QdPdHsmt> findBySoQd (String soQd);
    Optional<QdPdHsmt> findByIdQdPdKhlcnt (Long id);
    Optional<QdPdHsmt> findByIdQdPdKhlcntDtl (Long id);
    Optional<QdPdHsmt> findByIdQdPdKhlcntAndMaDvi (Long id, String maDvi);
    List<QdPdHsmt> findAllByIdIn(List<Long> ids);
    @Query(
            value = " SELECT * " +
                    " FROM HH_QD_PD_HSMT HSMT " +
                    " JOIN HH_QD_KHLCNT_HDR QDKH ON HSMT.ID_QD_PD_KHLCNT = QDKH.ID " +
                    " WHERE (:namKhoach IS NULL OR HSMT.NAM_KHOACH = TO_NUMBER(:namKhoach)) " +
                    "  AND (:soQd IS NULL OR LOWER(HSMT.SO_QD) LIKE LOWER(CONCAT(CONCAT('%', :soQd),'%'))) " +
                    "  AND (:soQdPdkhlcnt IS NULL OR LOWER(QDKH.SO_QD) LIKE LOWER(CONCAT(CONCAT('%', :soQdPdkhlcnt),'%')))" +
                    "  AND (:tuNgayKy IS NULL OR HSMT.NGAY_PDUYET >= TO_DATE(:tuNgayKy, 'YYYY-MM-DD HH24:MI:SS'))" +
                    "  AND (:denNgayKy IS NULL OR HSMT.NGAY_PDUYET <= TO_DATE(:denNgayKy, 'YYYY-MM-DD HH24:MI:SS'))" +
                    "  AND (:loaiVthh IS NULL OR QDKH.LOAI_VTHH LIKE CONCAT(:loaiVthh,'%')) " +
                    "  AND (:cloaiVthh IS NULL OR QDKH.CLOAI_VTHH LIKE CONCAT(:cloaiVthh,'%')) " +
                    "  AND (:trichYeu IS NULL OR LOWER(HSMT.TRICH_YEU) LIKE LOWER(CONCAT(CONCAT('%', :trichYeu),'%')))" +
                    "  AND (:trangThai IS NULL OR HSMT.TRANG_THAI = :trangThai) " +
                    "  AND (:maDvi IS NULL OR HSMT.MA_DVI = :maDvi) "
            ,nativeQuery = true)
    Page<QdPdHsmt> search(String namKhoach, String soQd,String soQdPdkhlcnt, String tuNgayKy,String denNgayKy,String loaiVthh,String cloaiVthh,String trichYeu,String trangThai, String maDvi, Pageable pageable);

    @Query(
            value = " SELECT DISTINCT HSMT.* " +
                    " FROM HH_QD_PD_HSMT HSMT " +
                    " JOIN HH_QD_KHLCNT_HDR QDKH ON HSMT.ID_QD_PD_KHLCNT = QDKH.ID " +
                    " JOIN HH_QD_KHLCNT_DSGTHAU GT ON QDKH.ID = GT.ID_QD_HDR " +
                    " JOIN HH_QD_KHLCNT_DSGTHAU_CTIET GTCT ON GT.ID = GTCT.ID_GOI_THAU " +
                    " WHERE (:namKhoach IS NULL OR HSMT.NAM_KHOACH = TO_NUMBER(:namKhoach)) " +
                    "  AND (:soQd IS NULL OR LOWER(HSMT.SO_QD) LIKE LOWER(CONCAT(CONCAT('%', :soQd),'%'))) " +
                    "  AND (:soQdPdkhlcnt IS NULL OR LOWER(QDKH.SO_QD) LIKE LOWER(CONCAT(CONCAT('%', :soQdPdkhlcnt),'%')))" +
                    "  AND (:tuNgayKy IS NULL OR HSMT.NGAY_PDUYET >= TO_DATE(:tuNgayKy, 'YYYY-MM-DD HH24:MI:SS'))" +
                    "  AND (:denNgayKy IS NULL OR HSMT.NGAY_PDUYET <= TO_DATE(:denNgayKy, 'YYYY-MM-DD HH24:MI:SS'))" +
                    "  AND (:loaiVthh IS NULL OR QDKH.LOAI_VTHH LIKE CONCAT(:loaiVthh,'%')) " +
                    "  AND (:cloaiVthh IS NULL OR QDKH.CLOAI_VTHH LIKE CONCAT(:cloaiVthh,'%')) " +
                    "  AND (:trichYeu IS NULL OR LOWER(HSMT.TRICH_YEU) LIKE LOWER(CONCAT(CONCAT('%', :trichYeu),'%')))" +
                    "  AND (:trangThai IS NULL OR HSMT.TRANG_THAI = :trangThai) " +
                    "  AND (:maDvi IS NULL OR GTCT.MA_DVI = :maDvi) "
            ,nativeQuery = true)
    Page<QdPdHsmt> searchVt(String namKhoach, String soQd,String soQdPdkhlcnt, String tuNgayKy,String denNgayKy,String loaiVthh,String cloaiVthh,String trichYeu,String trangThai, String maDvi, Pageable pageable);
}
