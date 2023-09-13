package com.tcdt.qlnvhang.table.dieuchuyennoibo;

import com.tcdt.qlnvhang.entities.BaseEntity;
import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.table.FileDinhKem;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = DcnbBienBanLayMauHdr.TABLE_NAME)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DcnbBienBanLayMauHdr extends BaseEntity implements Serializable, Cloneable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "DCNB_BIEN_BAN_LAY_MAU_HDR";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = DcnbBienBanLayMauHdr.TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = DcnbBienBanLayMauHdr.TABLE_NAME
            + "_SEQ", allocationSize = 1, name = DcnbBienBanLayMauHdr.TABLE_NAME + "_SEQ")
    private Long id;

    @Column(name = "LOAI_BB")
    private String loaiBb;

    @Column(name = "THOI_HAN_DIEU_CHUYEN")
    private LocalDate thoiHanDieuChuyen;

    @Column(name = "NAM")
    private Integer nam;

    @Column(name = "MA_DVI")
    private String maDvi;

    @Column(name = "TEN_DVI")
    private String tenDvi;

    @Column(name = "QHNS_ID")
    private Long qhnsId;

    @Column(name = "MA_QHNS")
    private String maQhns;

    @Column(name = "QDINH_DCC_ID")
    private Long qdccId;

    @Column(name = "SO_QDINH_DCC")
    private String soQdinhDcc;

    @Column(name = "KTV_BAO_QUAN")
    private String ktvBaoQuan;

    @Column(name = "KTV_BAO_QUAN_ID")
    private Long ktvBaoQuanId;

    @Column(name = "THU_KHO")
    private Long thuKho;

    @Column(name = "TEN_THU_KHO")
    private String tenThuKho;

    @Column(name = "DON_VI_TINH")
    private String donViTinh;

    @Column(name = "SO_BB_LAY_MAU")
    private String soBbLayMau;

    @Column(name = "NGAY_LAY_MAU")
    private LocalDate ngayLayMau;

    @Column(name = "SO_BB_NHAP_DAY_KHO")
    private String soBbNhapDayKho;

    @Column(name = "BB_NHAP_DAY_KHO_ID")
    private Long bBNhapDayKhoId;

    @Column(name = "NGAY_NHAP_DAY_KHO")
    private LocalDate ngayNhapDayKho;

    @Column(name = "SO_BB_NTBQLD")
    private String soBbNtBqLd;

    @Column(name = "BB_NTBQLD_ID")
    private Long bbNtBqLdId;

    @Column(name = "DV_KIEM_NGHIEM")
    private String dViKiemNghiem;

    @Column(name = "DIA_DIEM_LAY_MAU")
    private String diaDiemLayMau;

    @Column(name = "LOAI_VTHH")
    private String loaiVthh;

    @Column(name = "TEN_LOAI_VTHH")
    private String tenLoaiVthh;

    @Column(name = "CLOAI_VTHH")
    private String cloaiVthh;

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

    @Column(name = "THAY_DOI_THU_KHO")
    private Boolean thayDoiThuKho;

    @Column(name = "SO_LUONG_MAU")
    private Long soLuongMau;

    @Column(name = "PP_LAY_MAU")
    private String pPLayMau;

    @Column(name = "CHI_TIEU_KIEM_TRA")
    private String chiTieuKiemTra;

    @Column(name = "TRANG_THAI")
    @Access(value = AccessType.PROPERTY)
    private String trangThai;

    @Column(name = "LY_DO_TU_CHOI")
    private String lyDoTuChoi;

    @Column(name = "NGUOI_GDUYET")
    private Long nguoiGDuyet;

    @Column(name = "NGAY_GDUYET")
    private LocalDate ngayGDuyet;

    @Column(name = "NGUOI_PDUYET")
    private Long nguoiPDuyet;

    @Column(name = "NGAY_PDUYET")
    private LocalDate ngayPDuyet;

    @Column(name = "KET_QUA_NIEM_PHONG")
    private String ketQuaNiemPhong;

    @Column(name = "DIA_DIEM_BAN_GIAO")
    private String diaDiemBanGiao;

    @Column(name = "LOAI_DC")
    private String loaiDc;

    @Column(name = "TYPE")
    private String type;

    @Column(name = "SO_BB_TINH_KHO")
    private String soBbTinhKho;

    @Column(name = "BB_TINH_KHO_ID")
    private Long bbTinhKhoId;

    @Column(name = "NGAY_XUAT_DOC_KHO")
    private LocalDate ngayXuatDocKho;

    @Column(name = "SO_BB_HAO_DOI")
    private String soBbHaoDoi;

    @Column(name = "BB_HAO_DOI_ID")
    private Long bbHaoDoiId;

    @Column(name = "GHI_CHU")
    private String ghiChu;

    @Transient
    private List<FileDinhKem> canCu = new ArrayList<>();

    @Transient
    private List<FileDinhKem> bienBanLayMauDinhKem = new ArrayList<>();

    @Transient
    private List<FileDinhKem> fileDinhKemChupMauNiemPhong = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "HDR_ID")
    private List<DcnbBienBanLayMauDtl> dcnbBienBanLayMauDtl = new ArrayList<>();
    @Transient
    private String tenTrangThai;
    @Transient
    private String maDviCha;
    @Transient
    private String tenDviCha;

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
        this.tenTrangThai = TrangThaiAllEnum.getLabelById(this.trangThai);
    }

    public void setMaDvi(String maDvi) {
        this.maDvi = maDvi;
        // get đơn vị cấp cha
        this.maDviCha = this.maDvi.substring(0, this.maDvi.length() - 2);
    }
}
