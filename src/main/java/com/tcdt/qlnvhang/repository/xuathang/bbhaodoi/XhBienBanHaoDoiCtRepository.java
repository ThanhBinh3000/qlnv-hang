package com.tcdt.qlnvhang.repository.xuathang.bbhaodoi;

import com.tcdt.qlnvhang.entities.xuathang.bbhaodoi.XhBienBanHaoDoiCt;
import com.tcdt.qlnvhang.repository.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface XhBienBanHaoDoiCtRepository extends BaseRepository<XhBienBanHaoDoiCt, Long> {
    void deleteAllByBbHaoDoiId(Long id);

    void deleteByBbHaoDoiIdIn(List<Long> id);

    List<XhBienBanHaoDoiCt> findAllByBbHaoDoiId(Long id);
}
