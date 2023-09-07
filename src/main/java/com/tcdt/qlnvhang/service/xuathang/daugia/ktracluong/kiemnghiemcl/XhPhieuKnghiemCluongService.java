package com.tcdt.qlnvhang.service.xuathang.daugia.ktracluong.kiemnghiemcl;

import com.tcdt.qlnvhang.entities.xuathang.daugia.ktracluong.phieukiemnghiemcl.XhPhieuKnghiemCluong;
import com.tcdt.qlnvhang.request.bandaugia.bienbanlaymau.XhBbLayMauRequest;
import com.tcdt.qlnvhang.request.xuathang.phieukiemnghiemchatluong.XhPhieuKnghiemCluongReq;
import com.tcdt.qlnvhang.service.BaseService;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;

public interface XhPhieuKnghiemCluongService extends BaseService<XhPhieuKnghiemCluong,XhPhieuKnghiemCluongReq,Long> {

    ReportTemplateResponse preview(XhPhieuKnghiemCluongReq objReq) throws Exception;

}
