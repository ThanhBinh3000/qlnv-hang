package com.tcdt.qlnvhang.repository.xuathang.quyetdinhgiaonhiemvuxuat;

import com.tcdt.qlnvhang.entities.xuathang.XhQdGiaoNvuXuat;
import com.tcdt.qlnvhang.repository.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Optional;

@Repository
public interface XhQdGiaoNvuXuatRepository extends BaseRepository<XhQdGiaoNvuXuat, Long>, XhQdGiaoNvuXuatRepositoryCustom {
    @Transactional
    @Modifying
    void deleteByIdIn(Collection<Long> ids);

    Optional<XhQdGiaoNvuXuat> findFirstBySoQuyetDinh(String soQd);
}
