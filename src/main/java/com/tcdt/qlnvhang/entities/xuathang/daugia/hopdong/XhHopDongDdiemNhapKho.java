package com.tcdt.qlnvhang.entities.xuathang.daugia.hopdong;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "XH_HOP_DONG_DDIEM_NHAP_KHO")
@Data
public class XhHopDongDdiemNhapKho {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "XH_HD_DDIEM_NHAP_KHO_SEQ")
    @SequenceGenerator(sequenceName = "XH_HD_DDIEM_NHAP_KHO_SEQ", allocationSize = 1, name = "XH_HD_DDIEM_NHAP_KHO_SEQ")
    private Long id;
    private Long idDtl;
    private String maDiemKho;
    private String maNhaKho;
    private String maNganKho;
    private String maLoKho;
    private String maDviTsan;
    private BigDecimal tonKho;
    private BigDecimal soLuong;
    private BigDecimal donGia;
    private BigDecimal donGiaTraGia;
    private BigDecimal thanhTien;
    private String donViTinh;
    private String diaDiemKho;
    @Transient
    private String tenDiemKho;
    @Transient
    private String tenNhaKho;
    @Transient
    private String tenNganKho;
    @Transient
    private String tenLoKho;
}
