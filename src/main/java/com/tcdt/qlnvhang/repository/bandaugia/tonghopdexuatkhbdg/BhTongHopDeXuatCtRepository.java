package com.tcdt.qlnvhang.repository.bandaugia.tonghopdexuatkhbdg;


import com.tcdt.qlnvhang.entities.bandaugia.tonghopdexuatkhbdg.BhTongHopDeXuatCt;
import com.tcdt.qlnvhang.repository.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface BhTongHopDeXuatCtRepository extends BaseRepository<BhTongHopDeXuatCt, Long> {
	void deleteAllByIdIn(Collection<Long> ids);
}
