package com.tcdt.qlnvhang.service.nhaphang.dauthau.ktracluong.phieukiemtracl;

import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kiemtracl.phieuktracl.NhPhieuKtChatLuong;
import com.tcdt.qlnvhang.request.object.HhBbNghiemthuKlstHdrReq;
import com.tcdt.qlnvhang.request.phieuktracluong.QlpktclhPhieuKtChatLuongRequestDto;
import com.tcdt.qlnvhang.service.BaseService;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;

import java.math.BigDecimal;
import java.util.List;

public interface NhPhieuKtChatLuongService extends BaseService<NhPhieuKtChatLuong,QlpktclhPhieuKtChatLuongRequestDto,Long> {

    List<NhPhieuKtChatLuong> findAllByIdQdGiaoNvNh(Long idQdGiaoNvNh);

    List<NhPhieuKtChatLuong> findAllByIdDdiemGiaoNvNh(Long idDdiemGiaoNvNh);

    BigDecimal getSoLuongNhapKho(Long idDdiemGiaoNvNh);

    ReportTemplateResponse preview(QlpktclhPhieuKtChatLuongRequestDto objReq) throws Exception;

}
