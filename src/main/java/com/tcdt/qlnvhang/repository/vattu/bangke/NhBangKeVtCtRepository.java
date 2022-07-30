package com.tcdt.qlnvhang.repository.vattu.bangke;

import com.tcdt.qlnvhang.entities.nhaphang.vattu.bangke.NhBangKeVtCt;
import com.tcdt.qlnvhang.repository.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;

@Repository
public interface NhBangKeVtCtRepository extends BaseRepository<NhBangKeVtCt, Long> {
    List<NhBangKeVtCt> findByBangKeVtIdIn(Collection<Long> bkvtIds);

    @Transactional
    @Modifying
    void deleteByBangKeVtIdIn(Collection<Long> bkvtIds);
}
