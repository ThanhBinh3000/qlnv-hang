package com.tcdt.qlnvhang.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class HhQdPheduyetKhMttSLDDReq {

    @ApiModelProperty(notes = "bắt buộc set phải đối với updata")
    private Long id;
    Long idQdKhmtt;
    private String maDvi;
    private String tenDvi;
    private  String maDiemKho;
    private String diaDiemKho;
    private BigDecimal slChitieuKh;
    private BigDecimal slTheoKhdd;
    private BigDecimal slDxMuatt;
    private BigDecimal thanhTien;
    private  BigDecimal tongSl;
}
