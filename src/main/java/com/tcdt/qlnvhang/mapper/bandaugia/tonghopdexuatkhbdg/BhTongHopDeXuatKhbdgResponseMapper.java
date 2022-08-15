package com.tcdt.qlnvhang.mapper.bandaugia.tonghopdexuatkhbdg;

import com.tcdt.qlnvhang.entities.bandaugia.tonghopdexuatkhbdg.BhTongHopDeXuatKhbdg;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.mapper.AbstractMapper;
import com.tcdt.qlnvhang.response.banhangdaugia.tonghopdexuatkhbdg.BhTongHopDeXuatKhbdgResponse;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {})
public interface BhTongHopDeXuatKhbdgResponseMapper extends AbstractMapper<BhTongHopDeXuatKhbdg, BhTongHopDeXuatKhbdgResponse> {
	@AfterMapping
	static void processTenTrangThai(@MappingTarget BhTongHopDeXuatKhbdgResponse bhTongHopDeXuatKhbdgResponse) {
		if (StringUtils.isEmpty(bhTongHopDeXuatKhbdgResponse.getTrangThai())) return;
		String tenTrangThai = NhapXuatHangTrangThaiEnum.getTenById(bhTongHopDeXuatKhbdgResponse.getTrangThai());
		if (StringUtils.isEmpty(tenTrangThai)) return;
		bhTongHopDeXuatKhbdgResponse.setTenTrangThai(tenTrangThai);
	}
}
