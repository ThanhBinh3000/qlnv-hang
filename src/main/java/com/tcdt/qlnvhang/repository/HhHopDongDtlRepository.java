package com.tcdt.qlnvhang.repository;

import com.tcdt.qlnvhang.table.HhHopDongDtl;

import java.util.List;

public interface HhHopDongDtlRepository extends BaseRepository<HhHopDongDtl, Long> {

    void deleteAllByIdHdr(Long idHdr);

    List<HhHopDongDtl> findAllByIdHdr(Long idHdr);

}
