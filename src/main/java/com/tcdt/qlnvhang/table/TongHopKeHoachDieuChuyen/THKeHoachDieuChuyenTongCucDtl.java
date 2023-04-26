package com.tcdt.qlnvhang.table.TongHopKeHoachDieuChuyen;

import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbKeHoachDcDtl;
import lombok.*;
import org.checkerframework.checker.units.qual.C;

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

    @Column(name = "DCNB_KE_HOACH_DC_HDR_ID")
    private Long keHoachDcHdrId;

    @Column(name = "MA_CUC_DXUAT_DC")
    private String maCucDxuatDc;

    @Column(name = "TEN_CUC_DXUAT_DC")
    private String tenCucDxuatDc;

    @Column(name = "MA_CUC_NHAN_DC")
    private String maCucNhanDc;

    @Column(name = "TEN_CUC_NHAN_DC")
    private String tenCucNhanDc;

    @Column(name = "SO_DXUAT")
    private String soDxuat;

    @Column(name = "NGAY_DUYET_TC")
    private Date ngayDuyetTc;

    @Column(name = "DU_TOAN_KP")
    private Long duToanKp;

    @Column(name = "TRICH_YEU")
    private String trichYeu;

    @Transient
    private List<DcnbKeHoachDcDtl> dcnbKeHoachDcDtlList = new ArrayList<>();
}
