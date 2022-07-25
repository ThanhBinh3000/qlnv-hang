package com.tcdt.qlnvhang.request.bandaugia.bienbanbandaugia;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BhBbBanDauGiaCtReq {
    private Long id;
    private Long stt;
    private String hoTen;
    private String chucVu;
    private String noiCongTac;
    private String loaiTptg;
    private Long bbBanDauGiaId;
}
