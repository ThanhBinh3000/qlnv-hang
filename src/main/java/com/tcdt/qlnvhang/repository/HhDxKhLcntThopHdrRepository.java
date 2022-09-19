package com.tcdt.qlnvhang.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.tcdt.qlnvhang.table.HhDxKhLcntThopHdr;

import java.util.List;

public interface HhDxKhLcntThopHdrRepository extends BaseRepository<HhDxKhLcntThopHdr, Long> {

	@Transactional()
	@Modifying
	@Query(value = "UPDATE HH_DX_KHLCNT_THOP_HDR SET PHUONG_AN=:trangThai WHERE ID = :idThHdr", nativeQuery = true)
	void updateTongHop(Long idThHdr, String trangThai);

	@Transactional()
	@Modifying
	@Query(value = "UPDATE HH_DX_KHLCNT_THOP_HDR SET TRANG_THAI =:trangThai WHERE ID = :idThHdr", nativeQuery = true)
	void updateTrangThai(Long idThHdr, String trangThai);

	@Query(
			value = "SELECT * " +
					"FROM HH_DX_KHLCNT_THOP_HDR " +
					" WHERE (:namKh IS NULL OR HH_DX_KHLCNT_THOP_HDR.NAM_KHOACH = TO_NUMBER(:namKh))  " +
					"  AND (:loaiVthh IS NULL OR HH_DX_KHLCNT_THOP_HDR.LOAI_VTHH = :loaiVthh) " +
					"  AND (:cloaiVthh IS NULL OR HH_DX_KHLCNT_THOP_HDR.CLOAI_VTHH = :cloaiVthh) " +
					"  AND (:tuNgayThop IS NULL OR HH_DX_KHLCNT_THOP_HDR.NGAY_THOP >= TO_DATE(:tuNgayThop, 'yyyy-MM-dd')) " +
					"  AND (:denNgayThop IS NULL OR HH_DX_KHLCNT_THOP_HDR.NGAY_THOP <= TO_DATE(:denNgayThop, 'yyyy-MM-dd')) " +
					"  AND (:noiDung IS NULL OR LOWER(HH_DX_KHLCNT_THOP_HDR.NOI_DUNG) LIKE LOWER(CONCAT(CONCAT('%', :noiDung),'%'))) " +
					"  AND (:trangThai IS NULL OR HH_DX_KHLCNT_THOP_HDR.TRANG_THAI = :trangThai) ",
			nativeQuery = true)
	Page<HhDxKhLcntThopHdr> select(String namKh, String loaiVthh, String cloaiVthh, String tuNgayThop, String denNgayThop,String noiDung,String trangThai, Pageable pageable);

	@Query(
			value = "SELECT *  " +
					"FROM HH_DX_KHLCNT_THOP_HDR " +
					" WHERE (:namKh IS NULL OR HH_DX_KHLCNT_THOP_HDR.NAM_KHOACH = TO_NUMBER(:namKh)) " +
					"  AND (:loaiVthh IS NULL OR HH_DX_KHLCNT_THOP_HDR.LOAI_VTHH = :loaiVthh) " +
					"  AND (:cloaiVthh IS NULL OR HH_DX_KHLCNT_THOP_HDR.CLOAI_VTHH = :cloaiVthh) " +
					"  AND (:tuNgayThop IS NULL OR HH_DX_KHLCNT_THOP_HDR.NGAY_THOP >= TO_DATE(:tuNgayThop, 'yyyy-MM-dd'))  " +
					"  AND (:denNgayThop IS NULL OR HH_DX_KHLCNT_THOP_HDR.NGAY_THOP <= TO_DATE(:denNgayThop, 'yyyy-MM-dd'))  " +
					"  AND (:trangThai IS NULL OR HH_DX_KHLCNT_THOP_HDR.TRANG_THAI = :trangThai) ",
			nativeQuery = true)
	List<HhDxKhLcntThopHdr> selectAll(String namKh, String loaiVthh,String cloaiVthh, String tuNgayThop, String denNgayThop, String trangThai);

	@Transactional
	@Modifying
	void deleteAllByIdIn(List<Long> ids);

	List<HhDxKhLcntThopHdr> findAllByIdIn(List<Long> ids);
}
