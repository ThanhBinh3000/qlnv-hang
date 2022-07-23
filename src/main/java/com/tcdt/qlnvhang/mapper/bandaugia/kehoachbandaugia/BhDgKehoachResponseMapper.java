package com.tcdt.qlnvhang.mapper.bandaugia.kehoachbandaugia;

import com.tcdt.qlnvhang.entities.bandaugia.kehoachbanhangdaugia.KeHoachBanDauGia;
import com.tcdt.qlnvhang.enums.TrangThaiEnum;
import com.tcdt.qlnvhang.mapper.AbstractMapper;
import com.tcdt.qlnvhang.response.banhangdaugia.kehoachbanhangdaugia.BhDgKehoachResponse;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {})
public interface BhDgKehoachResponseMapper extends AbstractMapper<KeHoachBanDauGia, BhDgKehoachResponse> {
	@AfterMapping
	static void processTenTrangThai(@MappingTarget BhDgKehoachResponse bhDgKehoachResponse) {
		if (StringUtils.isEmpty(bhDgKehoachResponse.getTrangThai())) return;
		String tenTrangThai = TrangThaiEnum.getTenById(bhDgKehoachResponse.getTrangThai());
		if (StringUtils.isEmpty(tenTrangThai)) return;
		bhDgKehoachResponse.setTenTrangThai(tenTrangThai);
	}
}
