package com.tcdt.qlnvhang.request.search.vattu.phieunhapkhotamgui;

import com.tcdt.qlnvhang.request.BaseRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NhPhieuNhapKhoTamGuiSearchReq extends BaseRequest {
    private String soPhieu;
    private String soQdNhap;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate ngayNhapKhoTu;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate ngayNhapKhoDen;
    private String loaiVthh;
}
