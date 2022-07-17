package com.tcdt.qlnvhang.mapper;

import com.tcdt.qlnvhang.entities.kehoachbanhangdaugia.BhDgKhDiaDiemGiaoNhan;
import com.tcdt.qlnvhang.request.kehoachbanhangdaugia.BhDgKhDiaDiemGiaoNhanReq;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface BhDgKhDiaDiemGiaoNhanRequestMapper extends AbstractMapper<BhDgKhDiaDiemGiaoNhan, BhDgKhDiaDiemGiaoNhanReq> {
}
