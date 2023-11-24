package com.tcdt.qlnvhang.service.nhaphang.dauthau.nhapkho.bangkecanhang;

import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kiemtracl.phieuktracl.NhPhieuKtChatLuong;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhapkho.bangkecanhang.NhBangKeCanHang;
import com.tcdt.qlnvhang.request.object.quanlybangkecanhangluongthuc.QlBangKeCanHangLtReq;
import com.tcdt.qlnvhang.service.BaseService;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;

public interface NhBangKeCanHangService extends BaseService<NhBangKeCanHang,QlBangKeCanHangLtReq,Long> {

    List<NhBangKeCanHang> findAllByIdQdGiaoNvNh(Long idQdGiaoNvNh);

    List<NhBangKeCanHang> findAllByIdDdiemGiaoNvNh(Long idDdiemGiaoNvNh);

    ReportTemplateResponse preview(HashMap<String, Object> body) throws Exception;
}
