package com.tcdt.qlnvhang.repository.vattu.bienbanchuanbikho;

import com.tcdt.qlnvhang.entities.vattu.bienbanchuanbikho.NhBienBanChuanBiKho;
import com.tcdt.qlnvhang.repository.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Optional;

@Repository
public interface NhBienBanChuanBiKhoRepository extends BaseRepository<NhBienBanChuanBiKho, Long>, NhBienBanChuanBiKhoRepositoryCustom {
    @Transactional
    @Modifying
    void deleteByIdIn(Collection<Long> ids);

    Optional<NhBienBanChuanBiKho> findFirstBySoBienBan(String soBienBan);
}
