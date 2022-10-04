package com.tcdt.qlnvhang.table.nhaphangtheoptt;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name ="HH_DC_QD_PDUYET_KHMTT_SLDD")
@Data
public class HhDcQdPduyetKhmttSldd implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HH_DC_QD_PDUYET_KHMTT_SLDD_SEQ")
    @SequenceGenerator(sequenceName = "HH_DC_QD_PDUYET_KHMTT_SLDD_SEQ", allocationSize = 1, name = "HH_DC_QD_PDUYET_KHMTT_SLDD_SEQ")

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
}
