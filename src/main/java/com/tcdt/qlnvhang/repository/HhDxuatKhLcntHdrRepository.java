package com.tcdt.qlnvhang.repository;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.table.HhQdKhlcntHdr;
import com.tcdt.qlnvhang.util.Contains;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.tcdt.qlnvhang.table.HhDxuatKhLcntHdr;

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
					"  AND (:ngayKyTu IS NULL OR KHLCNT.NGAY_KY >= TO_DATE(:ngayKyTu, 'yyyy-MM-dd'))" +
					"  AND (:ngayKyDen IS NULL OR KHLCNT.NGAY_KY <= TO_DATE(:ngayKyDen, 'yyyy-MM-dd'))" +
					"  AND (:loaiVthh IS NULL OR KHLCNT.LOAI_VTHH = :loaiVthh) " +
					"  AND (:trichYeu IS NULL OR LOWER(KHLCNT.TRICH_YEU) LIKE LOWER(CONCAT(CONCAT('%', :trichYeu),'%')))" +
					"  AND (:trangThai IS NULL OR KHLCNT.TRANG_THAI = :trangThai) "+
					"  AND (:trangThaiTh IS NULL OR KHLCNT.TRANG_THAI_TH = :trangThaiTh) " +
					"  AND (:maDvi IS NULL OR KHLCNT.MA_DVI = :maDvi) "
			,nativeQuery = true)
	Page<HhDxuatKhLcntHdr> select(String namKh, String soTr,String soQd, String ngayKyTu,String ngayKyDen,String loaiVthh,String trichYeu,String trangThai,String trangThaiTh,String maDvi, Pageable pageable);

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
			" AND KHLCNT.HTHUC_LCNT = :hthucLcnt \n" +
			" AND KHLCNT.PTHUC_LCNT = :pthucLcnt \n" +
			" AND KHLCNT.LOAI_HDONG = :loaiHdong \n" +
			" AND KHLCNT.NGUON_VON = :nguonVon " +
			" AND KHLCNT.TRANG_THAI = '"+ Contains.DADUYET_LDC + "'" +
			" AND KHLCNT.TRANG_THAI_TH = '"+ Contains.CHUATONGHOP+ "' ", nativeQuery = true)
	List<HhDxuatKhLcntHdr> listTongHop(String loaiVthh,String cloaiVthh,Integer namKh, String hthucLcnt,String pthucLcnt, String loaiHdong,String nguonVon);

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

	@Query(value = " SELECT NVL(SUM(DSG.SO_LUONG),0) FROM HH_QD_KHLCNT_HDR HDR " +
			" INNER JOIN HH_QD_KHLCNT_DTL DTL on HDR.ID = DTL.ID_QD_HDR " +
			" LEFT JOIN HH_QD_KHLCNT_DSGTHAU DSG ON DSG.ID_QD_DTL = DTL.ID " +
			"WHERE HDR.NAM_KHOACH = :namKh AND HDR.LOAI_VTHH = :loaiVthh AND DSG.MA_DVI = :maDvi AND HDR.TRANG_THAI = :trangThai AND HDR.LASTEST = 1",
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
}
