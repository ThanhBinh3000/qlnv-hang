package com.tcdt.qlnvhang.repository.nhaphang.dauthau.kehoachlcnt.dexuatkhlcnt;

import java.util.List;

import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kehoachlcnt.dexuatkhlcnt.HhDxKhlcntDsgthau;
import com.tcdt.qlnvhang.repository.BaseRepository;

import javax.transaction.Transactional;

public interface HhDxuatKhLcntDsgtDtlRepository extends BaseRepository<HhDxKhlcntDsgthau, Long> {

//	@Query(value = "SELECT tb.* FROM HH_DXUAT_KH_LCNT_DSGT_DTL tb " + "WHERE tb.ID_HDR=?1", nativeQuery = true)
//	List<HhDxuatKhLcntDsgtDtl> findByIdHdr(Long id);

	List<HhDxKhlcntDsgthau> findByIdDxKhlcnt(Long idDxKhlcnt);
	List<HhDxKhlcntDsgthau> findByIdDxKhlcntOrderByGoiThau(Long idDxKhlcnt);

	void deleteAllByIdDxKhlcnt(Long idDxKhlcnt);

	long countByIdDxKhlcnt(Long idDxKhlcnt);


	List<HhDxKhlcntDsgthau> findByIdDxKhlcntIn(List<Long> idDxKhlcnt);

	@Transactional
	void deleteAllByIdDxKhlcntIn(List<Long> idDxKhlcnt);


}
