package com.tcdt.qlnvhang.service.xuathang.daugia.kehoach.dexuat;

import com.tcdt.qlnvhang.request.CountKhlcntSlReq;
import com.tcdt.qlnvhang.request.object.HhDxuatKhLcntHdrReq;
import com.tcdt.qlnvhang.request.xuathang.daugia.kehoachbdg.dexuat.XhDxKhBanDauGiaReq;
import com.tcdt.qlnvhang.service.BaseService;
import com.tcdt.qlnvhang.entities.xuathang.daugia.kehoach.dexuat.XhDxKhBanDauGia;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.HashMap;

@Service
public interface XhDxKhBanDauGiaService extends BaseService<XhDxKhBanDauGia, XhDxKhBanDauGiaReq, Long> {

  BigDecimal countSoLuongKeHoachNam(CountKhlcntSlReq req) throws Exception;

  BigDecimal getGiaBanToiThieu(String cloaiVthh, String maDvi, Integer namKh);

  ReportTemplateResponse preview(HashMap<String, Object> body) throws Exception;
}