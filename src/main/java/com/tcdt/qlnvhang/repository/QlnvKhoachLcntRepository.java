package com.tcdt.qlnvhang.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.tcdt.qlnvhang.table.QlnvKhoachLcnt;

@Repository
public interface QlnvKhoachLcntRepository extends CrudRepository<QlnvKhoachLcnt, Long> {
	@Query(value = "SELECT * FROM QLNV_KHOACH_LCNT t WHERE (:maDvi is null or t.MA_DVI = :maDvi) AND (:loaiHanghoa is null or t.LOAI_HANGHOA = :loaiHanghoa) "
			+ "AND (:nguoiTao is null or t.NGUOI_TAO = :nguoiTao) AND (:trangThai is null or t.TRANG_THAI = :trangThai) "
			+ "AND (:tuNgayLap is null or t.NGAY_TAO > TO_DATE(:tuNgayLap, 'dd/MM/yyyy')) "
			+ "AND (:denNgayLap is null or t.NGAY_TAO <= TO_DATE(:denNgayLap, 'dd/MM/yyyy'))", countQuery = "SELECT count(1) FROM QLNV_KHOACH_LCNT t "
					+ "WHERE (:maDvi is null or t.MA_DVI = :maDvi) AND (:loaiHanghoa is null or t.LOAI_HANGHOA = :loaiHanghoa) "
					+ "AND (:nguoiTao is null or t.NGUOI_TAO = :nguoiTao) AND (:trangThai is null or t.TRANG_THAI = :trangThai)"
					+ "AND (:tuNgayLap is null or t.NGAY_TAO > TO_DATE(:tuNgayLap, 'dd/MM/yyyy')) "
					+ "AND (:denNgayLap is null or t.NGAY_TAO <= TO_DATE(:denNgayLap, 'dd/MM/yyyy'))", nativeQuery = true)
	Page<QlnvKhoachLcnt> selectParams(String maDvi, String loaiHanghoa, String trangThai, String nguoiTao, String tuNgayLap,
			String denNgayLap, Pageable pageable);

}
