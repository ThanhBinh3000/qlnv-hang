package com.tcdt.qlnvhang.repository.vattu;

import com.tcdt.qlnvhang.request.search.vattu.phieunhapkhotamgui.NhPhieuNhapKhoTamGuiSearchReq;

import java.util.List;

public interface NhPhieuNhapKhoTamGuiRepositoryCustom {
    List<Object[]> search(NhPhieuNhapKhoTamGuiSearchReq req);

    int count(NhPhieuNhapKhoTamGuiSearchReq req);
}
