package com.tcdt.qlnvhang.service.quanlyphieunhapkholuongthuc;

import com.tcdt.qlnvhang.entities.quanlyphieunhapkholuongthuc.QlPhieuNhapKhoLt;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.object.quanlyphieunhapkholuongthuc.QlPhieuNhapKhoLtReq;
import com.tcdt.qlnvhang.request.search.quanlyphieunhapkholuongthuc.QlPhieuNhapKhoLtSearchReq;
import com.tcdt.qlnvhang.response.quanlyphieunhapkholuongthuc.QlPhieuNhapKhoLtRes;
import org.springframework.data.domain.Page;

public interface QlPhieuNhapKhoLtService {
    QlPhieuNhapKhoLtRes create(QlPhieuNhapKhoLtReq req) throws Exception;
    QlPhieuNhapKhoLtRes update(QlPhieuNhapKhoLtReq req) throws Exception;
    boolean delete(Long id) throws Exception;
    QlPhieuNhapKhoLtRes detail(Long id) throws Exception;
    boolean updateStatusQd(StatusReq req) throws Exception;

    Page<QlPhieuNhapKhoLtRes> search(QlPhieuNhapKhoLtSearchReq req) throws Exception;

    Page<QlPhieuNhapKhoLt> timKiem(QlPhieuNhapKhoLtSearchReq req) throws Exception;
}
