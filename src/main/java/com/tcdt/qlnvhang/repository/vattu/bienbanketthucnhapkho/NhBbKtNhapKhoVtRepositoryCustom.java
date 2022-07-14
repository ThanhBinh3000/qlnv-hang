package com.tcdt.qlnvhang.repository.vattu.bienbanketthucnhapkho;

import com.tcdt.qlnvhang.request.search.vattu.bienbanketthucnhapkho.NhBbKtNhapKhoVtSearchReq;

import java.util.List;

public interface NhBbKtNhapKhoVtRepositoryCustom  {

    List<Object[]> search(NhBbKtNhapKhoVtSearchReq req);

    int count(NhBbKtNhapKhoVtSearchReq req);
}
