package com.tcdt.qlnvhang.mapper;

import com.tcdt.qlnvhang.entities.kehoachbanhangdaugia.BhDgKhDiaDiemGiaoNhan;
import com.tcdt.qlnvhang.response.kehoachbanhangdaugia.BhDgKhDiaDiemGiaoNhanRes;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface BhDgKhDiaDiemGiaoNhanResponseMapper extends AbstractMapper<BhDgKhDiaDiemGiaoNhan, BhDgKhDiaDiemGiaoNhanRes> {
}
