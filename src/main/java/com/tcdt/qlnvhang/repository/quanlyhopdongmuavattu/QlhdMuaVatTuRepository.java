package com.tcdt.qlnvhang.repository.quanlyhopdongmuavattu;

import com.tcdt.qlnvhang.entities.quanlyhopdongmuavattu.QlhdMuaVatTu;
import com.tcdt.qlnvhang.repository.quyetdinhpheduyetketqualuachonnhathauvatu.GenericRepository;

import java.util.Collection;
import java.util.List;

public interface QlhdMuaVatTuRepository extends GenericRepository<QlhdMuaVatTu, Long>, QlhdMuaVatTuRepositoryCustom {
	@Override
	List<QlhdMuaVatTu> findByIdIn(Collection<Long> ids);
}