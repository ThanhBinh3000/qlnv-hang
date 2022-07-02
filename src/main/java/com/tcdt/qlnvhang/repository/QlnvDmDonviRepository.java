package com.tcdt.qlnvhang.repository;

import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.tcdt.qlnvhang.entities.QlnvDmDonviEntity;
import com.tcdt.qlnvhang.table.catalog.QlnvDmDonvi;

@Repository
public interface QlnvDmDonviRepository extends CrudRepository<QlnvDmDonvi, Long> {
	@Query(value = "SELECT t.id,t.ma_dvi,(select ten_dvi from DM_DONVI where ma_dvi = t.ma_dvi_cha ) as MA_DVI_CHA, t.TEN_DVI, t.MA_HCHINH, "
			+ "(select ten_dbhc from dm_dbhc where t.ma_tinh = ma_dbhc) as MA_TINH, "
			+ "(select ten_dbhc from dm_dbhc where t.ma_quan = ma_dbhc) as MA_QUAN, "
			+ "(select ten_dbhc from dm_dbhc where t.ma_phuong = ma_dbhc) as MA_PHUONG, "
			+ "t.dia_chi, t.cap_dvi, t.kieu_dvi, t.loai_dvi, t.ghi_chu, t.trang_thai, t.ngay_tao,"
			+ "t.nguoi_tao, t.ngay_sua, t.nguoi_sua FROM DM_DONVI t WHERE (:maDvi is null or lower(t.MA_DVI) like lower(concat(concat('%', :maDvi),'%'))) "
			+ "AND (:tenDvi is null or lower(t.TEN_DVI) like lower(concat(concat('%', :tenDvi),'%'))) AND (:trangThai is null or t.TRANG_THAI = :trangThai) "
			+ "AND (:maTinh is null or t.MA_TINH = :maTinh) AND (:maQuan is null or t.MA_QUAN = :maQuan) AND (:maPhuong is null or t.MA_PHUONG = :maPhuong) "
			+ "AND (:capDvi is null or t.CAP_DVI = :capDvi) AND (:kieuDvi is null or t.KIEU_DVI = :kieuDvi) AND (:loaiDvi is null or t.LOAI_DVI = :loaiDvi)", countQuery = "SELECT count(1) FROM DM_DONVI t "
					+ "WHERE (:maDvi is null or lower(t.MA_DVI) like lower(concat(concat('%', :maDvi),'%'))) "
					+ "AND (:tenDvi is null or lower(t.TEN_DVI) like lower(concat(concat('%', :tenDvi),'%'))) AND (:trangThai is null or t.TRANG_THAI = :trangThai) "
					+ "AND (:maTinh is null or t.MA_TINH = :maTinh) AND (:maQuan is null or t.MA_QUAN = :maQuan) AND (:maPhuong is null or t.MA_PHUONG = :maPhuong) "
					+ "AND (:capDvi is null or t.CAP_DVI = :capDvi) AND (:kieuDvi is null or t.KIEU_DVI = :kieuDvi) AND (:loaiDvi is null or t.LOAI_DVI = :loaiDvi)", nativeQuery = true)
	Page<QlnvDmDonviEntity> selectParams(String maDvi, String tenDvi, String trangThai, String maTinh, String maQuan,
			String maPhuong, String capDvi, String kieuDvi, String loaiDvi, Pageable pageable);

	QlnvDmDonvi findByMaDvi(String maDvi);

	List<QlnvDmDonvi> findByMaDviIn(Set<String> maDvi);

	@Transactional
	@Modifying
	@Query(value = "DELETE FROM DM_DONVI u WHERE u.ID in ?1", nativeQuery = true)
	int deleteWithIds(List<Long> ids);

	Iterable<QlnvDmDonvi> findByTrangThai(String trangThai);
	
	@Query(value = "SELECT * from DM_DONVI where MA_DVI_CHA = :maDviCha AND (:trangThai is null or TRANG_THAI = :trangThai) ", nativeQuery = true)
	List<QlnvDmDonvi> findByMaDviChaAndTrangThai(String maDviCha, String trangThai);

	@Query(value = "SELECT dv.maDvi FROM QlnvDmDonvi dv WHERE dv.parent.maDvi = ?1 AND dv.trangThai = ?2")
	Set<String> findMaDviByMaDviChaAndTrangThai(String maDviCha, String trangThai);
}
