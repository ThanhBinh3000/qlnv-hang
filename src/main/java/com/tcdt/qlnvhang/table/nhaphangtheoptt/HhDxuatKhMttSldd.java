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
    @Transient
    private Long idVirtual;

    private Long idDxKhmtt;

    private String maDvi;

    @Transient
    private String tenDvi;

    private String maDiemKho;

    @Transient
    private String tenDiemKho;

    private String diaDiemNhap;

    private BigDecimal soLuongChiTieu;

    private BigDecimal soLuongKhDd;

    private BigDecimal soLuong;

    private BigDecimal donGia;

    private BigDecimal thanhTien;

    private String loaiVthh;

    @Transient
    private String tenLoaiVthh;

    private String cloaiVthh;

    @Transient
    private String tenCloaiVthh;

    private BigDecimal donGiaVat;

    private BigDecimal thanhTienVat;

    private BigDecimal tongSoLuong;

    private BigDecimal tongDonGia;

    private BigDecimal tongThanhTien;



    @Transient
    private List<HhDxuatKhMttSlddDtl> children = new ArrayList<>();
}
