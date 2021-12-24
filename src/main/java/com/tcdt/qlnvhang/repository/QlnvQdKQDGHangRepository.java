package com.tcdt.qlnvhang.repository;

import java.util.Date;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import com.tcdt.qlnvhang.table.QlnvQdKQDGHangHdr;

public interface QlnvQdKQDGHangRepository extends BaseRepository<QlnvQdKQDGHangHdr, Long> {
	final String querySelect = "SELECT *";
	final String queryCount = "SELECT count(1)";
	final String queryTable = " FROM QLNV_QD_KQDG_HANG_HDR t"
			+ " WHERE 1 = 1"
			+ " AND (:maDvi is null or t.MA_DVI = :maDvi) "
			+ " AND (:trangThai is null or t.TRANG_THAI = :trangThai)"
			+ " AND (:tuNgay is null or t.NGAY_QDINH >= TO_DATE(:tuNgay,'dd/mm/yyyy'))"
			+ " AND (:denNgay is null or t.NGAY_QDINH < TO_DATE(:denNgay,'dd/mm/yyyy') + INTERVAL '1' DAY)"
			+ " AND (:maHhoa is null or lower(t.MA_HANGHOA) like lower(concat(concat('%', :maHhoa),'%')))"
			+ " AND (:soQdinh is null or lower(t.SO_QDINH) like lower(concat(concat('%', :soQdinh),'%')))";

	@Query(value = querySelect + queryTable, countQuery = queryCount + queryTable, nativeQuery = true)
	Page<QlnvQdKQDGHangHdr> selectParams(String maDvi, String trangThai, Date tuNgay, Date denNgay,
			String maHhoa, String soQdinh, Pageable pageable);
}
