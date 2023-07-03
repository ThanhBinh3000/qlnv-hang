package com.tcdt.qlnvhang.response.suachua;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScBangKeXuatVtDTO {
    private Long id;
    private Long soQdId;
    private String soQdGiaoNv;
    private Integer nam;
    private LocalDate thoiHanXuat;
    private String maDiemKho;
    private String tenDiemKho;
    private String maLoKho;
    private String tenLoKho;
    private Long soPhieuXuatKhoId;
    private String soPhieuXuatKho;
    private String soBangKeCanHang;
    private LocalDate ngayXuatKho;
    private String trangThai;
}
