package com.tcdt.qlnvhang.repository.bandaugia.bienbanbandaugia;

import com.tcdt.qlnvhang.entities.bandaugia.bienbanbandaugia.BhBbBanDauGia;
import com.tcdt.qlnvhang.repository.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Optional;

public interface BhBbBanDauGiaRepository extends BaseRepository<BhBbBanDauGia, Long>, BhBbBanDauGiaRepositoryCustom {
    @Transactional
    @Modifying
    void deleteByIdIn(Collection<Long> ids);

    Optional<BhBbBanDauGia> findFirstBySoBienBan(String soBienBan);
}
