package com.tcdt.qlnvhang.entities;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "KT_TRANGTHAI_HIENTHOI")
public class KtTrangThaiHienThoi {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "KT_TRANGTHAI_HIENTHOI_SEQ")
    @SequenceGenerator(sequenceName = "KT_TRANGTHAI_HIENTHOI_SEQ", allocationSize = 1, name = "KT_TRANGTHAI_HIENTHOI_SEQ")
    private Long id;
    private String maDonVi;
    private String maVthh;
    private String slHienThoi;
    private String nam;
    private String tenDonVi;
    private String tenVthh;
    private Long donViTinhId;
    private String tenDonViTinh;
    private BigDecimal duDau;
    private BigDecimal tongNhap;
    private BigDecimal tongXuat;
    private String loaiVthh;
    private String tenLoaiVthh;
    private String cloaiVthh;
    private String tenCloaiVthh;
    private String capDvi;
}
