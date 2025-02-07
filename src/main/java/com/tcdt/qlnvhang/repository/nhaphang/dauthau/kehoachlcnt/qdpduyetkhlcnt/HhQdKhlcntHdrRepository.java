package com.tcdt.qlnvhang.repository.nhaphang.dauthau.kehoachlcnt.qdpduyetkhlcnt;

import java.util.List;
import java.util.Optional;

import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kehoachlcnt.qdpduyetkhlcnt.HhQdKhlcntHdr;
import com.tcdt.qlnvhang.repository.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

public interface HhQdKhlcntHdrRepository extends BaseRepository<HhQdKhlcntHdr, Long> {


	List<HhQdKhlcntHdr> findBySoQd(String soQd);
	List<HhQdKhlcntHdr> findAllByIdIn(List<Long> ids);

	@Query(value = " SELECT DISTINCT QD_HDR.*, QD_PD_HDR.NGAY_PDUYET AS NGAY_PD_QD_PDKQ_KHLCNT FROM HH_QD_KHLCNT_HDR QD_HDR " +
			" LEFT JOIN HH_QD_KHLCNT_DTL QD_DTL ON QD_HDR.ID = QD_DTL.ID_QD_HDR " +
			" LEFT JOIN HH_QD_PDUYET_KQLCNT_HDR QD_PD_HDR ON QD_HDR.ID = QD_PD_HDR.ID_QD_PD_KHLCNT " +
			" WHERE (:namKh IS NULL OR QD_HDR.NAM_KHOACH = TO_NUMBER(:namKh)) "+
			" AND (:loaiVthh IS NULL OR QD_HDR.LOAI_VTHH LIKE CONCAT(:loaiVthh,'%')) "+
			" AND (:soQdPdKqlcnt IS NULL OR LOWER(QD_PD_HDR.SO_QD) LIKE LOWER(CONCAT(CONCAT('%', :soQdPdKqlcnt),'%')))" +
			" AND (:soQdPdKhlcnt IS NULL OR LOWER(QD_HDR.SO_QD) LIKE LOWER(CONCAT(CONCAT('%', :soQdPdKhlcnt),'%')))" +
			" AND (:soQd IS NULL OR LOWER(QD_HDR.SO_QD) LIKE LOWER(CONCAT(CONCAT('%', :soQd),'%'))) "+
			" AND (:trichYeu IS NULL OR LOWER(QD_HDR.TRICH_YEU) LIKE LOWER(CONCAT(CONCAT('%', :trichYeu),'%'))) "+
			" AND (:tuNgayQd IS NULL OR QD_HDR.NGAY_QD >= TO_DATE(:tuNgayQd, 'yyyy-MM-dd')) "+
			" AND (:denNgayQd IS NULL OR QD_HDR.NGAY_QD <= TO_DATE(:denNgayQd, 'yyyy-MM-dd')) "+
			" AND (:trangThai IS NULL OR QD_HDR.TRANG_THAI = :trangThai)" +
			" AND (:lastest IS NULL OR QD_HDR.LASTEST = :lastest) " +
			" AND (:maDvi IS NULL OR QD_DTL.MA_DVI = :maDvi) " +
			" AND (:trangThaiDtl IS NULL OR QD_DTL.TRANG_THAI = :trangThaiDtl) " +
			" AND (:trangThaiDt IS NULL OR QD_HDR.TRANG_THAI_DT = :trangThaiDt )"
			, countQuery = " SELECT COUNT(DISTINCT QD_HDR.ID) FROM HH_QD_KHLCNT_HDR  QD_HDR LEFT JOIN HH_QD_KHLCNT_DTL QD_DTL ON QD_HDR.ID = QD_DTL.ID_QD_HDR LEFT JOIN HH_QD_PDUYET_KQLCNT_HDR QD_PD_HDR ON QD_HDR.ID = QD_PD_HDR.ID_QD_PD_KHLCNT WHERE (:namKh IS NULL OR QD_HDR.NAM_KHOACH = TO_NUMBER(:namKh)) "+
					" AND (:loaiVthh IS NULL OR QD_HDR.LOAI_VTHH LIKE CONCAT(:loaiVthh,'%')) "+
					" AND (:soQdPdKqlcnt IS NULL OR LOWER(QD_PD_HDR.SO_QD) LIKE LOWER(CONCAT(CONCAT('%', :soQdPdKqlcnt),'%')))" +
					" AND (:soQdPdKhlcnt IS NULL OR LOWER(QD_HDR.SO_QD) LIKE LOWER(CONCAT(CONCAT('%', :soQdPdKhlcnt),'%')))" +
					" AND (:soQd IS NULL OR LOWER(QD_HDR.SO_QD) LIKE LOWER(CONCAT(CONCAT('%', :soQd),'%'))) "+
					" AND (:trichYeu IS NULL OR LOWER(QD_HDR.TRICH_YEU) LIKE LOWER(CONCAT(CONCAT('%', :trichYeu),'%'))) "+
					" AND (:tuNgayQd IS NULL OR QD_HDR.NGAY_QD >= TO_DATE(:tuNgayQd, 'yyyy-MM-dd')) "+
					" AND (:denNgayQd IS NULL OR QD_HDR.NGAY_QD <= TO_DATE(:denNgayQd, 'yyyy-MM-dd')) "+
					" AND (:trangThai IS NULL OR QD_HDR.TRANG_THAI = :trangThai) " +
					" AND (:lastest IS NULL OR QD_HDR.LASTEST = :lastest)" +
					" AND (:maDvi IS NULL OR QD_DTL.MA_DVI = :maDvi) " +
					" AND (:trangThaiDtl IS NULL OR QD_DTL.TRANG_THAI = :trangThaiDtl) " +
					" AND (:trangThaiDt IS NULL OR QD_HDR.TRANG_THAI_DT = :trangThaiDt )"
			, nativeQuery = true)
	Page<HhQdKhlcntHdr> selectPage(Integer namKh, String loaiVthh, String soQd,String trichYeu, String tuNgayQd, String denNgayQd,String trangThai,Integer lastest,String maDvi,String trangThaiDtl,String trangThaiDt, String soQdPdKhlcnt, String soQdPdKqlcnt, Pageable pageable);

