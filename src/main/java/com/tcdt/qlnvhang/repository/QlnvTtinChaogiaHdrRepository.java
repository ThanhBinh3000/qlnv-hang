package com.tcdt.qlnvhang.repository;

import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.tcdt.qlnvhang.table.QlnvTtinChaogiaHdr;

public interface QlnvTtinChaogiaHdrRepository extends CrudRepository<QlnvTtinChaogiaHdr, Long> {

	final String querySelect = "SELECT *";
	final String queryCount = "SELECT count(1)";
	final String queryTable = " FROM QLNV_TTIN_CHAOGIA_HDR t"
			+ " WHERE (:soQdKhoach is null or lower(t.SO_QD_KHOACH) like lower(concat(concat('%', :soQdKhoach),'%')))"
			+ " AND (:trangThai is null or t.TRANG_THAI = :trangThai)"
			+ " AND (:tuNgayCgia is null or t.NGAY_THIEN >= TO_DATE(:tuNgayCgia,'dd/mm/yyyy'))"
			+ " AND (:denNgayCgia is null or t.NGAY_THIEN < TO_DATE(:denNgayCgia,'dd/mm/yyyy') + INTERVAL '1' DAY)"
			+ " AND (:maHhoa is null or lower(t.MA_HHOA) like lower(concat(concat('%', :maHhoa),'%')))"
			+ " AND (:loaiMuaban is null or t.LOAI_MUABAN = :loaiMuaban)"
			+ " AND (:maDvi is null or t.MA_DVI = :maDvi)";

	@Query(value = querySelect + queryTable, countQuery = queryCount + queryTable, nativeQuery = true)
	Page<QlnvTtinChaogiaHdr> selectParams(String soQdKhoach, String trangThai, Date tuNgayCgia, Date denNgayCgia,
			String maHhoa, String loaiMuaban, String maDvi, Pageable pageable);

}
