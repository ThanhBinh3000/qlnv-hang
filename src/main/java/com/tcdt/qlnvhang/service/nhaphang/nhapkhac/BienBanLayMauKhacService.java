package com.tcdt.qlnvhang.service.nhaphang.nhapkhac;

import com.tcdt.qlnvhang.entities.nhaphang.nhapkhac.kiemtracl.bblaymaubangiaomau.BienBanLayMauKhac;
import com.tcdt.qlnvhang.request.object.BienBanLayMauKhacReq;
import com.tcdt.qlnvhang.service.BaseService;

public interface BienBanLayMauKhacService extends BaseService<BienBanLayMauKhac, BienBanLayMauKhacReq,Long> {

    BienBanLayMauKhac create(BienBanLayMauKhacReq req) throws Exception;

    void delete(Long id) throws Exception;
}
