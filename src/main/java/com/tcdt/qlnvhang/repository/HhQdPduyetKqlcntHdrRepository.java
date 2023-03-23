package com.tcdt.qlnvhang.repository;

import java.util.List;
import java.util.Optional;

import com.tcdt.qlnvhang.request.search.HhQdPduyetKqlcntSearchReq;
import com.tcdt.qlnvhang.table.HhQdPduyetKqlcntHdr;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

public interface HhQdPduyetKqlcntHdrRepository extends BaseRepository<HhQdPduyetKqlcntHdr, Long> {
	String QUERY_SEARCH = " SELECT DISTINCT QDPD.* FROM HH_QD_PDUYET_KQLCNT_HDR QDPD "+
			" LEFT JOIN HH_HOP_DONG_HDR HD ON QDPD.ID = HD.ID_QD_KQ_LCNT  " +
			" WHERE (:#{#req.namKhoach} IS NULL OR QDPD.NAM_KHOACH = TO_NUMBER(:#{#req.namKhoach})) "+
			" AND (:#{#req.loaiVthh} IS NULL OR QDPD.LOAI_VTHH LIKE CONCAT(:#{#req.loaiVthh},'%')) "+
			" AND (:#{#req.soHd} IS NULL OR LOWER(HD.SO_HD) LIKE LOWER(CONCAT(CONCAT('%', :#{#req.soHd}),'%')))" +
			" AND (:#{#req.tenHd} IS NULL OR LOWER(HD.TEN_HD) LIKE LOWER(CONCAT(CONCAT('%', :#{#req.tenHd}),'%')))" +
			" AND (:#{#req.tuNgayKy} IS NULL OR QDPD.NGAY_KY >= TO_DATE(:#{#req.tuNgayKy}, 'YYYY-MM-DD HH24:MI:SS')) "+
			" AND (:#{#req.denNgayKy} IS NULL OR QDPD.NGAY_TAO <= TO_DATE(:#{#req.denNgayKy}, 'YYYY-MM-DD HH24:MI:SS')) "+
			" AND (:#{#req.soQd} IS NULL OR QDPD.SO_QD = :#{#req.soQd}) "+
			" AND (:#{#req.tuNgayQd} IS NULL OR QDPD.NGAY_TAO >= TO_DATE(:#{#req.tuNgayQd}, 'yyyy-MM-dd')) "+
			" AND (:#{#req.denNgayQd} IS NULL OR QDPD.NGAY_TAO <= TO_DATE(:#{#req.denNgayQd}, 'yyyy-MM-dd')) "+
			" AND (:#{#req.trichYeu} IS NULL OR LOWER(QDPD.TRICH_YEU) LIKE LOWER(CONCAT(CONCAT('%', :#{#req.trichYeu}),'%')))" +
			" AND (:#{#req.maDvi} IS NULL OR QDPD.MA_DVI = :#{#req.maDvi}) "+
			" AND (:#{#req.trangThai} IS NULL OR QDPD.TRANG_THAI = :#{#req.trangThai}) ";

	Optional<HhQdPduyetKqlcntHdr> findBySoQd(String soQd);

	@Query(value = QUERY_SEARCH,
		countQuery = " SELECT COUNT(*) FROM ( "+ QUERY_SEARCH + ")",
			nativeQuery = true)
	Page<HhQdPduyetKqlcntHdr> selectPage(HhQdPduyetKqlcntSearchReq req, Pageable pageable);

//	@Query(" SELECT new com.tcdt.qlnvhang.response.dauthauvattu.HhQdPduyetKqlcntRes(HDR.id,HDR.soQd,HDR.ngayQd,HDR.trichYeu,DSG.goiThau,DSG.trangThai,DTGT.idNhaThau,DSG.lyDoHuy,DTGT.donGiaTrcVat,DTGT.vat,DTGT.soLuong,DTGT.loaiHdong,DTGT.tgianThienHd,HDR.trangThai,HDR.namKhoach,HDR.loaiVthh) " +
//			"    FROM HhQdPduyetKqlcntHdr HDR " +
//			"    LEFT JOIN HhQdPduyetKqlcntDtl DTL ON HDR.id = DTL.idQdPdHdr " +
//			"    LEFT JOIN HhQdKhlcntDsgthau DSG ON DTL.idGoiThau = DSG.id " +
//			" WHERE (?1 is null or HDR.namKhoach = ?1 ) " +
//			" AND (?2 is null or  DTGT.loaiVthh = ?2 ) " +
//			" AND (?3 is null or lower(HDR.trichYeu) like lower(concat(concat('%',?3),'%')))" +
//			" AND (?4 is null or lower(HDR.soQdPdKhlcnt) like lower(concat(concat('%',?4),'%')))" +
//			" AND (?5 is null or  HDR.maDvi = ?5 ) "
//			)
//	Page<HhQdPduyetKqlcntRes> customQuerySearchCuc(String namKh, String loaiVthh, String trichYeu,String soQdPdKhlcnt,String maDvi,  Pageable pageable);

//	@Query(" SELECT new com.tcdt.qlnvhang.response.dauthauvattu.HhQdPduyetKqlcntRes(HDR.id,HDR.soQd,HDR.ngayQd,HDR.trichYeu,HDR.trangThai,HDR.maDvi,HDR.namKhoach,HDR.loaiVthh,HDR.soQdPdKhlcnt) " +
//			" FROM HhQdPduyetKqlcntHdr HDR " +
//			"WHERE (?1 is null or HDR.namKhoach = ?1 ) " +
//			" AND (?2 is null or  HDR.loaiVthh = ?2 ) " +
//			" AND (?3 is null or lower(HDR.trichYeu) like lower(concat(concat('%',?3),'%')))"+
//			" AND (?4 is null or lower(HDR.soQdPdKhlcnt) like lower(concat(concat('%',?4),'%')))"+
//			" AND (?5 is null or  HDR.maDvi = ?5 ) "
//	)
//	Page<HhQdPduyetKqlcntRes> customQuerySearchTongCuc(String namKh, String loaiVthh, String trichYeu,String soQdPdKhlcnt,String maDvi, Pageable pageable);


	@Query(value = "SELECT * FROM HH_QD_PDUYET_KQLCNT_HDR QDPD " +
			" WHERE (:namKh IS NULL OR QDPD.NAM_KHOACH = TO_NUMBER(:namKh)) "+
			" AND (:loaiVthh IS NULL OR QDPD.LOAI_VTHH = :loaiVthh) "+
			" AND (:soQd IS NULL OR QDPD.SO_QD = :soQd) "+
			" AND (:tuNgayQd IS NULL OR QDPD.NGAY_TAO >= TO_DATE(:tuNgayQd, 'yyyy-MM-dd')) "+
			" AND (:denNgayQd IS NULL OR QDPD.NGAY_TAO <= TO_DATE(:denNgayQd, 'yyyy-MM-dd')) " +
			" AND (:maDvi IS NULL OR QDPD.MA_DVI = :maDvi) " +
			" AND (:trangThai IS NULL OR QDPD.TRANG_THAI = :trangThai) ",
			nativeQuery = true)
	List<HhQdPduyetKqlcntHdr> selectAll(String namKh, String loaiVthh, String soQd, String tuNgayQd, String denNgayQd, String trangThai,String maDvi);


}
