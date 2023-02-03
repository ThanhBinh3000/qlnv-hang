package com.tcdt.qlnvhang.request.xuathang.bantructiep.kehoach.dexuat;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class XhDxKhBanTrucTiepDdiemReq {
    @ApiModelProperty(notes = "Bắt buộc set đối với update")
    private Long id;

    private Long idDtl;

    private String maDiemKho;

    private String maNhaKho;

    private String maNganKho;

    private String maLoKho;

    private String maDviTsan;

    private BigDecimal duDau;

    private BigDecimal soLuong;

    private BigDecimal donGiaDeXuat;

    private BigDecimal donGiaVat;

    private String dviTinh;
}
