package com.tcdt.qlnvhang.repository;

import java.util.Optional;

import com.tcdt.qlnvhang.table.HhDxKhLcntThopHdr;
import com.tcdt.qlnvhang.table.HhQdGiaoNvuNhapxuatHdr;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

public interface HhQdGiaoNvuNhapxuatRepository extends BaseRepository<HhQdGiaoNvuNhapxuatHdr, Long> {

	Optional<HhQdGiaoNvuNhapxuatHdr> findBySoHd(String soHd);

	Optional<HhQdGiaoNvuNhapxuatHdr> findBySoQd(String soQd);

	@Query(
			value = "SELECT * \n" +
					"FROM NH_QD_GIAO_NVU_NHAPXUAT NX " +
					" WHERE (:namNhap IS NULL OR NX.NAM_NHAP = TO_NUMBER(:namNhap)) " +
					"  AND (:veViec IS NULL OR LOWER(NX.VE_VIEC) LIKE LOWER(CONCAT(CONCAT('%', :veViec),'%'))) " +
					"  AND (:soQd IS NULL OR LOWER(NX.SO_QD) LIKE LOWER(CONCAT(CONCAT('%', :soQd),'%'))) " +
					"  AND (:tuNgayQD IS NULL OR NX.NGAY_QDINH >= TO_DATE(:tuNgayQD, 'yyyy-MM-dd')) " +
					"  AND (:denNgayQD IS NULL OR NX.NGAY_QDINH <= TO_DATE(:denNgayQD, 'yyyy-MM-dd'))",
			nativeQuery = true)
	Page<HhQdGiaoNvuNhapxuatHdr> select(String namNhap, String veViec, String soQd, String tuNgayQD, String denNgayQD, Pageable pageable);

}
