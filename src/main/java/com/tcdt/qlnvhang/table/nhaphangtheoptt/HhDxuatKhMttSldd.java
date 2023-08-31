package com.tcdt.qlnvhang.table.nhaphangtheoptt;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "HH_DX_KHMTT_SLDD")
@Data
public class HhDxuatKhMttSldd implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "HH_DX_KHMTT_SLDD";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HH_DX_KHMTT_SLDD_SEQ")
    @SequenceGenerator(sequenceName = "HH_DX_KHMTT_SLDD_SEQ", allocationSize = 1, name = "HH_DX_KHMTT_SLDD_SEQ")
    private Long id;

    private String tenGoiThau;

    private Long idHdr;

    private String maDvi;
    @Transient
    private String tenDvi;

    private String diaChi;

    private BigDecimal soLuongChiTieu;

    private BigDecimal soLuongKhDd;

    private BigDecimal donGia;

    private BigDecimal donGiaVat;

    private BigDecimal tongSoLuong;

    private BigDecimal tongThanhTien;

    private BigDecimal tongThanhTienVat;

    private BigDecimal soLuong;
    @Transient
    private String tongThanhTienStr;
    private String dvt;
    private String cloaiVthh;
    private String tenCloaiVthh;
    private BigDecimal donGiaTdVat;

    @Transient
    private List<HhDxuatKhMttSlddDtl> children = new ArrayList<>();
}
