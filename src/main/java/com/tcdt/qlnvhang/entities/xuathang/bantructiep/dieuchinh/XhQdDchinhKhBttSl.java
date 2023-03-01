package com.tcdt.qlnvhang.entities.xuathang.bantructiep.dieuchinh;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = XhQdDchinhKhBttSl.TABLE_NAME)
@Data
public class XhQdDchinhKhBttSl implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_QD_DC_KH_BTT_SL";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = TABLE_NAME + "_SEQ", allocationSize = 1, name = TABLE_NAME + "_SEQ")
    private Long id;

    private Long idDtl;

    private String maDvi;
    @Transient
    private String tenDvi;

    private BigDecimal soLuong;

    private String diaChi;

    @Transient
    private List<XhQdDchinhKhBttSlDtl> children= new ArrayList<>();
}
