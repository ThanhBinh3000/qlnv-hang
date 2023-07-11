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
@Table(name = HhNkBbNhapDayKhoDtl.TABLE_NAME)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HhNkBbNhapDayKhoDtl implements Serializable, Cloneable{
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "HHNK_BB_NHAP_DAY_KHO_DTL";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = HhNkBbNhapDayKhoDtl.TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = HhNkBbNhapDayKhoDtl.TABLE_NAME
            + "_SEQ", allocationSize = 1, name = HhNkBbNhapDayKhoDtl.TABLE_NAME + "_SEQ")
    private Long id;
    @Column(name = "HDR_ID", insertable = true, updatable = true)
    private Long hdrId;
    private String phieuKtCluong;
    private Long idPhieuKtCluong;
    private String phieuNhapKho;
    private Long idPhieuNhapKho;
    private String soBangKeCh;
    private Long idBangKeCh;
    private LocalDate ngayNhap;
    private BigDecimal soLuong;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "HDR_ID", insertable = false, updatable = false)
    @JsonIgnore
    private HhNkBbNhapDayKhoHdr parent;
}
