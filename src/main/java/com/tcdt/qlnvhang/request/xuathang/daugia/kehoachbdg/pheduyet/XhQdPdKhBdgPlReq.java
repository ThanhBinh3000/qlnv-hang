package com.tcdt.qlnvhang.request.xuathang.daugia.kehoachbdg.pheduyet;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class XhQdPdKhBdgPlReq {
    private Long id;
    private Long idQdDtl;
    private String maDvi;
    private BigDecimal slChiTieu;
    private BigDecimal slKeHoachDd;
    private BigDecimal soLuongChiCuc;
    private BigDecimal tienDatTruocDx;
    private String donViTinh;
    private String diaChi;
    List<XhQdPdKhBdgPlDtlReq> children =new ArrayList<>();
}
