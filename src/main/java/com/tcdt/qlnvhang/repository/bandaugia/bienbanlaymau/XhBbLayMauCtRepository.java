package com.tcdt.qlnvhang.repository.bandaugia.bienbanlaymau;

import com.tcdt.qlnvhang.entities.bandaugia.bienbanlaymau.XhBbLayMauCt;
import com.tcdt.qlnvhang.repository.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface XhBbLayMauCtRepository extends BaseRepository<XhBbLayMauCt, Long> {
	void deleteAllByIdIn(Collection<Long> ids);

	List<XhBbLayMauCt> findByXhBbLayMauIdIn(Collection<Long> ids);


}
