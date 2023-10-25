package com.tcdt.qlnvhang.table.nhaphangtheoptt;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "HH_TT_CGIA_KHMTT_SLDD_DTL ")
@Data
public class HhTtChaoGiaSlddDtl implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "HH_TT_CGIA_KHMTT_SLDD_DTL ";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HH_TT_CGIA_KHMTT_SLDD_DTL_SEQ")
    @SequenceGenerator(sequenceName = "HH_TT_CGIA_KHMTT_SLDD_DTL_SEQ", allocationSize = 1, name = "HH_TT_CGIA_KHMTT_SLDD_DTL_SEQ")
    private Long id;
    private Long idDiaDiem;
    private String maDvi;
    @Transient
    private String tenDvi;
    private String maDiemKho;
    @Transient
    private String tenDiemKho;
    private String diaDiemNhap;
    private BigDecimal soLuong;
    private BigDecimal donGiaVat;
    private BigDecimal donGia;
    private BigDecimal thanhTien;
}
