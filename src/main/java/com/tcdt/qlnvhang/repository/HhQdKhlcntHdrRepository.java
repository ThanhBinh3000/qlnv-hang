package com.tcdt.qlnvhang.repository;

import java.util.List;
import java.util.Optional;

import com.tcdt.qlnvhang.table.HhDxKhLcntThopHdr;
import com.tcdt.qlnvhang.table.HhQdKhlcntHdr;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

public interface HhQdKhlcntHdrRepository extends BaseRepository<HhQdKhlcntHdr, Long> {

	Optional<HhQdKhlcntHdr> findByIdPaHdr(Long idPaHdr);

	Optional<HhQdKhlcntHdr> findBySoQd(String soQd);

	@Query(value = " SELECT * FROM HH_QD_KHLCNT_HDR QDKHLCNT "+
			" WHERE (:namKh IS NULL OR QDKHLCNT.NAM_KHOACH = TO_NUMBER(:namKh)) "+
			" AND (:loaiVthh IS NULL OR QDKHLCNT.LOAI_VTHH = :loaiVthh) "+
			" AND (:soQd IS NULL OR LOWER(QDKHLCNT.SO_QD) LIKE LOWER(CONCAT(CONCAT('%', :soQd),'%'))) "+
			" AND (:tuNgayQd IS NULL OR QDKHLCNT.NGAY_QD >= TO_DATE(:tuNgayQd, 'yyyy-MM-dd')) "+
			" AND (:denNgayQd IS NULL OR QDKHLCNT.NGAY_QD <= TO_DATE(:denNgayQd, 'yyyy-MM-dd')) "+
			" AND (:trangThai IS NULL OR QDKHLCNT.TRANG_THAI = :trangThai) ",
			nativeQuery = true)
	Page<HhQdKhlcntHdr> selectPage(String namKh, String loaiVthh, String soQd, String tuNgayQd, String denNgayQd,String trangThai, Pageable pageable);

	@Query(value = "SELECT * FROM HH_QD_KHLCNT_HDR QDKHLCNT " +
			" WHERE (:namKh IS NULL OR QDKHLCNT.NAM_KHOACH = TO_NUMBER(:namKh)) "+
			" AND (:loaiVthh IS NULL OR QDKHLCNT.LOAI_VTHH = :loaiVthh) "+
			" AND (:soQd IS NULL OR QDKHLCNT.SO_QD = :soQd) "+
			" AND (:tuNgayQd IS NULL OR QDKHLCNT.NGAY_QD >= TO_DATE(:tuNgayQd, 'yyyy-MM-dd')) "+
			" AND (:denNgayQd IS NULL OR QDKHLCNT.NGAY_QD <= TO_DATE(:denNgayQd, 'yyyy-MM-dd')) " +
			" AND (:trangThai IS NULL OR QDKHLCNT.TRANG_THAI = :trangThai) ",
			nativeQuery = true)
	List<HhQdKhlcntHdr> selectAll(String namKh, String loaiVthh, String soQd, String tuNgayQd, String denNgayQd,String trangThai);

}
