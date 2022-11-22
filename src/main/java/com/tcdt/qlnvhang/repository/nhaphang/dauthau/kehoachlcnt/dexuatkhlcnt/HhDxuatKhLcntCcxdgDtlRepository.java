package com.tcdt.qlnvhang.repository.nhaphang.dauthau.kehoachlcnt.dexuatkhlcnt;

import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kehoachlcnt.dexuatkhlcnt.HhDxuatKhLcntCcxdgDtl;
import com.tcdt.qlnvhang.repository.BaseRepository;

import javax.transaction.Transactional;
import java.util.List;

public interface HhDxuatKhLcntCcxdgDtlRepository extends BaseRepository<HhDxuatKhLcntCcxdgDtl, Long> {

    List<HhDxuatKhLcntCcxdgDtl> findByIdDxKhlcnt (Long idDxKhlcnt);

    void deleteAllByIdDxKhlcnt(Long idDxKhlcnt);

    @Transactional
    void deleteAllByIdDxKhlcntIn(List<Long> idDxKhlcnt);
}
