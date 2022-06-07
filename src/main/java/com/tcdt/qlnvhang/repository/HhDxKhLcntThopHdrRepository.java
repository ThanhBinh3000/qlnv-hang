package com.tcdt.qlnvhang.repository;

import com.tcdt.qlnvhang.table.HhDxuatKhLcntHdr;
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
					"  AND (:quyetDinh IS NULL OR HH_DX_KHLCNT_THOP_HDR.QD_CAN_CU = :quyetDinh) " +
					"  AND (:tuNgayTao IS NULL OR HH_DX_KHLCNT_THOP_HDR.NGAY_TAO >= TO_DATE(:tuNgayTao, 'yyyy-MM-dd')) " +
					"  AND (:denNgayTao IS NULL OR HH_DX_KHLCNT_THOP_HDR.NGAY_TAO <= TO_DATE(:denNgayTao, 'yyyy-MM-dd')) " +
					"  AND (:trangThai IS NULL OR HH_DX_KHLCNT_THOP_HDR.TRANG_THAI = :trangThai) ",
			nativeQuery = true)
	Page<HhDxKhLcntThopHdr> select(String namKh, String loaiVthh, String tuNgayTao, String denNgayTao,String quyetDinh,String trangThai, Pageable pageable);

	@Query(
			value = "SELECT *  " +
					"FROM HH_DX_KHLCNT_THOP_HDR " +
					" WHERE (:namKh IS NULL OR HH_DX_KHLCNT_THOP_HDR.NAM_KHOACH = TO_NUMBER(:namKh)) " +
					"  AND (:loaiVthh IS NULL OR HH_DX_KHLCNT_THOP_HDR.LOAI_VTHH = :loaiVthh) " +
					"  AND (:quyetDinh IS NULL OR HH_DX_KHLCNT_THOP_HDR.QD_CAN_CU = :quyetDinh) " +
					"  AND (:tuNgayTao IS NULL OR HH_DX_KHLCNT_THOP_HDR.NGAY_TAO >= TO_DATE(:tuNgayTao, 'yyyy-MM-dd'))  " +
					"  AND (:denNgayTao IS NULL OR HH_DX_KHLCNT_THOP_HDR.NGAY_TAO <= TO_DATE(:denNgayTao, 'yyyy-MM-dd'))  " +
					"  AND (:trangThai IS NULL OR HH_DX_KHLCNT_THOP_HDR.TRANG_THAI = :trangThai) ",
			nativeQuery = true)
	List<HhDxKhLcntThopHdr> selectAll(String namKh, String loaiVthh, String tuNgayTao, String denNgayTao, String quyetDinh, String trangThai);
}
