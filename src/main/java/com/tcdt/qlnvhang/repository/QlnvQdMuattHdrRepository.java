package com.tcdt.qlnvhang.repository;

import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.tcdt.qlnvhang.table.QlnvQdMuattHdr;

public interface QlnvQdMuattHdrRepository extends CrudRepository<QlnvQdMuattHdr, Long> {

	QlnvQdMuattHdr findBySoQdinh(String soQdinh);

	@Query(value = "SELECT * FROM QLNV_QD_MUATT_HDR t"
			+ " WHERE (:soQdinh is null or lower(t.SO_QDINH) like lower(concat(concat('%', :soQdinh),'%')))"
			+ " AND (:trangThai is null or t.TRANG_THAI = :trangThai)"
			+ " AND (:tuNgayQdinh is null or t.NGAY_QD >= TO_DATE(:tuNgayQdinh,'dd/mm/yyyy'))"
			+ " AND (:denNgayQdinh is null or t.NGAY_QD < TO_DATE(:denNgayQdinh,'dd/mm/yyyy') + INTERVAL '1' DAY)"
			+ " AND (:maHhoa is null or lower(t.MA_HANGHOA) like lower(concat(concat('%', :maHhoa),'%')))"
			+ " AND (:soQdKh is null or lower(t.SO_QD_KH) like lower(concat(concat('%', :soQdKh),'%')))", countQuery = "SELECT count(1) FROM QLNV_QD_MUATT_HDR t"
					+ " WHERE (:soQdinh is null or lower(t.SO_QDINH) like lower(concat(concat('%', :soQdinh),'%')))"
					+ " AND (:trangThai is null or t.TRANG_THAI = :trangThai)"
					+ " AND (:tuNgayQdinh is null or t.NGAY_QD >= TO_DATE(:tuNgayQdinh,'dd/mm/yyyy'))"
					+ " AND (:denNgayQdinh is null or t.NGAY_QD < TO_DATE(:denNgayQdinh,'dd/mm/yyyy') + INTERVAL '1' DAY)"
					+ " AND (:maHhoa is null or lower(t.MA_HANGHOA) like lower(concat(concat('%', :maHhoa),'%')))"
					+ " AND (:soQdKh is null or lower(t.SO_QD_KH) like lower(concat(concat('%', :soQdKh),'%')))", nativeQuery = true)
	Page<QlnvQdMuattHdr> selectParams(String soQdinh, String trangThai, Date tuNgayQdinh, Date denNgayQdinh,
			String maHhoa, String soQdKh, Pageable pageable);
}
