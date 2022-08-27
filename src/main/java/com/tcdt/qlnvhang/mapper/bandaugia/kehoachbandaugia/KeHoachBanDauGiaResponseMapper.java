package com.tcdt.qlnvhang.mapper.bandaugia.kehoachbandaugia;

import com.tcdt.qlnvhang.entities.bandaugia.kehoachbanhangdaugia.KeHoachBanDauGia;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.mapper.AbstractMapper;
import com.tcdt.qlnvhang.response.banhangdaugia.kehoachbanhangdaugia.BanDauGiaPhanLoTaiSanResponse;
import com.tcdt.qlnvhang.response.banhangdaugia.kehoachbanhangdaugia.KeHoachBanDauGiaResponse;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.Objects;

@Mapper(componentModel = "spring", uses = {})
public interface KeHoachBanDauGiaResponseMapper extends AbstractMapper<KeHoachBanDauGia, KeHoachBanDauGiaResponse> {
	@AfterMapping
	static void processMoreInfo(@MappingTarget KeHoachBanDauGiaResponse keHoachBanDauGiaResponse) {
		if (StringUtils.isEmpty(keHoachBanDauGiaResponse.getTrangThai())) return;
		String tenTrangThai = NhapXuatHangTrangThaiEnum.getTenById(keHoachBanDauGiaResponse.getTrangThai());
		if (StringUtils.isEmpty(tenTrangThai)) return;
		keHoachBanDauGiaResponse.setTenTrangThai(tenTrangThai);

		if (CollectionUtils.isEmpty(keHoachBanDauGiaResponse.getPhanLoTaiSanList())) return;
		keHoachBanDauGiaResponse
				.setTongGiaKhoiDiem(keHoachBanDauGiaResponse.getPhanLoTaiSanList()
						.stream().map(BanDauGiaPhanLoTaiSanResponse::getGiaKhoiDiem).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add));

		keHoachBanDauGiaResponse
				.setTongKhoanTienDatTruoc(keHoachBanDauGiaResponse.getPhanLoTaiSanList()
						.stream().map(BanDauGiaPhanLoTaiSanResponse::getSoTienDatTruoc).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add));

		keHoachBanDauGiaResponse
				.setTongSoLuongDonViTaiSan(keHoachBanDauGiaResponse.getPhanLoTaiSanList()
						.stream().map(BanDauGiaPhanLoTaiSanResponse::getSoLuong).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add));
	}
}
