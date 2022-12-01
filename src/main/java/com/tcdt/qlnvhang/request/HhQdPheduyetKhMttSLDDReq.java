package com.tcdt.qlnvhang.request;

import com.tcdt.qlnvhang.request.nhaphangtheoptt.HhQdPdKhMttSlddDtlReq;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class HhQdPheduyetKhMttSLDDReq {

    @ApiModelProperty(notes = "Bắt buộc set đối với update")
    private Long id;
    private Long idHdr;
    private BigDecimal soLuong;

    private String maDvi;
    private BigDecimal donGiaVat;
    private BigDecimal thanhTien;
    private BigDecimal donGiaTamTinh;

    private String loaiVthh;
    private String cloaiVthh;

    private String maDiemKho;
    private String diaDiemNhap;
    private BigDecimal soLuongChiTieu;
    private BigDecimal soLuongKhDd;


    List<HhQdPdKhMttSlddDtlReq> children = new ArrayList<>();

}
