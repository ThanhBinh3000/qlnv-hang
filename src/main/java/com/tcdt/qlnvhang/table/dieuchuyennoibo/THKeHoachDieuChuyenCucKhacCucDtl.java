package com.tcdt.qlnvhang.table.dieuchuyennoibo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Table(name = "DCNB_TH_KE_HOACH_DCC_KC_DTL")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class THKeHoachDieuChuyenCucKhacCucDtl implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DCNB_TH_KH_DCC_KC_DTL_SEQ")
    @SequenceGenerator(sequenceName = "DCNB_TH_KH_DCC_KC_DTL_SEQ", allocationSize = 1, name = "DCNB_TH_KH_DCC_KC_DTL_SEQ")
    private Long id;

    @Column(name = "NGAY_GUI_DUYET_TC")
    private LocalDate ngayGduyetTc;

    @Column(name = "HDR_ID")
    private Long hdrId;

    @Column(name = "DCNB_KE_HOACH_DC_HDR_ID")
    private String dcnbKeHoachDcHdrId;

    @Column(name = "MA_CUC_NHAN")
    private String maCucNhan;

    @Column(name = "TEN_CUC_NHAN")
    private String tenCucNhan;

    @Column(name = "TONG_DU_TOAN_KPHI")
    private Long tongDuToanKinhPhi;

    @Column(name = "PHE_DUYET")
    private Boolean pheDuyet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "HDR_ID",referencedColumnName="ID", insertable = false, updatable = false)
    @JsonIgnore
    private THKeHoachDieuChuyenCucHdr tHKeHoachDieuChuyenCucHdr;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "DCNB_KE_HOACH_DC_HDR_ID", insertable = false, updatable = false)
//    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler","phuongAnDieuChuyen","canCu"})
//    @NotFound(action = NotFoundAction.IGNORE)
//    private DcnbKeHoachDcHdr dcnbKeHoachDcHdr;

    @Transient
    private List<DcnbKeHoachDcHdr> dcnbKeHoachDcHdr ;
}
