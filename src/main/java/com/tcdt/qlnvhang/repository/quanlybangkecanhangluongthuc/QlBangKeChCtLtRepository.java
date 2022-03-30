package com.tcdt.qlnvhang.repository.quanlybangkecanhangluongthuc;

import com.tcdt.qlnvhang.entities.quanlybangkecanhangluongthuc.QlBangKeChCtLt;
import com.tcdt.qlnvhang.repository.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QlBangKeChCtLtRepository extends BaseRepository<QlBangKeChCtLt, Long> {

    List<QlBangKeChCtLt> findAllByQlBangKeCanHangLtId(Long bangKeId);
}
