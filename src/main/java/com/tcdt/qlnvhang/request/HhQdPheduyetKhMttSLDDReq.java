package com.tcdt.qlnvhang.request;

import com.tcdt.qlnvhang.request.nhaphangtheoptt.HhQdPdKhMttSlddDtlReq;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class HhQdPheduyetKhMttSLDDReq {

    @ApiModelProperty(notes = "bắt buộc set phải đối với updata")
    private Long id;
    private Long idDxKhmtt;
    private String maDvi;
    private String tenDvi;
    private String maDiemKho;
    private String diaDiemKho;
    private BigDecimal soLuongCtieu;
    private BigDecimal soLuongKhDd;
    private BigDecimal soLuongDxmtt;
    private BigDecimal donGiaVat;
    private BigDecimal thanhTien;

    List<HhQdPdKhMttSlddDtlReq> listQdPdSlddDtl = new ArrayList<>();
}
