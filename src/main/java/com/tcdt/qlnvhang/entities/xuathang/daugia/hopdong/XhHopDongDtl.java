package com.tcdt.qlnvhang.entities.xuathang.daugia.hopdong;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "XH_HOP_DONG_DTL")
@Data
public class XhHopDongDtl {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "XH_HOP_DONG_DTL_SEQ")
    @SequenceGenerator(sequenceName = "XH_HOP_DONG_DTL_SEQ", allocationSize = 1, name = "XH_HOP_DONG_DTL_SEQ")
    private Long id;

    private Long idHdr;

    private String maDvi;

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
    private String tenDvi;

    @Transient
    private List<XhHopDongDdiemNhapKho> children = new ArrayList<>();

}
