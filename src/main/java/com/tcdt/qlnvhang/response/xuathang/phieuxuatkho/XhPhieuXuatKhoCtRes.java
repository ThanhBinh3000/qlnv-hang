package com.tcdt.qlnvhang.response.xuathang.phieuxuatkho;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class XhPhieuXuatKhoCtRes {
    private Long id;
    private Long pxuatKhoId;
    private String chungTu;
    private String tenFile;
    private String ghiChu;
}
