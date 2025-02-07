package com.tcdt.qlnvhang.table.dieuchuyennoibo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tcdt.qlnvhang.table.FileDinhKem;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = DcnbBienBanLayMauDtl.TABLE_NAME)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DcnbBienBanLayMauDtl implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "DCNB_BIEN_BAN_LAY_MAU_DTL";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = DcnbBienBanLayMauDtl.TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = DcnbBienBanLayMauDtl.TABLE_NAME + "_SEQ", allocationSize = 1, name = DcnbBienBanLayMauDtl.TABLE_NAME + "_SEQ")
    private Long id;

    @Column(name = "HDR_ID", insertable = true, updatable = true)
    private Long hdrId;

    @Column(name = "LOAI_DAI_DIEN")
    private String loaiDaiDien;

    @Column(name = "TEN_DAI_DIEN")
    private String tenDaiDien;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "HDR_ID", insertable = false, updatable = false)
    @JsonIgnore
    private DcnbBienBanLayMauHdr dcnbBienBanLayMauHdr;

}
