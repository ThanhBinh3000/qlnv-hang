package com.tcdt.qlnvhang.repository.bandaugia.quyetdinhpheduyetkehoachbandaugia;

import com.tcdt.qlnvhang.entities.bandaugia.quyetdinhpheduyetkehoachbandaugia.BhQdPheDuyetKhbdg;
import com.tcdt.qlnvhang.repository.BaseRepository;

import java.util.Collection;
import java.util.List;

public interface BhQdPheDuyetKhbdgRepository extends BaseRepository<BhQdPheDuyetKhbdg, Long>, BhQdPheDuyetKhbdgRepositoryCustom {
	void deleteAllByIdIn(List<Long> ids);

	List<BhQdPheDuyetKhbdg> findByIdIn(Collection<Long> ids);
}
