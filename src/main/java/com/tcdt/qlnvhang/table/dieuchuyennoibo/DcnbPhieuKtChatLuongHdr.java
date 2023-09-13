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
@Table(name = DcnbPhieuKtChatLuongHdr.TABLE_NAME)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DcnbPhieuKtChatLuongHdr extends BaseEntity implements Serializable, Cloneable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "DCNB_PHIEU_KT_CHAT_LUONG_HDR";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DCNB_PHIEU_KT_CHLUONG_HDR_SEQ")
    @SequenceGenerator(sequenceName = "DCNB_PHIEU_KT_CHLUONG_HDR_SEQ", allocationSize = 1, name = "DCNB_PHIEU_KT_CHLUONG_HDR_SEQ")
    private Long id;

    @Column(name = "NAM")
    private Integer nam;

    @Column(name = "MA_DVI")
    private String maDvi;

    @Column(name = "MA_QHNS")
    private String maQhns;

    @Column(name = "QDINH_DC_ID")
    private Long qdDcId;

    @Column(name = "SO_QDINH_DC")
    private String soQdinhDc;
    @Column(name = "NGAY_QDINH_DCC")
    private LocalDate ngayQdinhDcc;

    @Column(name = "SO_PHIEU")
    private String soPhieu;

    @Column(name = "NGAY_LAP_PHIEU")
    private LocalDate ngayLapPhieu;

    @Column(name = "NGUOI_KT")
    private String nguoiKt;

    @Column(name = "NGUOI_KT_ID")
    private Long nguoiKtId;

    @Column(name = "TRUONG_PHONG_NGUOI_KT_ID")
    private Long tpNguoiKtId;

    @Column(name = "TRUONG_PHONG_NGUOI_KT")
    private String tpNguoiKt;

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

    @Column(name = "TEN_THU_KHO")
    private String tenThuKho;

    @Column(name = "THU_KHO_ID")
    private Long thuKhoId;

    @Column(name = "MA_DIEM_KHO_XUAT")
    private String maDiemKhoXuat;

    @Column(name = "TEN_DIEM_KHO_XUAT")
    private String tenDiemKhoXuat;

    @Column(name = "MA_NHA_KHO_XUAT")
    private String maNhaKhoXuat;

    @Column(name = "TEN_NHA_KHO_XUAT")
    private String tenNhaKhoXuat;

    @Column(name = "MA_NGAN_KHO_XUAT")
    private String maNganKhoXuat;

    @Column(name = "TEN_NGAN_KHO_XUAT")
    private String tenNganKhoXuat;

    @Column(name = "MA_LO_KHO_XUAT")
    private String maLoKhoXuat;

    @Column(name = "TEN_LO_KHO_XUAT")
    private String tenLoKhoXuat;

    @Column(name = "SO_BB_LAY_MAU")
    private String soBbLayMau;

    @Column(name = "BB_LAY_MAU_ID")
    private Long bbLayMauId;

    @Column(name = "NGAY_LAY_MAU")
    private LocalDate ngayLayMau;

    @Column(name = "NGAY_KIEM")
    private LocalDate ngayKiem;

    @Column(name = "LOAI_VTHH")
    private String loaiVthh;

    @Column(name = "TEN_LOAI_VTHH")
    private String tenLoaiVthh;

    @Column(name = "CLOAI_VTHH")
    private String cloaiVthh;

    @Column(name = "TEN_CLOAI_VTHH")
    private String tenCloaiVthh;

    @Column(name = "HINH_THUC_BQ")
    private String hinhThucBq;

    @Column(name = "DANH_GIA_CAM_QUAN")
    private String danhGiaCamQuan;

    @Column(name = "NHAN_XET_KET_LUAN")
    private String nhanXetKetLuan;

    @Column(name = "NGUOI_PDUYET")
    private Long nguoiPDuyet;

    @Column(name = "NGAY_PDUYET")
    private LocalDate ngayPDuyet;

    @Column(name = "TRANG_THAI")
    @Access(value = AccessType.PROPERTY)
    private String trangThai;

    @Column(name = "NGUOI_GDUYET")
    private Long nguoiGDuyet;

    @Column(name = "NGAY_GDUYET")
    private LocalDate ngayGDuyet;

    @Column(name = "LY_DO_TU_CHOI")
    private String lyDoTuChoi;

    @Column(name = "THAY_DOI_THU_KHO")
    private Boolean thayDoiThuKho;

    @Column(name = "NGAY_DUYET_TP")
    private LocalDate ngayDuyetTp;

    @Column(name = "NGUOI_DUYET_TP")
    private Long nguoiDuyetTp;

    @Column(name = "BB_TINH_KHO_ID")
    private Long bbTinhKhoId;

    @Column(name = "SO_BB_TINH_KHO")
    private String soBbTinhKho;

    @Column(name = "NGAY_XUAT_DOC_KHO")
    private LocalDate ngayXuatDocKho;

    @Column(name = "BB_HAO_DOI_ID")
    private Long bbHaoDoiId;

    @Column(name = "SO_BB_HAO_DOI")
    private String soBbHaoDoi;

    @Column(name = "NGUOI_GIAO_HANG")
    private String nguoiGiaoHang;
    @Column(name = "SO_CMT")
    private String soCmt;
    @Column(name = "DON_VI_GIAO_HANG")
    private String dVGiaoHang;
    @Column(name = "DIA_CHI_DON_VI_GIAO_HANG")
    private String diaChiDonViGiaoHang;
    @Column(name = "BIEN_SO_XE")
    private String bienSoXe;
    @Column(name = "SL_NHAP_THEO_QD")
    private BigDecimal slNhapTheoQd;
    @Column(name = "SL_NHAP_THEO_KB")
    private BigDecimal slNhapTheoKb;
    @Column(name = "SL_NHAP_THEO_KT")
    private BigDecimal slNhapTheoKt;
    @Column(name = "SO_CHUNG_THU_GIAM_DINH")
    private String soChungThuGiamDinh;
    @Column(name = "NGAY_GIAM_DINH")
    private LocalDate ngayGiamDinh;
    @Column(name = "TO_CHUC_GIAM_DINH")
    private String toChucGiamDinh;
    @Column(name = "DON_VI_TINH")
    private String donViTinh;
    @Column(name = "TICH_LUONG_KHA_DUNG")
    private BigDecimal tichLuongKhaDung;
    @Column(name = "BB_NT_BQ_LD_ID")
    private String bBNtLdId;
    @Column(name = "SO_BB_NT_BQ_LD")
    private String soBBNtLd;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "HDR_ID")
    private List<DcnbPhieuKtChatLuongDtl> dcnbPhieuKtChatLuongDtl = new ArrayList<>();
    @Transient
    private List<FileDinhKem> phieuKTCLDinhKem = new ArrayList<>();
    @Transient
    private List<FileDinhKem> bienBanLayMauDinhKem = new ArrayList<>();
    @Transient
    private String tenTrangThai;

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
        this.tenTrangThai = TrangThaiAllEnum.getLabelById(this.trangThai);
    }
}
