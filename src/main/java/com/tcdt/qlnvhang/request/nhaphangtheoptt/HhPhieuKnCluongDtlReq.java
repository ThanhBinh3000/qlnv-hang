package com.tcdt.qlnvhang.request.nhaphangtheoptt;

import lombok.Data;

@Data
public class HhPhieuKnCluongDtlReq {
    private Long id;
    private Long idHdr;
    private String ctieuCl;
    private String soCtieuCl;
    private String ketQuaPt;
    private String phuongPhap;
}
