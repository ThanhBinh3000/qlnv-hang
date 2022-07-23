package com.tcdt.qlnvhang.mapper.bandaugia.tochuctrienkhaikehoachbandaugia;

import com.tcdt.qlnvhang.entities.bandaugia.tochuctrienkhaikehoachbandaugia.ThongBaoBanDauGia;
import com.tcdt.qlnvhang.mapper.AbstractMapper;
import com.tcdt.qlnvhang.response.banhangdaugia.tochuctrienkhaikehoachbandaugia.ThongBaoBanDauGiaResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface ThongBaoBanDauGiaResponseMapper extends AbstractMapper<ThongBaoBanDauGia, ThongBaoBanDauGiaResponse> {
}
