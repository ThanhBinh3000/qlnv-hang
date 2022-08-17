package com.tcdt.qlnvhang.repository.bandaugia.tonghopdexuatkhbdg;


import com.tcdt.qlnvhang.entities.bandaugia.tonghopdexuatkhbdg.BhQdPheDuyetKhBdgThongTinTaiSan;
import com.tcdt.qlnvhang.repository.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface BhQdPheDuyetKhBdgThongTinTaiSanRepository extends BaseRepository<BhQdPheDuyetKhBdgThongTinTaiSan, Long> {
	void deleteAllByIdIn(Collection<Long> ids);
	List<BhQdPheDuyetKhBdgThongTinTaiSan> findByBhDgKehoachId(Long id);
	List<BhQdPheDuyetKhBdgThongTinTaiSan> findByBhDgKehoachIdIn(List<Long> id);
	void deleteAllByBhDgKehoachIdIn(List<Long> ids);
	List<BhQdPheDuyetKhBdgThongTinTaiSan> findByBbBanDauGiaIdIn(Collection<Long> bbBdgIds);
	List<BhQdPheDuyetKhBdgThongTinTaiSan> findByThongBaoBdgIdIn(Collection<Long> ids);
	List<BhQdPheDuyetKhBdgThongTinTaiSan> findByQdPheDuyetKqbdgIdIn(Collection<Long> bbBdgIds);
}
