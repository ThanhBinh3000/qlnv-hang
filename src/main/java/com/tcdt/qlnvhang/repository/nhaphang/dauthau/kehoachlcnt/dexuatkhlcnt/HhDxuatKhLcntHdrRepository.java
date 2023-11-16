package com.tcdt.qlnvhang.repository.nhaphang.dauthau.kehoachlcnt.dexuatkhlcnt;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import com.tcdt.qlnvhang.repository.BaseRepository;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kehoachlcnt.qdpduyetkhlcnt.HhQdKhlcntHdr;
import com.tcdt.qlnvhang.util.Contains;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kehoachlcnt.dexuatkhlcnt.HhDxuatKhLcntHdr;

public interface HhDxuatKhLcntHdrRepository extends BaseRepository<HhDxuatKhLcntHdr, Long> {

	Optional<HhDxuatKhLcntHdr> findBySoDxuat(String soDxuat);

	@Transactional()
	@Modifying
	@Query(value = "UPDATE HH_DX_KHLCNT_HDR SET TRANG_THAI_TH=:trangThaiTh WHERE SO_DXUAT IN :soDxuatList", nativeQuery = true)
	void updateStatusInList(List<String> soDxuatList, String trangThaiTh);

	@Transactional()
	@Modifying
	@Query(value = "UPDATE HH_DX_KHLCNT_HDR SET TRANG_THAI=:trangThai WHERE ID = :id ", nativeQuery = true)
	void updateStatus(Long id, String trangThai);

	@Query(value = "select shgt.nextval from dual", nativeQuery = true)
	Long getIdShgt();

	@Query(
			value = "SELECT * " +
					"FROM HH_DX_KHLCNT_HDR KHLCNT " +
					" WHERE (:namKh IS NULL OR KHLCNT.NAM_KHOACH = TO_NUMBER(:namKh)) " +
					"  AND (:soTr IS NULL OR LOWER(KHLCNT.SO_DXUAT) LIKE LOWER(CONCAT(CONCAT('%', :soTr),'%'))) " +
					"  AND (:soQd IS NULL OR LOWER(KHLCNT.SO_QD) LIKE LOWER(CONCAT(CONCAT('%', :soQd),'%')))" +
					"  AND (:ngayKyTu IS NULL OR KHLCNT.NGAY_PDUYET >= TO_DATE(:ngayKyTu, 'YYYY-MM-DD HH24:MI:SS'))" +
					"  AND (:ngayKyDen IS NULL OR KHLCNT.NGAY_PDUYET <= TO_DATE(:ngayKyDen, 'YYYY-MM-DD HH24:MI:SS'))" +
					"  AND (:ngayTaoTu IS NULL OR KHLCNT.NGAY_TAO >= TO_DATE(:ngayTaoTu, 'YYYY-MM-DD HH24:MI:SS'))" +
					"  AND (:ngayTaoDen IS NULL OR KHLCNT.NGAY_TAO <= TO_DATE(:ngayTaoDen, 'YYYY-MM-DD HH24:MI:SS'))" +
					"  AND (:loaiVthh IS NULL OR KHLCNT.LOAI_VTHH LIKE CONCAT(:loaiVthh,'%')) " +
					"  AND (:trichYeu IS NULL OR LOWER(KHLCNT.TRICH_YEU) LIKE LOWER(CONCAT(CONCAT('%', :trichYeu),'%')))" +
					"  AND (:trangThai IS NULL OR KHLCNT.TRANG_THAI = :trangThai) "+
					"  AND (:trangThaiTh IS NULL OR KHLCNT.TRANG_THAI_TH = :trangThaiTh) " +
					"  AND (:maDvi IS NULL OR KHLCNT.MA_DVI = :maDvi) "
			,nativeQuery = true)
	Page<HhDxuatKhLcntHdr> select(String namKh, String soTr,String soQd, String ngayKyTu,String ngayKyDen, String ngayTaoTu,String ngayTaoDen,String loaiVthh,String trichYeu,String trangThai,String trangThaiTh,String maDvi, Pageable pageable);

	@Query(
			value = "SELECT * " +
					"FROM HH_DX_KHLCNT_HDR KHLCNT " +
					" WHERE (:namKh IS NULL OR KHLCNT.NAM_KHOACH = TO_NUMBER(:namKh)) " +
					"  AND (:maDvi IS NULL OR KHLCNT.MA_DVI = :maDvi) " +
					"  AND ( KHLCNT.TRANG_THAI IN :trangThaiList ) " +
					"  AND ( KHLCNT.TRANG_THAI_TH IN :trangThaiThList ) "
			,nativeQuery = true)
	Page<HhDxuatKhLcntHdr> selectDropdown(String namKh, String maDvi,List<String> trangThaiList,List<String> trangThaiThList, Pageable pageable);


