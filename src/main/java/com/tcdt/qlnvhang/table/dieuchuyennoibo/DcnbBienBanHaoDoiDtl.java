package com.tcdt.qlnvhang.table.dieuchuyennoibo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = DcnbBienBanHaoDoiDtl.TABLE_NAME)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DcnbBienBanHaoDoiDtl implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "DCNB_BIEN_BAN_HAO_DOI_DTL";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = DcnbBienBanHaoDoiDtl.TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = DcnbBienBanHaoDoiDtl.TABLE_NAME + "_SEQ", allocationSize = 1, name = DcnbBienBanHaoDoiDtl.TABLE_NAME + "_SEQ")
    private Long id;

    @Column(name = "HDR_ID", insertable = true, updatable = true)
    private Long hdrId;
    @NotNull
    @Column(name = "NGAY_BAT_DAU")
    private LocalDate ngayBatDau;
    @NotNull
    @Column(name = "NGAY_KET_THUC")
    private LocalDate ngayKetThuc;
    @NotNull
    @Column(name = "SL_BAO_QUAN")
    private Double slBaoQuan;
    @NotNull
    @Column(name = "DINH_MUC_HAO_HUT")
    private Double dinhMucHaoHut;
    @NotNull
    @Column(name = "SL_HAO")
    private Double slHao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "HDR_ID", insertable = false, updatable = false)
    @JsonIgnore
    private DcnbBienBanHaoDoiHdr dcnbBienBanHaoDoiHdr;
}
