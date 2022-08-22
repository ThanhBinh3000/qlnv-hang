package com.tcdt.qlnvhang.service.bandaugia.thongbaobandaugiakhongthanh;

import com.tcdt.qlnvhang.request.DeleteReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.bandaugia.thongbaobandaugiakhongthanh.BhThongBaoBdgKtReq;
import com.tcdt.qlnvhang.request.bandaugia.thongbaobandaugiakhongthanh.BhThongBaoBdgKtSearchReq;
import com.tcdt.qlnvhang.response.banhangdaugia.thongbaobandaugiakhongthanh.BhThongBaoBdgKtRes;
import org.springframework.data.domain.Page;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

public interface BhThongBaoBdgKtService {
    @Transactional(rollbackOn = Exception.class)
    BhThongBaoBdgKtRes create(BhThongBaoBdgKtReq req) throws Exception;

    @Transactional(rollbackOn = Exception.class)
    BhThongBaoBdgKtRes update(BhThongBaoBdgKtReq req) throws Exception;

    BhThongBaoBdgKtRes detail(Long id) throws Exception;

    @Transactional(rollbackOn = Exception.class)
    boolean delete(Long id) throws Exception;

    @Transactional(rollbackOn = Exception.class)
    boolean updateStatusQd(StatusReq stReq) throws Exception;

    Page<BhThongBaoBdgKtRes> search(BhThongBaoBdgKtSearchReq req) throws Exception;

    @Transactional(rollbackOn = Exception.class)
    boolean deleteMultiple(DeleteReq req) throws Exception;

    boolean exportToExcel(BhThongBaoBdgKtSearchReq objReq, HttpServletResponse response) throws Exception;
}
