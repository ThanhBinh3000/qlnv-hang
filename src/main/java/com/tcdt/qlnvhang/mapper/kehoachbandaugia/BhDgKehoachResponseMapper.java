package com.tcdt.qlnvhang.mapper.kehoachbandaugia;

import com.tcdt.qlnvhang.entities.kehoachbanhangdaugia.BhDgKehoach;
import com.tcdt.qlnvhang.enums.TrangThaiEnum;
import com.tcdt.qlnvhang.mapper.AbstractMapper;
import com.tcdt.qlnvhang.response.kehoachbanhangdaugia.BhDgKehoachRes;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {})
public interface BhDgKehoachResponseMapper extends AbstractMapper<BhDgKehoach, BhDgKehoachRes> {
	@AfterMapping
	static void processTenTrangThai(@MappingTarget BhDgKehoachRes bhDgKehoachRes) {
		if (StringUtils.isEmpty(bhDgKehoachRes.getTrangThai())) return;
		String tenTrangThai = TrangThaiEnum.getTenById(bhDgKehoachRes.getTrangThai());
		if (StringUtils.isEmpty(tenTrangThai)) return;
		bhDgKehoachRes.setTenTrangThai(tenTrangThai);
	}
}
