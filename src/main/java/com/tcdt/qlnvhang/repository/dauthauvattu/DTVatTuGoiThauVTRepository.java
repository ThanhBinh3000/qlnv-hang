package com.tcdt.qlnvhang.repository.dauthauvattu;

import com.tcdt.qlnvhang.entities.dauthauvattu.DTVatTuGoiThauVT;
import com.tcdt.qlnvhang.repository.BaseRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface DTVatTuGoiThauVTRepository extends BaseRepository<DTVatTuGoiThauVT, Long> {
	List<DTVatTuGoiThauVT> findByTtdtVtId(Long ttdtVtId);
	List<DTVatTuGoiThauVT> findByTtdtVtId(Long ttdtVtId, Pageable pageable);
	Long countByTtdtVtId(Long ttdtVtId);
	void deleteByTtdtVtIdAndIdNotIn(Long ttdtVtId, Set<Long> ids);
	void deleteByTtdtVtIdIn(Set<Long> ttdtVtIds);
}
