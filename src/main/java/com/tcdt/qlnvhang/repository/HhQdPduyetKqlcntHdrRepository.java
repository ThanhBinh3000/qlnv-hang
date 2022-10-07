package com.tcdt.qlnvhang.repository;

import java.util.List;
import java.util.Optional;

import com.tcdt.qlnvhang.response.dauthauvattu.HhQdPduyetKqlcntRes;
import com.tcdt.qlnvhang.table.HhQdKhlcntHdr;
import com.tcdt.qlnvhang.table.HhQdPduyetKqlcntHdr;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

public interface HhQdPduyetKqlcntHdrRepository extends BaseRepository<HhQdPduyetKqlcntHdr, Long> {

	Optional<HhQdPduyetKqlcntHdr> findBySoQd(String canCu);

	@Query(value = " SELECT * FROM HH_QD_PDUYET_KQLCNT_HDR QDPD "+
			" WHERE (:namKh IS NULL OR QDPD.NAM_KHOACH = TO_NUMBER(:namKh)) "+
			" AND (:loaiVthh IS NULL OR QDPD.LOAI_VTHH = :loaiVthh) "+
			" AND (:soQd IS NULL OR QDPD.SO_QD = :soQd) "+
			" AND (:tuNgayQd IS NULL OR QDPD.NGAY_QD >= TO_DATE(:tuNgayQd, 'yyyy-MM-dd')) "+
			" AND (:denNgayQd IS NULL OR QDPD.NGAY_QD <= TO_DATE(:denNgayQd, 'yyyy-MM-dd')) "+
			" AND (:trangThai IS NULL OR QDPD.TRANG_THAI = :trangThai) ",
			nativeQuery = true)
	Page<HhQdPduyetKqlcntHdr> selectPage(String namKh, String loaiVthh, String soQd, String tuNgayQd, String denNgayQd, String trangThai, Pageable pageable);

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

	@Query(" SELECT new com.tcdt.qlnvhang.response.dauthauvattu.HhQdPduyetKqlcntRes(HDR.id,HDR.soQd,HDR.ngayQd,HDR.trichYeu,HDR.trangThai,HDR.maDvi,HDR.namKhoach,HDR.loaiVthh,HDR.soQdPdKhlcnt) " +
			" FROM HhQdPduyetKqlcntHdr HDR " +
			"WHERE (?1 is null or HDR.namKhoach = ?1 ) " +
			" AND (?2 is null or  HDR.loaiVthh = ?2 ) " +
			" AND (?3 is null or lower(HDR.trichYeu) like lower(concat(concat('%',?3),'%')))"+
			" AND (?4 is null or lower(HDR.soQdPdKhlcnt) like lower(concat(concat('%',?4),'%')))"+
			" AND (?5 is null or  HDR.maDvi = ?5 ) "
	)
	Page<HhQdPduyetKqlcntRes> customQuerySearchTongCuc(String namKh, String loaiVthh, String trichYeu,String soQdPdKhlcnt,String maDvi, Pageable pageable);


	@Query(value = "SELECT * FROM HH_QD_PDUYET_KQLCNT_HDR QDPD " +
			" WHERE (:namKh IS NULL OR QDPD.NAM_KHOACH = TO_NUMBER(:namKh)) "+
			" AND (:loaiVthh IS NULL OR QDPD.LOAI_VTHH = :loaiVthh) "+
			" AND (:soQd IS NULL OR QDPD.SO_QD = :soQd) "+
			" AND (:tuNgayQd IS NULL OR QDPD.NGAY_QD >= TO_DATE(:tuNgayQd, 'yyyy-MM-dd')) "+
			" AND (:denNgayQd IS NULL OR QDPD.NGAY_QD <= TO_DATE(:denNgayQd, 'yyyy-MM-dd')) " +
			" AND (:maDvi IS NULL OR QDPD.MA_DVI = :maDvi) " +
			" AND (:trangThai IS NULL OR QDPD.TRANG_THAI = :trangThai) ",
			nativeQuery = true)
	List<HhQdPduyetKqlcntHdr> selectAll(String namKh, String loaiVthh, String soQd, String tuNgayQd, String denNgayQd, String trangThai,String maDvi);

}
