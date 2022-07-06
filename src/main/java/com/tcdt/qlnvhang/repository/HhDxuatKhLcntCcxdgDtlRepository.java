package com.tcdt.qlnvhang.repository;

import com.tcdt.qlnvhang.table.HhDxuatKhLcntCcxdgDtl;

import java.util.List;

public interface HhDxuatKhLcntCcxdgDtlRepository extends BaseRepository<HhDxuatKhLcntCcxdgDtl, Long> {

    List<HhDxuatKhLcntCcxdgDtl> findByIdDxKhlcnt (Long idDxKhlcnt);

    void deleteAllByIdDxKhlcnt(Long idDxKhlcnt);

}
