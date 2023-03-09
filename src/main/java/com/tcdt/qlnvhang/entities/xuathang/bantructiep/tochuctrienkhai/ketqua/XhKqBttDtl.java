package com.tcdt.qlnvhang.entities.xuathang.bantructiep.tochuctrienkhai.ketqua;


import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "XH_KQ_BTT_DTL")
@Data
public class XhKqBttDtl {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_KQ_BTT_DTL ";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "XH_KQ_BTT_DTL_SEQ")
    @SequenceGenerator(sequenceName = "XH_KQ_BTT_DTL_SEQ", allocationSize = 1, name = "XH_KQ_BTT_DTL_SEQ")
    private Long id;

    private Long idHdr;

    private String maDvi;
    @Transient
    private String tenDvi;

    private BigDecimal soLuong;

    private String diaChi;

    private BigDecimal donGiaVat;

    @Transient
    private List<XhKqBttDdiem> children = new ArrayList<>();

}
