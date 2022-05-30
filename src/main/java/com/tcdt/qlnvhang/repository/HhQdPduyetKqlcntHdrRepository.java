package com.tcdt.qlnvhang.repository;

import java.util.List;
import java.util.Optional;

import com.tcdt.qlnvhang.table.HhQdKhlcntHdr;
import com.tcdt.qlnvhang.table.HhQdPduyetKqlcntHdr;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

public interface HhQdPduyetKqlcntHdrRepository extends BaseRepository<HhQdPduyetKqlcntHdr, Long> {

	Optional<HhQdPduyetKqlcntHdr> findBySoQd(String canCu);

	@Query(value = " SELECT * FROM HH_QD_PDUYET_KQLCNT_HDR QDPD "+
			" WHERE (:namKh IS NULL OR QDPD.NAM_KHOACH = TO_NUMBER(:namKh)) "+
			" AND (:loaiVthh IS NULL OR QDPD.LOAI_VTHH = :loaiVthh) "+
			" AND (:soQd IS NULL OR QDPD.SO_QD = :soQd) "+
			" AND (:tuNgayQd IS NULL OR QDPD.NGAY_QD >= TO_DATE(:tuNgayQd, 'yyyy-MM-dd')) "+
			" AND (:denNgayQd IS NULL OR QDPD.NGAY_QD <= TO_DATE(:denNgayQd, 'yyyy-MM-dd')) "+
			" AND (:trangThai IS NULL OR QDPD.TRANG_THAI = :trangThai) ",
			nativeQuery = true)
	Page<HhQdPduyetKqlcntHdr> selectPage(String namKh, String loaiVthh, String soQd, String tuNgayQd, String denNgayQd, String trangThai, Pageable pageable);

	@Query(value = "SELECT * FROM HH_QD_PDUYET_KQLCNT_HDR QDPD " +
			" WHERE (:namKh IS NULL OR QDPD.NAM_KHOACH = TO_NUMBER(:namKh)) "+
			" AND (:loaiVthh IS NULL OR QDPD.LOAI_VTHH = :loaiVthh) "+
			" AND (:soQd IS NULL OR QDPD.SO_QD = :soQd) "+
			" AND (:tuNgayQd IS NULL OR QDPD.NGAY_QD >= TO_DATE(:tuNgayQd, 'yyyy-MM-dd')) "+
			" AND (:denNgayQd IS NULL OR QDPD.NGAY_QD <= TO_DATE(:denNgayQd, 'yyyy-MM-dd')) " +
			" AND (:trangThai IS NULL OR QDPD.TRANG_THAI = :trangThai) ",
			nativeQuery = true)
	List<HhQdPduyetKqlcntHdr> selectAll(String namKh, String loaiVthh, String soQd, String tuNgayQd, String denNgayQd, String trangThai);

}
