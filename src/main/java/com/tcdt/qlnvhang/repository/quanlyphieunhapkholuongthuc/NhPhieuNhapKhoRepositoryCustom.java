package com.tcdt.qlnvhang.repository.quanlyphieunhapkholuongthuc;

import com.tcdt.qlnvhang.request.search.quanlyphieunhapkholuongthuc.NhPhieuNhapKhoSearchReq;
import com.tcdt.qlnvhang.response.quanlyphieunhapkholuongthuc.NhPhieuNhapKhoRes;
import org.springframework.data.domain.Page;

public interface NhPhieuNhapKhoRepositoryCustom {
    Page<NhPhieuNhapKhoRes> search(NhPhieuNhapKhoSearchReq req);

    int count(NhPhieuNhapKhoSearchReq req);
}
