package com.tcdt.qlnvhang.table.nhaphangtheoptt;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "HH_DX_KHMTT_SLDD_DTL")
@Data
public class HhDxuatKhMttSlddDtl implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "HH_DX_KHMTT_SLDD_DTL";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HH_DX_KHMTT_SLDD_DTL_SEQ")
    @SequenceGenerator(sequenceName = "HH_DX_KHMTT_SLDD_DTL_SEQ", allocationSize = 1, name = "HH_DX_KHMTT_SLDD_DTL_SEQ")
    private Long id;

    private String maDiemKho;
    @Transient
    private String tenDiemKho;

    private String diaDiemNhap;

    private Long idDtl;

    private BigDecimal soLuong;

    private BigDecimal donGia;

    private BigDecimal donGiaVat;
}
