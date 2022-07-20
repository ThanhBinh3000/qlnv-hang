package com.tcdt.qlnvhang.mapper.chitieukehoachnam;

import com.tcdt.qlnvhang.entities.chitieukehoachnam.ChiTieuKeHoachNam;
import com.tcdt.qlnvhang.entities.kehoachbanhangdaugia.BhDgKehoach;
import com.tcdt.qlnvhang.enums.TrangThaiEnum;
import com.tcdt.qlnvhang.mapper.AbstractMapper;
import com.tcdt.qlnvhang.response.chitieukehoachnam.ChiTieuKeHoachNamRes;
import com.tcdt.qlnvhang.response.kehoachbanhangdaugia.BhDgKehoachRes;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {})
public interface ChiTieuKeHoachNamResponseMapper extends AbstractMapper<ChiTieuKeHoachNam, ChiTieuKeHoachNamRes> {
}
