package com.tcdt.qlnvhang.response.dieuChuyenNoiBo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class DcnbBangKeXuatVTHdrDTO {
    private Long id;
    private Long qDinhDccId;
    private String soQdinh;
    private Integer namKh;
    private LocalDate thoiHanDieuChuyen;
    private String maNhaKho;
    private String tenNhaKho;
    private String maDiemKho;
    private String tenDiemKho;
    private String maloKho;
    private String tenloKho;
    private String maNganKho;
    private String tenNganKho;

    private String soPhieuXuat;
    private String soBangKe;
    private LocalDate ngayXuatKho;
    private String trangThai;
    private String tenTrangThai;
}
