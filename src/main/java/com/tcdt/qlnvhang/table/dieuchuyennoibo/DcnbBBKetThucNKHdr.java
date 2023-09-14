package com.tcdt.qlnvhang.table.dieuchuyennoibo;

import com.tcdt.qlnvhang.entities.BaseEntity;
import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.table.FileDinhKem;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = DcnbBBKetThucNKHdr.TABLE_NAME)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DcnbBBKetThucNKHdr extends BaseEntity implements Serializable, Cloneable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "DCNB_BB_KET_THUC_NK_HDR";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = DcnbBBKetThucNKHdr.TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = DcnbBBKetThucNKHdr.TABLE_NAME + "_SEQ", allocationSize = 1, name = DcnbBBKetThucNKHdr.TABLE_NAME + "_SEQ")
    private Long id;
    @Column(name = "LOAI_DC")
    private String loaiDc;
    @Column(name = "LOAI_VTHH")
    private String loaiVthh;
    @Column(name = "CLOAI_VTHH")
    private String cloaiVthh;
    @Column(name = "TEN_LOAI_VTHH")
    private String tenLoaiVthh;
    @Column(name = "TEN_CLOAI_VTHH")
    private String tenCloaiVthh;
    @Column(name = "NAM")
    private Integer nam;
    @Column(name = "SO_BB")
    private String soBb;
    @Column(name = "NGAY_LAP")
    private LocalDate ngayLap;
    @Column(name = "MA_DVI")
    private String maDvi;
    @Column(name = "QHNS_ID")
    private Long qhnsId;
    @Column(name = "MA_QHNS")
    private String maQhns;
    @Column(name = "QDINH_DCC_ID")
    private Long qdinhDccId;
    @Column(name = "SO_QDINH_DCC")
    private String soQdinhDcc;
    @Column(name = "NGAY_QDINH_DCC")
    private LocalDate ngayQdinhDcc;
    @Column(name = "MA_DIEM_KHO")
    private String maDiemKho;
    @Column(name = "TEN_DIEM_KHO")
    private String tenDiemKho;
    @Column(name = "DIA_DIEM_KHO")
    private String diaDaDiemKho;
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
    @Column(name = "NGAY_BAT_DAU_NHAP")
    private LocalDate ngayBatDauNhap;
    @Column(name = "NGAY_KET_THUC_NHAP")
    private LocalDate ngayKetThucNhap;
    @Column(name = "TONG_SL_THEO_QD")
    private BigDecimal tongSlTheoQd;
    @Column(name = "MA_LANH_DAO_CHI_CUC")
    private String maLanhDaoChiCuc;
    @Column(name = "TEN_LANH_DAO_CHI_CUC")
    private String tenLanhDaoChiCuc;
    @Column(name = "THU_KHO_ID")
    private Long thuKhoId;
    @Column(name = "TEN_THU_KHO")
    private String tenThuKho;
    @Column(name = "KTV_BAOQUAN")
    private Long ktvBQuan;
    @Column(name = "TEN_KTV_BAOQUAN")
    private String tenKtvBQuan;
    @Column(name = "KE_TOAN_TRUONG")
    private Long keToanTruong;
    @Column(name = "TEN_KE_TOAN_TRUONG")
    private String tenKeToanTruong;
    @Column(name = "DON_VI_TINH")
    private String donViTinh;
    @Column(name = "TRANG_THAI")
    @Access(value = AccessType.PROPERTY)
    private String trangThai;
    @Column(name = "LY_DO_TU_CHOI")
    private String lyDoTuChoi;
    @Column(name = "NGUOI_GDUYET")
    private Long nguoiGDuyet;

    @Column(name = "NGAY_GDUYET")
    private LocalDate ngayGDuyet;

    @Column(name = "NGUOI_PDUYET_KTV")
    private Long nguoiPDuyetKtv;

    @Column(name = "NGAY_GDUYET_KTV")
    private LocalDate ngayPDuyetKtv;

    @Column(name = "NGUOI_PDUYET_TVQT")
    private Long nguoiPDuyetTvqt;

    @Column(name = "NGAY_GDUYET_TVQT")
    private LocalDate ngayPDuyetTvqt;

    @Column(name = "NGUOI_PDUYET_KT")
    private Long nguoiPDuyetKt;

    @Column(name = "NGAY_GDUYET_KT")
    private LocalDate ngayPDuyetKt;

    @Column(name = "NGUOI_PDUYET")
    private Long nguoiPDuyet;

    @Column(name = "NGAY_PDUYET")
    private LocalDate ngayPDuyet;
    @Column(name = "TYPE")
    private String type;
    @Column(name = "GHI_CHU")
    private String ghiChu;
    @Column(name = "SO_BB_LM_BG")
    private String soBbLmBg;
    @Column(name = "BB_LM_BG_ID")
    private Long bbLmBgId;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "HDR_ID")
    private List<DcnbBBKetThucNKDtl> dcnbBBKetThucNKDtl = new ArrayList<>();
    @Transient
    private List<FileDinhKem> fileDinhKems = new ArrayList<>();
    @Transient
    private String tenTrangThai;

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
        this.tenTrangThai = TrangThaiAllEnum.getLabelById(this.trangThai);
    }
}
