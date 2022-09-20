package com.tcdt.qlnvhang.repository.bandaugia.tonghopdexuatkhbdg;


import com.tcdt.qlnvhang.entities.bandaugia.tonghopdexuatkhbdg.BhTongHopDeXuatCt;
import com.tcdt.qlnvhang.repository.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface BhTongHopDeXuatCtRepository extends BaseRepository<BhTongHopDeXuatCt, Long> {
	void deleteAllByIdIn(Collection<Long> ids);

	void deleteAllByBhTongHopDeXuatId(Long id);
	void deleteAllByBhTongHopDeXuatIdIn(List<Long> ids);

	List<BhTongHopDeXuatCt> findByBhTongHopDeXuatId(Long id);

	List<BhTongHopDeXuatCt> findByBhTongHopDeXuatIdIn(List<Long> ids);
}
