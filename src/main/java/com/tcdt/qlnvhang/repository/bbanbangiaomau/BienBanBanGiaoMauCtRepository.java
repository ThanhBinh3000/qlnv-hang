package com.tcdt.qlnvhang.repository.bbanbangiaomau;

import com.tcdt.qlnvhang.entities.nhaphang.bbanlaymau.BienBanBanGiaoMauCt;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;

@Repository
public interface BienBanBanGiaoMauCtRepository extends CrudRepository<BienBanBanGiaoMauCt, Long> {

    List<BienBanBanGiaoMauCt> findByBbBanGiaoMauIdIn(Collection<Long> bbBanGiaoMauIds);

    @Transactional
    @Modifying
    void deleteByBbBanGiaoMauIdIn(Collection<Long> bbBanGiaoMauIds);
}
