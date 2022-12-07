package com.tcdt.qlnvhang.request;

import com.tcdt.qlnvhang.request.nhaphangtheoptt.HhQdPdKhMttSlddDtlReq;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class HhQdPheduyetKhMttSLDDReq {

    @ApiModelProperty(notes = "Bắt buộc set đối với update")
    private Long id;
    private Long idHdr;
    private String maDvi;

    private String maDiemKho;

    private String diaDiemNhap;

    private BigDecimal soLuongChiTieu;

    private BigDecimal soLuongKhDd;

    private BigDecimal soLuong;

    private BigDecimal donGia;

    private BigDecimal thanhTien;

    private String loaiVthh;

    private String cloaiVthh;

    private BigDecimal donGiaVat;

    private BigDecimal thanhTienVat;

    private BigDecimal tongSoLuong;

    private BigDecimal tongDonGia;

    private BigDecimal tongThanhTien;



    List<HhQdPdKhMttSlddDtlReq> children = new ArrayList<>();

}
