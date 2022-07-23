package com.tcdt.qlnvhang.mapper.bandaugia.tochuctrienkhaikehoachbandaugia;

import com.tcdt.qlnvhang.entities.bandaugia.tochuctrienkhaikehoachbandaugia.ThongBaoBanDauGia;
import com.tcdt.qlnvhang.mapper.AbstractMapper;
import com.tcdt.qlnvhang.request.bandaugia.tochuctrienkhaikehoachbandaugia.ThongBaoBanDauGiaRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface ThongBaoBanDauGiaRequestMapper extends AbstractMapper<ThongBaoBanDauGia, ThongBaoBanDauGiaRequest> {
}
