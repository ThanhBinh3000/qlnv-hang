package com.tcdt.qlnvhang.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CommonResponse {
    private String trangThai;
    private String tenTrangThai;
    private String trangThaiDuyet;

    private String maDvi;
    private String tenDvi;
    private String maQhns;
    private String lyDoTuChoi;

    private Integer so;
    private Integer nam;

    private String maVatTuCha;
    private String maVatTu;

    private String tenVatTuCha;
    private String tenVatTu;
}
