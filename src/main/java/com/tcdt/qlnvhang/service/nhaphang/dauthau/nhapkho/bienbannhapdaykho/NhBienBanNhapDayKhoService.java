package com.tcdt.qlnvhang.service.nhaphang.dauthau.nhapkho.bienbannhapdaykho;

import com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhapkho.bienbannhapdaykho.NhBbNhapDayKho;
import com.tcdt.qlnvhang.request.object.quanlybienbannhapdaykholuongthuc.QlBienBanNhapDayKhoLtReq;
import com.tcdt.qlnvhang.request.search.HhQdNhapxuatSearchReq;
import com.tcdt.qlnvhang.service.BaseService;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

public interface NhBienBanNhapDayKhoService extends BaseService<NhBbNhapDayKho,QlBienBanNhapDayKhoLtReq,Long> {
//    ReportTemplateResponse preview(QlBienBanNhapDayKhoLtReq req) throws Exception;
    ReportTemplateResponse preview(HashMap<String, Object> body) throws Exception;
    void exportToExcel(HhQdNhapxuatSearchReq searchReq, HttpServletResponse response) throws Exception;
}
