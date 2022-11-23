package com.tcdt.qlnvhang.request.xuathang.xuattheophuongthucdaugia;

import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class XhQdPdKhBdgPlReq {
    private Long id;
    private Long idQdPdDtl;
    private String maDvi;
    private String tenDvi;
    private String maDiemKho;
    private String maNganKho;
    private String maLoKho;
    private String maDviTsan;
    private Integer soLuong;
    private String DviTinh;
    private BigDecimal giaKhongVat;
    private BigDecimal giaKhoiDiem;
    private BigDecimal tienDatTruoc;
    private Integer soLuongChiTieu;
    private Integer soLuongKh;

    List<XhQdPdKhBdgPlDtlReq> qdPdKhBdgPlDtlReq =new ArrayList<>();
}
