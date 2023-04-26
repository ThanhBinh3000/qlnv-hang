package com.tcdt.qlnvhang.service.xuathang.bantructiep.kehoach.dexuat;

import com.tcdt.qlnvhang.entities.xuathang.bantructiep.kehoach.dexuat.XhDxKhBanTrucTiepHdr;
import com.tcdt.qlnvhang.request.CountKhlcntSlReq;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.kehoach.dexuat.XhDxKhBanTrucTiepHdrReq;
import com.tcdt.qlnvhang.service.BaseService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public interface XhDxKhBanTrucTiepService extends BaseService<XhDxKhBanTrucTiepHdr, XhDxKhBanTrucTiepHdrReq, Long> {
    BigDecimal countSoLuongKeHoachNam(CountKhlcntSlReq req) throws Exception;

    BigDecimal getGiaBanToiThieu (String cloaiVthh, String maDvi, Integer namKh);
}
