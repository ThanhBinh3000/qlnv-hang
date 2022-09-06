package com.tcdt.qlnvhang.mapper.bandaugia.bienbanlaymau;

import com.tcdt.qlnvhang.entities.bandaugia.bienbanlaymau.XhBbLayMauCt;
import com.tcdt.qlnvhang.mapper.AbstractMapper;
import com.tcdt.qlnvhang.response.banhangdaugia.bienbanlaymau.XhBbLayMauCtResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface XhBbLayMauCtResponseMapper extends AbstractMapper<XhBbLayMauCt, XhBbLayMauCtResponse> {
}
