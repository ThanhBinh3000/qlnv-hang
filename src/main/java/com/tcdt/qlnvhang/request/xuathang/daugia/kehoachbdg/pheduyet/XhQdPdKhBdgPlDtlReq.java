package com.tcdt.qlnvhang.request.xuathang.daugia.kehoachbdg.pheduyet;

import lombok.Data;

import javax.persistence.Transient;
import java.math.BigDecimal;

@Data
public class XhQdPdKhBdgPlDtlReq {
    private Long id;

    private Long idPhanLo;

    private String maDiemKho;

    private String maNhaKho;

    private String maNganKho;

    private String maLoKho;

    private String maDviTsan;

    private BigDecimal tonKho;

    private BigDecimal soLuongDeXuat;

    private BigDecimal donGiaDeXuat;

    private String donViTinh;

    private String loaiVthh;

    private String cloaiVthh;

}
