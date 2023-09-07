package com.tcdt.qlnvhang.table.dieuchuyennoibo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = DcnbBangKeNhapVTDtl.TABLE_NAME)
public class DcnbBangKeNhapVTDtl {

    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "DCNB_BANG_KE_NHAP_VT_DTL";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = DcnbBangKeNhapVTDtl.TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = DcnbBangKeNhapVTDtl.TABLE_NAME + "_SEQ", allocationSize = 1, name = DcnbBangKeNhapVTDtl.TABLE_NAME + "_SEQ")
    @Column(name = "ID")
    private Long id;

    @Column(name = "HDR_ID",insertable = true, updatable = true)
    private Long hdrId;
    @NotNull
    @Column(name = "SO_SERIAL")
    private String soSerial;
    @NotNull
    @Column(name = "SO_LUONG")
    private BigDecimal soBaoBi;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "HDR_ID", insertable = false, updatable = false)
    @JsonIgnore
    private DcnbBangKeNhapVTHdr dcnbBangKeNhapVTHdr;
}