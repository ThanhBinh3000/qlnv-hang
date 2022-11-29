package com.tcdt.qlnvhang.request.khoahoccongnghebaoquan;

import lombok.Data;

@Data
public class KhCnNghiemThuThanhLyDtlReq {
    private Long id;
    private Long idNghiemThu;
    private String hoTen;
    private String donVi;
}
