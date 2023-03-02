package com.tcdt.qlnvhang.entities.xuathang.bantructiep.hopdong;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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

    private String diaChi;

//    phu luc
    private Long idHdDtl;

    @Transient
    private String tenDviHd;
    @Transient
    private String diaChiHd;

    @Transient
    private List<XhHopDongBttDvi> children = new ArrayList<>();


}
