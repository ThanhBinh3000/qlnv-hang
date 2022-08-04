package com.tcdt.qlnvhang.repository.xuathang.quyetdinhgiaonhiemvuxuat;

import com.tcdt.qlnvhang.entities.xuathang.XhQdGiaoNvuXuatCt;
import com.tcdt.qlnvhang.entities.xuathang.XhQdGiaoNvuXuatCt1;
import com.tcdt.qlnvhang.repository.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;

@Repository
public interface XhQdGiaoNvuXuatCt1Repository extends BaseRepository<XhQdGiaoNvuXuatCt1, Long> {
    List<XhQdGiaoNvuXuatCt1> findByQdgnvxIdIn(Collection<Long> qdgnvxIds);

    @Transactional
    @Modifying
    void deleteByQdgnvxIdIn(Collection<Long> qdgnvxIds);
}
