package com.tcdt.qlnvhang.repository;

import com.tcdt.qlnvhang.table.HhQdPduyetKqlcntDtl;

import java.util.List;

public interface HhQdPduyetKqlcntDtlRepository extends BaseRepository<HhQdPduyetKqlcntDtl, Long> {

    List<HhQdPduyetKqlcntDtl> findByIdQdPdHdr(Long idQdPdHdr);

    long countByIdQdPdHdr(Long idQdPdHdr);

    void deleteAllByIdQdPdHdr(Long idQdPdHdr);

}
