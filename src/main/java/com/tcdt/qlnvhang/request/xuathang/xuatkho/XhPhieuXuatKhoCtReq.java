package com.tcdt.qlnvhang.request.xuathang.xuatkho;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class XhPhieuXuatKhoCtReq {
    private Long id;
    private Long pxuatKhoId;
    private String chungTu;
    private String tenFile;
    private String ghiChu;
}
