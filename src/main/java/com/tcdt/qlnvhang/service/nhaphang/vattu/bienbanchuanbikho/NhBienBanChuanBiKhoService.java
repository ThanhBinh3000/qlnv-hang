package com.tcdt.qlnvhang.service.nhaphang.vattu.bienbanchuanbikho;

import com.tcdt.qlnvhang.request.DeleteReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.object.vattu.bienbanchuanbikho.NhBienBanChuanBiKhoReq;
import com.tcdt.qlnvhang.request.search.vattu.bienbanchuanbikho.NhBienBanChuanBiKhoSearchReq;
import com.tcdt.qlnvhang.response.BaseNhapHangCount;
import com.tcdt.qlnvhang.response.vattu.bienbanchuanbikho.NhBienBanChuanBiKhoRes;
import org.springframework.data.domain.Page;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.util.Set;

public interface NhBienBanChuanBiKhoService {
    NhBienBanChuanBiKhoRes create(NhBienBanChuanBiKhoReq req) throws Exception;

    NhBienBanChuanBiKhoRes update(NhBienBanChuanBiKhoReq req) throws Exception;

    NhBienBanChuanBiKhoRes detail(Long id) throws Exception;

    boolean delete(Long id) throws Exception;


    @Transactional(rollbackOn = Exception.class)
    boolean updateStatusQd(StatusReq req) throws Exception;

    Page<NhBienBanChuanBiKhoRes> search(NhBienBanChuanBiKhoSearchReq req) throws Exception;

    @Transactional(rollbackOn = Exception.class)
    boolean deleteMultiple(DeleteReq req) throws Exception;

    boolean exportToExcel(NhBienBanChuanBiKhoSearchReq objReq, HttpServletResponse response) throws Exception;

    BaseNhapHangCount count(Set<String> maDvis) throws Exception;
}
