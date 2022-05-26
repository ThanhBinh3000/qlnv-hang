package com.tcdt.qlnvhang.repository.quanlybienbannhapdaykholuongthuc;

import com.tcdt.qlnvhang.entities.quanlybienbannhapdaykholuongthuc.QlBienBanNdkCtLt;
import com.tcdt.qlnvhang.repository.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QlBienBanNdkCtLtRepository extends BaseRepository<QlBienBanNdkCtLt, Long> {
    List<QlBienBanNdkCtLt> findAllByQlBienBanNdkLtIdOrderBySttAsc(Long qlBienBanNdkLtId);
}
