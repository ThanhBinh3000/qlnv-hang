package com.tcdt.qlnvhang.request.search.xuathang;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class XhBienBanTinhKhoSearchReq {
    private String soQuyetDinh;
    private String soBienBan;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate ngayXuatTu;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate ngayXuatDen;
}