	@Query(value = " SELECT DISTINCT QD_HDR.* FROM HH_QD_KHLCNT_HDR QD_HDR " +
			" LEFT JOIN HH_QD_KHLCNT_DSGTHAU GT ON QD_HDR.ID = GT.ID_QD_HDR " +
			" LEFT JOIN HH_QD_KHLCNT_DSGTHAU_CTIET CT ON GT.ID = CT.ID_GOI_THAU " +
//			" LEFT JOIN HH_QD_PDUYET_KQLCNT_HDR QD_PD_HDR ON QD_HDR.ID = QD_PD_HDR.ID_QD_PD_KHLCNT " +
//			" LEFT JOIN HH_QD_PD_HSMT HSMT ON QD_HDR.ID = HSMT.ID_QD_PD_KHLCNT " +
			" WHERE (:namKh IS NULL OR QD_HDR.NAM_KHOACH = TO_NUMBER(:namKh)) "+
			" AND (:loaiVthh IS NULL OR QD_HDR.LOAI_VTHH LIKE CONCAT(:loaiVthh,'%')) "+
//			" AND (:soQdPdKqlcnt IS NULL OR LOWER(QD_PD_HDR.SO_QD) LIKE LOWER(CONCAT(CONCAT('%', :soQdPdKqlcnt),'%')))" +
			" AND (:soQdPdKhlcnt IS NULL OR LOWER(QD_HDR.SO_QD) LIKE LOWER(CONCAT(CONCAT('%', :soQdPdKhlcnt),'%')))" +
			" AND (:soQd IS NULL OR LOWER(QD_HDR.SO_TR_HDR) LIKE LOWER(CONCAT(CONCAT('%', :soQd),'%'))) "+
			" AND (:trichYeu IS NULL OR LOWER(QD_HDR.TRICH_YEU) LIKE LOWER(CONCAT(CONCAT('%', :trichYeu),'%'))) "+
			" AND (:tuNgayQd IS NULL OR QD_HDR.NGAY_QD >= TO_DATE(:tuNgayQd, 'yyyy-MM-dd')) "+
			" AND (:denNgayQd IS NULL OR QD_HDR.NGAY_QD <= TO_DATE(:denNgayQd, 'yyyy-MM-dd')) "+
			" AND (:trangThai IS NULL OR QD_HDR.TRANG_THAI = :trangThai)" +
			" AND (:lastest IS NULL OR QD_HDR.LASTEST = :lastest) " +
			" AND (:maDvi IS NULL OR CT.MA_DVI = :maDvi) " +
//			" AND HSMT.TRANG_THAI = '29' " +
//			" AND (:trangThaiDtl IS NULL OR QD_DTL.TRANG_THAI = :trangThaiDtl) " +
			" AND (:trangThaiDt IS NULL OR QD_HDR.TRANG_THAI_DT = :trangThaiDt )"
			, countQuery = " SELECT COUNT(DISTINCT QD_HDR.ID) FROM HH_QD_KHLCNT_HDR  QD_HDR LEFT JOIN HH_QD_KHLCNT_DSGTHAU GT ON QD_HDR.ID = GT.ID_QD_HDR LEFT JOIN HH_QD_KHLCNT_DSGTHAU_CTIET CT ON GT.ID = CT.ID_GOI_THAU " +
//			" LEFT JOIN HH_QD_PD_HSMT HSMT ON QD_HDR.ID = HSMT.ID_QD_PD_KHLCNT" +
			" WHERE (:namKh IS NULL OR QD_HDR.NAM_KHOACH = TO_NUMBER(:namKh)) "+
			" AND (:loaiVthh IS NULL OR QD_HDR.LOAI_VTHH LIKE CONCAT(:loaiVthh,'%')) "+
//			" AND (:soQdPdKqlcnt IS NULL OR LOWER(QD_PD_HDR.SO_QD) LIKE LOWER(CONCAT(CONCAT('%', :soQdPdKqlcnt),'%')))" +
			" AND (:soQdPdKhlcnt IS NULL OR LOWER(QD_HDR.SO_QD) LIKE LOWER(CONCAT(CONCAT('%', :soQdPdKhlcnt),'%')))" +
			" AND (:soQd IS NULL OR LOWER(QD_HDR.SO_TR_HDR) LIKE LOWER(CONCAT(CONCAT('%', :soQd),'%'))) "+
			" AND (:trichYeu IS NULL OR LOWER(QD_HDR.TRICH_YEU) LIKE LOWER(CONCAT(CONCAT('%', :trichYeu),'%'))) "+
			" AND (:tuNgayQd IS NULL OR QD_HDR.NGAY_QD >= TO_DATE(:tuNgayQd, 'yyyy-MM-dd')) "+
			" AND (:denNgayQd IS NULL OR QD_HDR.NGAY_QD <= TO_DATE(:denNgayQd, 'yyyy-MM-dd')) "+
			" AND (:trangThai IS NULL OR QD_HDR.TRANG_THAI = :trangThai) " +
			" AND (:lastest IS NULL OR QD_HDR.LASTEST = :lastest)" +
			" AND (:maDvi IS NULL OR CT.MA_DVI = :maDvi) " +
//			" AND HSMT.TRANG_THAI = '29' " +
//			" AND (:trangThaiDtl IS NULL OR QD_DTL.TRANG_THAI = :trangThaiDtl) " +
			" AND (:trangThaiDt IS NULL OR QD_HDR.TRANG_THAI_DT = :trangThaiDt )"
			, nativeQuery = true)
	Page<HhQdKhlcntHdr> selectPageVt(Integer namKh, String loaiVthh, String soQd,String trichYeu, String tuNgayQd, String denNgayQd,String trangThai,Integer lastest,String maDvi,String trangThaiDt, String soQdPdKhlcnt, Pageable pageable);

