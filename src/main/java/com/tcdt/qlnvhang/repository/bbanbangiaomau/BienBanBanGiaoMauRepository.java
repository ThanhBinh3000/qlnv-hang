package com.tcdt.qlnvhang.repository.bbanbangiaomau;

import com.tcdt.qlnvhang.entities.bbanlaymau.BienBanBanGiaoMau;
import com.tcdt.qlnvhang.repository.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Optional;

@Repository
public interface BienBanBanGiaoMauRepository extends BaseRepository<BienBanBanGiaoMau, Long>, BienBanBanGiaoMauRepositoryCustom {

    @Transactional
    @Modifying
    void deleteByIdIn(Collection<Long> ids);

    Optional<BienBanBanGiaoMau> findFirstBySoBienBan(String soBienBan);
}