	@Query(value = " SELECT KHLCNT.* \n" +
			"FROM HH_DX_KHLCNT_HDR KHLCNT \n" +
			" WHERE KHLCNT.LOAI_VTHH = :loaiVthh \n" +
			" AND KHLCNT.CLOAI_VTHH = :cloaiVthh \n" +
			" AND KHLCNT.NAM_KHOACH = :namKh \n" +
			" AND (:allDvi = 1 OR KHLCNT.MA_DVI IN :listMaDvi)  \n" +
			" AND (:hthucLcnt IS NULL OR KHLCNT.HTHUC_LCNT = :hthucLcnt) \n" +
			" AND (:pthucLcnt IS NULL OR KHLCNT.PTHUC_LCNT = :pthucLcnt) \n" +
			" AND (:loaiHdong IS NULL OR KHLCNT.LOAI_HDONG = :loaiHdong) \n" +
			" AND (:nguonVon IS NULL OR KHLCNT.NGUON_VON = :nguonVon) " +
			" AND KHLCNT.TRANG_THAI = '"+ Contains.DADUYET_LDC + "'" +
			" AND KHLCNT.TRANG_THAI_TH = '"+ Contains.CHUATONGHOP+ "' ", nativeQuery = true)
	List<HhDxuatKhLcntHdr> listTongHop(String loaiVthh,String cloaiVthh,Integer namKh, String hthucLcnt,String pthucLcnt, String loaiHdong,String nguonVon, List<String> listMaDvi, int allDvi);

	List<HhDxuatKhLcntHdr> findByIdIn(List<Long> id);

	@Transactional
	void deleteAllByIdIn(List<Long> ids);

	@Query(value = "SELECT * FROM HH_QD_KHLCNT_HDR QDKHLCNT " +
			" WHERE (:namKh IS NULL OR QDKHLCNT.NAM_KHOACH = TO_NUMBER(:namKh)) "+
			" AND (:loaiVthh IS NULL OR QDKHLCNT.LOAI_VTHH = :loaiVthh) "+
			" AND (:cloaiVthh IS NULL OR QDKHLCNT.CLOAI_VTHH = :cloaiVthh) "+
			" AND (:soQd IS NULL OR QDKHLCNT.SO_QD = :soQd) "+
			" AND (:tuNgayQd IS NULL OR QDKHLCNT.NGAY_QD >= TO_DATE(:tuNgayQd, 'yyyy-MM-dd')) "+
			" AND (:denNgayQd IS NULL OR QDKHLCNT.NGAY_QD <= TO_DATE(:denNgayQd, 'yyyy-MM-dd')) " +
			" AND (:trangThai IS NULL OR QDKHLCNT.TRANG_THAI = :trangThai) ",
			nativeQuery = true)
	List<HhQdKhlcntHdr> selectAll(String namKh, String loaiVthh, String cloaiVthh, String soQd, String tuNgayQd, String denNgayQd, String trangThai);

	@Query(value = " SELECT NVL(SUM(DSGCT.SO_LUONG),0) FROM HH_QD_KHLCNT_HDR HDR " +
			" INNER JOIN HH_QD_KHLCNT_DTL DTL on HDR.ID = DTL.ID_QD_HDR " +
			" LEFT JOIN HH_QD_KHLCNT_DSGTHAU DSG ON DSG.ID_QD_DTL = DTL.ID " +
			" LEFT JOIN HH_QD_KHLCNT_DSGTHAU_CTIET DSGCT ON DSG.ID = DSGCT.ID_GOI_THAU " +
			"WHERE HDR.NAM_KHOACH = :namKh AND HDR.LOAI_VTHH = :loaiVthh AND DSGCT.MA_DVI = :maDvi AND HDR.TRANG_THAI = :trangThai AND HDR.LASTEST = 1",
			nativeQuery = true)
	BigDecimal countSLDalenKh(Integer namKh, String loaiVthh, String maDvi,String trangThai);

	HhDxuatKhLcntHdr findAllByLoaiVthhAndCloaiVthhAndNamKhoachAndMaDviAndTrangThaiNot(String loaiVthh,String cloaiVthh,Integer namKhoach,String maDvi,String trangThai);

	@Query(value = "SELECT DX_HDR.ID AS A,QD_HDR.ID,QD_HDR.SO_QD," +
			"       NVL((SELECT COUNT(QD_GT.ID)  FROM  HH_QD_KHLCNT_DSGTHAU QD_GT WHERE (:trangThai is null or QD_GT.TRANG_THAI = :trangThai) AND QD_GT.ID_QD_DTL = QD_DTL.ID GROUP BY QD_DTL.ID),0) AS C " +
			" FROM HH_DX_KHLCNT_HDR DX_HDR , HH_QD_KHLCNT_DTL QD_DTL , HH_QD_KHLCNT_HDR QD_HDR " +
			" WHERE DX_HDR.ID = QD_DTL.ID_DX_HDR " +
			" AND QD_HDR.ID = QD_DTL.ID_QD_HDR " +
			" AND QD_HDR.LASTEST = 1 " +
			" AND DX_HDR.ID IN (:dxIds) "
			, nativeQuery = true)
	List<Object[]> getQdPdKhLcnt(Collection<Long> dxIds, String trangThai);

