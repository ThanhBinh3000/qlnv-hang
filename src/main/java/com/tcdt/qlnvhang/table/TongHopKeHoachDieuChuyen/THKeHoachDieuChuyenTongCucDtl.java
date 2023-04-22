package com.tcdt.qlnvhang.table.TongHopKeHoachDieuChuyen;

import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbKeHoachDcDtl;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@Table(name = "DCNB_TH_KE_HOACH_DCTC_DTL")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class THKeHoachDieuChuyenTongCucDtl implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DCNB_TH_KH_DCTC_DTL_SEQ")
    @SequenceGenerator(sequenceName = "DCNB_TH_KH_DCTC_DTL_SEQ", allocationSize = 1, name = "DCNB_TH_KH_DCTC_DTL_SEQ")
    private Long id;

    @Column(name = "HDR_ID")
    private Long hdrId;

    @Column(name = "DCNB_KE_HOACH_DC_HDR_ID")
    private Long keHoachDcHdrId;

    @Column(name = "DCNB_KE_HOACH_DC_DTL_ID")
    private Long keHoachDcDtlId;

    @Transient
    private List<DcnbKeHoachDcDtl> dcnbKeHoachDcDtlList = new ArrayList<>();
}
