package com.tcdt.qlnvhang.repository.quanlybangkecanhangluongthuc;

import com.tcdt.qlnvhang.entities.quanlybangkecanhangluongthuc.QlBangKeCanHangLt;
import com.tcdt.qlnvhang.repository.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QlBangKeCanHangLtRepository extends BaseRepository<QlBangKeCanHangLt, Long>, QlBangKeCanHangLtRepositoryCustom {
}
