package com.tcdt.qlnvhang.mapper.bandaugia.bienbanlaymau;

import com.tcdt.qlnvhang.entities.bandaugia.bienbanlaymau.XhBbLayMau;
import com.tcdt.qlnvhang.mapper.AbstractMapper;
import com.tcdt.qlnvhang.request.bandaugia.bienbanlaymau.XhBbLayMauRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface XhBbLayMauRequestMapper extends AbstractMapper<XhBbLayMau, XhBbLayMauRequest> {
}
