package com.tcdt.qlnvhang.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.tcdt.qlnvhang.table.QlnvQdMuaHangHdr2;

public interface QlnvQdMuaHangHdr2Repository extends CrudRepository<QlnvQdMuaHangHdr2, Long> {
	
	
	final String querySelect = "SELECT *";
	final String queryCount = "SELECT count(1)";
	final String queryTable = " FROM QLNV_QD_MUA_HANG_HDR t"
			+ " WHERE (:soQdinh is null or lower(t.SO_QDINH) like lower(concat(concat('%', :soQdinh),'%')))"
			+ " AND (:trangThai is null or t.TRANG_THAI = :trangThai)"
			+ " AND (:tuNgayQdinh is null or t.NGAY_QDINH >= TO_DATE(:tuNgayQdinh,'dd/mm/yyyy'))"
			+ " AND (:denNgayQdinh is null or t.NGAY_QDINH < TO_DATE(:denNgayQdinh,'dd/mm/yyyy') + INTERVAL '1' DAY)"
			+ " AND (:maHhoa is null or lower(t.MA_HANGHOA) like lower(concat(concat('%', :maHhoa),'%')))"
			+ " AND (:soQdKh is null or lower(t.SO_QD_KH) like lower(concat(concat('%', :soQdKh),'%')))"
			+ " AND LOAI_DCHINH in :listLoaiDc";

	@Query(value = querySelect + queryTable, countQuery = queryCount + queryTable, nativeQuery = true)
	Page<QlnvQdMuaHangHdr2> selectParams(String soQdinh, String trangThai, Date tuNgayQdinh, Date denNgayQdinh,
			String maHhoa, String soQdKh, List<String> listLoaiDc, Pageable pageable);
	
	
	
}
