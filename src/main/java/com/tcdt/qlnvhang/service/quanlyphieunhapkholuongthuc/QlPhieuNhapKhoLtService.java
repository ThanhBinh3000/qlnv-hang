package com.tcdt.qlnvhang.service.quanlyphieunhapkholuongthuc;

import com.tcdt.qlnvhang.request.DeleteReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.object.quanlyphieunhapkholuongthuc.QlPhieuNhapKhoLtReq;
import com.tcdt.qlnvhang.request.search.quanlyphieunhapkholuongthuc.QlPhieuNhapKhoLtSearchReq;
import com.tcdt.qlnvhang.response.BaseNhapHangCount;
import com.tcdt.qlnvhang.response.SoBienBanPhieuRes;
import com.tcdt.qlnvhang.response.quanlyphieunhapkholuongthuc.QlPhieuNhapKhoLtRes;
import org.springframework.data.domain.Page;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

public interface QlPhieuNhapKhoLtService {
    QlPhieuNhapKhoLtRes create(QlPhieuNhapKhoLtReq req) throws Exception;
    QlPhieuNhapKhoLtRes update(QlPhieuNhapKhoLtReq req) throws Exception;
    boolean delete(Long id) throws Exception;
    QlPhieuNhapKhoLtRes detail(Long id) throws Exception;
    boolean updateStatusQd(StatusReq req) throws Exception;

    Page<QlPhieuNhapKhoLtRes> search(QlPhieuNhapKhoLtSearchReq req) throws Exception;

    BaseNhapHangCount count() throws Exception;

    @Transactional(rollbackOn = Exception.class)
    boolean deleteMultiple(DeleteReq req) throws Exception;

    boolean exportToExcel(QlPhieuNhapKhoLtSearchReq objReq, HttpServletResponse response) throws Exception;

    SoBienBanPhieuRes getSo() throws Exception;
}