	@Query(value = "SELECT * FROM HH_QD_KHLCNT_HDR QDKHLCNT " +
			" LEFT JOIN HH_QD_KHLCNT_DTL QD_DTL ON QDKHLCNT.ID = QD_DTL.ID_QD_HDR " +
			" WHERE (:namKh IS NULL OR QDKHLCNT.NAM_KHOACH = TO_NUMBER(:namKh)) "+
			" AND (:loaiVthh IS NULL OR QDKHLCNT.LOAI_VTHH LIKE CONCAT(:loaiVthh,'%')) "+
			" AND (:cloaiVthh IS NULL OR QDKHLCNT.CLOAI_VTHH = :cloaiVthh) "+
			" AND (:soQd IS NULL OR QDKHLCNT.SO_QD = :soQd) "+
			" AND (:tuNgayQd IS NULL OR QDKHLCNT.NGAY_QD >= TO_DATE(:tuNgayQd, 'yyyy-MM-dd')) "+
			" AND (:denNgayQd IS NULL OR QDKHLCNT.NGAY_QD <= TO_DATE(:denNgayQd, 'yyyy-MM-dd')) " +
			" AND (:trangThai IS NULL OR QDKHLCNT.TRANG_THAI = :trangThai) " +
			" AND (:maDvi IS NULL OR QD_DTL.MA_DVI = :maDvi) " +
			" AND (:lastest IS NULL OR QDKHLCNT.LASTEST = :lastest) ",
			nativeQuery = true)
	List<HhQdKhlcntHdr> selectAll(Integer namKh, String loaiVthh, String cloaiVthh, String soQd, String tuNgayQd, String denNgayQd,String trangThai, Integer lastest, String maDvi);



