package com.tcdt.qlnvhang.repository.bbanlaymau;

import com.tcdt.qlnvhang.entities.bbanlaymau.BienBanLayMau;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Optional;

@Repository
public interface BienBanLayMauRepository extends CrudRepository<BienBanLayMau, Long>, BienBanLayMauRepositoryCustom {
    @Transactional
    @Modifying
    void deleteByIdIn(Collection<Long> ids);

    Optional<BienBanLayMau> findFirstBySoBienBan(String soBienBan);
}
