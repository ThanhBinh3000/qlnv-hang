package com.tcdt.qlnvhang.response.suachua;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScPhieuNhapKhoDTO {
    private Long id;
    private Long qdGiaoNvNhapId;
    private String soQdGiaoNvNhap;
    private Integer nam;
    private LocalDate thoiGianNhapHang;
    private String diemKho;
    private String loKho;
    private String soPhieuNhapKho;
    private LocalDate ngayNhapKho;
    private Long bangKeNhapVtId;
    private String soBangKeNhapVt;
    private Long bbGiaoNhanId;
    private String soBbGiaoNhan;
    private String trangThai;

}
