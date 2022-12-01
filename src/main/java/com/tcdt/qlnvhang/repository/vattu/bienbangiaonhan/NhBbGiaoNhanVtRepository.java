package com.tcdt.qlnvhang.repository.vattu.bienbangiaonhan;

import com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhapkho.bienbangiaonhan.NhBbGiaoNhanVt;
import com.tcdt.qlnvhang.repository.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Optional;

@Repository
public interface NhBbGiaoNhanVtRepository extends BaseRepository<NhBbGiaoNhanVt, Long>, NhBbGiaoNhanVtRepositoryCustom {
    @Transactional
    @Modifying
    void deleteByIdIn(Collection<Long> ids);

    Optional<NhBbGiaoNhanVt> findFirstBySoBienBan(String soBienBan);
}
