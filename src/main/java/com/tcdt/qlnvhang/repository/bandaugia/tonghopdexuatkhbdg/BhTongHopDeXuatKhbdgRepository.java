package com.tcdt.qlnvhang.repository.bandaugia.tonghopdexuatkhbdg;


import com.tcdt.qlnvhang.entities.bandaugia.tonghopdexuatkhbdg.BhTongHopDeXuatKhbdg;
import com.tcdt.qlnvhang.repository.BaseRepository;
import com.tcdt.qlnvhang.table.HhDxKhLcntThopHdr;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface BhTongHopDeXuatKhbdgRepository extends BaseRepository<BhTongHopDeXuatKhbdg, Long>, BhTongHopDeXuatKhbdgRepositoryCustom {
	void deleteAllByIdIn(Collection<Long> ids);

	@Query(
			value = "SELECT * " +
					"FROM BH_TONG_HOP_DE_XUAT_KHBDG TH " +
					" WHERE (:namKh IS NULL OR TH.NAM_KE_HOACH = TO_NUMBER(:namKh))  " +
					"  AND (:loaiVthh IS NULL OR TH.LOAI_VTHH = :loaiVthh) " +
					"  AND (:cloaiVthh IS NULL OR TH.CLOAI_VTHH = :cloaiVthh) " +
					"  AND (:tuNgayThop IS NULL OR TH.NGAY_TONG_HOP >= TO_DATE(:tuNgayThop, 'yyyy-MM-dd')) " +
					"  AND (:denNgayThop IS NULL OR TH.NGAY_TONG_HOP <= TO_DATE(:denNgayThop, 'yyyy-MM-dd')) " +
					"  AND (:noiDung IS NULL OR LOWER(TH.NOI_DUNG_TONG_HOP) LIKE LOWER(CONCAT(CONCAT('%', :noiDung),'%'))) " +
					"  AND (:trangThai IS NULL OR TH.TRANG_THAI = :trangThai) ",
			nativeQuery = true)
	Page<BhTongHopDeXuatKhbdg> select(Integer namKh, String loaiVthh, String cloaiVthh, String tuNgayThop, String denNgayThop, String noiDung, String trangThai, Pageable pageable);

}
