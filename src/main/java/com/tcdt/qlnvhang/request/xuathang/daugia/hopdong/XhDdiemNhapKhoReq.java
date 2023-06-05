package com.tcdt.qlnvhang.request.xuathang.daugia.hopdong;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Data
public class XhDdiemNhapKhoReq {

    private Long id;

    private Long idDtl;

    private String maDiemKho;

    private String diaChi;

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
