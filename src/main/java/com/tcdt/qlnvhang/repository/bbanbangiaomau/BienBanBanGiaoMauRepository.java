package com.tcdt.qlnvhang.repository.bbanbangiaomau;

import com.tcdt.qlnvhang.entities.bbanlaymau.BienBanBanGiaoMau;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Collection;

@Repository
public interface BienBanBanGiaoMauRepository extends CrudRepository<BienBanBanGiaoMau, Long>, BienBanBanGiaoMauRepositoryCustom {

    @Transactional
    @Modifying
    void deleteByIdIn(Collection<Long> ids);
}
