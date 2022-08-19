package com.tcdt.qlnvhang.service.nhaphang.vattu.bienbanguihang;

import com.tcdt.qlnvhang.request.DeleteReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.object.vattu.bienbanguihang.NhBienBanGuiHangReq;
import com.tcdt.qlnvhang.request.search.vattu.bienbanguihang.NhBienBanGuiHangSearchReq;
import com.tcdt.qlnvhang.response.BaseNhapHangCount;
import com.tcdt.qlnvhang.response.vattu.bienbanguihang.NhBienBanGuiHangRes;
import org.springframework.data.domain.Page;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.util.Set;

public interface NhBienBanGuiHangService {
    NhBienBanGuiHangRes create(NhBienBanGuiHangReq req) throws Exception;

    NhBienBanGuiHangRes update(NhBienBanGuiHangReq req) throws Exception;

    NhBienBanGuiHangRes detail(Long id) throws Exception;

    boolean delete(Long id) throws Exception;


    @Transactional(rollbackOn = Exception.class)
    boolean updateStatusQd(StatusReq req) throws Exception;

    Page<NhBienBanGuiHangRes> search(NhBienBanGuiHangSearchReq req) throws Exception;

    @Transactional(rollbackOn = Exception.class)
    boolean deleteMultiple(DeleteReq req) throws Exception;

    boolean exportToExcel(NhBienBanGuiHangSearchReq objReq, HttpServletResponse response) throws Exception;

    Integer getSo() throws Exception;

    BaseNhapHangCount count(Set<String> maDvis) throws Exception;
}
