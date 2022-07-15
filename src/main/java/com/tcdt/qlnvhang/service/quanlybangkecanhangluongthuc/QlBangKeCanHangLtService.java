package com.tcdt.qlnvhang.service.quanlybangkecanhangluongthuc;

import com.tcdt.qlnvhang.request.DeleteReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.object.quanlybangkecanhangluongthuc.QlBangKeCanHangLtReq;
import com.tcdt.qlnvhang.request.search.quanlybangkecanhangluongthuc.QlBangKeCanHangLtSearchReq;
import com.tcdt.qlnvhang.response.BaseNhapHangCount;
import com.tcdt.qlnvhang.response.quanlybangkecanhangluongthuc.QlBangKeCanHangLtRes;
import org.springframework.data.domain.Page;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

public interface QlBangKeCanHangLtService {

    QlBangKeCanHangLtRes create(QlBangKeCanHangLtReq req) throws Exception;

    QlBangKeCanHangLtRes update(QlBangKeCanHangLtReq req) throws Exception;

    QlBangKeCanHangLtRes detail(Long id) throws Exception;

    boolean delete(Long id) throws Exception;


    @Transactional(rollbackOn = Exception.class)
    boolean updateStatusQd(StatusReq req) throws Exception;

    Page<QlBangKeCanHangLtRes> search(QlBangKeCanHangLtSearchReq req) throws Exception;

    BaseNhapHangCount count() throws Exception;

    @Transactional(rollbackOn = Exception.class)
    boolean deleteMultiple(DeleteReq req) throws Exception;

    boolean exportToExcel(QlBangKeCanHangLtSearchReq objReq, HttpServletResponse response) throws Exception;

    Integer getSo() throws Exception;
}
