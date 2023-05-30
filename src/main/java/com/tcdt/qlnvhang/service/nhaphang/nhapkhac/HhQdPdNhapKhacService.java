package com.tcdt.qlnvhang.service.nhaphang.nhapkhac;

import com.tcdt.qlnvhang.entities.nhaphang.nhapkhac.qdpdnk.HhQdPdNhapKhacHdr;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.nhaphang.nhapkhac.HhDxuatKhNhapKhacDTO;
import com.tcdt.qlnvhang.request.nhaphang.nhapkhac.HhQdPdNhapKhacDTO;
import com.tcdt.qlnvhang.request.nhaphang.nhapkhac.HhQdPdNhapKhacHdrReq;
import com.tcdt.qlnvhang.request.nhaphang.nhapkhac.HhQdPdNhapKhacSearch;
import org.springframework.data.domain.Page;

import javax.servlet.http.HttpServletResponse;

public interface HhQdPdNhapKhacService {
    Page<HhQdPdNhapKhacHdr> timKiem(HhQdPdNhapKhacSearch req);
    HhQdPdNhapKhacHdr themMoi (HhQdPdNhapKhacHdrReq req) throws Exception;
    HhQdPdNhapKhacHdr capNhat (HhQdPdNhapKhacHdrReq req) throws Exception;
    HhQdPdNhapKhacHdr chiTiet (Long id) throws Exception;
    HhQdPdNhapKhacHdr pheDuyet(StatusReq stReq) throws Exception;
    void xoa (IdSearchReq idSearchReq) throws Exception;
    void xoaNhieu (IdSearchReq idSearchReq) throws Exception;
    void xuatFile(HhQdPdNhapKhacSearch req , HttpServletResponse response);
}
