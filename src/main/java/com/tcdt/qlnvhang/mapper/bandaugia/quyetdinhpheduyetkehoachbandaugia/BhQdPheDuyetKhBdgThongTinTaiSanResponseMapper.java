package com.tcdt.qlnvhang.mapper.bandaugia.quyetdinhpheduyetkehoachbandaugia;

import com.tcdt.qlnvhang.entities.bandaugia.tonghopdexuatkhbdg.BhQdPheDuyetKhBdgThongTinTaiSan;
import com.tcdt.qlnvhang.mapper.AbstractMapper;
import com.tcdt.qlnvhang.response.banhangdaugia.tonghopdexuatkhbdg.BhQdPheDuyetKhBdgThongTinTaiSanResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface BhQdPheDuyetKhBdgThongTinTaiSanResponseMapper extends AbstractMapper<BhQdPheDuyetKhBdgThongTinTaiSan, BhQdPheDuyetKhBdgThongTinTaiSanResponse> {
}
