package com.tcdt.qlnvhang.response.dieuChuyenNoiBo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class DcnbBangKeNhapVTHdrDTO {
    private Long id;
    private Long qDinhDcId;
    private String soQdinh;
    private Integer nam;
    private String maDiemKho;
    private String tenDiemKho;
    private String maNhaKho;
    private String tenNhaKho;
    private String maNganKho;
    private String tenNganKho;
    private String maLoKho;
    private String tenLoKho;
    private Long bBLayMauId;
    private String soBBLayMau;
    private String soBangKe;
    private String soBBGuiHang;
    private String soPhieuNhapKho;
    private Long phieuNhapKhoId;
    private LocalDate ngayNhapKho;
    private String trangThai;
    private String tenTrangThai;
}
