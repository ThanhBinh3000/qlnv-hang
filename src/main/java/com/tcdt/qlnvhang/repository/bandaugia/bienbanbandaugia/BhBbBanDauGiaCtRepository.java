package com.tcdt.qlnvhang.repository.bandaugia.bienbanbandaugia;

import com.tcdt.qlnvhang.entities.bandaugia.bienbanbandaugia.BhBbBanDauGiaCt;
import com.tcdt.qlnvhang.repository.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;

@Repository
public interface BhBbBanDauGiaCtRepository extends BaseRepository<BhBbBanDauGiaCt, Long> {

    List<BhBbBanDauGiaCt> findByBbBanDauGiaIdIn(Collection<Long> bbBanDauGiaIds);

    @Transactional
    @Modifying
    void deleteByBbBanDauGiaIdIn(Collection<Long> bbBanDauGiaIds);
}
