package com.tcdt.qlnvhang.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.tcdt.qlnvhang.table.QlnvDxkhMuaHangHdr;

public interface QlnvDxkhBanHangRepository extends BaseRepository<QlnvDxkhMuaHangHdr, Long> {
//	final String querySelect = "SELECT *";
//	final String queryCount = "SELECT count(1)";
//	final String queryTable = " FROM QLNV_DXKH_MUA_HANG_HDR t"
//			+ " WHERE (:soDxuat is null or lower(t.SO_DXUAT) like lower(concat(concat('%', :soDxuat),'%')))"
//			+ " AND (:maDvi is null or t.MA_DVI = :maDvi) AND (:trangThai is null or t.TRANG_THAI = :trangThai)"
//			+ " AND (:tuNgayLap is null or t.NGAY_LAP >= TO_DATE(:tuNgayLap,'dd/mm/yyyy'))"
//			+ " AND (:denNgayLap is null or t.NGAY_LAP < TO_DATE(:denNgayLap,'dd/mm/yyyy') + INTERVAL '1' DAY)"
//			+ " AND (:maHhoa is null or lower(t.MA_HHOA) like lower(concat(concat('%', :maHhoa),'%')))"
//			+ " AND (:soQdKhoach is null or lower(t.SO_QD_KHOACH) like lower(concat(concat('%', :soQdKhoach),'%')))";
//
//	@Query(value = querySelect + queryTable, countQuery = queryCount + queryTable, nativeQuery = true)
//	Page<QlnvDxkhMuaHangHdr> selectParams(String soDxuat, String maDvi, String trangThai, Date tuNgayLap, Date denNgayLap,
//			String maHhoa, String soQdKhoach, Pageable pageable);

	@Transactional()
	@Modifying
	@Query(value = "UPDATE QLNV_DXKH_MUA_HANG_HDR SET TRANG_THAI=:trangThai WHERE SO_DXUAT IN :soDxuatList", nativeQuery = true)
	void updateTongHop(List<String> soDxuatList, String trangThai);

	@EntityGraph(value = "QLNV_DXKH_MUA_HANG_HDR.children")
	Optional<QlnvDxkhMuaHangHdr> findById(Long id);
}