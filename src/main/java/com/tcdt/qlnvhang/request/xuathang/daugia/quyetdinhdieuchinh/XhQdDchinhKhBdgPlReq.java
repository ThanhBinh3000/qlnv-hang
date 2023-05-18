package com.tcdt.qlnvhang.request.xuathang.daugia.quyetdinhdieuchinh;
import lombok.Data;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class XhQdDchinhKhBdgPlReq {

    private Long id;

    private Long idDcDtl;

    private String maDvi;

    private BigDecimal slChiTieu;

    private BigDecimal slKeHoachDd;

    private BigDecimal soLuongChiCuc;

    private String donViTinh;

    private String diaChi;

    @Transient
    private List<XhQdDchinhKhBdgPlDtlReq> children =new ArrayList<>();

}
