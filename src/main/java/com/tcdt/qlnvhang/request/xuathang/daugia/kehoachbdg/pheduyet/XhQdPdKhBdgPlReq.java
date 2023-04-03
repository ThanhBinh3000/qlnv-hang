package com.tcdt.qlnvhang.request.xuathang.daugia.kehoachbdg.pheduyet;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class XhQdPdKhBdgPlReq {
    @ApiModelProperty(notes = "Bắt buộc set đối với update")
    private Long id;

    private Long idHdr;

    private String maDvi;

    private BigDecimal soLuongChiCuc;

    private BigDecimal tienDtruocDxChiCuc;

    private BigDecimal tienDtruocDdChiCuc;

    private String diaChi;

    List<XhQdPdKhBdgPlDtlReq> children =new ArrayList<>();
}
