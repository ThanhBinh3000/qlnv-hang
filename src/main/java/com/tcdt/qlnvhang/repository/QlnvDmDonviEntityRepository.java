package com.tcdt.qlnvhang.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.tcdt.qlnvhang.entities.QlnvDmDonviEntity;

@Repository
public interface QlnvDmDonviEntityRepository extends CrudRepository<QlnvDmDonviEntity, Long> {
	String value = "SELECT t.id,t.ma_dvi,(select ten_dvi from QLNV_DM_DONVI where ma_dvi = t.ma_dvi_cha ) as MA_DVI_CHA, t.TEN_DVI, t.MA_HCHINH, "
			+ "(select ten_dbhc from qlnv_dm_dbhc where t.ma_tinh = ma_dbhc) as MA_TINH, "
			+ "(select ten_dbhc from qlnv_dm_dbhc where t.ma_quan = ma_dbhc) as MA_QUAN, "
			+ "(select ten_dbhc from qlnv_dm_dbhc where t.ma_phuong = ma_dbhc) as MA_PHUONG, "
			+ "t.dia_chi, t.cap_dvi, t.kieu_dvi, t.loai_dvi, t.ghi_chu, t.trang_thai, t.ngay_tao,"
			+ "t.nguoi_tao, t.ngay_sua, t.nguoi_sua FROM QLNV_DM_DONVI t WHERE (:maDvi is null or lower(t.MA_DVI) like lower(concat(concat('%', :maDvi),'%'))) "
			+ "AND (:tenDvi is null or lower(t.TEN_DVI) like lower(concat(concat('%', :tenDvi),'%'))) AND (:trangThai is null or t.TRANG_THAI = :trangThai) "
			+ "AND (:maTinh is null or t.MA_TINH = :maTinh) AND (:maQuan is null or t.MA_QUAN = :maQuan) AND (:maPhuong is null or t.MA_PHUONG = :maPhuong) "
			+ "AND (:capDvi is null or t.CAP_DVI = :capDvi) AND (:kieuDvi is null or t.KIEU_DVI = :kieuDvi) AND (:loaiDvi is null or t.LOAI_DVI = :loaiDvi)";
	String countQuery = "SELECT count(1) FROM QLNV_DM_DONVI t "
			+ "WHERE (:maDvi is null or lower(t.MA_DVI) like lower(concat(concat('%', :maDvi),'%'))) "
			+ "AND (:tenDvi is null or lower(t.TEN_DVI) like lower(concat(concat('%', :tenDvi),'%'))) AND (:trangThai is null or t.TRANG_THAI = :trangThai) "
			+ "AND (:maTinh is null or t.MA_TINH = :maTinh) AND (:maQuan is null or t.MA_QUAN = :maQuan) AND (:maPhuong is null or t.MA_PHUONG = :maPhuong) "
			+ "AND (:capDvi is null or t.CAP_DVI = :capDvi) AND (:kieuDvi is null or t.KIEU_DVI = :kieuDvi) AND (:loaiDvi is null or t.LOAI_DVI = :loaiDvi)";
	@Query(value = value, countQuery = countQuery, nativeQuery = true)
	Page<QlnvDmDonviEntity> selectParams(String maDvi, String tenDvi, String trangThai, String maTinh, String maQuan,
			String maPhuong, String capDvi, String kieuDvi, String loaiDvi, Pageable pageable);
	
	@Query(value = value, nativeQuery = true)
	Iterable<QlnvDmDonviEntity> finDvi(String maDvi, String tenDvi, String trangThai, String maTinh, String maQuan,
			String maPhuong, String capDvi, String kieuDvi, String loaiDvi);
}
