package com.tcdt.qlnvhang.repository.xuathang.bbtinhkho;

import com.tcdt.qlnvhang.entities.xuathang.bbtinhkho.XhBienBanTinhKho;
import com.tcdt.qlnvhang.repository.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Optional;

@Repository
public interface XhBienBanTinhKhoRepository extends BaseRepository<XhBienBanTinhKho, Long>, XhBienBanTinhKhoRepositoryCustom {
    @Transactional
    @Modifying
    void deleteByIdIn(Collection<Long> ids);

    Optional<XhBienBanTinhKho> findFirstByQdgnvnxId(String qdgnvnxId);
}
