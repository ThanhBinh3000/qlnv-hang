package com.tcdt.qlnvhang.repository.bandaugia.bienbanlaymau;

import com.tcdt.qlnvhang.entities.bandaugia.bienbanlaymau.XhBbLayMau;
import com.tcdt.qlnvhang.repository.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface XhBbLayMauRepository extends BaseRepository<XhBbLayMau, Long>, XhBbLayMauRepositoryCustom {
	void deleteAllByIdIn(Collection<Long> ids);

	List<XhBbLayMau> findByIdIn(List<Long> ids);
}
