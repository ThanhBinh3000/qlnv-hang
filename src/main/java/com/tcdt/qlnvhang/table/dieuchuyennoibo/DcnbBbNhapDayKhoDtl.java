package com.tcdt.qlnvhang.table.dieuchuyennoibo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = DcnbBbNhapDayKhoDtl.TABLE_NAME)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DcnbBbNhapDayKhoDtl implements Serializable, Cloneable{
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "DCNB_BB_NHAP_DAY_KHO_DTL";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = DcnbBbNhapDayKhoDtl.TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = DcnbBbNhapDayKhoDtl.TABLE_NAME
            + "_SEQ", allocationSize = 1, name = DcnbBbNhapDayKhoDtl.TABLE_NAME + "_SEQ")
    private Long id;
    @Column(name = "HDR_ID", insertable = true, updatable = true)
    private Long hdrId;
    @NotNull
    @Column(name = "PHIEU_KT_CLUONG")
    private String soPhieuKiemTraCl;
    @NotNull
    @Column(name = "ID_PHIEU_KT_CLUONG")
    private Long phieuKiemTraClId;
    @NotNull
    private String phieuNhapKho;
    @NotNull
    private Long idPhieuNhapKho;
    @NotNull
    private String soBangKeCh;
    @NotNull
    private Long idBangKeCh;
    @NotNull
    private LocalDate ngayNhap;
    @NotNull
    private BigDecimal soLuong;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "HDR_ID", insertable = false, updatable = false)
    @JsonIgnore
    private DcnbBbNhapDayKhoHdr parent;
}
