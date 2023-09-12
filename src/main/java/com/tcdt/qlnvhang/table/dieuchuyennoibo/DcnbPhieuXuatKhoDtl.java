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
@Table(name = DcnbPhieuXuatKhoDtl.TABLE_NAME)
public class DcnbPhieuXuatKhoDtl {

    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "DCNB_PHIEU_XUAT_KHO_DTL";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = DcnbPhieuXuatKhoDtl.TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = DcnbPhieuXuatKhoDtl.TABLE_NAME + "_SEQ", allocationSize = 1, name = DcnbPhieuXuatKhoDtl.TABLE_NAME + "_SEQ")
    @Column(name = "ID")
    private Long id;

    @Column(name = "HDR_ID",insertable = true, updatable = true)
    private Long hdrId;
    @NotNull
    private String loaiVthh;
    @NotNull
    private String tenLoaiVthh;
    @NotNull
    private String cloaiVthh;
    @NotNull
    private String tenCloaiVthh;
    @NotNull
    private String maSo;
    @NotNull
    private String donViTinh;
    @NotNull
    private BigDecimal slDcThucTe;
    @NotNull
    private BigDecimal kinhPhiDc;
    @NotNull
    private BigDecimal duToanKinhPhiDc;
    @NotNull
    private BigDecimal kinhPhiDcTt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "HDR_ID", insertable = false, updatable = false)
    @JsonIgnore
    private DcnbPhieuXuatKhoHdr dcnbPhieuXuatKhoHdr;
}