	@Query(value = "select nvl(sum(so_luong),0) from HH_DX_KHLCNT_DSGTHAU dtl,HH_DX_KHLCNT_HDR hdr where dtl.id_dx_khlcnt = hdr.id and hdr.nam_khoach = :namKh and dtl.ma_dvi = :maDvi and hdr.loai_vthh = :loaiVthh ",
			nativeQuery = true)
	Long countSLDalenKh(Integer namKh, String loaiVthh, String maDvi);

	Optional<HhQdKhlcntHdr> findByIdThHdrAndLastest(Long id, Boolean lastest);
	Optional<HhQdKhlcntHdr> findByIdTrHdr(Long id);
	@Query(value = " SELECT DISTINCT QD_HDR.* FROM HH_QD_KHLCNT_HDR QD_HDR " +
			" LEFT JOIN HH_QD_KHLCNT_DSGTHAU GT ON QD_HDR.ID = GT.ID_QD_HDR " +
			" LEFT JOIN HH_QD_KHLCNT_DSGTHAU_CTIET CT ON GT.ID = CT.ID_GOI_THAU " +
//			" LEFT JOIN HH_QD_PDUYET_KQLCNT_HDR QD_PD_HDR ON QD_HDR.ID = QD_PD_HDR.ID_QD_PD_KHLCNT " +
//			" LEFT JOIN HH_QD_PD_HSMT HSMT ON QD_HDR.ID = HSMT.ID_QD_PD_KHLCNT " +
			" WHERE (:namKh IS NULL OR QD_HDR.NAM_KHOACH = TO_NUMBER(:namKh)) "+
			" AND (:loaiVthh IS NULL OR QD_HDR.LOAI_VTHH LIKE CONCAT(:loaiVthh,'%')) "+
			" AND (:trangThai IS NULL OR QD_HDR.TRANG_THAI = :trangThai)" +
			" AND QD_HDR.LASTEST = 0 " +
			" AND (:maDvi IS NULL OR CT.MA_DVI = :maDvi) "
			, nativeQuery = true)
	List<HhQdKhlcntHdr> getDsTtDthauVt(Integer namKh , String loaiVthh, String maDvi,  String trangThai );

	@Query(value = " SELECT DISTINCT QD_HDR.* FROM HH_QD_KHLCNT_HDR QD_HDR " +
			" LEFT JOIN HH_QD_KHLCNT_DTL QD_DTL ON QD_HDR.ID = QD_DTL.ID_QD_HDR " +
			" LEFT JOIN HH_QD_KHLCNT_DSGTHAU DSG ON QD_DTL.ID = DSG.ID_QD_DTL " +
			" LEFT JOIN HH_QD_PD_HSMT HSMT ON DSG.ID_QD_PD_HSMT = HSMT.ID  " +
			" WHERE (:namKh IS NULL OR QD_HDR.NAM_KHOACH = TO_NUMBER(:namKh)) "+
			" AND (:loaiVthh IS NULL OR QD_HDR.LOAI_VTHH LIKE CONCAT(:loaiVthh,'%')) "+
			" AND (:trangThai IS NULL OR QD_HDR.TRANG_THAI = :trangThai)" +
			" AND (:lastest IS NULL OR QD_HDR.LASTEST = :lastest) " +
			" AND (:maDvi IS NULL OR QD_DTL.MA_DVI = :maDvi) " +
			" AND (DSG.ID_QD_PD_HSMT IS NULL OR (DSG.ID_QD_PD_HSMT IS NOT NULL AND HSMT.TRANG_THAI != 29)) "
			, countQuery = " SELECT COUNT(DISTINCT QD_HDR.ID) FROM HH_QD_KHLCNT_HDR  QD_HDR " +
			" LEFT JOIN HH_QD_KHLCNT_DTL QD_DTL ON QD_HDR.ID = QD_DTL.ID_QD_HDR " +
			" LEFT JOIN HH_QD_KHLCNT_DSGTHAU DSG ON QD_DTL.ID = DSG.ID_QD_DTL " +
			" LEFT JOIN HH_QD_PD_HSMT HSMT ON DSG.ID_QD_PD_HSMT = HSMT.ID  " +
			" WHERE (:namKh IS NULL OR QD_HDR.NAM_KHOACH = TO_NUMBER(:namKh)) "+
			" AND (:loaiVthh IS NULL OR QD_HDR.LOAI_VTHH LIKE CONCAT(:loaiVthh,'%')) "+
			" AND (:trangThai IS NULL OR QD_HDR.TRANG_THAI = :trangThai)" +
			" AND (:lastest IS NULL OR QD_HDR.LASTEST = :lastest) " +
			" AND (:maDvi IS NULL OR QD_DTL.MA_DVI = :maDvi) " +
			" AND (DSG.ID_QD_PD_HSMT IS NULL OR (DSG.ID_QD_PD_HSMT IS NOT NULL AND HSMT.TRANG_THAI != 29)) "
			, nativeQuery = true)
	List<HhQdKhlcntHdr> selectDieuChinhQdPdKhlcnt(Integer namKh, String loaiVthh,String trangThai,Integer lastest,String maDvi);

