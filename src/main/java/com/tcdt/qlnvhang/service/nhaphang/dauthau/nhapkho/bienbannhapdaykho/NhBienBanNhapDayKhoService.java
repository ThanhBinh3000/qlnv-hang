package com.tcdt.qlnvhang.service.nhaphang.dauthau.nhapkho.bienbannhapdaykho;

import com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhapkho.bienbannhapdaykho.NhBbNhapDayKho;
import com.tcdt.qlnvhang.request.object.quanlybienbannhapdaykholuongthuc.QlBienBanNhapDayKhoLtReq;
import com.tcdt.qlnvhang.service.BaseService;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;

public interface NhBienBanNhapDayKhoService extends BaseService<NhBbNhapDayKho,QlBienBanNhapDayKhoLtReq,Long> {
    ReportTemplateResponse preview(QlBienBanNhapDayKhoLtReq req) throws Exception;
}
