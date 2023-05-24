package com.tcdt.qlnvhang.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kiemtracl.bbnghiemthubqld.HhBbNghiemthuKlstHdr;
import com.tcdt.qlnvhang.response.DieuChuyenNoiBo.BienBanNghiemThuLanDauDTO;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface HhBbNghiemthuKlstRepository extends BaseRepository<HhBbNghiemthuKlstHdr, Long> {

	Optional<HhBbNghiemthuKlstHdr> findFirstBySoBbNtBq(String soBbNtBq);

	Optional<HhBbNghiemthuKlstHdr> findFirstByNamAndMaDvi(Integer nam, String maDvi);

	List<HhBbNghiemthuKlstHdr> findByIdQdGiaoNvNhAndMaDvi(Long idQdGiaoNvNh, String maDvi);

	@Query("SELECT new com.tcdt.qlnvhang.response.DieuChuyenNoiBo.BienBanNghiemThuLanDauDTO(" +
			"hdr.id, hdr.soBbNtBq ) " +
			"FROM HhBbNghiemthuKlstHdr hdr\n" +
			"WHERE hdr.maDiemKho  = ?1 AND hdr.maNganKho = ?2 AND hdr.maLoKho = ?3 AND hdr.maNhaKho = ?4 AND hdr.loaiVthh = ?5 AND hdr.cloaiVthh = ?6 AND hdr.maDvi = ?7 group by hdr.id,hdr.soBbNtBq")
	List<BienBanNghiemThuLanDauDTO> findByMaDiemKhoAndMaNganKhoAndMaDvi(String maDiemKho,String maNganKho, String maLoKho, String maNhaKho, String loaiVthh, String cLoaiVthh, String maDvi);

	@Transactional
	@Modifying
	void deleteByIdIn(Collection<Long> ids);
}
