package com.tcdt.qlnvhang.entities.xuathang.bantructiep.hopdong;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "XH_HOP_DONG_BTT_DVI")
@Data
public class XhHopDongBttDvi {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_HOP_DONG_BTT_DVI";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = TABLE_NAME + "_SEQ", allocationSize = 1, name = TABLE_NAME + "_SEQ")
    private Long id;

    private Long idDtl;

    String maDiemKho;
    @Transient
    private String tenDiemKho;

    String maNhaKho;
    @Transient
    private String tenNhaKho;

    String maNganKho;
    @Transient
    private String tenNganKho;

    String maLoKho;
    @Transient
    private String tenLoKho;

    BigDecimal soLuong;
}
