package com.tcdt.qlnvhang.service.nhaphang.nhapkhac;

import com.tcdt.qlnvhang.entities.nhaphang.nhapkhac.nvnhap.HhQdGiaoNvuNhapHangKhacHdr;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.nhaphang.nhapkhac.HhQdGiaoNvuNhapKhacHdrReq;
import com.tcdt.qlnvhang.request.nhaphang.nhapkhac.HhQdGiaoNvuNhapKhacSearch;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;
import org.springframework.data.domain.Page;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface HhQdGiaoNvNhapKhacService {
    Page<HhQdGiaoNvuNhapHangKhacHdr> timKiem(HhQdGiaoNvuNhapKhacSearch req);
    Page<HhQdGiaoNvuNhapHangKhacHdr> dsQdNvuDuocLapBb(HhQdGiaoNvuNhapKhacSearch req);
    HhQdGiaoNvuNhapHangKhacHdr themMoi (HhQdGiaoNvuNhapKhacHdrReq req) throws Exception;
    HhQdGiaoNvuNhapHangKhacHdr capNhat (HhQdGiaoNvuNhapKhacHdrReq req) throws Exception;
    HhQdGiaoNvuNhapHangKhacHdr chiTiet (Long id) throws Exception;
    HhQdGiaoNvuNhapHangKhacHdr pheDuyet(StatusReq stReq) throws Exception;
    void xoa (IdSearchReq idSearchReq) throws Exception;
    void xoaNhieu (IdSearchReq idSearchReq) throws Exception;
    void xuatFile(HhQdGiaoNvuNhapKhacSearch req , HttpServletResponse response) throws Exception;
    void xuatFileBbLm(HhQdGiaoNvuNhapKhacSearch req , HttpServletResponse response) throws Exception;
    void xuatFilePkncl(HhQdGiaoNvuNhapKhacSearch req , HttpServletResponse response) throws Exception;

    ReportTemplateResponse preview(HhQdGiaoNvuNhapKhacSearch objReq) throws Exception;
}
