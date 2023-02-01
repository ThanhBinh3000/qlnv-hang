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

    String maDiemKho;

    String maNhaKho;

    String maNganKho;

    String maLoKho;

    String maDviTsan;

    BigDecimal soLuong;

    BigDecimal donGiaVat;

    String toChucCaNhan;

    String dviTinh;

    // Teansient
    @Transient
    private String tenDiemKho;
    @Transient
    private String tenNhaKho;
    @Transient
    private String tenNganKho;
    @Transient
    private String tenLoKho;
}
