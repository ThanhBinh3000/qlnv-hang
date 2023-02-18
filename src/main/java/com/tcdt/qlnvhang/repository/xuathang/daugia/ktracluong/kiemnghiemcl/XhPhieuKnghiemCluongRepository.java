package com.tcdt.qlnvhang.repository.xuathang.daugia.ktracluong.kiemnghiemcl;

import com.tcdt.qlnvhang.entities.xuathang.daugia.ktracluong.phieukiemnghiemcl.XhPhieuKnghiemCluong;
import com.tcdt.qlnvhang.repository.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Optional;

@Repository
public interface XhPhieuKnghiemCluongRepository extends BaseRepository<XhPhieuKnghiemCluong, Long> {

    @Transactional
    @Modifying
    void deleteByIdIn(Collection<Long> ids);

    Optional<XhPhieuKnghiemCluong> findFirstBySoPhieu(String soPhieu);
}
