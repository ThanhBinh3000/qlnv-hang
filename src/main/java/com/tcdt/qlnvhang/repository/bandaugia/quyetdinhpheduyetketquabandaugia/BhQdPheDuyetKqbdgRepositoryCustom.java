package com.tcdt.qlnvhang.repository.bandaugia.quyetdinhpheduyetketquabandaugia;

import com.tcdt.qlnvhang.request.bandaugia.quyetdinhpheduyetketquabandaugia.BhQdPheDuyetKqbdgSearchReq;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BhQdPheDuyetKqbdgRepositoryCustom {
	List<Object[]> search(BhQdPheDuyetKqbdgSearchReq req, Pageable pageable);

	int count(BhQdPheDuyetKqbdgSearchReq req);
}
