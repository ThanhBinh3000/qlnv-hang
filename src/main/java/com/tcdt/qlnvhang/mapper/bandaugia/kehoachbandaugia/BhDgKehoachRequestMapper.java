package com.tcdt.qlnvhang.mapper.bandaugia.kehoachbandaugia;

import com.tcdt.qlnvhang.entities.bandaugia.kehoachbanhangdaugia.KeHoachBanDauGia;
import com.tcdt.qlnvhang.mapper.AbstractMapper;
import com.tcdt.qlnvhang.request.bandaugia.kehoachbanhangdaugia.KehoachBanDauGiaRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface BhDgKehoachRequestMapper extends AbstractMapper<KeHoachBanDauGia, KehoachBanDauGiaRequest> {
}
