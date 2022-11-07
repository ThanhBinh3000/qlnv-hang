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
    private Long idDxKhmtt;
    private String maDvi;
    private String tenDvi;
    private String maDiemKho;
    private String diaDiemKho;
    private BigDecimal soLuongCtieu;
    private BigDecimal soLuongKhDd;
    private BigDecimal soLuongDxmtt;
    private BigDecimal donGiaVat;
    private BigDecimal thanhTien;

    @Transient
    private List<HhDxuatKhMttSlddDtl> ListSlddDtl = new ArrayList<>();
}
