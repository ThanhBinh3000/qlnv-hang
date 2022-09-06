package com.tcdt.qlnvhang.repository;

import com.tcdt.qlnvhang.table.HhDxuatKhLcntCcxdgDtl;

import javax.transaction.Transactional;
import java.util.List;

public interface HhDxuatKhLcntCcxdgDtlRepository extends BaseRepository<HhDxuatKhLcntCcxdgDtl, Long> {

    List<HhDxuatKhLcntCcxdgDtl> findByIdDxKhlcnt (Long idDxKhlcnt);

    void deleteAllByIdDxKhlcnt(Long idDxKhlcnt);

    @Transactional
    void deleteAllByIdDxKhlcntIn(List<Long> idDxKhlcnt);
}
