package com.tcdt.qlnvhang.request.xuathang.bantructiep.kehoach.dexuat;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class XhDxKhBanTrucTiepDtlReq {

    @ApiModelProperty(notes = "Bắt buộc set đối với update")
    private Long id;

    private Long idHdr;

    private BigDecimal soLuongChiCuc;

    private String maDvi;

    private String diaChi;

    private BigDecimal donGiaVat;

    private BigDecimal soLuongChiTieu;

    private BigDecimal soLuongKh;

    @Transient
    private List<XhDxKhBanTrucTiepDdiemReq> children = new ArrayList<>();

}
