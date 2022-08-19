package com.tcdt.qlnvhang.service.nhaphang.vattu.phieunhapkhotamgui;

import com.tcdt.qlnvhang.request.DeleteReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.object.vattu.phieunhapkhotamgui.NhPhieuNhapKhoTamGuiReq;
import com.tcdt.qlnvhang.request.search.vattu.phieunhapkhotamgui.NhPhieuNhapKhoTamGuiSearchReq;
import com.tcdt.qlnvhang.response.BaseNhapHangCount;
import com.tcdt.qlnvhang.response.vattu.phieunhapkhotamgui.NhPhieuNhapKhoTamGuiRes;
import org.springframework.data.domain.Page;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.util.Set;

public interface NhPhieuNhapKhoTamGuiService {

    NhPhieuNhapKhoTamGuiRes create(NhPhieuNhapKhoTamGuiReq req) throws Exception;

    NhPhieuNhapKhoTamGuiRes update(NhPhieuNhapKhoTamGuiReq req) throws Exception;

    NhPhieuNhapKhoTamGuiRes detail(Long id) throws Exception;

    boolean delete(Long id) throws Exception;


    @Transactional(rollbackOn = Exception.class)
    boolean updateStatusQd(StatusReq req) throws Exception;

    Page<NhPhieuNhapKhoTamGuiRes> search(NhPhieuNhapKhoTamGuiSearchReq req) throws Exception;

    @Transactional(rollbackOn = Exception.class)
    boolean deleteMultiple(DeleteReq req) throws Exception;

    boolean exportToExcel(NhPhieuNhapKhoTamGuiSearchReq objReq, HttpServletResponse response) throws Exception;

    Integer getSo() throws Exception;

    BaseNhapHangCount count(Set<String> maDvis) throws Exception;
}
