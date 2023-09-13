package com.tcdt.qlnvhang.table.dieuchuyennoibo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = DcnbBbGiaoNhanTTDtl.TABLE_NAME)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DcnbBbGiaoNhanTTDtl implements Serializable, Cloneable{
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "DCNB_BB_GIAO_NHAN_TT_DTL";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = DcnbBbGiaoNhanTTDtl.TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = DcnbBbGiaoNhanTTDtl.TABLE_NAME
            + "_SEQ", allocationSize = 1, name = DcnbBbGiaoNhanTTDtl.TABLE_NAME + "_SEQ")
    private Long id;
    @Column(name = "HDR_ID", insertable = true, updatable = true)
    private Long hdrId;
    @NotNull
    @Column(name = "BB_CBI_KHO_ID")
    private Long bbCbiKhoId;
    @NotNull
    @Column(name = "SO_BB_CBI_KHO")
    private String soBbCbiKho;
    @NotNull
    @Column(name = "PHIEU_NHAP_KHO_ID")
    private Long phieuNhapKhoId;
    @NotNull
    @Column(name = "SO_PHIEU_NHAP_KHO")
    private String soPhieuNhapKho;
    @NotNull
    @Column(name = "BANG_KE_NHAP_ID")
    private Long bangKeNhapVtId;
    @NotNull
    @Column(name = "SO_BANG_KE_NHAP")
    private String soBangKeNhapVt;
    @NotNull
    @Column(name = "NGAY_NHAP")
    private LocalDate ngayNhapKho;
    @NotNull
    @Column(name = "SO_LUONG")
    private BigDecimal soLuong;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "HDR_ID", insertable = false, updatable = false)
    @JsonIgnore
    private DcnbBbGiaoNhanHdr parent;
}
