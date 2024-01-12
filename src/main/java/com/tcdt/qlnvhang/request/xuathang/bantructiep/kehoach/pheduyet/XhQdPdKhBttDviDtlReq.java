package com.tcdt.qlnvhang.request.xuathang.bantructiep.kehoach.pheduyet;

import com.tcdt.qlnvhang.request.xuathang.bantructiep.tochuctrienkhai.thongtin.XhTcTtinBttReq;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class XhQdPdKhBttDviDtlReq {
    private Long id;
    private Long idDvi;
    private String maDiemKho;
    private String maNhaKho;
    private String maNganKho;
    private String maLoKho;
    private String maDviTsan;
    private BigDecimal tonKho;
    private BigDecimal soLuongDeXuat;
    private String donViTinh;
    private BigDecimal donGiaDeXuat;
    private BigDecimal donGiaDuocDuyet;
    private BigDecimal thanhTienDeXuat;
    private BigDecimal thanhTienDuocDuyet;
    private String loaiVthh;
    private String cloaiVthh;
    private Integer namNhap;
    private List<XhTcTtinBttReq> children = new ArrayList<>();
}