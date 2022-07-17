package com.tcdt.qlnvhang.mapper;

import com.tcdt.qlnvhang.entities.kehoachbanhangdaugia.BhDgKehoach;
import com.tcdt.qlnvhang.request.kehoachbanhangdaugia.BhDgKehoachReq;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface BhDgKehoachRequestMapper extends AbstractMapper<BhDgKehoach, BhDgKehoachReq> {
}
