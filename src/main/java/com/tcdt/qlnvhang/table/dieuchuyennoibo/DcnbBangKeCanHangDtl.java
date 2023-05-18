package com.tcdt.qlnvhang.table.dieuchuyennoibo;

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
@Table(name = DcnbBienBanLayMauDtl.TABLE_NAME)
public class DcnbBangKeCanHangDtl {

    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "DCNB_BANG_KE_CAN_HANG_DTL";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = DcnbBienBanLayMauDtl.TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = DcnbBienBanLayMauDtl.TABLE_NAME + "_SEQ", allocationSize = 1, name = DcnbBienBanLayMauDtl.TABLE_NAME + "_SEQ")
    @Column(name = "ID")
    private Long id;

    @Column(name = "HDR_ID",insertable = true, updatable = true)
    private Long hdrId;

    @Column(name = "MA_CAN")
    private String maCan;

    @Column(name = "SO_BAO_BI")
    private BigDecimal soBaoBi;

    @Column(name = "TRONG_LUONG_CA_BAO_BI")
    private BigDecimal trongLuongCaBaoBi;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "HDR_ID", insertable = false, updatable = false)
    @JsonIgnore
    private DcnbBangKeCanHangHdr dcnbBangKeCanHangHdr;
}