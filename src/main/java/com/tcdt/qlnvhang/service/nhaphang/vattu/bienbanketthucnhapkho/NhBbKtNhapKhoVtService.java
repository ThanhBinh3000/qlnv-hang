package com.tcdt.qlnvhang.service.nhaphang.vattu.bienbanketthucnhapkho;

import com.tcdt.qlnvhang.request.DeleteReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.object.vattu.bienbanketthucnhapkho.NhBbKtNhapKhoVtReq;
import com.tcdt.qlnvhang.request.search.vattu.bienbanketthucnhapkho.NhBbKtNhapKhoVtSearchReq;
import com.tcdt.qlnvhang.response.vattu.bienbanketthucnhapkho.NhBbKtNhapKhoVtRes;
import org.springframework.data.domain.Page;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

public interface NhBbKtNhapKhoVtService {
    NhBbKtNhapKhoVtRes create(NhBbKtNhapKhoVtReq req) throws Exception;

    NhBbKtNhapKhoVtRes update(NhBbKtNhapKhoVtReq req) throws Exception;

    NhBbKtNhapKhoVtRes detail(Long id) throws Exception;

    boolean delete(Long id) throws Exception;


    @Transactional(rollbackOn = Exception.class)
    boolean updateStatusQd(StatusReq req) throws Exception;

    Page<NhBbKtNhapKhoVtRes> search(NhBbKtNhapKhoVtSearchReq req) throws Exception;

    @Transactional(rollbackOn = Exception.class)
    boolean deleteMultiple(DeleteReq req) throws Exception;

    boolean exportToExcel(NhBbKtNhapKhoVtSearchReq objReq, HttpServletResponse response) throws Exception;
}
