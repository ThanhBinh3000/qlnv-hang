package com.tcdt.qlnvhang.request;

import com.tcdt.qlnvhang.request.nhaphangtheoptt.HhChiTietKqTTinChaoGiaReq;
import com.tcdt.qlnvhang.request.nhaphangtheoptt.HhChiTietTTinChaoGiaReq;
import com.tcdt.qlnvhang.request.nhaphangtheoptt.HhQdPdKhMttSlddDtlReq;
import com.tcdt.qlnvhang.request.nhaphangtheoptt.HhQdPdKqMttSlddDtlReq;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class HhQdPheduyetKqMttSLDDReq {

    @ApiModelProperty(notes = "Bắt buộc set đối với update")
    private Long id;

    private Long idQdPdKq;

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
    private BigDecimal soLuongHd;

    List<HhQdPdKqMttSlddDtlReq> children = new ArrayList<>();
    @Transient
    private List<HhChiTietKqTTinChaoGiaReq> listChaoGia = new ArrayList<>();

}
