package com.tcdt.qlnvhang.mapper.chitieukehoachnam;

import com.tcdt.qlnvhang.entities.chitieukehoachnam.ChiTieuKeHoachNam;
import com.tcdt.qlnvhang.mapper.AbstractMapper;
import com.tcdt.qlnvhang.response.chitieukehoachnam.ChiTieuKeHoachNamRes;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface ChiTieuKeHoachNamResponseMapper extends AbstractMapper<ChiTieuKeHoachNam, ChiTieuKeHoachNamRes> {
}
