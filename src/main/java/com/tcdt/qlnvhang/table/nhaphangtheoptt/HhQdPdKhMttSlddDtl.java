package com.tcdt.qlnvhang.table.nhaphangtheoptt;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "HH_QD_PD_KHMTT_SLDD_DTL ")
@Data
public class HhQdPdKhMttSlddDtl implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "HH_QD_PD_KHMTT_SLDD_DTL  ";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HH_QD_PD_KHMTT_SLDD_DTL_SEQ ")
    @SequenceGenerator(sequenceName = "HH_QD_PD_KHMTT_SLDD_DTL_SEQ ", allocationSize = 1, name = "HH_QD_PD_KHMTT_SLDD_DTL_SEQ ")
    private Long id;

    private Long idDiaDiem;

    private String maDiemKho;
    @Transient
    private String tenDiemKho;
    @Transient
    private Boolean edit = false;

    private String diaDiemNhap;

    private BigDecimal soLuong;

    private BigDecimal donGia;

    private BigDecimal donGiaVat;
}
