package com.tcdt.qlnvhang.mapper.bandaugia.quyetdinhpheduyetkehoachbandaugia;

import com.tcdt.qlnvhang.entities.bandaugia.quyetdinhpheduyetkehoachbandaugia.BhQdPheDuyetKhbdg;
import com.tcdt.qlnvhang.mapper.AbstractMapper;
import com.tcdt.qlnvhang.request.bandaugia.quyetdinhpheduyetkehochbandaugia.BhQdPheDuyetKhbdgRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface BhQdPheDuyetKhbdgRequestMapper extends AbstractMapper<BhQdPheDuyetKhbdg, BhQdPheDuyetKhbdgRequest> {
}
