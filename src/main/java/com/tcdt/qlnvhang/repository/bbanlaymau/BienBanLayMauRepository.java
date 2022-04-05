package com.tcdt.qlnvhang.repository.bbanlaymau;

import com.tcdt.qlnvhang.entities.bbanlaymau.BienBanLayMau;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BienBanLayMauRepository extends CrudRepository<BienBanLayMau, Long>, BienBanLayMauRepositoryCustom {
}
