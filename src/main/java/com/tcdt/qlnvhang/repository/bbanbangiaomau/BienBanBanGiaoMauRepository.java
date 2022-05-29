package com.tcdt.qlnvhang.repository.bbanbangiaomau;

import com.tcdt.qlnvhang.entities.bbanlaymau.BienBanBanGiaoMau;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BienBanBanGiaoMauRepository extends CrudRepository<BienBanBanGiaoMau, Long>, BienBanBanGiaoMauRepositoryCustom {
}
