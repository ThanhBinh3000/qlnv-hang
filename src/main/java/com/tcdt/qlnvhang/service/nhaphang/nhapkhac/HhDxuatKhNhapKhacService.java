package com.tcdt.qlnvhang.service.nhaphang.nhapkhac;

import com.tcdt.qlnvhang.entities.nhaphang.nhapkhac.HhDxuatKhNhapKhacHdr;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.nhaphang.nhapkhac.HhDxuatKhNhapKhacDTO;
import com.tcdt.qlnvhang.request.nhaphang.nhapkhac.HhDxuatKhNhapKhacHdrReq;
import com.tcdt.qlnvhang.request.nhaphang.nhapkhac.HhDxuatKhNhapKhacSearch;
import org.springframework.data.domain.Page;

import javax.servlet.http.HttpServletResponse;

public interface HhDxuatKhNhapKhacService {
    Page<HhDxuatKhNhapKhacHdr> timKiem(HhDxuatKhNhapKhacSearch req);
    HhDxuatKhNhapKhacHdr themMoi (HhDxuatKhNhapKhacHdrReq req) throws Exception;
    HhDxuatKhNhapKhacHdr capNhat (HhDxuatKhNhapKhacHdrReq req) throws Exception;
    HhDxuatKhNhapKhacDTO chiTiet (Long id) throws Exception;
    HhDxuatKhNhapKhacHdr pheDuyet(StatusReq stReq) throws Exception;
    void xoa (IdSearchReq idSearchReq) throws Exception;
    void xoaNhieu (IdSearchReq idSearchReq) throws Exception;
    void xuatFile(HhDxuatKhNhapKhacSearch req , HttpServletResponse response);
}
