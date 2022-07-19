package com.tcdt.qlnvhang.repository.kehoachbanhangdaugia;

import com.tcdt.qlnvhang.entities.kehoachbanhangdaugia.BhDgKehoach;
import com.tcdt.qlnvhang.repository.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface BhDgKehoachRepository extends BaseRepository<BhDgKehoach, Long> {
	List<BhDgKehoach> findByIdIn(Collection<Long> ids);

	void deleteAllByIdIn(List<Long> ids);
}
