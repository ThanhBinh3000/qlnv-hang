package com.tcdt.qlnvhang.request.xuathang.quyetdinhgiaonhiemvuxuat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class XhQdGiaoNvuXuatCtReq {
    private Long id;

    private Integer stt;
    private String maDvi;
    private String maVatTuCha;
    private String maVatTu;
    private String maDiemKho;
    private String maNhaKho;
    private String maNganKho;
    private String maNganLo;
    private String donViTinh;
    private BigDecimal soLuong;
    private BigDecimal donGiaKhongThue;
    private BigDecimal thanhTien;
    private LocalDate thoiHanXuatBan;
    private Long qdgnvxId;
}
