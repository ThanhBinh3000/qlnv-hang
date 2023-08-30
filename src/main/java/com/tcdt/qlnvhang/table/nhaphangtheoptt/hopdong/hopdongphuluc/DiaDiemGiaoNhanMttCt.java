package com.tcdt.qlnvhang.table.nhaphangtheoptt.hopdong.hopdongphuluc;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "HH_HD_MTT_DIA_DIEM_CT")
@Data
public class DiaDiemGiaoNhanMttCt {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "HH_HD_MTT_DIA_DIEM_CT";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = TABLE_NAME + "_SEQ", allocationSize = 1, name = TABLE_NAME + "_SEQ")
    private Long id;

    private String maDiemKho;
    @Transient
    private String tenDiemKho;

    private String diaDiemNhap;

    private Long idDiaDiem;

    private BigDecimal soLuong;
    private BigDecimal soLuongHd;

    private BigDecimal donGia;

    private BigDecimal donGiaVat;
}
