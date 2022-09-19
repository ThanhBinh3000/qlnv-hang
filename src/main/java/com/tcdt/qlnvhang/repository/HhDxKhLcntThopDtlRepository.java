package com.tcdt.qlnvhang.repository;

import com.tcdt.qlnvhang.table.HhDxKhLcntThopDtl;
import org.springframework.data.jpa.repository.Modifying;

import javax.transaction.Transactional;
import java.util.List;

public interface HhDxKhLcntThopDtlRepository extends BaseRepository<HhDxKhLcntThopDtl, Long> {

    List<HhDxKhLcntThopDtl> findByIdThopHdr(Long idThopHdr);

    @Transactional
    @Modifying
    void deleteAllByIdIn(List<HhDxKhLcntThopDtl> ids);

}
