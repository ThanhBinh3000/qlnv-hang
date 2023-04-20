package com.tcdt.qlnvhang.table.TongHopKeHoachDieuChuyen;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbKeHoachDcDtl;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbKeHoachDcHdr;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
    private Long dcnbKeHoachDcHdrId;

    @Column(name = "DCNB_KE_HOACH_DC_DTL_ID")
    private Long dcnbKeHoachDcDtlId;

    @Transient
    private List<DcnbKeHoachDcDtl> dcnbKeHoachDcDtlList = new ArrayList<>();


}
