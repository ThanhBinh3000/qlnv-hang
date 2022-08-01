package com.tcdt.qlnvhang.repository.bandaugia.tonghopdexuatkhbdg;


import com.tcdt.qlnvhang.entities.bandaugia.tonghopdexuatkhbdg.BhTongHopDeXuatKhbdg;
import com.tcdt.qlnvhang.repository.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface BhTongHopDeXuatKhbdgRepository extends BaseRepository<BhTongHopDeXuatKhbdg, Long>, BhTongHopDeXuatKhbdgRepositoryCustom {
	void deleteAllByIdIn(Collection<Long> ids);
}
