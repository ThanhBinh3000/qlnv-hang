package com.tcdt.qlnvhang.repository.vattu.bienbanguihang;

import com.tcdt.qlnvhang.entities.vattu.bienbanguihang.NhBienBanGuiHang;
import com.tcdt.qlnvhang.repository.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Optional;

@Repository
public interface NhBienBanGuiHangRepository extends BaseRepository<NhBienBanGuiHang, Long>, NhBienBanGuiHangRepositoryCustom {
    @Transactional
    @Modifying
    void deleteByIdIn(Collection<Long> ids);

    Optional<NhBienBanGuiHang> findFirstBySoBienBan(String soBienBan);
}
