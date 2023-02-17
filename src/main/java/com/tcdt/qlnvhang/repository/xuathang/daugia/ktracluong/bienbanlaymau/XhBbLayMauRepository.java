package com.tcdt.qlnvhang.repository.xuathang.daugia.ktracluong.bienbanlaymau;

import com.tcdt.qlnvhang.entities.xuathang.daugia.ktracluong.bienbanlaymau.XhBbLayMau;
import com.tcdt.qlnvhang.entities.xuathang.daugia.nhiemvuxuat.XhQdGiaoNvXh;
import com.tcdt.qlnvhang.repository.BaseRepository;
import com.tcdt.qlnvhang.request.bandaugia.bienbanlaymau.XhBbLayMauRequest;
import com.tcdt.qlnvhang.request.xuathang.quyetdinhgiaonhiemvuxuat.XhQdGiaoNvuXuatReq;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface XhBbLayMauRepository extends BaseRepository<XhBbLayMau, Long> {

	@Query("SELECT c FROM XhBbLayMau c " +
			" WHERE 1 = 1 " +
			"AND (:#{#param.maDvi} IS NULL OR c.maDvi = :#{#param.maDvi}) " +
			"AND (:#{#param.nam} IS NULL OR c.nam = :#{#param.nam}) " +
			"AND (:#{#param.loaiVthh } IS NULL OR LOWER(c.loaiVthh) LIKE CONCAT(:#{#param.loaiVthh},'%')) " +
			"AND (:#{#param.trangThai} IS NULL OR c.trangThai = :#{#param.trangThai}) "
	)
	Page<XhBbLayMau> searchPage(@Param("param") XhBbLayMauRequest param, Pageable pageable);
	void deleteAllByIdIn(Collection<Long> ids);

	List<XhBbLayMau> findByIdIn(List<Long> ids);
}
