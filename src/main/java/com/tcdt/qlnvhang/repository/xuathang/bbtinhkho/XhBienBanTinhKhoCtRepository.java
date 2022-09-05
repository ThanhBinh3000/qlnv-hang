package com.tcdt.qlnvhang.repository.xuathang.bbtinhkho;

import com.tcdt.qlnvhang.entities.xuathang.bbtinhkho.XhBienBanTinhKhoCt;
import com.tcdt.qlnvhang.repository.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface XhBienBanTinhKhoCtRepository extends BaseRepository<XhBienBanTinhKhoCt, Long> {
    void deleteAllByBbTinhKhoId(Long id);

    void deleteByBbTinhKhoIdIn(List<Long> id);
}
