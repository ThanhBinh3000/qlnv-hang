package com.tcdt.qlnvhang.repository.xuathang.daugia.ktracluong.bienbanlaymau;

import com.tcdt.qlnvhang.entities.xuathang.daugia.ktracluong.bienbanlaymau.XhBbLayMauCt;
import com.tcdt.qlnvhang.repository.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface XhBbLayMauCtRepository extends BaseRepository<XhBbLayMauCt, Long> {
	void deleteAllByIdIn(Collection<Long> ids);

//	List<XhBbLayMauCt> findByXhBbLayMauIdIn(Collection<Long> ids);


}
