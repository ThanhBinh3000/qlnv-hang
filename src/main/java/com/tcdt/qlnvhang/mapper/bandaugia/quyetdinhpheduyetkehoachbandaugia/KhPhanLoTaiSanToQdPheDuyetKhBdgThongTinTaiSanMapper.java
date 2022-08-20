package com.tcdt.qlnvhang.mapper.bandaugia.quyetdinhpheduyetkehoachbandaugia;

import com.tcdt.qlnvhang.entities.bandaugia.kehoachbanhangdaugia.BanDauGiaPhanLoTaiSan;
import com.tcdt.qlnvhang.entities.bandaugia.quyetdinhpheduyetkehoachbandaugia.BhQdPheDuyetKhBdgThongTinTaiSan;
import com.tcdt.qlnvhang.mapper.AbstractMapper;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

/**
 * Mapper thông tin tài sản của đề xuất kế hoạch bán đấu giá -> thông tin tài sản của quyết định phê duyệt kế hoạch bán đấu giá
 */
@Mapper(componentModel = "spring", uses = {})
public interface KhPhanLoTaiSanToQdPheDuyetKhBdgThongTinTaiSanMapper extends AbstractMapper<BhQdPheDuyetKhBdgThongTinTaiSan, BanDauGiaPhanLoTaiSan> {
	@AfterMapping
	static void ignoreId (@MappingTarget BhQdPheDuyetKhBdgThongTinTaiSan qdPheDuyetKhBdgThongTinTaiSan) {
		qdPheDuyetKhBdgThongTinTaiSan.setId(null);
	}
}
