package com.tcdt.qlnvhang.repository.quyetdinhgiaonhiemvunhapxuat;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import com.tcdt.qlnvhang.repository.BaseRepository;
import com.tcdt.qlnvhang.table.HhQdGiaoNvuNhapxuatHdr;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface HhQdGiaoNvuNhapxuatRepository extends BaseRepository<HhQdGiaoNvuNhapxuatHdr, Long>, HhQdGiaoNvuNhapxuatRepositoryCustom {

	Optional<HhQdGiaoNvuNhapxuatHdr> findFirstBySoQd(String soQd);

	@Query(
			value = "SELECT * \n" +
					"FROM NH_QD_GIAO_NVU_NHAPXUAT NX " +
					" WHERE (:namNhap IS NULL OR NX.NAM_NHAP = TO_NUMBER(:namNhap)) " +
					"  AND (:veViec IS NULL OR LOWER(NX.VE_VIEC) LIKE LOWER(CONCAT(CONCAT('%', :veViec),'%'))) " +
					"  AND (:soQd IS NULL OR LOWER(NX.SO_QD) LIKE LOWER(CONCAT(CONCAT('%', :soQd),'%'))) " +
					"  AND (:tuNgayQD IS NULL OR NX.NGAY_QDINH >= TO_DATE(:tuNgayQD, 'yyyy-MM-dd')) " +
					"  AND (:denNgayQD IS NULL OR NX.NGAY_QDINH <= TO_DATE(:denNgayQD, 'yyyy-MM-dd')) " +
					"  AND (:trichYeu IS NULL OR LOWER(NX.TRICH_YEU) LIKE LOWER(CONCAT(CONCAT('%', :trichYeu),'%'))) " +
					"  AND (:loaiVthh IS NULL OR NX.LOAI_VTHH LIKE :loaiVthh) " +
					"  AND (:trangThai IS NULL OR NX.TRANG_THAI LIKE :trangThai) " +
					"  AND NX.MA_DVI LIKE :maDvi ",
			nativeQuery = true)
	Page<HhQdGiaoNvuNhapxuatHdr> select(String namNhap,
										String veViec,
										String soQd,
										String tuNgayQD,
										String denNgayQD,
										String trichYeu,
										String loaiVthh,
										String maDvi,
										String trangThai,
										Pageable pageable);

	@Query(
			value = "SELECT * \n" +
					"FROM NH_QD_GIAO_NVU_NHAPXUAT NX " +
					"INNER JOIN NH_QD_GIAO_NVU_NHAPXUAT_CT NXCT ON NXCT.ID_HDR = NX.ID " +
					" WHERE (:namNhap IS NULL OR NX.NAM_NHAP = TO_NUMBER(:namNhap)) " +
					"  AND (:veViec IS NULL OR LOWER(NX.VE_VIEC) LIKE LOWER(CONCAT(CONCAT('%', :veViec),'%'))) " +
					"  AND (:soQd IS NULL OR LOWER(NX.SO_QD) LIKE LOWER(CONCAT(CONCAT('%', :soQd),'%'))) " +
					"  AND (:tuNgayQD IS NULL OR NX.NGAY_QDINH >= TO_DATE(:tuNgayQD, 'yyyy-MM-dd')) " +
					"  AND (:denNgayQD IS NULL OR NX.NGAY_QDINH <= TO_DATE(:denNgayQD, 'yyyy-MM-dd')) " +
					"  AND (:trichYeu IS NULL OR LOWER(NX.TRICH_YEU) LIKE LOWER(CONCAT(CONCAT('%', :trichYeu),'%'))) " +
					"  AND (:loaiVthh IS NULL OR NX.LOAI_VTHH LIKE :loaiVthh) " +
					"  AND (:trangThai IS NULL OR NX.TRANG_THAI LIKE :trangThai) " +
					"  AND NXCT.MA_DVI LIKE :maDvi ",
			nativeQuery = true)
	Page<HhQdGiaoNvuNhapxuatHdr> findQdChiCuc(String namNhap,
										String veViec,
										String soQd,
										String tuNgayQD,
										String denNgayQD,
										String trichYeu,
										String loaiVthh,
										String maDvi, String trangThai,
										Pageable pageable);

	@Query("SELECT COUNT(DISTINCT qd.id) FROM HhQdGiaoNvuNhapxuatHdr qd " +
			"WHERE qd.maDvi = ?1 " +
			"AND (?2 IS NULL OR qd.loaiVthh = ?2)")
	int countQdCuc(String maDvi, String loaiVthh);

	@Query("SELECT COUNT(DISTINCT qd.id) FROM HhQdGiaoNvuNhapxuatHdr qd " +
			"INNER JOIN HhQdGiaoNvuNhapxuatDtl ct ON ct.parent = qd " +
			"WHERE ct.maDvi = ?1 " +
			"AND (?2 IS NULL OR qd.loaiVthh = ?2)")
	int countQdChiCuc(String maDvi, String loaiVtth);

	List<HhQdGiaoNvuNhapxuatHdr> findByIdIn(Collection<Long> ids);

	@Transactional
	@Modifying
	void deleteByIdIn(Collection<Long> ids);

	@Query(
			value = " SELECT NX.* " +
					" FROM NH_QD_GIAO_NVU_NHAPXUAT NX " +
					" WHERE (:namNhap IS NULL OR NX.NAM_NHAP = TO_NUMBER(:namNhap)) " +
					"  AND (:soQd IS NULL OR LOWER(NX.SO_QD) LIKE LOWER(CONCAT(CONCAT('%', :soQd),'%'))) " +
					"  AND (:loaiVthh IS NULL OR NX.LOAI_VTHH LIKE LOWER(:loaiVthh)) " +
					"  AND (:trichYeu IS NULL OR LOWER(NX.TRICH_YEU) LIKE LOWER(CONCAT(CONCAT('%', :trichYeu),'%'))) " +
					"  AND (:tuNgayQD IS NULL OR NX.NGAY_QDINH >= TO_DATE(:tuNgayQD, 'yyyy-MM-dd')) " +
					"  AND (:denNgayQD IS NULL OR NX.NGAY_QDINH <= TO_DATE(:denNgayQD, 'yyyy-MM-dd')) " +
					" AND ( NX.MA_DVI = :maDvi)  ",
			countQuery = " SELECT COUNT(1) " +
					" FROM NH_QD_GIAO_NVU_NHAPXUAT NX " +
					" WHERE 1 = 1 AND (:namNhap IS NULL OR NX.NAM_NHAP = TO_NUMBER(:namNhap)) " +
					"  AND (:soQd IS NULL OR LOWER(NX.SO_QD) LIKE LOWER(CONCAT(CONCAT('%', :soQd),'%'))) " +
					"  AND (:loaiVthh IS NULL OR NX.LOAI_VTHH LIKE LOWER(:loaiVthh)) " +
					"  AND (:trichYeu IS NULL OR LOWER(NX.TRICH_YEU) LIKE LOWER(CONCAT(CONCAT('%', :trichYeu),'%'))) " +
					"  AND (:tuNgayQD IS NULL OR NX.NGAY_QDINH >= TO_DATE(:tuNgayQD, 'yyyy-MM-dd')) " +
					"  AND (:denNgayQD IS NULL OR NX.NGAY_QDINH <= TO_DATE(:denNgayQD, 'yyyy-MM-dd')) " +
					"  AND ( NX.MA_DVI = :maDvi) "
			, nativeQuery = true)
	Page<HhQdGiaoNvuNhapxuatHdr> selectPageCuc(Long namNhap, String soQd, String loaiVthh, String trichYeu, String tuNgayQD, String denNgayQD, String maDvi, Pageable pageable);

	@Query(
			value = " SELECT NX.* " +
					" FROM NH_QD_GIAO_NVU_NHAPXUAT NX,NH_QD_GIAO_NVU_NHAPXUAT_CT NX_DTL " +
					" WHERE 1 = 1 AND (:namNhap IS NULL OR NX.NAM_NHAP = TO_NUMBER(:namNhap)) " +
					"  AND (NX.ID = NX_DTL.ID_HDR) " +
					"  AND (:soQd IS NULL OR LOWER(NX.SO_QD) LIKE LOWER(CONCAT(CONCAT('%', :soQd),'%'))) " +
					"  AND (:loaiVthh IS NULL OR NX.LOAI_VTHH LIKE LOWER(:loaiVthh)) " +
					"  AND (:trichYeu IS NULL OR LOWER(NX.TRICH_YEU) LIKE LOWER(CONCAT(CONCAT('%', :trichYeu),'%'))) " +
					"  AND (:tuNgayQD IS NULL OR NX.NGAY_QDINH >= TO_DATE(:tuNgayQD, 'yyyy-MM-dd')) " +
					"  AND (:denNgayQD IS NULL OR NX.NGAY_QDINH <= TO_DATE(:denNgayQD, 'yyyy-MM-dd')) " +
					"  AND ( NX_DTL.MA_DVI = :maDvi) ",
			countQuery = " SELECT COUNT(1) " +
					" FROM NH_QD_GIAO_NVU_NHAPXUAT NX , NH_QD_GIAO_NVU_NHAPXUAT_CT NX_DTL " +
					" WHERE 1 = 1 AND (:namNhap IS NULL OR NX.NAM_NHAP = TO_NUMBER(:namNhap)) " +
					"  AND (NX.ID = NX_DTL.ID_HDR) " +
					"  AND (:soQd IS NULL OR LOWER(NX.SO_QD) LIKE LOWER(CONCAT(CONCAT('%', :soQd),'%'))) " +
					"  AND (:loaiVthh IS NULL OR NX.LOAI_VTHH LIKE LOWER(:loaiVthh)) " +
					"  AND (:trichYeu IS NULL OR LOWER(NX.TRICH_YEU) LIKE LOWER(CONCAT(CONCAT('%', :trichYeu),'%'))) " +
					"  AND (:tuNgayQD IS NULL OR NX.NGAY_QDINH >= TO_DATE(:tuNgayQD, 'yyyy-MM-dd')) " +
					"  AND (:denNgayQD IS NULL OR NX.NGAY_QDINH <= TO_DATE(:denNgayQD, 'yyyy-MM-dd')) " +
					"   AND ( NX_DTL.MA_DVI = :maDvi) "
			, nativeQuery = true)
	Page<HhQdGiaoNvuNhapxuatHdr> selectPageChiCuc(Long namNhap, String soQd, String loaiVthh, String trichYeu, String tuNgayQD, String denNgayQD, String maDvi, Pageable pageable);
}
