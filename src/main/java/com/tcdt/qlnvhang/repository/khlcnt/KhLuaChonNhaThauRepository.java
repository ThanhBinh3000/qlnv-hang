package com.tcdt.qlnvhang.repository.khlcnt;

import com.tcdt.qlnvhang.entities.kehoachluachonnhathau.KhLuaChonNhaThau;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KhLuaChonNhaThauRepository extends KhLuaChonNhaThauRepositoryCustom, CrudRepository<KhLuaChonNhaThau, Long> {

}
