package com.tcdt.qlnvhang.entities.xuathang.daugia.hopdong;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = XhHopDongDtl.TABLE_NAME)
@Data
public class XhHopDongDtl implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_HOP_DONG_DTL";
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhHopDongDtl.TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = XhHopDongDtl.TABLE_NAME + "_SEQ", allocationSize = 1, name = XhHopDongDtl.TABLE_NAME + "_SEQ")
    private Long id;
    private Long idHdr;
    private String maDvi;
    private String diaChi;
    private BigDecimal soLuongXuatBan;
    private BigDecimal thanhTienXuatBan;

    //    phu luc
    private Boolean tyPe; // tyPe : 0 hợp đồng hdr ; 1 phụ lục hdr
    @Transient
    private String tenDviHd;
    @Transient
    private String diaChiHd;
    @Transient
    private String tenDvi;
    @Transient
    private List<XhHopDongDdiemNhapKho> children = new ArrayList<>();
}
