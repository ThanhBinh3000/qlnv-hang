package com.tcdt.qlnvhang.service.nhaphang.quanlyphieunhapkho;

import com.tcdt.qlnvhang.request.DeleteReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.object.quanlyphieunhapkholuongthuc.NhPhieuNhapKhoReq;
import com.tcdt.qlnvhang.request.search.quanlyphieunhapkholuongthuc.NhPhieuNhapKhoSearchReq;
import com.tcdt.qlnvhang.response.BaseNhapHangCount;
import com.tcdt.qlnvhang.response.quanlyphieunhapkholuongthuc.NhPhieuNhapKhoRes;
import org.springframework.data.domain.Page;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

public interface NhPhieuNhapKhoService {
    NhPhieuNhapKhoRes create(NhPhieuNhapKhoReq req) throws Exception;
    NhPhieuNhapKhoRes update(NhPhieuNhapKhoReq req) throws Exception;
    boolean delete(Long id) throws Exception;
    NhPhieuNhapKhoRes detail(Long id) throws Exception;
    boolean updateStatusQd(StatusReq req) throws Exception;

    Page<NhPhieuNhapKhoRes> search(NhPhieuNhapKhoSearchReq req) throws Exception;

    BaseNhapHangCount count() throws Exception;

    @Transactional(rollbackOn = Exception.class)
    boolean deleteMultiple(DeleteReq req) throws Exception;

    boolean exportToExcel(NhPhieuNhapKhoSearchReq objReq, HttpServletResponse response) throws Exception;

    Integer getSo() throws Exception;
}
