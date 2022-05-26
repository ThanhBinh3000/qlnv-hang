package com.tcdt.qlnvhang.repository;

import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.tcdt.qlnvhang.table.HhSoKhoHdr;
import com.tcdt.qlnvhang.util.Contains;

@Repository
public interface HhSoKhoHdrRepository extends CrudRepository<HhSoKhoHdr, Long> {

	@Query(value = "SELECT * FROM HH_SO_KHO_HDR t"
			+ " WHERE (:maDvi is null or t.MA_DVI = :maDvi) AND (:tenHhoa is null or t.TEN_HHOA = :tenHhoa)"
			+ "AND (:maHhoa is null or t.MA_HHOA = :maHhoa)"
			+ "AND (:maNgan is null or t.MA_NGAN = :maNgan)"
			+ "AND (:maLo is null or t.MA_LO = :maLo)"
			+ " AND (:tuNgayMoSo is null or t.NGAY_MO_SO >= TO_DATE(:tuNgayMoSo,'" + Contains.FORMAT_DATE_STR + "'))"
			+ " AND (:denNgayMoSo is null or t.NGAY_MO_SO < TO_DATE(:denNgayMoSo,'" + Contains.FORMAT_DATE_STR
			+ "') + INTERVAL '1' DAY)", nativeQuery = true)
	Page<HhSoKhoHdr> selectParams(String maDvi, String tenHhoa, String maHhoa,String maNgan,String maLo, Date tuNgayMoSo, Date denNgayMoSo,
                                  Pageable pageable);

}
