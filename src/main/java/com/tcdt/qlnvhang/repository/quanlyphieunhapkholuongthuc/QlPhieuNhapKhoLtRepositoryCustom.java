package com.tcdt.qlnvhang.repository.quanlyphieunhapkholuongthuc;

import com.tcdt.qlnvhang.request.search.quanlyphieunhapkholuongthuc.QlPhieuNhapKhoLtSearchReq;
import com.tcdt.qlnvhang.response.quanlyphieunhapkholuongthuc.QlPhieuNhapKhoLtRes;
import org.springframework.data.domain.Page;

public interface QlPhieuNhapKhoLtRepositoryCustom {
    Page<QlPhieuNhapKhoLtRes> search(QlPhieuNhapKhoLtSearchReq req);
}
