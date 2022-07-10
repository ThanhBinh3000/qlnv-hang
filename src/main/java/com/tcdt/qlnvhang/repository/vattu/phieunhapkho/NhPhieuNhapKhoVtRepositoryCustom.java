package com.tcdt.qlnvhang.repository.vattu.phieunhapkho;

import com.tcdt.qlnvhang.request.search.vattu.phieunhapkho.NhPhieuNhapKhoVtSearchReq;

import java.util.List;

public interface NhPhieuNhapKhoVtRepositoryCustom {
    List<Object[]> search(NhPhieuNhapKhoVtSearchReq req);

    int count(NhPhieuNhapKhoVtSearchReq req);
}
