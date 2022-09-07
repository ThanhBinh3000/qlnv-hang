package com.tcdt.qlnvhang.mapper.bandaugia.bienbanlaymau;

import com.tcdt.qlnvhang.entities.bandaugia.bienbanlaymau.XhBbLayMau;
import com.tcdt.qlnvhang.mapper.AbstractMapper;
import com.tcdt.qlnvhang.response.banhangdaugia.bienbanlaymau.XhBbLayMauResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface XhBbLayMauResponseMapper extends AbstractMapper<XhBbLayMau, XhBbLayMauResponse> {
}
