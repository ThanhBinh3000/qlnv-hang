package com.tcdt.qlnvhang.service.xuathang.xuattheophuongthucdaugia.kehoach.dexuat;

import com.tcdt.qlnvhang.request.CountKhlcntSlReq;
import com.tcdt.qlnvhang.request.xuathang.xuattheophuongthucdaugia.kehoachbdg.dexuat.XhDxKhBanDauGiaReq;
import com.tcdt.qlnvhang.service.BaseService;
import com.tcdt.qlnvhang.table.xuathang.xuattheophuongthucdaugia.kehoach.dexuat.XhDxKhBanDauGia;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public interface XhDxKhBanDauGiaService extends BaseService<XhDxKhBanDauGia, XhDxKhBanDauGiaReq,Long> {

    BigDecimal countSoLuongKeHoachNam(CountKhlcntSlReq req) throws Exception;

}