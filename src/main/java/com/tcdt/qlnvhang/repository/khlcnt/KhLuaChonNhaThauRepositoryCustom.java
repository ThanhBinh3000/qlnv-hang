package com.tcdt.qlnvhang.repository.khlcnt;

import com.tcdt.qlnvhang.entities.kehoachluachonnhathau.KhLuaChonNhaThau;
import com.tcdt.qlnvhang.request.object.khlcnt.KhLuaChonNhaThauSearchReq;
import org.springframework.data.domain.Page;

public interface KhLuaChonNhaThauRepositoryCustom {
	Page<KhLuaChonNhaThau> search(KhLuaChonNhaThauSearchReq req);
}
