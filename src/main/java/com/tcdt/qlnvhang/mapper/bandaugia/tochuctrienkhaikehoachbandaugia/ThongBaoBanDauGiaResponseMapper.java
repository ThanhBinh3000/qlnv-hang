package com.tcdt.qlnvhang.mapper.bandaugia.tochuctrienkhaikehoachbandaugia;

import com.tcdt.qlnvhang.entities.bandaugia.tochuctrienkhaikehoachbandaugia.ThongBaoBanDauGia;
import com.tcdt.qlnvhang.enums.TrangThaiEnum;
import com.tcdt.qlnvhang.mapper.AbstractMapper;
import com.tcdt.qlnvhang.response.banhangdaugia.tochuctrienkhaikehoachbandaugia.ThongBaoBanDauGiaResponse;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {})
public interface ThongBaoBanDauGiaResponseMapper extends AbstractMapper<ThongBaoBanDauGia, ThongBaoBanDauGiaResponse> {
	@AfterMapping
	static void processTenTrangThai(@MappingTarget ThongBaoBanDauGiaResponse response) {
		if (StringUtils.isEmpty(response.getTrangThai())) return;
		String tenTrangThai = TrangThaiEnum.getTenById(response.getTrangThai());
		if (StringUtils.isEmpty(tenTrangThai)) return;
		response.setTenTrangThai(tenTrangThai);
	}
}
