package com.tcdt.qlnvhang.mapper.bandaugia.kehoachbandaugia;

import com.tcdt.qlnvhang.entities.bandaugia.kehoachbanhangdaugia.BanDauGiaPhanLoTaiSan;
import com.tcdt.qlnvhang.mapper.AbstractMapper;
import com.tcdt.qlnvhang.response.banhangdaugia.kehoachbanhangdaugia.BanDauGiaPhanLoTaiSanResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface BanDauGiaPhanLoTaiSanResponseMapper extends AbstractMapper<BanDauGiaPhanLoTaiSan, BanDauGiaPhanLoTaiSanResponse> {
}
