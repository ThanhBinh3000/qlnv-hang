package com.tcdt.qlnvhang.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import com.tcdt.qlnvhang.table.HhHopDongHdr;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

public interface HhHopDongRepository extends BaseRepository<HhHopDongHdr, Long> {

	Optional<HhHopDongHdr> findBySoHd(String soHd);

	@Query(
			value = "SELECT * " +
					"FROM HH_HOP_DONG_HDR  HDR" +
					" WHERE (:loaiVthh IS NULL OR HDR.LOAI_VTHH = :loaiVthh) " +
					"  AND (:soHd IS NULL OR HDR.SO_HD = :soHd) " +
					"  AND (:tenHd IS NULL OR HDR.TEN_HD = :tenHd) " +
					"  AND (:nhaCcap IS NULL OR HDR.SO_HD = :nhaCcap) " +
					"  AND (:tuNgayKy IS NULL OR HDR.NGAY_KY >= TO_DATE(:tuNgayKy, 'yyyy-MM-dd')) " +
					"  AND (:denNgayKy IS NULL OR HDR.NGAY_KY <= TO_DATE(:denNgayKy, 'yyyy-MM-dd')) " +
					"  AND (:trangThai IS NULL OR HDR.TRANG_THAI = :trangThai) ",
			nativeQuery = true)
	Page<HhHopDongHdr> select(String loaiVthh, String soHd, String tenHd, String nhaCcap,String tuNgayKy,String denNgayKy, String trangThai, Pageable pageable);

	List<HhHopDongHdr> findByIdIn(Collection<Long> ids);
}
