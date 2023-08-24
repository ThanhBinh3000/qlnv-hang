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
	Page<HhQdKhlcntHdr> selectPage(Integer namKh, String loaiVthh, String soQd,String trichYeu, String tuNgayQd, String denNgayQd,String trangThai,Integer lastest,String maDvi,String trangThaiDtl,String trangThaiDt, Pageable pageable);

	@Query(value = "SELECT * FROM HH_QD_KHLCNT_HDR QDKHLCNT " +
			" WHERE (:namKh IS NULL OR QDKHLCNT.NAM_KHOACH = TO_NUMBER(:namKh)) "+
			" AND (:loaiVthh IS NULL OR QDKHLCNT.LOAI_VTHH LIKE CONCAT(:loaiVthh,'%')) "+
			" AND (:cloaiVthh IS NULL OR QDKHLCNT.CLOAI_VTHH = :cloaiVthh) "+
			" AND (:soQd IS NULL OR QDKHLCNT.SO_QD = :soQd) "+
			" AND (:tuNgayQd IS NULL OR QDKHLCNT.NGAY_QD >= TO_DATE(:tuNgayQd, 'yyyy-MM-dd')) "+
			" AND (:denNgayQd IS NULL OR QDKHLCNT.NGAY_QD <= TO_DATE(:denNgayQd, 'yyyy-MM-dd')) " +
			" AND (:trangThai IS NULL OR QDKHLCNT.TRANG_THAI = :trangThai) " +
			" AND (:lastest IS NULL OR QDKHLCNT.LASTEST = :lastest) ",
			nativeQuery = true)
	List<HhQdKhlcntHdr> selectAll(Integer namKh, String loaiVthh, String cloaiVthh, String soQd, String tuNgayQd, String denNgayQd,String trangThai, Integer lastest);



	@Query(value = "select nvl(sum(so_luong),0) from HH_DX_KHLCNT_DSGTHAU dtl,HH_DX_KHLCNT_HDR hdr where dtl.id_dx_khlcnt = hdr.id and hdr.nam_khoach = :namKh and dtl.ma_dvi = :maDvi and hdr.loai_vthh = :loaiVthh ",
			nativeQuery = true)
	Long countSLDalenKh(Integer namKh, String loaiVthh, String maDvi);

	Optional<HhQdKhlcntHdr> findByIdThHdrAndLastest(Long id, Boolean lastest);
}
