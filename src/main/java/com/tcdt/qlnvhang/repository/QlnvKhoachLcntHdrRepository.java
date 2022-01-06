package com.tcdt.qlnvhang.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.tcdt.qlnvhang.table.QlnvKhoachLcntHdr;

@Repository
public interface QlnvKhoachLcntHdrRepository extends CrudRepository<QlnvKhoachLcntHdr, Long> {
	@Query(value = "SELECT * FROM QLNV_KHOACH_LCNT_HDR t WHERE (:maDvi is null or t.MA_DVI = :maDvi) AND (:loaiHanghoa is null or t.LOAI_HANGHOA = :loaiHanghoa) "
			+ "AND (:hanghoa is null or t.HANGHOA = :hanghoa) "
			+ "AND (:trangThai is null or t.TRANG_THAI = :trangThai) "
			+ "AND (:soDx is null or t.SO_DX = :soDx) "
			+ "AND (:tuNgayLap is null or t.NGAY_DX > TO_DATE(:tuNgayLap, 'dd/MM/yyyy')) "
			+ "AND (:denNgayLap is null or t.NGAY_DX <= TO_DATE(:denNgayLap, 'dd/MM/yyyy'))", countQuery = "SELECT count(1) FROM QLNV_KHOACH_LCNT_HDR t "
					+ "WHERE (:maDvi is null or t.MA_DVI = :maDvi) AND (:loaiHanghoa is null or t.LOAI_HANGHOA = :loaiHanghoa) "
					+ "AND (:hanghoa is null or t.HANGHOA = :hanghoa) "
					+ "AND (:trangThai is null or t.TRANG_THAI = :trangThai)"
					+ "AND (:soDx is null or t.SO_DX = :soDx) "
					+ "AND (:tuNgayLap is null or t.NGAY_DX > TO_DATE(:tuNgayLap, 'dd/MM/yyyy')) "
					+ "AND (:denNgayLap is null or t.NGAY_DX <= TO_DATE(:denNgayLap, 'dd/MM/yyyy'))", nativeQuery = true)
	Page<QlnvKhoachLcntHdr> selectParams(String maDvi, String loaiHanghoa,String hanghoa, String trangThai, String soDx, String tuNgayLap,
			String denNgayLap, Pageable pageable);
	
	@Query(value = "SELECT * FROM QLNV_KHOACH_LCNT_HDR t WHERE (:loaiHanghoa is null or t.LOAI_HANGHOA = :loaiHanghoa) "
			+ "AND (:nguonvon is null or t.NGUONVON = :nguonvon) "
			+ "AND (:hanghoa is null or t.HANGHOA = :hanghoa) "
			+ "AND (:soQdGiaoCtkh is null or t.SO_QD_GIAO_CTKH = :soQdGiaoCtkh) "
			+ "AND (:trangThai is null or t.TRANG_THAI = :trangThai) and t.TRANG_THAI_TH = '00'", nativeQuery = true)
	List<QlnvKhoachLcntHdr> getTHop(String loaiHanghoa, String nguonvon, String hanghoa, String soQdGiaoCtkh,
			String trangThai);

}
