package com.tcdt.qlnvhang.table.TongHopKeHoachDieuChuyen;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbKeHoachDcDtl;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbKeHoachDcHdr;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

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

    @Column(name = "MA_CUC_NHAN")
    private String maCucNhan;

    @Column(name = "TEN_CUC_NHAN")
    private String tenCucNhan;

    @Column(name = "SO_DXUAT")
    private String soDxuat;

    @Column(name = "NGAY_DXUAT")
    private LocalDate ngayDxuat;

    @Column(name = "NGAY_DUYET_TC")
    private LocalDate ngayGduyetTc;

    @Column(name = "TONG_DU_TOAN_KP")
    private Long tongDuToanKp;

    @Column(name = "TRICH_YEU")
    private String trichYeu;

    @Column(name = "HDR_ID")
    private Long hdrId;

    @Column(name = "DCNB_KE_HOACH_DC_HDR_ID")
    private Long dcnbKeHoachDcHdrId;


}
