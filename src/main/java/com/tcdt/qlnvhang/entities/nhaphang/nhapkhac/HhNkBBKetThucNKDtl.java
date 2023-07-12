package com.tcdt.qlnvhang.entities.nhaphang.nhapkhac;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = HhNkBBKetThucNKDtl.TABLE_NAME)
public class HhNkBBKetThucNKDtl {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "HHNK_BB_KET_THUC_NK_DTL";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = HhNkBBKetThucNKDtl.TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = HhNkBBKetThucNKDtl.TABLE_NAME + "_SEQ", allocationSize = 1, name = HhNkBBKetThucNKDtl.TABLE_NAME + "_SEQ")
    @Column(name = "ID")
    private Long id;

    @Column(name = "HDR_ID", insertable = true, updatable = true)
    private Long hdrId;

    @Column(name = "PHIEU_NHAP_KHO_ID")
    private Long phieuNhapKhoId;
    @Column(name = "SO_PHIEU_NHAP_KHO")
    private String soPhieuNhapKho;
    @Column(name = "BANG_KE_NHAP_ID")
    private Long bangKeNhapKhoId;
    @Column(name = "SO_BANG_KE_NHAP")
    private String soBangKeNhap;
    @Column(name = "NGAY_NHAP")
    private LocalDate ngayNhap;
    @Column(name = "SO_LUONG")
    private BigDecimal soLuong;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "HDR_ID", insertable = false, updatable = false)
    @JsonIgnore
    private HhNkBBKetThucNKHdr hhNkBBKetThucNKHdr;
}
