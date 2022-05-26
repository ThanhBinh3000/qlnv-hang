package com.tcdt.qlnvhang.repository;

import com.tcdt.qlnvhang.table.HhSoKhoDtl;
import com.tcdt.qlnvhang.table.HhSoKhoHdr;
import com.tcdt.qlnvhang.util.Contains;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface HhSoKhoDtlRepository extends CrudRepository<HhSoKhoDtl, Long> {

	@Query(value = "SELECT * FROM HH_SO_KHO_DTL t"
			+ " WHERE ID_HDR = TO_NUMBER(:idHdr)"
			+ " AND (:tuNgayGhi is null or t.NGAY_GHI >= TO_DATE(:tuNgayGhi,'" + Contains.FORMAT_DATE_STR + "'))"
			+ " AND (:denNgayGhi is null or t.NGAY_GHI < TO_DATE(:denNgayGhi,'" + Contains.FORMAT_DATE_STR
			+ "') + INTERVAL '1' DAY)"
			+ " AND (:tuNgayChungTu is null or t.NGAY_CHUNG_TU >= TO_DATE(:tuNgayChungTu,'" + Contains.FORMAT_DATE_STR + "'))"
			+ " AND (:denNgayChungTu is null or t.NGAY_CHUNG_TU < TO_DATE(:denNgayChungTu,'" + Contains.FORMAT_DATE_STR
			+ "') + INTERVAL '1' DAY)"
			+ " AND (:tuNgayNhapXuat is null or t.NGAY_NHAP_XUAT >= TO_DATE(:tuNgayNhapXuat,'" + Contains.FORMAT_DATE_STR + "'))"
			+ " AND (:denNgayNhapXuat is null or t.NGAY_NHAP_XUAT < TO_DATE(:denNgayNhapXuat,'" + Contains.FORMAT_DATE_STR
			+ "') + INTERVAL '1' DAY)", nativeQuery = true)
	Page<HhSoKhoDtl> selectParams(Long idHdr, Date tuNgayGhi, Date denNgayGhi,Date tuNgayChungTu, Date denNgayChungTu,Date tuNgayNhapXuat, Date denNgayNhapXuat,
                                  Pageable pageable);
}
