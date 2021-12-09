package com.tcdt.qlnvhang.repository;

import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.tcdt.qlnvhang.table.QlnvDxkhMuaTtHdr;

public interface QlnvDxkhMuaTtHdrRepository extends CrudRepository<QlnvDxkhMuaTtHdr, Long> {

	@Query(value = "SELECT * FROM QLNV_DXKH_MUA_TT_HDR t"
			+ " WHERE (:soDxuat is null or lower(t.SO_DXUAT) like lower(concat(concat('%', :soDxuat),'%')))"
			+ " AND (:maDvi is null or t.MA_DVI = :maDvi) AND (:trangThai is null or t.TRANG_THAI = :trangThai)"
			+ " AND (:tuNgayLap is null or t.NGAY_LAP >= TO_DATE(:tuNgayLap,'dd/mm/yyyy'))"
			+ " AND (:denNgayLap is null or t.NGAY_LAP < TO_DATE(:denNgayLap,'dd/mm/yyyy') + INTERVAL '1' DAY)"
			+ " AND (:maHhoa is null or lower(t.MA_HHOA) like lower(concat(concat('%', :maHhoa),'%')))"
			+ " AND (:soQdKhoach is null or lower(t.SO_QD_KHOACH) like lower(concat(concat('%', :soQdKhoach),'%')))", countQuery = "SELECT count(1) FROM QLNV_DXKH_MUA_TT_HDR t"
					+ " WHERE (:soDxuat is null or lower(t.SO_DXUAT) like lower(concat(concat('%', :soDxuat),'%')))"
					+ " AND (:maDvi is null or t.MA_DVI = :maDvi) AND (:trangThai is null or t.TRANG_THAI = :trangThai)"
					+ " AND (:tuNgayLap is null or t.NGAY_LAP >= TO_DATE(:tuNgayLap,'dd/mm/yyyy'))"
					+ " AND (:denNgayLap is null or t.NGAY_LAP < TO_DATE(:denNgayLap,'dd/mm/yyyy') + INTERVAL '1' DAY)"
					+ " AND (:maHhoa is null or lower(t.MA_HHOA) like lower(concat(concat('%', :maHhoa),'%')))"
					+ " AND (:soQdKhoach is null or lower(t.SO_QD_KHOACH) like lower(concat(concat('%', :soQdKhoach),'%')))", nativeQuery = true)
	Page<QlnvDxkhMuaTtHdr> selectParams(String soDxuat, String maDvi, String trangThai, Date tuNgayLap, Date denNgayLap,
			String maHhoa, String soQdKhoach, Pageable pageable);
}
