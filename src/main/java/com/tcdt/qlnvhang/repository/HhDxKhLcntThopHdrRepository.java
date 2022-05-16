package com.tcdt.qlnvhang.repository;

import com.tcdt.qlnvhang.table.HhDxuatKhLcntHdr;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.tcdt.qlnvhang.table.HhDxKhLcntThopHdr;

public interface HhDxKhLcntThopHdrRepository extends BaseRepository<HhDxKhLcntThopHdr, Long> {

	@Transactional()
	@Modifying
	@Query(value = "UPDATE HH_DX_KHLCNT_THOP_HDR SET PHUONG_AN=:trangThai WHERE ID = :idThHdr", nativeQuery = true)
	void updateTongHop(Long idThHdr, String trangThai);

	@Query(
			value = "SELECT * \n" +
					"FROM HH_DX_KHLCNT_THOP_HDR THOP \n" +
					"WHERE (:namKh IS NULL OR THOP.NAM_KHOACH = TO_NUMBER(:namKh))\n" +
					"  AND (:loatVthh IS NULL OR THOP.LOAI_VTHH = :loatVthh)\n" +
					"  AND (:quyetDinh IS NULL OR THOP.QD_CAN_CU = :quyetDinh)\n" +
					"  AND (:tuNgayTao IS NULL OR THOP.NGAY_TAO >= TO_DATE(:tuNgayTao, 'yyyy-MM-dd'))\n" +
					"  AND (:denNgayTao IS NULL OR THOP.NGAY_TAO <= TO_DATE(:denNgayTao, 'yyyy-MM-dd'))",
			nativeQuery = true)
	Page<HhDxKhLcntThopHdr> select(String namKh, String loatVthh, String tuNgayTao, String denNgayTao,String quyetDinh, Pageable pageable);
}
