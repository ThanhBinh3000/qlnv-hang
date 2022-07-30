package com.tcdt.qlnvhang.repository.bbanlaymau;

import com.tcdt.qlnvhang.entities.nhaphang.bbanlaymau.BienBanLayMauCt;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;

@Repository
public interface BienBanLayMauCtRepository extends CrudRepository<BienBanLayMauCt, Long> {

    List<BienBanLayMauCt> findByBbLayMauIdIn(Collection<Long> bbLayMauIds);

    @Transactional
    @Modifying
    void deleteByBbLayMauIdIn(Collection<Long> bbLayMauIds);
}
