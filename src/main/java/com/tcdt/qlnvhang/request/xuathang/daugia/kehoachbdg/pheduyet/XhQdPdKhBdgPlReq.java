package com.tcdt.qlnvhang.request.xuathang.daugia.kehoachbdg.pheduyet;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class XhQdPdKhBdgPlReq {
    @ApiModelProperty(notes = "Bắt buộc set đối với update")
    private Long id;

    private Long idHdr;

    private String maDvi;

    private String slChiTieu;

    private String slKeHoachDd;

    private BigDecimal soLuongChiCuc;

    private String donViTinh;

    private String diaChi;

    List<XhQdPdKhBdgPlDtlReq> children =new ArrayList<>();
}
