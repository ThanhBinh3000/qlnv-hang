package com.tcdt.qlnvhang.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.tcdt.qlnvhang.entities.DiemKhoEntity;

public interface QlnvDiemKhoEntityRepository extends CrudRepository<DiemKhoEntity, Long> {

	final String qr = "SELECT k.id, d.MA_DIEM_KHO,d.TEN_DIEM_KHO,d.DIA_CHI,d.TOA_DO,k.MA_DVI,k.MA_KHO,k.TEN_KHO "
			+ "FROM QLNV_KHO_TANG k left join DM_DIEM_KHO d on k.MA_DIEM_KHO=d.MA_DIEM_KHO  where (:maDvi is null or k.MA_DVI = :maDvi )";
	@Query(value = qr, nativeQuery = true)
	List<DiemKhoEntity> selectParams(@Param("maDvi") String maDvi);
}
