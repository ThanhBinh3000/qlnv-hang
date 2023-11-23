package com.tcdt.qlnvhang.table;

import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhChiTietKqTTinChaoGia;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhChiTietTTinChaoGia;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhQdPdKQMttSlddDtl;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhQdPdKhMttSlddDtl;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.hopdong.hopdongphuluc.HopDongMttHdr;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name ="HH_QD_PD_KQMTT_SLDD")
@Data

public class HhQdPheduyetKqMttSLDD implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "HH_QD_PD_KQMTT_SLDD";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HH_QD_PD_KQMTT_SLDD_SEQ")
    @SequenceGenerator(sequenceName = "HH_QD_PD_KQMTT_SLDD_SEQ", allocationSize = 1, name = "HH_QD_PD_KQMTT_SLDD_SEQ")

    private Long id;

    private Long idQdPdKq;

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
    private BigDecimal soLuongHd;

    @Transient
    private List<HhQdPdKQMttSlddDtl> children = new ArrayList<>();
    @Transient
    private List<HhChiTietKqTTinChaoGia> listChaoGia = new ArrayList<>();
    @Transient
    private List<HopDongMttHdr> listHdong = new ArrayList<>();

    @Transient
    private String tongThanhTienStr;

}
