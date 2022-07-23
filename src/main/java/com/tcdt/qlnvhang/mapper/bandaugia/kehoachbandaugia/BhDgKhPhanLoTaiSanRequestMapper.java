package com.tcdt.qlnvhang.mapper.bandaugia.kehoachbandaugia;

import com.tcdt.qlnvhang.entities.bandaugia.kehoachbanhangdaugia.BanDauGiaPhanLoTaiSan;
import com.tcdt.qlnvhang.mapper.AbstractMapper;
import com.tcdt.qlnvhang.request.bandaugia.kehoachbanhangdaugia.BanDauGiaPhanLoTaiSanRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface BhDgKhPhanLoTaiSanRequestMapper extends AbstractMapper<BanDauGiaPhanLoTaiSan, BanDauGiaPhanLoTaiSanRequest> {
}
