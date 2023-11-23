package com.tcdt.qlnvhang.table;

import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhChiTietTTinChaoGia;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhQdPdKhMttSlddDtl;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name ="HH_QD_PHE_DUYET_KHMTT_SLDD")
@Data

public class HhQdPheduyetKhMttSLDD implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "HH_QD_PHE_DUYET_KHMTT_SLDD";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HH_QD_PHE_DUYET_KHMTT_SLDD_SEQ")
    @SequenceGenerator(sequenceName = "HH_QD_PHE_DUYET_KHMTT_SLDD_SEQ", allocationSize = 1, name = "HH_QD_PHE_DUYET_KHMTT_SLDD_SEQ")

    private Long id;

    private Long idQdDtl;

    private String maDvi;
    @Transient
    private String tenDvi;

    private String diaChi;

    private String tenGoiThau;

    private BigDecimal soLuongChiTieu;

    private BigDecimal soLuongKhDd;

    private BigDecimal donGia;

    private BigDecimal donGiaVat;

    private BigDecimal tongSoLuong;

    private BigDecimal tongThanhTien;

    private BigDecimal tongThanhTienVat;

    private BigDecimal soLuong;

    @Transient
    private List<HhQdPdKhMttSlddDtl> children = new ArrayList<>();
    @Transient
    private List<HhChiTietTTinChaoGia> listChaoGia = new ArrayList<>();
    @Transient
    private String tongThanhTienVatStr;

}
