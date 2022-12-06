package com.tcdt.qlnvhang.request.kiemtrachatluong;

import lombok.Data;

@Data
public class NhHoSoBienBanCtReq {
    private Long id;
    private Long idHoSoBienBan;
    private String daiDien;
    private String loaiDaiDien;
}
