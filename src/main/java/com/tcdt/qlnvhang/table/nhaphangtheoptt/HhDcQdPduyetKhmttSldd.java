package com.tcdt.qlnvhang.table.nhaphangtheoptt;

import com.tcdt.qlnvhang.request.nhaphangtheoptt.HhDcQdPdKhmttSlddDtlReq;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name ="HH_DC_QD_PDUYET_KHMTT_SLDD")
@Data
public class HhDcQdPduyetKhmttSldd implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "HH_DC_QD_PDUYET_KHMTT_SLDD";
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HH_DC_QD_PDUYET_KHMTT_SLDD_SEQ")
    @SequenceGenerator(sequenceName = "HH_DC_QD_PDUYET_KHMTT_SLDD_SEQ", allocationSize = 1, name = "HH_DC_QD_PDUYET_KHMTT_SLDD_SEQ")

    private Long id;
    private Long idDxKhmtt;
    private Long idDcKhmtt;
    private String maDvi;
    private String tenDvi;
    private String maDiemKho;
    private String diaDiemKho;
    private BigDecimal soLuongCtieu;
    private BigDecimal soLuongKhDd;
    private BigDecimal soLuongDxmtt;
    private BigDecimal donGiaVat;
    private BigDecimal thanhTien;

    private String diaChi;
    private String tenGoiThau;
    private BigDecimal soLuongChiTieu;
    private BigDecimal donGia;
    private BigDecimal tongSoLuong;
    private BigDecimal tongThanhTien;
    private BigDecimal tongThanhTienVat;
    private BigDecimal soLuong;
    @Transient
    List<HhDcQdPdKhmttSlddDtl> children= new ArrayList<>();
    @Transient
    private List<HhChiTietTTinChaoGia> listChaoGia = new ArrayList<>();
    //preview
    @Transient
    private String soLuongGocStr;
    @Transient
    private String soLuongStr;
    @Transient
    private String tongThanhTienVatStr;
}
