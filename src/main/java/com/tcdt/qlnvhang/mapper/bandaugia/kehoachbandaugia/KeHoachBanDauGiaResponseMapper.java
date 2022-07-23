package com.tcdt.qlnvhang.mapper.bandaugia.kehoachbandaugia;

import com.tcdt.qlnvhang.entities.bandaugia.kehoachbanhangdaugia.KeHoachBanDauGia;
import com.tcdt.qlnvhang.enums.TrangThaiEnum;
import com.tcdt.qlnvhang.mapper.AbstractMapper;
import com.tcdt.qlnvhang.response.banhangdaugia.kehoachbanhangdaugia.KeHoachBanDauGiaResponse;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {})
public interface KeHoachBanDauGiaResponseMapper extends AbstractMapper<KeHoachBanDauGia, KeHoachBanDauGiaResponse> {
	@AfterMapping
	static void processTenTrangThai(@MappingTarget KeHoachBanDauGiaResponse keHoachBanDauGiaResponse) {
		if (StringUtils.isEmpty(keHoachBanDauGiaResponse.getTrangThai())) return;
		String tenTrangThai = TrangThaiEnum.getTenById(keHoachBanDauGiaResponse.getTrangThai());
		if (StringUtils.isEmpty(tenTrangThai)) return;
		keHoachBanDauGiaResponse.setTenTrangThai(tenTrangThai);
	}
}
