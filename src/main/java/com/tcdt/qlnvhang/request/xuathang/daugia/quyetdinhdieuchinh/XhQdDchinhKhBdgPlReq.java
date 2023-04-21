package com.tcdt.qlnvhang.request.xuathang.daugia.quyetdinhdieuchinh;

import com.tcdt.qlnvhang.entities.xuathang.daugia.quyetdinhdieuchinhbdg.XhQdDchinhKhBdgPlDtl;
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
    @Transient
    private String tenDvi;

    private BigDecimal soLuongChiCuc;

    private String diaChi;

    private String slChiTieu;

    private String slKeHoachDd;

    @Transient
    List<XhQdDchinhKhBdgPlDtlReq> children =new ArrayList<>();

}
