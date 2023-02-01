package com.tcdt.qlnvhang.repository.xuathang.daugia.nhiemvuxuat;

import com.tcdt.qlnvhang.entities.xuathang.daugia.nhiemvuxuat.XhQdGiaoNvXhDdiem;
import com.tcdt.qlnvhang.repository.BaseRepository;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhQdGiaoNvNhDdiem;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;

@Repository
public interface XhQdGiaoNvXhDdiemRepository extends BaseRepository<XhQdGiaoNvXhDdiem, Long> {
    List<XhQdGiaoNvXhDdiem> findAllByIdDtlIn(List<Long> ids);
}
