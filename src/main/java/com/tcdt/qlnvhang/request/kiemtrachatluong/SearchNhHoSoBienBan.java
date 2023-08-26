package com.tcdt.qlnvhang.request.kiemtrachatluong;

import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.table.report.ReportTemplateRequest;
import lombok.Data;

@Data
public class SearchNhHoSoBienBan extends BaseRequest {
    private Long id;
    private ReportTemplateRequest reportTemplateRequest;
}
