package com.tcdt.qlnvhang.mapper.kehoachbandaugia;

import com.tcdt.qlnvhang.entities.kehoachbanhangdaugia.BhDgKhPhanLoTaiSan;
import com.tcdt.qlnvhang.mapper.AbstractMapper;
import com.tcdt.qlnvhang.request.kehoachbanhangdaugia.BhDgKhPhanLoTaiSanReq;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface BhDgKhPhanLoTaiSanRequestMapper extends AbstractMapper<BhDgKhPhanLoTaiSan, BhDgKhPhanLoTaiSanReq> {
}
