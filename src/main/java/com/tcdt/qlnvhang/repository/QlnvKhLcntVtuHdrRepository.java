package com.tcdt.qlnvhang.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import com.tcdt.qlnvhang.table.QlnvKhLcntVtuHdr;

public interface QlnvKhLcntVtuHdrRepository extends BaseRepository<QlnvKhLcntVtuHdr, Long> {

	@Deprecated
	@Query(value = "SELECT * FROM QLNV_KH_LCNT_VTU_HDR t WHERE (:maVtu is null or t.MA_VTU = :maVtu) "
			+ "AND (:nguoiTao is null or t.NGUOI_TAO = :nguoiTao) AND (:trangThai is null or t.TRANG_THAI = :trangThai) "
			+ "AND (:tuNgayLap is null or t.NGAY_TAO > TO_DATE(:tuNgayLap, 'DD/MM/YYYY')) "
			+ "AND (:denNgayLap is null or t.NGAY_TAO <= TO_DATE(:denNgayLap, 'DD/MM/YYYY'))", countQuery = "SELECT count(1) FROM QLNV_KH_LCNT_VTU_HDR t "
					+ "WHERE (:maVtu is null or t.MA_VTU = :maVtu) "
					+ "AND (:nguoiTao is null or t.NGUOI_TAO = :nguoiTao) AND (:trangThai is null or t.TRANG_THAI = :trangThai)"
					+ "AND (:tuNgayLap is null or t.NGAY_TAO > TO_DATE(:tuNgayLap, 'DD/MM/YYYY')) "
					+ "AND (:denNgayLap is null or t.NGAY_TAO <= TO_DATE(:denNgayLap, 'DD/MM/YYYY'))", nativeQuery = true)
	Page<QlnvKhLcntVtuHdr> selectParams(String maVtu, String trangThai, String nguoiTao, String tuNgayLap,
			String denNgayLap, Pageable pageable);

}
