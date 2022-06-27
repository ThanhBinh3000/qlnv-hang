package com.tcdt.qlnvhang.repository.quanlybienbannhapdaykholuongthuc;

import com.tcdt.qlnvhang.entities.quanlybienbannhapdaykholuongthuc.QlBienBanNdkCtLt;
import com.tcdt.qlnvhang.repository.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;

@Repository
public interface QlBienBanNdkCtLtRepository extends BaseRepository<QlBienBanNdkCtLt, Long> {
    List<QlBienBanNdkCtLt> findAllByQlBienBanNdkLtIdOrderBySttAsc(Long qlBienBanNdkLtId);

    @Transactional
    @Modifying
    void deleteByQlBienBanNdkLtIdIn(Collection<Long> bbndkIds);
}
