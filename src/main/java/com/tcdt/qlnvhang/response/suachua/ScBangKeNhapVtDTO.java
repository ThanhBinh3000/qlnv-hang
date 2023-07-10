package com.tcdt.qlnvhang.response.suachua;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScBangKeNhapVtDTO {
    private Long id;
    private Long qdNhapHangId;
    private String soQdNhapHang;
    private Integer nam;
    private LocalDate thoiHanNhapHang;
    private String maLoKho;
    private String tenLoKho;
    private String maDiemKho;
    private String tenDiemKho;
//    private Long bbLayMauId;
//    private String soBbLayMau;
    private String soBangKeNvt;
    private Long phieuNhapKhoId;
    private String soPhieuNhapKho;
    private LocalDate ngayNhapKho;
    private String trangThai;

}
