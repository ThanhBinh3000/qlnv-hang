package com.tcdt.qlnvhang.request.bandaugia.bienbanlaymau;

import lombok.Data;

@Data
public class XhBbLayMauCtRequest {
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
