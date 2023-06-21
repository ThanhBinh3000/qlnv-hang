package com.tcdt.qlnvhang.request;

import com.tcdt.qlnvhang.request.nhaphangtheoptt.HhChiTietTTinChaoGiaReq;
import com.tcdt.qlnvhang.request.nhaphangtheoptt.HhQdPdKhMttSlddDtlReq;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhChiTietTTinChaoGia;
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

    private Long idQdDtl;

    private String maDvi;

    private String diaChi;

    private String tenGoiThau;

    private BigDecimal soLuongChiTieu;

    private BigDecimal soLuongKhDd;

    private BigDecimal donGia;

    private BigDecimal donGiaVat;

    private BigDecimal tongSoLuong;

    private BigDecimal tongThanhTien;

    private BigDecimal tongThanhTienVat;

    private BigDecimal soLuong;

    List<HhQdPdKhMttSlddDtlReq> children = new ArrayList<>();
    @Transient
    private List<HhChiTietTTinChaoGiaReq> listChaoGia = new ArrayList<>();

}
