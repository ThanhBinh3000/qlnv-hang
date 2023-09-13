package com.tcdt.qlnvhang.table.dieuchuyennoibo;

import com.tcdt.qlnvhang.entities.BaseEntity;
import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.table.FileDinhKem;
import lombok.*;
import org.apache.tomcat.jni.Local;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = DcnbBienBanHaoDoiHdr.TABLE_NAME)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DcnbBienBanHaoDoiHdr extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "DCNB_BIEN_BAN_HAO_DOI_HDR";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = DcnbBienBanHaoDoiHdr.TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = DcnbBienBanHaoDoiHdr.TABLE_NAME + "_SEQ", allocationSize = 1, name = DcnbBienBanHaoDoiHdr.TABLE_NAME + "_SEQ")
    private Long id;

    @Column(name = "LOAI_DC")
    private String loaiDc;

    @Column(name = "THAY_DOI_THU_KHO")
    private Boolean thayDoiThuKho;

    @Column(name = "LOAI_VTHH")
    private String loaiVthh;

    @Column(name = "TEN_LOAI_VTHH")
    private String tenLoaiVthh;

    @Column(name = "CLOAI_VTHH")
    private String cloaiVthh;

    @Column(name = "TEN_CLOAI_VTHH")
    private String tenCloaiVthh;

    @Column(name = "NAM")
    private Integer nam;

    @Column(name = "MA_DVI")
    private String maDvi;

    @Column(name = "TEN_DVI")
    private String tenDvi;

    @Column(name = "MA_QHNS")
    private String maQhns;

    @Column(name = "SO_BBAN")
    private String soBienBan;

    @Column(name = "NGAY_LAP")
    private LocalDate ngayLap;

    @Column(name = "SO_QD_DC_CUC")
    private String soQdinhDcc;

    @Column(name = "QD_DC_CUC_ID")
    private Long qDinhDccId;

    @Column(name = "NGAY_QD_DC_CUC")
    private LocalDate ngayKyQdDcc;

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

    @Column(name = "SO_BB_TINH_KHO")
    private String soBbTinhKho;

    @Column(name = "BB_TINH_KHO_HDR_ID")
    private Long bBTinhKhoId;

    @Column(name = "TONG_SL_XUAT_THEO_QD")
    private Double tongSlXuatTheoQd;

    @Column(name = "NGAY_KET_THUC_XUAT_QD")
    private LocalDate ngayKetThucXuatQd;

    @Column(name = "TONG_SL_XUAT_THEO_TT")
    private Double tongSlXuatTheoTt;

    @Column(name = "NGAY_KET_THUC_XUAT_TT")
    private LocalDate ngayKetThucXuatTt;
    @Column(name = "NGAY_BAT_DAU_XUAT")
    private LocalDate ngayBatDauXuat;
    @Column(name = "NGAY_KET_THUC_XUAT")
    private LocalDate ngayKetThucXuat;
    @Column(name = "SL_HAO_TT")
    private Double slHaoTt;

    @Column(name = "TI_LE_HAO_TT")
    private Double tiLeHaoTt;

    @Column(name = "SL_HAO_VUOT_DM")
    private Double slHaoVuotDm;

    @Column(name = "TI_LE_HAO_VUOT_DM")
    private Double tiLeHaoVuotDm;

    @Column(name = "NGUYEN_NHAN")
    private String nguyenNhan;

    @Column(name = "KIEN_NGHI")
    private String kienNghi;

    @Column(name = "GHI_CHU")
    private String ghiChu;

    @Column(name = "THU_KHO")
    private String thuKho;

    @Column(name = "NGAY_PDUYET_KTV_BAO_QUAN")
    private LocalDate ngayPduyetKtvBQ;

    @Column(name = "KTV_BAO_QUAN")
    private String ktvBaoQuan;

    @Column(name = "KTV_BAO_QUAN_ID")
    private Long ktvBaoQuanId;

    @Column(name = "NGAY_PDUYET_KE_TOAN")
    private LocalDate ngayPduyetKt;

    @Column(name = "KE_TOAN")
    private String keToan;

    @Column(name = "KE_TOAN_ID")
    private Long keToanId;

    @Column(name = "NGAY_PDUYET_LDCCUC")
    private LocalDate ngayPduyetLdcc;

    @Column(name = "LANH_DAO_CHI_CUC")
    private String lanhDaoChiCuc;

    @Column(name = "LANH_DAO_CHI_CUC_ID")
    private Long lanhDaoChiCucId;

    @Column(name = "TRANG_THAI")
    @Access(value=AccessType.PROPERTY)
    private String trangThai;

    @Column(name = "NGAY_GDUYET")
    private LocalDate ngayGduyet;

    @Column(name = "NGUOI_GDUYET_ID")
    private Long nguoiGduyetId;

    @Column(name = "LY_DO_TU_CHOI")
    private String lyDoTuChoi;

    @Column(name = "PHIEU_KT_CHAT_LUONG_HDR_ID")
    private Long phieuKtChatLuongHdrId;
    @Column(name = "SO_PHIEU_KT_CHAT_LUONG")
    private String soPhieuKtChatLuong;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "HDR_ID")
    private List<DcnbBienBanHaoDoiTtDtl> danhSachBangKe = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "HDR_ID")
    private List<DcnbBienBanHaoDoiDtl> thongTinHaoHut = new ArrayList<>();

    @Transient
    private List<FileDinhKem> fileDinhKems = new ArrayList<>();

    @Transient
    private String tenTrangThai;
    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
        this.tenTrangThai = TrangThaiAllEnum.getLabelById(this.trangThai);
    }
}
