package com.tcdt.qlnvhang.entities.xuathang.daugia.tochuctrienkhai.thongtin;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = XhTcTtinBdgDtl.TABLE_NAME)
@Data
public class XhTcTtinBdgDtl implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_TC_TTIN_BDG_DTL";
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhTcTtinBdgDtl.TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = XhTcTtinBdgDtl.TABLE_NAME + "_SEQ", allocationSize = 1, name = XhTcTtinBdgDtl.TABLE_NAME + "_SEQ")
    private Long id;
    private Long idHdr;
    private String maDvi;
    private String diaChi;
    private BigDecimal soLuongXuatBan;
    private BigDecimal tienDatTruoc;
    @Transient
    private String tenDvi;
    @Transient
    List<XhTcTtinBdgPlo> children = new ArrayList<>();
}
