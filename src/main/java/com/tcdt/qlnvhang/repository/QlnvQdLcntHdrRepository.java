package com.tcdt.qlnvhang.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tcdt.qlnvhang.table.QlnvQdLcntHdr;

public interface QlnvQdLcntHdrRepository extends BaseRepository<QlnvQdLcntHdr, Long> {

	final String qr = "SELECT * FROM QLNV_QD_LCNT_HDR WHERE (:soQdinh is null or lower(SO_QDINH) like lower(concat(concat('%', :soQdinh),'%'))) AND (:soQdGiaoCtkh is null or lower(SO_QD_GIAO_CTKH) like lower(concat(concat('%', :soQdGiaoCtkh),'%'))) "
			+ "AND (:tuNgayQd is null or t.NGAY_QD > TO_DATE(:tuNgayQd, 'DD/MM/YYYY')) "
			+ "AND (:denNgayQd is null or t.NGAY_QD <= TO_DATE(:denNgayQd, 'DD/MM/YYYY')) "
			+ "AND (:maHanghoa is null or lower(MA_HANGHOA) like lower(concat(concat('%', :maHanghoa),'%'))) "
			+ "AND (:loaiHanghoa is null or lower(LOAI_HANGHOA) like lower(concat(concat('%', :loaiHanghoa),'%'))) "
			+ "AND (:soQdinhGoc is null or lower(SO_QDINH_GOC) like lower(concat(concat('%', :soQdinhGoc),'%'))) "
			+ "AND (:loaiDieuChinh is null or lower(LOAI_DIEU_CHINH) like lower(concat(concat('%', :loaiDieuChinh),'%'))) ";
	final String qrCount = "SELECT count(1) FROM QLNV_QD_LCNT_HDR WHERE (:soQdinh is null or lower(SO_QDINH) like lower(concat(concat('%', :soQdinh),'%'))) AND (:soQdGiaoCtkh is null or lower(SO_QD_GIAO_CTKH) like lower(concat(concat('%', :soQdGiaoCtkh),'%'))) "
			+ "AND (:tuNgayQd is null or t.NGAY_QD > TO_DATE(:tuNgayQd, 'DD/MM/YYYY')) "
			+ "AND (:denNgayQd is null or t.NGAY_QD <= TO_DATE(:denNgayQd, 'DD/MM/YYYY')) "
			+ "AND (:maHanghoa is null or lower(MA_HANGHOA) like lower(concat(concat('%', :maHanghoa),'%'))) "
			+ "AND (:loaiHanghoa is null or lower(LOAI_HANGHOA) like lower(concat(concat('%', :loaiHanghoa),'%'))) "
			+ "AND (:soQdinhGoc is null or lower(SO_QDINH_GOC) like lower(concat(concat('%', :soQdinhGoc),'%'))) "
			+ "AND (:loaiDieuChinh is null or lower(LOAI_DIEU_CHINH) like lower(concat(concat('%', :loaiDieuChinh),'%'))) ";

	@Deprecated
	@Query(value = qr, countQuery = qrCount, nativeQuery = true)
	Page<QlnvQdLcntHdr> selectParams(@Param("soQdinh") String soQdinh, @Param("soQdGiaoCtkh") String soQdGiaoCtkh,
			@Param("tuNgayQd") String tuNgayQd,@Param("denNgayQd") String denNgayQd,@Param("maHanghoa") String maHanghoa,@Param("loaiHanghoa") String loaiHanghoa,
			@Param("soQdinhGoc") String soQdinhGoc, @Param("loaiDieuChinh") String loaiDieuChinh,
			Pageable pageable);
}
