package com.tcdt.qlnvhang.entities.nhaphang.nhapkhac;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = HhNkBbGiaoNhanTTDtl.TABLE_NAME)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HhNkBbGiaoNhanTTDtl implements Serializable, Cloneable{
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "HHNK_BB_GIAO_NHAN_TT_DTL";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = HhNkBbGiaoNhanTTDtl.TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = HhNkBbGiaoNhanTTDtl.TABLE_NAME
            + "_SEQ", allocationSize = 1, name = HhNkBbGiaoNhanTTDtl.TABLE_NAME + "_SEQ")
    private Long id;
    @Column(name = "HDR_ID", insertable = true, updatable = true)
    private Long hdrId;
    @Column(name = "BB_CBI_KHO_ID")
    private Long bbCbiKhoId;
    @Column(name = "SO_BB_CBI_KHO")
    private String soBbCbiKho;
    @Column(name = "PHIEU_NHAP_KHO_ID")
    private Long phieuNhapKhoId;
    @Column(name = "SO_PHIEU_NHAP_KHO")
    private String soPhieuNhapKho;
    @Column(name = "BANG_KE_NHAP_ID")
    private Long bangKeNhapVtId;
    @Column(name = "SO_BANG_KE_NHAP")
    private String soBangKeNhapVt;
    @Column(name = "NGAY_NHAP")
    private LocalDate ngayNhapKho;
    @Column(name = "SO_LUONG")
    private BigDecimal soLuong;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "HDR_ID", insertable = false, updatable = false)
    @JsonIgnore
    private HhNkBbGiaoNhanHdr parent;
}
