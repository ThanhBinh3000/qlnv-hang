package com.tcdt.qlnvhang.mapper.bandaugia.quyetdinhpheduyetkehoachbandaugia;

import com.tcdt.qlnvhang.entities.bandaugia.quyetdinhpheduyetkehoachbandaugia.BhQdPheDuyetKhbdg;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.mapper.AbstractMapper;
import com.tcdt.qlnvhang.response.banhangdaugia.quyetdinhpheduyetkehoachbandaugia.BhQdPheDuyetKhbdgResponse;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {})
public interface BhQdPheDuyetKhbdgResponseMapper extends AbstractMapper<BhQdPheDuyetKhbdg, BhQdPheDuyetKhbdgResponse> {
	@AfterMapping
	static void processTenTrangThai(@MappingTarget BhQdPheDuyetKhbdgResponse qdPheDuyetKhbdgResponse) {
		if (StringUtils.isEmpty(qdPheDuyetKhbdgResponse.getTrangThai())) return;
		String tenTrangThai = NhapXuatHangTrangThaiEnum.getTenById(qdPheDuyetKhbdgResponse.getTrangThai());
		if (StringUtils.isEmpty(tenTrangThai)) return;
		qdPheDuyetKhbdgResponse.setTenTrangThai(tenTrangThai);
	}
}
