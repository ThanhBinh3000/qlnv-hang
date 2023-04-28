package com.tcdt.qlnvhang.table.dieuchuyennoibo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbKeHoachDcDtl;
import lombok.*;
import org.checkerframework.checker.units.qual.C;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
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

    @Column(name = "DCNB_TH_KE_HOACH_DCC_HDR_ID",insertable = true,updatable = true)
    private Long thKhDcHdrId;

    @Column(name = "DCNB_KE_HOACH_DC_HDR_ID",insertable = true,updatable = true)
    private Long keHoachDcHdrId;

    @Column(name = "DCNB_TH_KE_HOACH_DCC_DTL_ID",insertable = true,updatable = true)
    private Long thKhDcDtlId;

    @Column(name = "DCNB_KE_HOACH_DC_DTL_ID",insertable = true,updatable = true)
    private Long keHoachDcDtlId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "HDR_ID",insertable = false,updatable = false)
    @JsonIgnore
    private THKeHoachDieuChuyenTongCucHdr thKeHoachDieuChuyenTongCucHdr;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DCNB_KE_HOACH_DC_HDR_ID",insertable = false,updatable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler","ngayTao","nguoiTaoId","ngaySua","nguoiSuaId"})
    @NotFound(action = NotFoundAction.IGNORE)
    private DcnbKeHoachDcHdr dcnbKeHoachDcHdr;

//    @Transient
//    private List<DcnbKeHoachDcDtl> dcnbKeHoachDcDtls = new ArrayList<>();
}
