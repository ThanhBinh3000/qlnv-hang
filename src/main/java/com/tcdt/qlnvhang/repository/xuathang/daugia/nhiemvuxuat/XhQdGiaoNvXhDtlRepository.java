package com.tcdt.qlnvhang.repository.xuathang.daugia.nhiemvuxuat;

import com.tcdt.qlnvhang.entities.xuathang.daugia.nhiemvuxuat.XhQdGiaoNvXhDtl;
import com.tcdt.qlnvhang.repository.BaseRepository;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhQdGiaoNvNhangDtl;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface XhQdGiaoNvXhDtlRepository extends BaseRepository<XhQdGiaoNvXhDtl, Long> {
    List<XhQdGiaoNvXhDtl> findAllByIdQdHdr(Long ids);
    List<XhQdGiaoNvXhDtl> findAllByIdQdHdrIn(List<Long> ids);

}
