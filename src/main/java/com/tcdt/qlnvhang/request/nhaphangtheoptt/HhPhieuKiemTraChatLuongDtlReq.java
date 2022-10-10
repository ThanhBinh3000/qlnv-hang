package com.tcdt.qlnvhang.request.nhaphangtheoptt;

import lombok.Data;

@Data
public class HhPhieuKiemTraChatLuongDtlReq {
    private Long id;
    private Long idHdr;
    private String chiTieuCl;
    private String chiSoCl;
    private String ketQuaPt;
    private String phuongPhap;
}
