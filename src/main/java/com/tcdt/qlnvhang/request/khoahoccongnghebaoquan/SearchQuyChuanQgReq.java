package com.tcdt.qlnvhang.request.khoahoccongnghebaoquan;

import com.tcdt.qlnvhang.request.BaseRequest;
import lombok.Data;

import java.time.LocalDate;

@Data
public class SearchQuyChuanQgReq extends BaseRequest {
    private Long id;
    private String soVanBan;
    private String soHieuQuyChuan;
    private String loaiVthh;
    private String cloaiVthh;
    private String trichYeu;
    private LocalDate ngayKyTu;
    private LocalDate ngayKyDen;
    private LocalDate ngayHieuLucTu;
    private LocalDate ngayHieuLucDen;
    private String maDvi;
}
