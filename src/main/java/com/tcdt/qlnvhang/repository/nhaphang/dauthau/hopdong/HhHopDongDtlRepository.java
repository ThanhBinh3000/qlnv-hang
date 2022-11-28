package com.tcdt.qlnvhang.repository.nhaphang.dauthau.hopdong;

import com.tcdt.qlnvhang.entities.nhaphang.dauthau.hopdong.HhHopDongDtl;
import com.tcdt.qlnvhang.repository.BaseRepository;

import java.util.List;

public interface HhHopDongDtlRepository extends BaseRepository<HhHopDongDtl, Long> {

    void deleteAllByIdHdr(Long idHdr);

    List<HhHopDongDtl> findAllByIdHdr(Long idHdr);

}
