package com.tcdt.qlnvhang.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.tcdt.qlnvhang.table.HhDxuatKhLcntDsgtDtl;

public interface HhDxuatKhLcntDsgtDtlRepository extends BaseRepository<HhDxuatKhLcntDsgtDtl, Long> {

//	@Query(value = "SELECT tb.* FROM HH_DXUAT_KH_LCNT_DSGT_DTL tb " + "WHERE tb.ID_HDR=?1", nativeQuery = true)
//	List<HhDxuatKhLcntDsgtDtl> findByIdHdr(Long id);

	List<HhDxuatKhLcntDsgtDtl> findByIdDxKhlcnt(Long idDxKhlcnt);

}
