package com.tcdt.qlnvhang.repository.vattu.bangke;

import com.tcdt.qlnvhang.entities.vattu.bangke.NhBangKeVt;
import com.tcdt.qlnvhang.repository.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Optional;

@Repository
public interface NhBangKeVtRepository extends BaseRepository<NhBangKeVt, Long>, NhBangKeVtRepositoryCustom {
    @Transactional
    @Modifying
    void deleteByIdIn(Collection<Long> ids);

    Optional<NhBangKeVt> findFirstBySoBangKe(String soBienBan);
}
