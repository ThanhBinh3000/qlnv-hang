package com.tcdt.qlnvhang.repository.bandaugia.kehoachbanhangdaugia;

import com.tcdt.qlnvhang.entities.bandaugia.kehoachbanhangdaugia.KeHoachBanDauGia;
import com.tcdt.qlnvhang.repository.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface KeHoachBanDauGiaRepository extends BaseRepository<KeHoachBanDauGia, Long> {
	List<KeHoachBanDauGia> findByIdIn(Collection<Long> ids);

	void deleteAllByIdIn(List<Long> ids);

	@Query(value = "SELECT * FROM BH_DG_KEHOACH KH WHERE (:namKh IS NULL OR KH.NAM_KE_HOACH = TO_NUMBER(:namKh))" +
			"AND (:soKh IS NULL OR LOWER(KH.SO_KE_HOACH) LIKE LOWER(CONCAT(CONCAT('%',:soKh),'%')))" +
			"AND (:trichYeu IS NULL OR LOWER(KH.TRICH_YEU) LIKE LOWER(CONCAT(CONCAT('%',:trichYeu),'%')))" +
			"AND (:ngayKyTu IS NULL OR KH.NGAY_KY >=  TO_DATE(:ngayKyTu,'yyyy-MM-dd'))"+
			"AND (:ngayKyDen IS NULL OR KH.NGAY_KY <= TO_DATE(:ngayKyDen,'yyyy-MM-dd'))"+
			"AND (:loaiVthh IS NULL OR KH.LOAI_VTHH = :loaiVthh) "
			, nativeQuery = true)
	Page<KeHoachBanDauGia> selectPage(Integer namKh, String soKh, String trichYeu, String ngayKyTu, String ngayKyDen, String loaiVthh, Pageable pageable);

}
