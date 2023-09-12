package com.tcdt.qlnvhang.service.nhaphang.nhapkhac;

import com.tcdt.qlnvhang.entities.nhaphang.nhapkhac.HhNkPhieuKtcl;
import com.tcdt.qlnvhang.entities.nhaphang.nhapkhac.nvnhap.HhQdGiaoNvuNhapHangKhacHdr;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.nhaphang.nhapkhac.HhNkPhieuKtclReq;
import com.tcdt.qlnvhang.request.nhaphang.nhapkhac.HhNkPhieuKtclSearch;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;
import org.springframework.data.domain.Page;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface HhNkPhieuKtclService {
    Page<HhQdGiaoNvuNhapHangKhacHdr> timKiem(HhNkPhieuKtclSearch req) throws Exception;
    List<HhQdGiaoNvuNhapHangKhacHdr> dsQdNvuDuocLapPhieuKtcl(HhNkPhieuKtclSearch req) throws Exception;
    HhNkPhieuKtcl timKiemPhieuKtclTheoMaNganLo(HhNkPhieuKtclSearch req) throws Exception;
    HhNkPhieuKtcl themMoi (HhNkPhieuKtclReq objReq) throws Exception;
    HhNkPhieuKtcl capNhat (HhNkPhieuKtclReq objReq) throws Exception;
    HhNkPhieuKtcl chiTiet (Long id) throws Exception;
    HhNkPhieuKtcl pheDuyet (StatusReq stReq) throws Exception;
    void xoa (IdSearchReq idSearchReq) throws Exception;
    void export(HhNkPhieuKtclSearch searchReq, HttpServletResponse response) throws Exception;
    ReportTemplateResponse preview(HhNkPhieuKtclSearch objReq) throws Exception;
}
