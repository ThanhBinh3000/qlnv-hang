package com.tcdt.qlnvhang.entities.nhaphang.nhapkhac;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = HhNkBangKeNhapVTDtl.TABLE_NAME)
public class HhNkBangKeNhapVTDtl {

    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "HHNK_BANG_KE_NHAP_VT_DTL";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = HhNkBangKeNhapVTDtl.TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = HhNkBangKeNhapVTDtl.TABLE_NAME + "_SEQ", allocationSize = 1, name = HhNkBangKeNhapVTDtl.TABLE_NAME + "_SEQ")
    @Column(name = "ID")
    private Long id;

    @Column(name = "HDR_ID",insertable = true, updatable = true)
    private Long hdrId;

    @Column(name = "SO_SERIAL")
    private String soSerial;

    @Column(name = "SO_LUONG")
    private BigDecimal soBaoBi;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "HDR_ID", insertable = false, updatable = false)
    @JsonIgnore
    private HhNkBangKeNhapVTHdr hhNkBangKeNhapVTHdr;
}