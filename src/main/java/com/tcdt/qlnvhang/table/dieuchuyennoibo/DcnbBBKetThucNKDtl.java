package com.tcdt.qlnvhang.table.dieuchuyennoibo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = DcnbBBKetThucNKDtl.TABLE_NAME)
public class DcnbBBKetThucNKDtl {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "DCNB_BB_KET_THUC_NK_DTL";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = DcnbBBKetThucNKDtl.TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = DcnbBBKetThucNKDtl.TABLE_NAME + "_SEQ", allocationSize = 1, name = DcnbBBKetThucNKDtl.TABLE_NAME + "_SEQ")
    @Column(name = "ID")
    private Long id;

    @Column(name = "HDR_ID",insertable = true, updatable = true)
    private Long hdrId;
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
    private DcnbBBKetThucNKHdr dcnbBBKetThucNKHdr;
}
