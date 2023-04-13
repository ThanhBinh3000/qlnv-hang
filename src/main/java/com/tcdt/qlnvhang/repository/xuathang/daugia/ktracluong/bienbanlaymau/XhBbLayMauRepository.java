package com.tcdt.qlnvhang.repository.xuathang.daugia.ktracluong.bienbanlaymau;

import com.tcdt.qlnvhang.entities.xuathang.daugia.ktracluong.bienbanlaymau.XhBbLayMau;
import com.tcdt.qlnvhang.repository.BaseRepository;
import com.tcdt.qlnvhang.request.bandaugia.bienbanlaymau.XhBbLayMauRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface XhBbLayMauRepository extends BaseRepository<XhBbLayMau, Long> {

	@Query("SELECT DISTINCT LM from XhBbLayMau LM " +
			"LEFT JOIN XhQdGiaoNvXh QD ON LM.idQd = QD.id WHERE 1 = 1 " +
			"AND (:#{#param.nam} IS NULL OR LM.nam = :#{#param.nam}) " +
			"AND (:#{#param.soBienBan} IS NULL OR LOWER(LM.soBienBan) LIKE LOWER(CONCAT(CONCAT('%',:#{#param.soBienBan}),'%' ) ) )" +
			"AND (:#{#param.soQd} IS NULL OR LOWER(LM.soQd) LIKE LOWER(CONCAT(CONCAT('%',:#{#param.soQd}),'%' ) ) )" +
			"AND (:#{#param.dviKnghiem} IS NULL OR LOWER(LM.dviKnghiem) LIKE LOWER(CONCAT(CONCAT('%',:#{#param.dviKnghiem}),'%'))) " +
			"AND (:#{#param.ngayLayMauTu} IS NULL OR LM.ngayLayMau >= :#{#param.ngayLayMauTu}) " +
			"AND (:#{#param.ngayLayMauDen} IS NULL OR LM.ngayLayMau <= :#{#param.ngayLayMauDen}) " +
			"AND (:#{#param.loaiVthh} IS NULL OR LM.loaiVthh LIKE CONCAT(:#{#param.loaiVthh},'%')) " +
			"AND (:#{#param.trangThai} IS NULL OR LM.trangThai = :#{#param.trangThai}) " +
			"AND (:#{#param.maDviCuc} IS NULL OR QD.maDvi = :#{#param.maDviCuc}) " +
			"AND (:#{#param.maDvi} IS NULL OR LM.maDvi = :#{#param.maDvi})")
	Page<XhBbLayMau> searchPage(@Param("param") XhBbLayMauRequest param, Pageable pageable);

	Optional<XhBbLayMau> findBySoBienBan(String soBienBan);

	List<XhBbLayMau> findByIdIn(List<Long> idList);

	List<XhBbLayMau> findAllByIdQd (Long idQd);



}
