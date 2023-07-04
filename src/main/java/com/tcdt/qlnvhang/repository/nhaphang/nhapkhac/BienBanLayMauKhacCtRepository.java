package com.tcdt.qlnvhang.repository.nhaphang.nhapkhac;

import com.tcdt.qlnvhang.entities.nhaphang.nhapkhac.kiemtracl.bblaymaubangiaomau.BienBanLayMauKhacCt;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;

@Repository
public interface BienBanLayMauKhacCtRepository extends CrudRepository<BienBanLayMauKhacCt, Long> {

    List<BienBanLayMauKhacCt> findByBbLayMauIdIn(Collection<Long> bbLayMauIds);

    @Transactional
    @Modifying
    void deleteByBbLayMauIdIn(Collection<Long> bbLayMauIds);

    @Transactional
    @Modifying
    void deleteByBbLayMauId(Long bbLayMauId);
}
