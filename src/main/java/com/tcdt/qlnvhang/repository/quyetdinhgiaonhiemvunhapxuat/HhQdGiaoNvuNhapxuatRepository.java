package com.tcdt.qlnvhang.repository.quyetdinhgiaonhiemvunhapxuat;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import com.tcdt.qlnvhang.repository.BaseRepository;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhiemvunhap.NhQdGiaoNvuNhapxuatHdr;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface HhQdGiaoNvuNhapxuatRepository extends BaseRepository<NhQdGiaoNvuNhapxuatHdr, Long>, HhQdGiaoNvuNhapxuatRepositoryCustom {

	Optional<NhQdGiaoNvuNhapxuatHdr> findFirstBySoQd(String soQd);

	Optional<NhQdGiaoNvuNhapxuatHdr> findByIdHdAndMaDviAndNamNhap(Long idHd, String maDvi, Integer nam);

//	@Query(
//			value = "SELECT * \n" +
//					"FROM NH_QD_GIAO_NVU_NHAPXUAT NX " +
//					" WHERE (:namNhap IS NULL OR NX.NAM_NHAP = TO_NUMBER(:namNhap)) " +
//					"  AND (:veViec IS NULL OR LOWER(NX.VE_VIEC) LIKE LOWER(CONCAT(CONCAT('%', :veViec),'%'))) " +
//					"  AND (:soQd IS NULL OR LOWER(NX.SO_QD) LIKE LOWER(CONCAT(CONCAT('%', :soQd),'%'))) " +
//					"  AND (:tuNgayQD IS NULL OR NX.NGAY_QDINH >= TO_DATE(:tuNgayQD, 'yyyy-MM-dd')) " +
//					"  AND (:denNgayQD IS NULL OR NX.NGAY_QDINH <= TO_DATE(:denNgayQD, 'yyyy-MM-dd')) " +
//					"  AND (:trichYeu IS NULL OR LOWER(NX.TRICH_YEU) LIKE LOWER(CONCAT(CONCAT('%', :trichYeu),'%'))) " +
//					"  AND (:loaiVthh IS NULL OR NX.LOAI_VTHH LIKE :loaiVthh) " +
//					"  AND (:trangThai IS NULL OR NX.TRANG_THAI LIKE :trangThai) " +
//					"  AND NX.MA_DVI LIKE :maDvi ",
//			nativeQuery = true)
//	Page<HhQdGiaoNvuNhapxuatHdr> select(String namNhap,
//										String veViec,
//										String soQd,
//										String tuNgayQD,
//										String denNgayQD,
//										String trichYeu,
//										String loaiVthh,
//										String maDvi,
//										String trangThai,
//										Pageable pageable);
//
//	@Query(
//			value = "SELECT * \n" +
//					"FROM NH_QD_GIAO_NVU_NHAPXUAT NX " +
//					"INNER JOIN NH_QD_GIAO_NVU_NHAPXUAT_CT NXCT ON NXCT.ID_HDR = NX.ID " +
//					" WHERE (:namNhap IS NULL OR NX.NAM_NHAP = TO_NUMBER(:namNhap)) " +
//					"  AND (:veViec IS NULL OR LOWER(NX.VE_VIEC) LIKE LOWER(CONCAT(CONCAT('%', :veViec),'%'))) " +
//					"  AND (:soQd IS NULL OR LOWER(NX.SO_QD) LIKE LOWER(CONCAT(CONCAT('%', :soQd),'%'))) " +
//					"  AND (:tuNgayQD IS NULL OR NX.NGAY_QDINH >= TO_DATE(:tuNgayQD, 'yyyy-MM-dd')) " +
//					"  AND (:denNgayQD IS NULL OR NX.NGAY_QDINH <= TO_DATE(:denNgayQD, 'yyyy-MM-dd')) " +
//					"  AND (:trichYeu IS NULL OR LOWER(NX.TRICH_YEU) LIKE LOWER(CONCAT(CONCAT('%', :trichYeu),'%'))) " +
//					"  AND (:loaiVthh IS NULL OR NX.LOAI_VTHH LIKE :loaiVthh) " +
//					"  AND (:trangThai IS NULL OR NX.TRANG_THAI LIKE :trangThai) " +
//					"  AND NXCT.MA_DVI LIKE :maDvi ",
//			nativeQuery = true)
//	Page<HhQdGiaoNvuNhapxuatHdr> findQdChiCuc(String namNhap,
//										String veViec,
//										String soQd,
//										String tuNgayQD,
//										String denNgayQD,
//										String trichYeu,
//										String loaiVthh,
//										String maDvi, String trangThai,
//										Pageable pageable);
//
//	@Query("SELECT COUNT(DISTINCT qd.id) FROM HhQdGiaoNvuNhapxuatHdr qd " +
//			"WHERE qd.maDvi = ?1 " +
//			"AND (?2 IS NULL OR qd.loaiVthh = ?2)")
//	int countQdCuc(String maDvi, String loaiVthh);
//
////	@Query("SELECT COUNT(DISTINCT qd.id) FROM HhQdGiaoNvuNhapxuatHdr qd " +
////			"INNER JOIN HhQdGiaoNvuNhapxuatDtl ct ON ct.parent = qd " +
////			"WHERE ct.maDvi = ?1 " +
////			"AND (?2 IS NULL OR qd.loaiVthh = ?2)")
////	int countQdChiCuc(String maDvi, String loaiVtth);

	List<NhQdGiaoNvuNhapxuatHdr> findByIdIn(Collection<Long> ids);

	@Transactional
	@Modifying
	void deleteByIdIn(Collection<Long> ids);

	@Query(
			value = " SELECT NX.* " +
					" FROM NH_QD_GIAO_NVU_NHAPXUAT NX " +
					" WHERE (:namNhap IS NULL OR NX.NAM_NHAP = TO_NUMBER(:namNhap)) " +
					"  AND (:soQd IS NULL OR LOWER(NX.SO_QD) LIKE LOWER(CONCAT(CONCAT('%', :soQd),'%'))) " +
					"  AND (:loaiVthh IS NULL OR NX.LOAI_VTHH LIKE CONCAT(:loaiVthh,'%')) " +
					"  AND (:cloaiVthh IS NULL OR NX.CLOAI_VTHH LIKE CONCAT(:cloaiVthh,'%')) " +
					"  AND (:trichYeu IS NULL OR LOWER(NX.TRICH_YEU) LIKE LOWER(CONCAT(CONCAT('%', :trichYeu),'%'))) " +
					"  AND (:tuNgayQD IS NULL OR NX.NGAY_QDINH >= TO_DATE(:tuNgayQD, 'YYYY-MM-DD HH24:MI:SS')) " +
					"  AND (:denNgayQD IS NULL OR NX.NGAY_QDINH <= TO_DATE(:denNgayQD, 'YYYY-MM-DD HH24:MI:SS')) " +
					"  AND (:trangThai IS NULL OR NX.TRANG_THAI = TO_NUMBER(:trangThai)) " +
					" AND (:maDvi IS NULL OR NX.MA_DVI LIKE CONCAT(:maDvi, '%'))  ",
			countQuery = " SELECT COUNT(1) " +
					" FROM NH_QD_GIAO_NVU_NHAPXUAT NX " +
					" WHERE 1 = 1 AND (:namNhap IS NULL OR NX.NAM_NHAP = TO_NUMBER(:namNhap)) " +
					"  AND (:soQd IS NULL OR LOWER(NX.SO_QD) LIKE LOWER(CONCAT(CONCAT('%', :soQd),'%'))) " +
					"  AND (:loaiVthh IS NULL OR NX.LOAI_VTHH LIKE CONCAT(:loaiVthh,'%')) " +
					"  AND (:cloaiVthh IS NULL OR NX.CLOAI_VTHH LIKE CONCAT(:cloaiVthh,'%')) " +
					"  AND (:trichYeu IS NULL OR LOWER(NX.TRICH_YEU) LIKE LOWER(CONCAT(CONCAT('%', :trichYeu),'%'))) " +
					"  AND (:tuNgayQD IS NULL OR NX.NGAY_QDINH >= TO_DATE(:tuNgayQD, 'YYYY-MM-DD HH24:MI:SS')) " +
					"  AND (:denNgayQD IS NULL OR NX.NGAY_QDINH <= TO_DATE(:denNgayQD, 'YYYY-MM-DD HH24:MI:SS')) " +
					"  AND (:trangThai IS NULL OR NX.TRANG_THAI = TO_NUMBER(:trangThai)) " +
					"  AND (:maDvi IS NULL OR NX.MA_DVI LIKE CONCAT(:maDvi, '%')) "
			, nativeQuery = true)
	Page<NhQdGiaoNvuNhapxuatHdr> selectPageCuc(Long namNhap, String soQd, String loaiVthh,String cloaiVthh, String trichYeu, String tuNgayQD, String denNgayQD, String maDvi, String trangThai, Pageable pageable);

	@Query(
			value = " SELECT NX.ID,NX.SO_QD,NX.MA_DVI,NX.LOAI_QD,NX.TRANG_THAI,NX.NGAY_TAO,NX.NGUOI_TAO,NX.NGAY_SUA,NX.NGUOI_SUA,NX.NGAY_GUI_DUYET,NX.NGUOI_GUI_DUYET,NX.LDO_TUCHOI,NX.NGAY_PDUYET,NX.NGUOI_PDUYET,NX.GHI_CHU,NX.CAP_DVI,NX.NAM_NHAP,NX.NGAY_QDINH,NX.LOAI_VTHH,NX.TRICH_YEU,NX.ID_HD,NX.SO_HD,NX.CLOAI_VTHH,NX.DON_VI_TINH,NX.SO_LUONG, NX.TGIAN_NKHO, NX.TEN_GOI_THAU, NX.MO_TA_HANG_HOA " +
					" FROM NH_QD_GIAO_NVU_NHAPXUAT NX " +
					" LEFT JOIN NH_BB_NGHIEM_THU BNT ON NX.ID = BNT.ID_QD_GIAO_NV_NH " +
					" LEFT JOIN NH_QD_GIAO_NVU_NHAPXUAT_CT NX_DTL ON NX.ID = NX_DTL.ID_HDR " +
					" LEFT JOIN NH_PHIEU_KT_CHAT_LUONG PKT ON NX.ID = PKT.ID_QD_GIAO_NV_NH " +
					" WHERE 1 = 1 AND (:namNhap IS NULL OR NX.NAM_NHAP = TO_NUMBER(:namNhap)) " +
					"  AND (:soBbNtBq IS NULL OR BNT.SO_BB_NT_BQ LIKE CONCAT(:soBbNtBq,'%')) " +
					"  AND (:tuNgayGD IS NULL OR PKT.NGAY_GDINH >= TO_DATE(:tuNgayGD, 'yyyy-MM-dd')) " +
					"  AND (:denNgayGD IS NULL OR PKT.NGAY_GDINH <= TO_DATE(:denNgayGD, 'yyyy-MM-dd')) " +
					"  AND (:kqDanhGia IS NULL OR LOWER(PKT.KQ_DANH_GIA) LIKE LOWER(CONCAT(CONCAT('%', :kqDanhGia),'%'))) " +
					"  AND (:tuNgayLP IS NULL OR BNT.NGAY_TAO >= TO_DATE(:tuNgayLP, 'yyyy-MM-dd')) " +
					"  AND (:denNgayLP IS NULL OR BNT.NGAY_TAO <= TO_DATE(:denNgayLP, 'yyyy-MM-dd')) " +
					"  AND (:tuNgayKT IS NULL OR BNT.NGAY_NGHIEM_THU >= TO_DATE(:denNgayKT, 'yyyy-MM-dd')) " +
					"  AND (:denNgayKT IS NULL OR BNT.NGAY_NGHIEM_THU <= TO_DATE(:denNgayKT, 'yyyy-MM-dd')) " +
					"  AND (:trangThai IS NULL OR NX.TRANG_THAI = TO_NUMBER(:trangThai)) " +
					"  AND (:soQd IS NULL OR LOWER(NX.SO_QD) LIKE LOWER(CONCAT(CONCAT('%', :soQd),'%'))) " +
					"  AND (:loaiVthh IS NULL OR NX.LOAI_VTHH LIKE CONCAT(:loaiVthh,'%')) " +
					"  AND (:trichYeu IS NULL OR LOWER(NX.TRICH_YEU) LIKE LOWER(CONCAT(CONCAT('%', :trichYeu),'%'))) " +
					"  AND ( NX_DTL.MA_DVI = :maDvi) " +
					"  GROUP BY NX.ID,NX.SO_QD,NX.MA_DVI,NX.LOAI_QD,NX.TRANG_THAI,NX.NGAY_TAO,NX.NGUOI_TAO,NX.NGAY_SUA,NX.NGUOI_SUA,NX.NGAY_GUI_DUYET,NX.NGUOI_GUI_DUYET,NX.LDO_TUCHOI,NX.NGAY_PDUYET,NX.NGUOI_PDUYET,NX.GHI_CHU,NX.CAP_DVI,NX.NAM_NHAP,NX.NGAY_QDINH,NX.LOAI_VTHH,NX.TRICH_YEU,NX.ID_HD,NX.SO_HD,NX.CLOAI_VTHH,NX.DON_VI_TINH,NX.SO_LUONG, NX.TGIAN_NKHO, NX.TEN_GOI_THAU, NX.MO_TA_HANG_HOA "
			,
			countQuery = " SELECT NX.ID,NX.SO_QD,NX.MA_DVI,NX.LOAI_QD,NX.TRANG_THAI,NX.NGAY_TAO,NX.NGUOI_TAO,NX.NGAY_SUA,NX.NGUOI_SUA,NX.NGAY_GUI_DUYET,NX.NGUOI_GUI_DUYET,NX.LDO_TUCHOI,NX.NGAY_PDUYET,NX.NGUOI_PDUYET,NX.GHI_CHU,NX.CAP_DVI,NX.NAM_NHAP,NX.NGAY_QDINH,NX.LOAI_VTHH,NX.TRICH_YEU,NX.ID_HD,NX.SO_HD,NX.CLOAI_VTHH,NX.DON_VI_TINH,NX.SO_LUONG, NX.TGIAN_NKHO, NX.TEN_GOI_THAU, NX.MO_TA_HANG_HOA " +
					" FROM NH_QD_GIAO_NVU_NHAPXUAT NX " +
					" LEFT JOIN NH_BB_NGHIEM_THU BNT ON NX.ID = BNT.ID_QD_GIAO_NV_NH " +
					" LEFT JOIN NH_QD_GIAO_NVU_NHAPXUAT_CT NX_DTL ON NX.ID = NX_DTL.ID_HDR " +
					" WHERE 1 = 1 AND (:namNhap IS NULL OR NX.NAM_NHAP = TO_NUMBER(:namNhap)) " +
					"  AND (:soBbNtBq IS NULL OR BNT.SO_BB_NT_BQ LIKE CONCAT(:soBbNtBq,'%')) " +
					"  AND (:tuNgayGD IS NULL OR PKT.NGAY_GDINH >= TO_DATE(:tuNgayGD, 'yyyy-MM-dd')) " +
					"  AND (:denNgayGD IS NULL OR PKT.NGAY_GDINH <= TO_DATE(:denNgayGD, 'yyyy-MM-dd')) " +
					"  AND (:kqDanhGia IS NULL OR LOWER(PKT.KQ_DANH_GIA) LIKE LOWER(CONCAT(CONCAT('%', :kqDanhGia),'%'))) " +
					"  AND (:tuNgayLP IS NULL OR BNT.NGAY_TAO >= TO_DATE(:tuNgayLP, 'yyyy-MM-dd')) " +
					"  AND (:denNgayLP IS NULL OR BNT.NGAY_TAO <= TO_DATE(:denNgayLP, 'yyyy-MM-dd')) " +
					"  AND (:tuNgayKT IS NULL OR BNT.NGAY_NGHIEM_THU >= TO_DATE(:tuNgayKT, 'yyyy-MM-dd')) " +
					"  AND (:denNgayKT IS NULL OR BNT.NGAY_NGHIEM_THU <= TO_DATE(:denNgayKT, 'yyyy-MM-dd')) " +
					"  AND (:trangThai IS NULL OR NX.TRANG_THAI = TO_NUMBER(:trangThai)) " +
					"  AND (:soQd IS NULL OR LOWER(NX.SO_QD) LIKE LOWER(CONCAT(CONCAT('%', :soQd),'%'))) " +
					"  AND (:loaiVthh IS NULL OR NX.LOAI_VTHH LIKE CONCAT(:loaiVthh,'%')) " +
					"  AND (:trichYeu IS NULL OR LOWER(NX.TRICH_YEU) LIKE LOWER(CONCAT(CONCAT('%', :trichYeu),'%'))) " +
					"  AND ( NX_DTL.MA_DVI = :maDvi) " +
					"  GROUP BY NX.ID,NX.SO_QD,NX.MA_DVI,NX.LOAI_QD,NX.TRANG_THAI,NX.NGAY_TAO,NX.NGUOI_TAO,NX.NGAY_SUA,NX.NGUOI_SUA,NX.NGAY_GUI_DUYET,NX.NGUOI_GUI_DUYET,NX.LDO_TUCHOI,NX.NGAY_PDUYET,NX.NGUOI_PDUYET,NX.GHI_CHU,NX.CAP_DVI,NX.NAM_NHAP,NX.NGAY_QDINH,NX.LOAI_VTHH,NX.TRICH_YEU,NX.ID_HD,NX.SO_HD,NX.CLOAI_VTHH,NX.DON_VI_TINH,NX.SO_LUONG, NX.TGIAN_NKHO, NX.TEN_GOI_THAU, NX.MO_TA_HANG_HOA "
			, nativeQuery = true)
	Page<NhQdGiaoNvuNhapxuatHdr> selectPageChiCuc(Long namNhap, String soQd, String loaiVthh, String trichYeu, String tuNgayLP, String denNgayLP, String tuNgayKT, String denNgayKT, String maDvi, String soBbNtBq, String trangThai, String tuNgayGD, String denNgayGD, String kqDanhGia, Pageable pageable);

	List<NhQdGiaoNvuNhapxuatHdr> findAllByLoaiVthhStartsWithAndTrangThaiAndMaDviStartsWith (String loaiVthh, String trangThai, String maDvi);
}
