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

@Service
public interface XhDxKhBanDauGiaService extends BaseService<XhDxKhBanDauGia, XhDxKhBanDauGiaReq, Long> {

  BigDecimal countSoLuongKeHoachNam(CountKhlcntSlReq req) throws Exception;

  BigDecimal getGiaBanToiThieu(String cloaiVthh, String maDvi, Integer namKh);

  //    byte[] preview(HttpServletResponse response) throws Exception;
  ReportTemplateResponse preview(HttpServletResponse response) throws Exception;

}