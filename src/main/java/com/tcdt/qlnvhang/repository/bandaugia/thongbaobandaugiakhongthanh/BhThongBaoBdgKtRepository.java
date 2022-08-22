package com.tcdt.qlnvhang.repository.bandaugia.thongbaobandaugiakhongthanh;

import com.tcdt.qlnvhang.entities.bandaugia.thongbaobandaugiakhongthanh.BhThongBaoBdgKt;
import com.tcdt.qlnvhang.repository.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Optional;

@Repository
public interface BhThongBaoBdgKtRepository extends BaseRepository<BhThongBaoBdgKt, Long>, BhThongBaoBdgKtRepositoryCustom {
    @Transactional
    @Modifying
    void deleteByIdIn(Collection<Long> ids);

    Optional<BhThongBaoBdgKt> findFirstByMaThongBao(String soBienBan);
}