	@Query(value = "SELECT DX_HDR.ID AS A,QD_HDR.ID,QD_HDR.SO_QD," +
			"       NVL((SELECT COUNT(QD_GT.ID)  FROM  HH_QD_KHLCNT_DSGTHAU QD_GT WHERE (:trangThai is null or QD_GT.TRANG_THAI = :trangThai) AND QD_GT.ID_QD_DTL = QD_DTL.ID GROUP BY QD_DTL.ID),0) AS C " +
			" FROM HH_DX_KHLCNT_HDR DX_HDR , HH_QD_KHLCNT_DTL QD_DTL , HH_QD_KHLCNT_HDR QD_HDR " +
			" WHERE DX_HDR.ID = QD_HDR.ID_TR_HDR " +
			" AND QD_HDR.ID = QD_DTL.ID_QD_HDR " +
			" AND QD_HDR.LASTEST = 1 " +
			" AND DX_HDR.ID IN (:dxIds) "
			, nativeQuery = true)
	List<Object[]> getQdPdKhLcntVt(Collection<Long> dxIds, String trangThai);

	@Query(value = " SELECT dtl.GIA_QD_VAT_BTC FROM KH_PAG_QD_BTC_CTIET dtl " +
			"JOIN KH_PAG_QD_BTC hdr ON dtl.QD_BTC_ID = hdr.ID " +
			" WHERE hdr.TRANG_THAI = '29' AND hdr.LOAI_GIA = 'LG01'  AND hdr.CLOAI_VTHH = :cloaiVthh AND dtl.MA_DVI = :maDvi AND hdr.NAM_KE_HOACH = :namKhoach AND hdr.NGAY_HIEU_LUC <= SYSDATE " +
			" FETCH FIRST 1 ROWS ONLY ",
			nativeQuery = true)
	BigDecimal getGiaBanToiDaLt(String cloaiVthh, String maDvi, String namKhoach);

	@Query(value = " SELECT dtl.GIA_QD_VAT FROM KH_PAG_TT_CHUNG dtl " +
			"JOIN KH_PAG_QD_BTC hdr ON dtl.QD_BTC_ID = hdr.ID " +
			" WHERE hdr.TRANG_THAI = '29' AND hdr.LOAI_GIA = 'LG01'  AND dtl.CLOAI_VTHH = :cloaiVthh AND hdr.NAM_KE_HOACH = :namKhoach AND hdr.NGAY_HIEU_LUC <= SYSDATE " +
			" FETCH FIRST 1 ROWS ONLY ",
			nativeQuery = true)
	BigDecimal getGiaBanToiDaVt(String cloaiVthh, String namKhoach);

	@Query(value = " select ct.id from KH_CHI_TIEU_KE_HOACH_NAM ct join HH_DX_KHLCNT_HDR hdr ON ct.SO_QUYET_DINH = hdr.SO_QD " +
			" WHERE hdr.id = :khlcntId AND ct.NAM_KE_HOACH = :namKhoach ",
			nativeQuery = true)
	BigDecimal getIdByKhLcnt (Long khlcntId, Integer namKhoach);

	@Query(value = "SELECT DX.* FROM HH_DX_KHLCNT_HDR DX " +
			" JOIN HH_DX_KHLCNT_THOP_DTL dtl ON dx.id = dtl.ID_DX_HDR "+
			" JOIN HH_DX_KHLCNT_THOP_HDR hdr ON dtl.ID_THOP_HDR = HDR.ID "+
			" WHERE 1=1 "+
			" AND (:idThopHdr IS NULL OR HDR.ID = :idThopHdr) ",
			nativeQuery = true)
	Optional<HhDxuatKhLcntHdr> getByIdThopHrd(Long idThopHdr);
	@Query(value = "SELECT DX.* FROM HH_DX_KHLCNT_HDR DX " +
			" JOIN HH_DX_KHLCNT_THOP_DTL dtl ON dx.id = dtl.ID_DX_HDR "+
			" JOIN HH_DX_KHLCNT_THOP_HDR hdr ON dtl.ID_THOP_HDR = HDR.ID "+
			" WHERE 1=1 "+
			" AND (:idThopHdr IS NULL OR HDR.ID = :idThopHdr) ",
			nativeQuery = true)
	List<HhDxuatKhLcntHdr> getAllByIdThopHrd(Long idThopHdr);
}
