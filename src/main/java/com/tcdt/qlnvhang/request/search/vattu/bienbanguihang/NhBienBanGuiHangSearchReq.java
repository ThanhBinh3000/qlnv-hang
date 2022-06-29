package com.tcdt.qlnvhang.request.search.vattu.bienbanguihang;

import com.tcdt.qlnvhang.request.BaseRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NhBienBanGuiHangSearchReq extends BaseRequest {
    private String soBienBan;
    private String soQdNhap;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate ngayGuiHangTu;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate ngayGuiHangDen;
    private String maDvi;
    private String loaiVthh;
}
