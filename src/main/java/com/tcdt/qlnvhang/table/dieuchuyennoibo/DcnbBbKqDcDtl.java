package com.tcdt.qlnvhang.table.dieuchuyennoibo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tcdt.qlnvhang.entities.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = DcnbBbKqDcDtl.TABLE_NAME)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DcnbBbKqDcDtl extends BaseEntity implements Serializable, Cloneable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "DCNB_BB_KQ_DC_DTL";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = DcnbBbKqDcDtl.TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = DcnbBbKqDcDtl.TABLE_NAME
            + "_SEQ", allocationSize = 1, name = DcnbBbKqDcDtl.TABLE_NAME + "_SEQ")
    private Long id;
    @Column(name = "HDR_ID", insertable = true, updatable = true)
    private Long hdrId;
    @Column(name = "QD_DC_CUC_ID")
    private Long qdDcCucId;
    @Column(name = "SO_QD_DC_CUC")
    private String soQdDcCuc;
    @Column(name = "NGAY_KY_QD_CUC")
    private LocalDate ngayKyQdCuc;
    @Column(name = "LOAI_VTHH")
    private String loaiVthh;
    @Column(name = "CLOAI_VTHH")
    private String cloaiVthh;
    @Column(name = "TEN_LOAI_VTHH")
    private String tenLoaiVthh;
    @Column(name = "TEN_CLOAI_VTHH")
    private String tenCloaiVthh;
    @Column(name = "MA_DIEM_KHO")
    private String maDiemKho;
    @Column(name = "TEN_DIEM_KHO")
    private String tenDiemKho;
    @Column(name = "MA_NHA_KHO")
    private String maNhaKho;
    @Column(name = "TEN_NHA_KHO")
    private String tenNhaKho;
    @Column(name = "MA_NGAN_KHO")
    private String maNganKho;
    @Column(name = "TEN_NGAN_KHO")
    private String tenNganKho;
    @Column(name = "MA_LO_KHO")
    private String maLoKho;
    @Column(name = "TEN_LO_KHO")
    private String tenLoKho;
    @Column(name = "DON_VI_TINH")
    private String donViTinh;
    @Column(name = "TEN_DON_VI_TINH")
    private String tenDonViTinh;
    @Column(name = "SL_TON")
    private BigDecimal slTon;
    @Column(name = "SL_DIEU_CHUYEN_QD")
    private BigDecimal slDieuChuyenQd;
    @Column(name = "SL_DIEU_CHUYEN_TT")
    private BigDecimal slDieuChuyenTt;
    @Column(name = "KINH_PHI_THEO_QD")
    private BigDecimal kinhPhiTheoQd;
    @Column(name = "KINH_PHI_THEO_TT")
    private BigDecimal kinhPhiTheoTt;
    @Column(name = "TRICH_YEU")
    private BigDecimal trichYeu;
    @Column(name = "KET_QUA")
    private Boolean ketQua;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "HDR_ID", insertable = false, updatable = false)
    @JsonIgnore
    private DcnbBbKqDcHdr dcnbBbKqDcHdr;
}
