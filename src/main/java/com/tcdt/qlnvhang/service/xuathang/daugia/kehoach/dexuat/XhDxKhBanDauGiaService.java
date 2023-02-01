package com.tcdt.qlnvhang.service.xuathang.daugia.kehoach.dexuat;

import com.tcdt.qlnvhang.request.CountKhlcntSlReq;
import com.tcdt.qlnvhang.request.xuathang.daugia.kehoachbdg.dexuat.XhDxKhBanDauGiaReq;
import com.tcdt.qlnvhang.service.BaseService;
import com.tcdt.qlnvhang.entities.xuathang.daugia.kehoach.dexuat.XhDxKhBanDauGia;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public interface XhDxKhBanDauGiaService extends BaseService<XhDxKhBanDauGia, XhDxKhBanDauGiaReq,Long> {

    BigDecimal countSoLuongKeHoachNam(CountKhlcntSlReq req) throws Exception;

}