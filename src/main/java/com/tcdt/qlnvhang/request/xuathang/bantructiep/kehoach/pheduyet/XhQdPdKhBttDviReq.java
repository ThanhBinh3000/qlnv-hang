package com.tcdt.qlnvhang.request.xuathang.bantructiep.kehoach.pheduyet;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class XhQdPdKhBttDviReq {
    @ApiModelProperty(notes = "Bắt buộc set đối với update")
    private Long id;

    private Long idDtl;

    private BigDecimal soLuongChiCuc;

    private String maDvi;

    private String diaChi;

    private BigDecimal soLuongChiTieu;

    private BigDecimal soLuongKhDaDuyet;

    private String donViTinh;

    List<XhQdPdKhBttDviDtlReq> children =new ArrayList<>();
}
