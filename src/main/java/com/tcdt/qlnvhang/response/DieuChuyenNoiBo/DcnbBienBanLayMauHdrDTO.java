package com.tcdt.qlnvhang.response.DieuChuyenNoiBo;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class DcnbBienBanLayMauHdrDTO {
    private Long qDinhDccId;
    private String soQdinh;
    private LocalDate ngaylayMau;
    private String maloKho;

    public DcnbBienBanLayMauHdrDTO(Long qDinhDccId, String soQdinh , LocalDate ngaylayMau) {
        this.qDinhDccId = qDinhDccId;
        this.soQdinh = soQdinh;
        this.ngaylayMau = ngaylayMau;
    }

    public DcnbBienBanLayMauHdrDTO(String maloKho) {
       this.maloKho = maloKho;
    }
}
