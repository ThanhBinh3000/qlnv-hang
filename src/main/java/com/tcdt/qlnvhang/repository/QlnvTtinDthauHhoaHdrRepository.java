package com.tcdt.qlnvhang.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import com.tcdt.qlnvhang.table.QlnvTtinDthauHhoaHdr;

public interface QlnvTtinDthauHhoaHdrRepository extends CrudRepository<QlnvTtinDthauHhoaHdr, Long> {

	final String qr = "SELECT * FROM QLNV_TTIN_DTHAU_HHOA_HDR WHERE (:soQdKh is null or lower(SO_QD_KH) like lower(concat(concat('%', :soQdKh),'%'))) AND (:maHhoa is null or lower(MA_HHOA) like lower(concat(concat('%', :maHhoa),'%'))) ";
	final String qrCount = "SELECT count(1) FROM QLNV_TTIN_DTHAU_HHOA_HDR WHERE (:soQdKh is null or lower(SO_QD_KH) like lower(concat(concat('%', :soQdKh),'%'))) AND (:maHhoa is null or lower(MA_HHOA) like lower(concat(concat('%', :maHhoa),'%'))) ";

	@Query(value = qr, countQuery = qrCount, nativeQuery = true)
	Page<QlnvTtinDthauHhoaHdr> selectParams(@Param("soQdKh") String soQdKh, @Param("maHhoa") String maHhoa,
			Pageable pageable);
}
