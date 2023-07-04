package com.tcdt.qlnvhang.response.suachua;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScPhieuXuatKhoDTO {
    private Long id;
    private Long soQdId;
    private String soQd;
    private Integer nam;
    private LocalDate thoiHanXuatHang;
    private String maDiemKho;
    private String tenDiemKho;
    private String maLoKho;
    private String tenLoKho;
    private String soPhieuXuatKho;
    private LocalDate ngayXuatKho;
    private String soPhieuKdcl;
    private LocalDate ngayGiamDinh;
    private String trangThai;
}
