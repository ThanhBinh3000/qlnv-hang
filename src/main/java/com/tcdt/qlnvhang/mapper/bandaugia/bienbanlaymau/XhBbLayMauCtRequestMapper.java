package com.tcdt.qlnvhang.mapper.bandaugia.bienbanlaymau;

import com.tcdt.qlnvhang.entities.bandaugia.bienbanlaymau.XhBbLayMauCt;
import com.tcdt.qlnvhang.mapper.AbstractMapper;
import com.tcdt.qlnvhang.request.bandaugia.bienbanlaymau.XhBbLayMauCtRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface XhBbLayMauCtRequestMapper extends AbstractMapper<XhBbLayMauCt, XhBbLayMauCtRequest> {
}
