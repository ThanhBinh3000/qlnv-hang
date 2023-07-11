package com.tcdt.qlnvhang.response.suachua;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScKiemTraChatLuongDTO {
    private Long id;
    private Long soQdGiaoNvId;
    private String soQdGiaoNv;
    private Integer nam;
    private LocalDate thoiGianXuatSc;
    private String maDiemKho;
    private String tenDiemKho;
    private String maLoKho;
    private String tenLoKho;
    private String soPhieuKdcl;
    private LocalDate ngayKiemDinh;
    private String trangThai;
}
