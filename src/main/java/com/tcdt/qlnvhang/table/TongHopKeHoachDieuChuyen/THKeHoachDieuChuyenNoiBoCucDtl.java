package com.tcdt.qlnvhang.table.TongHopKeHoachDieuChuyen;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbKeHoachDcDtl;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Table(name = "DCNB_TH_KE_HOACH_DCC_NBC_DTL")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@PrimaryKeyJoinColumn(name = "DCNB_KE_HOACH_DC_DTL_ID", referencedColumnName = "id")
public class THKeHoachDieuChuyenNoiBoCucDtl extends DcnbKeHoachDcDtl implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DCNB_TH_KH_DCC_NBC_DTL_SEQ")
    @SequenceGenerator(sequenceName = "DCNB_TH_KH_DCC_NBC_DTL_SEQ", allocationSize = 1, name = "DCNB_TH_KH_DCC_NBC_DTL_SEQ")
    private Long id;

    @Column(name = "HDR_ID")
    private Long hdrId;

    @Column(name = "DCNB_KE_HOACH_DC_DTL_ID", insertable = false, updatable = false)
    private Long dcKeHoachDcDtlId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "HDR_ID", insertable = false, updatable = false)
    @JsonIgnore
    private THKeHoachDieuChuyenCucHdr tHKeHoachDieuChuyenCucHdr;
}
