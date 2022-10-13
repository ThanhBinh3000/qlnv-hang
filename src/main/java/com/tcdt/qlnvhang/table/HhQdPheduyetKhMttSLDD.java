package com.tcdt.qlnvhang.table;

import com.tcdt.qlnvhang.entities.bandaugia.kehoachbanhangdaugia.BanDauGiaDiaDiemGiaoNhan;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

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
