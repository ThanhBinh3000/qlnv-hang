package com.tcdt.qlnvhang.repository.vattu.hosokythuat;

import com.tcdt.qlnvhang.entities.vattu.hosokythuat.NhHoSoKyThuat;
import com.tcdt.qlnvhang.repository.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Optional;

@Repository
public interface NhHoSoKyThuatRepository extends BaseRepository<NhHoSoKyThuat, Long>, NhHoSoKyThuatRepositoryCustom {
    @Transactional
    @Modifying
    void deleteByIdIn(Collection<Long> ids);

    Optional<NhHoSoKyThuat> findFirstBySoBienBan(String soBienBan);
}
