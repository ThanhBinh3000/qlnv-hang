package com.tcdt.qlnvhang.table.TongHopKeHoachDieuChuyen;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbKeHoachDcDtl;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbKeHoachDcHdr;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
@Table(name = "DCNB_TH_KE_HOACH_DCC_NBC_DTL")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class THKeHoachDieuChuyenNoiBoCucDtl implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DCNB_TH_KH_DCC_NBC_DTL_SEQ")
    @SequenceGenerator(sequenceName = "DCNB_TH_KH_DCC_NBC_DTL_SEQ", allocationSize = 1, name = "DCNB_TH_KH_DCC_NBC_DTL_SEQ")
    private Long id;

    @Column(name = "HDR_ID")
    private Long hdrId;

    @Column(name = "DCNB_KE_HOACH_DC_HDR_ID")
    private Long dcKeHoachDcHdrId;

    @Column(name = "DCNB_KE_HOACH_DC_DTL_ID")
    private Long dcKeHoachDcDtlId;

    @Column(name = "MA_CHI_CUC_DXUAT")
    private String maChiCucDxuat;

    @Column(name = "TEN_CHI_CUC_DXUAT")
    private String tenChiCucDxuat;

    @Transient
    private List<DcnbKeHoachDcDtl> dcnbKeHoachDcDtlList = new ArrayList<>();


}
