package com.tcdt.qlnvhang.repository.bandaugia.quyetdinhpheduyetkehoachbandaugia;

import com.tcdt.qlnvhang.entities.bandaugia.quyetdinhpheduyetkehoachbandaugia.BhQdPheDuyetKhbdgCt;
import com.tcdt.qlnvhang.repository.BaseRepository;

import java.util.Collection;
import java.util.List;

public interface BhQdPheDuyetKhbdgCtRepository extends BaseRepository<BhQdPheDuyetKhbdgCt, Long> {
	void deleteAllByIdIn(Collection<Long> ids);

	List<BhQdPheDuyetKhbdgCt> findByIdIn(Collection<Long> ids);
}
