package com.tcdt.qlnvhang.service.vattu.phieunhapkho;

import com.tcdt.qlnvhang.request.DeleteReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.object.vattu.phieunhapkho.NhPhieuNhapKhoVtReq;
import com.tcdt.qlnvhang.request.search.vattu.phieunhapkho.NhPhieuNhapKhoVtSearchReq;
import com.tcdt.qlnvhang.response.vattu.phieunhapkho.NhPhieuNhapKhoVtRes;
import org.springframework.data.domain.Page;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

public interface NhPhieuNhapKhoVtService {

    NhPhieuNhapKhoVtRes create(NhPhieuNhapKhoVtReq req) throws Exception;

    NhPhieuNhapKhoVtRes update(NhPhieuNhapKhoVtReq req) throws Exception;

    NhPhieuNhapKhoVtRes detail(Long id) throws Exception;

    boolean delete(Long id) throws Exception;


    @Transactional(rollbackOn = Exception.class)
    boolean updateStatusQd(StatusReq req) throws Exception;

    Page<NhPhieuNhapKhoVtRes> search(NhPhieuNhapKhoVtSearchReq req) throws Exception;

    @Transactional(rollbackOn = Exception.class)
    boolean deleteMultiple(DeleteReq req) throws Exception;

    boolean exportToExcel(NhPhieuNhapKhoVtSearchReq objReq, HttpServletResponse response) throws Exception;
}
