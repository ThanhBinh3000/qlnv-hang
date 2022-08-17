package com.tcdt.qlnvhang.repository.bandaugia.tonghopdexuatkhbdg;


import com.tcdt.qlnvhang.entities.bandaugia.tonghopdexuatkhbdg.BhQdPheDuyetKhBdgThongTinTaiSan;
import com.tcdt.qlnvhang.repository.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface BhQdPheDuyetKhBdgThongTinTaiSanRepository extends BaseRepository<BhQdPheDuyetKhBdgThongTinTaiSan, Long> {
	void deleteAllByIdIn(Collection<Long> ids);
}
