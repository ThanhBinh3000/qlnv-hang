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
import java.math.BigDecimal;
import java.time.LocalDate;
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

    @Column(name = "DCNB_TH_KE_HOACH_DCC_HDR_ID", insertable = true, updatable = true)
    private Long thKhDcHdrId;

    @Column(name = "DCNB_TH_KE_HOACH_DCC_DTL_ID", insertable = true, updatable = true)
    private Long thKhDcDtlId;

    @Column(name = "MA_CUC_NHAN")
    private String maCucNhan;

    @Column(name = "SO_DXUAT")
    private String soDxuat;

    @Column(name = "TEN_CUC_NHAN")
    private String tenCucNhan;

    @Column(name = "MA_CUC_DXUAT")
    private String maCucDxuat;

    @Column(name = "TEN_CUC_DXUAT")
    private String tenCucDxuat;

    @Column(name = "TRICH_YEU")
    private String trichYeu;

    @Column(name = "DU_TOAN_KP")
    private BigDecimal tongDuToanKp;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "HDR_ID", insertable = false, updatable = false)
    @JsonIgnore
    private THKeHoachDieuChuyenTongCucHdr thKeHoachDieuChuyenTongCucHdr;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DCNB_TH_KE_HOACH_DCC_HDR_ID", insertable = false, updatable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "ngayTao", "nguoiTaoId", "ngaySua", "nguoiSuaId"})
    @NotFound(action = NotFoundAction.IGNORE)
    private THKeHoachDieuChuyenCucHdr thKeHoachDieuChuyenCucHdr;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DCNB_TH_KE_HOACH_DCC_DTL_ID", insertable = false, updatable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "ngayTao", "nguoiTaoId", "ngaySua", "nguoiSuaId"})
    @NotFound(action = NotFoundAction.IGNORE)
    private THKeHoachDieuChuyenCucKhacCucDtl thKeHoachDieuChuyenCucKhacCucDtl;

    @Transient
    private LocalDate ngayTrinhTc;
    @Transient
    private String maCucXuat;
    @Transient
    private String tenCucXuat;
    @Transient
    private List<DcnbQuyetDinhDcTcTTDtl> danhSachQuyetDinhChiTiet;
}
