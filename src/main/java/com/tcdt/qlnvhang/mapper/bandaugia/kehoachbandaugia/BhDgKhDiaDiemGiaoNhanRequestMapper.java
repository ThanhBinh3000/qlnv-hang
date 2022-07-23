package com.tcdt.qlnvhang.mapper.bandaugia.kehoachbandaugia;

import com.tcdt.qlnvhang.entities.bandaugia.kehoachbanhangdaugia.BanDauGiaDiaDiemGiaoNhan;
import com.tcdt.qlnvhang.mapper.AbstractMapper;
import com.tcdt.qlnvhang.request.bandaugia.kehoachbanhangdaugia.BanDauGiaDiaDiemGiaoNhanRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface BhDgKhDiaDiemGiaoNhanRequestMapper extends AbstractMapper<BanDauGiaDiaDiemGiaoNhan, BanDauGiaDiaDiemGiaoNhanRequest> {
}