	@Query(value = " SELECT DISTINCT QD_HDR.* FROM HH_QD_KHLCNT_HDR QD_HDR " +
			" LEFT JOIN HH_DC_DX_LCNT_HDR DC_HDR ON QD_HDR.ID = DC_HDR.ID_QD_GOC AND DC_HDR.LASTEST = 1 " +
			" LEFT JOIN HH_DC_DX_LCNT_DSGTHAU DC_DSG ON DC_HDR.ID = DC_DSG.ID_DC_DX_HDR " +
			" LEFT JOIN HH_QD_PD_HSMT HSMT_DC ON DC_DSG.ID_QD_PD_HSMT = HSMT_DC.ID  " +
			" LEFT JOIN HH_QD_KHLCNT_DSGTHAU DSG ON QD_HDR.ID = DSG.ID_QD_HDR  " +
			" LEFT JOIN HH_QD_PD_HSMT HSMT ON DSG.ID_QD_PD_HSMT = HSMT.ID  " +
			" WHERE (:namKh IS NULL OR QD_HDR.NAM_KHOACH = TO_NUMBER(:namKh)) "+
			" AND (:loaiVthh IS NULL OR QD_HDR.LOAI_VTHH LIKE CONCAT(:loaiVthh,'%')) "+
			" AND (:trangThai IS NULL OR QD_HDR.TRANG_THAI = :trangThai)" +
			" AND (:lastest IS NULL OR QD_HDR.LASTEST = :lastest) " +
			" AND (DC_DSG.ID_QD_PD_HSMT IS NULL OR (DC_DSG.ID_QD_PD_HSMT IS NOT NULL AND HSMT_DC.TRANG_THAI != 29)) " +
			" AND (DSG.ID_QD_PD_HSMT IS NULL OR (DSG.ID_QD_PD_HSMT IS NOT NULL AND HSMT.TRANG_THAI != 29)) "
			, countQuery = " SELECT COUNT(DISTINCT QD_HDR.ID) FROM HH_QD_KHLCNT_HDR  QD_HDR " +
			" LEFT JOIN HH_QD_KHLCNT_DTL QD_DTL ON QD_HDR.ID = QD_DTL.ID_QD_HDR " +
			" LEFT JOIN HH_QD_KHLCNT_DSGTHAU DSG ON QD_DTL.ID = DSG.ID_QD_DTL " +
			" LEFT JOIN HH_QD_PD_HSMT HSMT ON DSG.ID_QD_PD_HSMT = HSMT.ID  " +
			" WHERE (:namKh IS NULL OR QD_HDR.NAM_KHOACH = TO_NUMBER(:namKh)) "+
			" AND (:loaiVthh IS NULL OR QD_HDR.LOAI_VTHH LIKE CONCAT(:loaiVthh,'%')) "+
			" AND (:trangThai IS NULL OR QD_HDR.TRANG_THAI = :trangThai)" +
			" AND (:lastest IS NULL OR QD_HDR.LASTEST = :lastest) " +
			" AND (DC_DSG.ID_QD_PD_HSMT IS NULL OR (DC_DSG.ID_QD_PD_HSMT IS NOT NULL AND HSMT_DC.TRANG_THAI != 29)) " +
			" AND (DSG.ID_QD_PD_HSMT IS NULL OR (DSG.ID_QD_PD_HSMT IS NOT NULL AND HSMT.TRANG_THAI != 29)) "
			, nativeQuery = true)
	List<HhQdKhlcntHdr> selectDieuChinhQdPdKhlcntVt(Integer namKh, String loaiVthh,String trangThai,Integer lastest);
}
