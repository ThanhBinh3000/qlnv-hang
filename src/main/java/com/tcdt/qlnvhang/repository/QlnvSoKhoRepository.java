package com.tcdt.qlnvhang.repository;

import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.tcdt.qlnvhang.table.QlnvSoKho;
import com.tcdt.qlnvhang.util.Contains;

@Repository
public interface QlnvSoKhoRepository extends CrudRepository<QlnvSoKho, Long> {

	@Query(value = "SELECT * FROM QLNV_SO_KHO t"
			+ " WHERE (:soKho is null or lower(t.SO_KHO) like lower(concat(concat('%', :soKho),'%')))"
			+ " AND (:maDvi is null or t.MA_DVI = :maDvi) AND (:maKho is null or t.MA_KHO = :maKho)"
			+ " AND (:tuNgayLap is null or t.NGAY_LAP >= TO_DATE(:tuNgayLap,'" + Contains.FORMAT_DATE_STR + "'))"
			+ " AND (:denNgayLap is null or t.NGAY_LAP < TO_DATE(:denNgayLap,'" + Contains.FORMAT_DATE_STR
			+ "') + INTERVAL '1' DAY)", countQuery = "SELECT count(1) FROM QLNV_DXKH_MUA_TT_HDR t"
					+ " WHERE (:soKho is null or lower(t.SO_KHO) like lower(concat(concat('%', :soKho),'%')))"
					+ " AND (:maDvi is null or t.MA_DVI = :maDvi) " + " AND (:maKho is null or t.MA_KHO = :maKho)"
					+ " AND (:tuNgayLap is null or t.NGAY_LAP >= TO_DATE(:tuNgayLap,'" + Contains.FORMAT_DATE_STR
					+ "'))" + " AND (:denNgayLap is null or t.NGAY_LAP < TO_DATE(:denNgayLap,'"
					+ Contains.FORMAT_DATE_STR + "') + INTERVAL '1' DAY)", nativeQuery = true)
	Page<QlnvSoKho> selectParams(String soKho, String maDvi, String maKho, Date tuNgayLap, Date denNgayLap,
			Pageable pageable);

}
