package com.tcdt.qlnvhang.repository.xuathang.daugia.nhiemvuxuat;

import com.tcdt.qlnvhang.entities.xuathang.daugia.nhiemvuxuat.XhQdGiaoNvXhDdiem;
import com.tcdt.qlnvhang.repository.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface XhQdGiaoNvXhDdiemRepository extends BaseRepository<XhQdGiaoNvXhDdiem, Long> {

    void deleteAllByIdDtl(Long idDtl);

    List<XhQdGiaoNvXhDdiem> findAllByIdDtl(Long idDtl);

    List<XhQdGiaoNvXhDdiem> findByIdDtlIn(List<Long> listId);
}
