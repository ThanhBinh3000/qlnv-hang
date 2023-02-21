package com.tcdt.qlnvhang.entities.xuathang.bantructiep.hopdong;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = XhHopDongBttDtl.TABLE_NAME)
@Data
public class XhHopDongBttDtl implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_HOP_DONG_BTT_DTL";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = TABLE_NAME + "_SEQ", allocationSize = 1, name = TABLE_NAME + "_SEQ")
    private Long id;

    private Long idHdr;

    private String maDvi;

    @Transient
    private String tenDvi;

    private BigDecimal soLuong;

    private BigDecimal donGiaVat;

}
