package com.tcdt.qlnvhang.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import com.tcdt.qlnvhang.table.QlnvTtinDthauVtuHdr;

public interface QlnvTtinDthauVtuHdrRepository extends CrudRepository<QlnvTtinDthauVtuHdr, Long> {

	final String qr = "SELECT * FROM QLNV_TTIN_DTHAU_VTU_HDR WHERE (:soQdKh is null or lower(SO_QD_KH) like lower(concat(concat('%', :soQdKh),'%'))) AND (:maVtu is null or lower(MA_VTU) like lower(concat(concat('%', :maVtu),'%'))) ";
	final String qrCount = "SELECT count(1) FROM QLNV_TTIN_DTHAU_VTU_HDR WHERE (:soQdKh is null or lower(SO_QD_KH) like lower(concat(concat('%', :soQdKh),'%'))) AND (:maVtu is null or lower(MA_VTU) like lower(concat(concat('%', :maVtu),'%'))) ";

	@Query(value = qr, countQuery = qrCount, nativeQuery = true)
	Page<QlnvTtinDthauVtuHdr> selectParams(@Param("soQdKh") String soQdKh, @Param("maVtu") String maVtu,
			Pageable pageable);
}
