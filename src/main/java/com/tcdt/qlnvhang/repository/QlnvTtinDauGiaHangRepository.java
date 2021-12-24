package com.tcdt.qlnvhang.repository;

import java.util.Date;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import com.tcdt.qlnvhang.table.QlnvTtinDauGiaHangHdr;

public interface QlnvTtinDauGiaHangRepository extends CrudRepository<QlnvTtinDauGiaHangHdr, Long> {
	final String querySelect = "SELECT *";
	final String queryCount = "SELECT count(1)";
	final String queryTable = " FROM QLNV_DAUGIA_HANG_HDR t"
			+ " WHERE 1 = 1"
			+ " AND (:maDvi is null or t.MA_DVI = :maDvi) "
			+ " AND (:trangThai is null or t.TRANG_THAI = :trangThai)"
			+ " AND (:tuNgayDgia is null or t.NGAY_DAUGIA >= TO_DATE(:tuNgayDgia,'dd/mm/yyyy'))"
			+ " AND (:denNgayDgia is null or t.NGAY_DAUGIA < TO_DATE(:denNgayDgia,'dd/mm/yyyy') + INTERVAL '1' DAY)"
			+ " AND (:maHhoa is null or lower(t.MA_HANGHOA) like lower(concat(concat('%', :maHhoa),'%')))"
			+ " AND (:soQdKhoach is null or lower(t.SO_QD_KHOACH) like lower(concat(concat('%', :soQdKhoach),'%')))";

	@Query(value = querySelect + queryTable, countQuery = queryCount + queryTable, nativeQuery = true)
	Page<QlnvTtinDauGiaHangHdr> selectParams(String maDvi, String trangThai, Date tuNgayDgia, Date denNgayDgia,
			String maHhoa, String soQdKhoach, Pageable pageable);
}
