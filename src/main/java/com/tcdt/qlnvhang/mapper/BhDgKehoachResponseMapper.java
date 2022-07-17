package com.tcdt.qlnvhang.mapper;

import com.tcdt.qlnvhang.entities.kehoachbanhangdaugia.BhDgKehoach;
import com.tcdt.qlnvhang.response.kehoachbanhangdaugia.BhDgKehoachRes;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface BhDgKehoachResponseMapper extends AbstractMapper<BhDgKehoach, BhDgKehoachRes> {
}
