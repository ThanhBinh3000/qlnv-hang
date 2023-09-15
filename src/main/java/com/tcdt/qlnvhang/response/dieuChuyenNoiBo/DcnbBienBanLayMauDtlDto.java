package com.tcdt.qlnvhang.response.dieuChuyenNoiBo;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class DcnbBienBanLayMauDtlDto {
    private int stt;
    private String loaiDaiDien;
    private String tenDaiDien;
}
