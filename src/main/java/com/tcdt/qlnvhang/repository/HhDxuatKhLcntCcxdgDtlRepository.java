package com.tcdt.qlnvhang.repository;

import com.tcdt.qlnvhang.table.HhDxuatKhLcntCcxdgDtl;
import com.tcdt.qlnvhang.table.HhDxuatKhLcntDsgtDtl;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface HhDxuatKhLcntCcxdgDtlRepository extends BaseRepository<HhDxuatKhLcntCcxdgDtl, Long> {

    List<HhDxuatKhLcntCcxdgDtl> findByIdDxKhlcnt (Long idDxKhlcnt);

}
