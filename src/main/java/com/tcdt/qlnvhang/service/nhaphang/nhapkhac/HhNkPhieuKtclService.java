package com.tcdt.qlnvhang.service.nhaphang.nhapkhac;

import com.tcdt.qlnvhang.entities.nhaphang.nhapkhac.HhNkPhieuKtcl;
import com.tcdt.qlnvhang.entities.nhaphang.nhapkhac.nvnhap.HhQdGiaoNvuNhapHangKhacHdr;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.nhaphang.nhapkhac.HhNkPhieuKtclReq;
import com.tcdt.qlnvhang.request.nhaphang.nhapkhac.HhNkPhieuKtclSearch;
import org.springframework.data.domain.Page;

public interface HhNkPhieuKtclService {
    Page<HhQdGiaoNvuNhapHangKhacHdr> timKiem(HhNkPhieuKtclSearch req) throws Exception;
    HhNkPhieuKtcl themMoi (HhNkPhieuKtclReq objReq) throws Exception;
    HhNkPhieuKtcl capNhat (HhNkPhieuKtclReq objReq) throws Exception;
    HhNkPhieuKtcl chiTiet (Long id) throws Exception;
    HhNkPhieuKtcl pheDuyet (StatusReq stReq) throws Exception;
    void xoa (IdSearchReq idSearchReq) throws Exception;
}
