package com.tcdt.qlnvhang.mapper.bandaugia.quyetdinhpheduyetkehoachbandaugia;

import com.tcdt.qlnvhang.entities.bandaugia.quyetdinhpheduyetkehoachbandaugia.BhQdPheDuyetKhBdgThongTinTaiSan;
import com.tcdt.qlnvhang.mapper.AbstractMapper;
import com.tcdt.qlnvhang.request.bandaugia.tonghopdexuatkhbdg.BhQdPheDuyetKhBdgThongTinTaiSanRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface BhQdPheDuyetKhBdgThongTinTaiSanRequestMapper extends AbstractMapper<BhQdPheDuyetKhBdgThongTinTaiSan, BhQdPheDuyetKhBdgThongTinTaiSanRequest> {
}
