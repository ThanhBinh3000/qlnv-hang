package com.tcdt.qlnvhang.request.search.quanlybienbannhapdaykholuongthuc;

import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.request.search.BaseSearchRequest;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class QlBienBanNhapDayKhoLtSearchReq extends BaseRequest {
    private String soBienBan;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate ngayNhapDayKhoTu;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate ngayNhapDayKhoDen;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate ngayBatDauNhap;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate ngayKetThucNhap;
    private String maDiemKho;
    private String maNhaKho;
    private String maKhoNganLo;
    private String kyThuatVien;
    private String maDonViLap;
    private String maHang;
    private String maDonVi;
    private LocalDate tuNgay;
    private LocalDate denNgay;
}
