package com.tcdt.qlnvhang.repository.quanlybangkecanhangluongthuc;

import com.tcdt.qlnvhang.entities.quanlybangkecanhangluongthuc.QlBangKeCanHangLt;
import com.tcdt.qlnvhang.repository.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Optional;

@Repository
public interface QlBangKeCanHangLtRepository extends BaseRepository<QlBangKeCanHangLt, Long>, QlBangKeCanHangLtRepositoryCustom {

    @Transactional
    @Modifying
    void deleteByIdIn(Collection<Long> ids);

    Optional<QlBangKeCanHangLt> findFirstBySoBangKe(String soBangKe);
}
