package com.tcdt.qlnvhang.mapper.bandaugia.kehoachbandaugia;

import com.tcdt.qlnvhang.entities.bandaugia.kehoachbanhangdaugia.BanDauGiaPhanLoTaiSan;
import com.tcdt.qlnvhang.mapper.AbstractMapper;
import com.tcdt.qlnvhang.response.banhangdaugia.kehoachbanhangdaugia.BhDgKhPhanLoTaiSanResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface BhDgKhPhanLoTaiSanResponseMapper extends AbstractMapper<BanDauGiaPhanLoTaiSan, BhDgKhPhanLoTaiSanResponse> {
}
