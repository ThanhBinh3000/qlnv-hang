package com.tcdt.qlnvhang.repository.dauthauvattu;

import com.tcdt.qlnvhang.entities.dauthauvattu.DTGoiThauDiaDiemNhapVT;
import com.tcdt.qlnvhang.repository.BaseRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface DTGoiThauDiaDiemNhapVTRepository extends BaseRepository<DTGoiThauDiaDiemNhapVT, Long> {
	List<DTGoiThauDiaDiemNhapVT> findByDtvtGoiThauId(Long dtvtGoiThauId);
	List<DTGoiThauDiaDiemNhapVT> findByDtvtGoiThauId(Long dtvtGoiThauId, Pageable pageable);
	Long countByDtvtGoiThauId(Long dtvtGoiThauId);
	void deleteByDtvtGoiThauIdIn(Set<Long> dtvtGoiThauIds);
	void deleteByDtvtGoiThauIdAndIdNotIn(Long dtvtGoiThauId, Set<Long> ids);
}
