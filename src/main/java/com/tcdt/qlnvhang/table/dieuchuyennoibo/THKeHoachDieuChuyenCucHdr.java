package com.tcdt.qlnvhang.table.dieuchuyennoibo;


import com.tcdt.qlnvhang.entities.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Getter
@Setter
@Table(name = "DCNB_TH_KE_HOACH_DCC_HDR")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class THKeHoachDieuChuyenCucHdr implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DCNB_TH_KE_HOACH_DCC_HDR_SEQ")
    @SequenceGenerator(sequenceName = "DCNB_TH_KE_HOACH_DCC_HDR_SEQ", allocationSize = 1, name = "DCNB_TH_KE_HOACH_DCC_HDR_SEQ")
    private Long id;

    @Column(name = "NGAY_TAO")
    private LocalDate ngaytao;

    @Column(name = "NGUOI_TAO_ID")
    private Long nguoiTaoId;

    @Column(name = "NGAY_SUA")
    private LocalDate ngaySua;

    @Column(name = "NGUOI_SUA_ID")
    private Long nguoiSuaId;

    @Column(name = "MA_TONG_HOP")
    private Long maTongHop;

    @Column(name = "SO_DXUAT")
    private String soDeXuat;

    @Column(name = "NGAY_TONG_HOP")
    private LocalDate ngayTongHop;

    @Column(name = "TRICH_YEU")
    private String trichYeu;

    @Column(name = "NAM_KE_HOACH")
    private Integer namKeHoach;

    @Column(name = "LOAI_DC")
    private String loaiDieuChuyen;

    @Column(name = "TH_TU_NGAY")
    private LocalDate thTuNgay;

    @Column(name = "TH_DEN_NGAY")
    private LocalDate thDenNgay;

    @Column(name = "TRANG_THAI")
    private String trangThai;

    @Column(name = "NGAY_GDUYET")
    private LocalDate ngayGDuyet;

    @Column(name = "NGUOI_GDUYET_ID")
    private Long nguoiGDuyetId;

    @Column(name = "NGAY_DUYET_TP")
    private LocalDate ngayDuyetTp;

    @Column(name = "NGUOI_DUYET_TP_ID")
    private Long nguoiDuyetTpId;

    @Column(name = "NGAY_DUYET_LDC")
    private LocalDate ngayDuyetLdc;

    @Column(name = "NGUOI_DUYET_LDC_ID")
    private Long nguoiDuyetLdcId;

    @Column(name = "LY_DO_TU_CHOI")
    private String lyDoTuChoi;

    @Column(name = "MA_DVI")
    private String maDvi;

    @Column(name = "TEN_DVI")
    private String tenDvi;

    @Column(name = "THOI_GIAN_TONG_HOP")
    private LocalDateTime thoiGianTongHop;

    @Column(name = "ID_THOP_TC")
    private Long idThTongCuc;

    @Column(name = "NGAY_TRINH_DUYET_TC")
    private LocalDate ngayTrinhDuyetTc;

    @Column(name = "NGAY_PHE_DUYET_TC")
    private LocalDate ngayPheDuyetTc;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "DCNB_TH_KE_HOACH_DCC_HDR_ID")
    private List<THKeHoachDieuChuyenNoiBoCucDtl> thKeHoachDieuChuyenNoiBoCucDtls = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "HDR_ID")
    private List<THKeHoachDieuChuyenCucKhacCucDtl> thKeHoachDieuChuyenCucKhacCucDtls = new ArrayList<>();
}
