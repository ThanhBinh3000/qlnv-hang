package com.tcdt.qlnvhang.mapper;

import com.tcdt.qlnvhang.entities.kehoachbanhangdaugia.BhDgKhPhanLoTaiSan;
import com.tcdt.qlnvhang.response.kehoachbanhangdaugia.BhDgKhPhanLoTaiSanRes;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface BhDgKhPhanLoTaiSanResponseMapper extends AbstractMapper<BhDgKhPhanLoTaiSan, BhDgKhPhanLoTaiSanRes> {
}
