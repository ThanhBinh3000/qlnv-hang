package com.tcdt.qlnvhang.repository.quanlybangkecanhangluongthuc;

import com.tcdt.qlnvhang.entities.quanlybangkecanhangluongthuc.QlBangKeChCtLt;
import com.tcdt.qlnvhang.repository.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;

@Repository
public interface QlBangKeChCtLtRepository extends BaseRepository<QlBangKeChCtLt, Long> {

    List<QlBangKeChCtLt> findAllByQlBangKeCanHangLtId(Long bangKeId);

    @Transactional
    @Modifying
    void deleteByQlBangKeCanHangLtIdIn(Collection<Long> bangKeIds);
}
