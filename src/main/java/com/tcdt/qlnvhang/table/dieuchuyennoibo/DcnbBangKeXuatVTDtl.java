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
@Table(name = DcnbBangKeXuatVTDtl.TABLE_NAME)
public class DcnbBangKeXuatVTDtl {

    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "DCNB_BANG_KE_XUAT_VT_DTL";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = DcnbBangKeXuatVTDtl.TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = DcnbBangKeXuatVTDtl.TABLE_NAME + "_SEQ", allocationSize = 1, name = DcnbBangKeXuatVTDtl.TABLE_NAME + "_SEQ")
    @Column(name = "ID")
    private Long id;

    @Column(name = "HDR_ID",insertable = true, updatable = true)
    private Long hdrId;
    @NotNull
    @Column(name = "SO_SERIAL")
    private String soSerial;
    @NotNull
    @Column(name = "SO_LUONG")
    private BigDecimal soLuong;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "HDR_ID", insertable = false, updatable = false)
    @JsonIgnore
    private DcnbBangKeXuatVTHdr dcnbBangKeXuatVTHdr;
}