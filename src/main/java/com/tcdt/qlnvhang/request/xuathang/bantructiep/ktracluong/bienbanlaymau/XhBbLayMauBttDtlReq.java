package com.tcdt.qlnvhang.request.xuathang.bantructiep.ktracluong.bienbanlaymau;

import lombok.Data;

@Data
public class XhBbLayMauBttDtlReq {
    private Long id;
    private Long idHdr;
    private String ten;
    private String loai;
    private Integer ma;
    private String chiSoCl;
    private String phuongPhap;
    private Boolean checked;
    private String type;
}