package com.tcdt.qlnvhang.response.dieuChuyenNoiBo;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class DcnbBbChuanBiKhoDtlThucHienDto {
    private int stt;
    private String noiDung;
    private String matHang;
    private String dviTinh;
    private BigDecimal soLuongTrongNam;
    private BigDecimal donGiaTrongNam;
    private BigDecimal thanhTienTrongNam;
    private Double soLuongNamTruoc;
    private BigDecimal thanhTienNamTruoc;
    private BigDecimal tongGiaTri;
    private String type;
}
