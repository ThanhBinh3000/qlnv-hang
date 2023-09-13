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
@Table(name = DcnbBBNTBQHdr.TABLE_NAME)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DcnbBBNTBQHdr extends BaseEntity implements Serializable, Cloneable{
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "DCNB_BB_NT_BQ_HDR";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = DcnbBBNTBQHdr.TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = DcnbBBNTBQHdr.TABLE_NAME
            + "_SEQ", allocationSize = 1, name = DcnbBBNTBQHdr.TABLE_NAME + "_SEQ")
    private Long id;
    @Column(name = "LOAI_DC")
    private String loaiDc;
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
    private String soBban;
    @Column(name = "NGAY_LAP")
    private LocalDate ngayLap;
    @Column(name = "NGAY_KET_THUC_NT")
    private LocalDate ngayKetThucNt;
    @Column(name = "SO_QD_DC_CUC")
    private String soQdDcCuc;
    @Column(name = "QD_DC_CUC_ID")
    private Long qdDcCucId;
    @Column(name = "NGAY_QD_DC_CUC")
    private LocalDate ngayQdDcCuc;
    @Column(name = "THU_KHO")
    private String thuKho;
    @Column(name = "KTHUAT_VIEN")
    private String kthuatVien;
    @Column(name = "KE_TOAN")
    private String keToan;
    @Column(name = "LD_CHI_CUC")
    private String ldChiCuc;
    @Column(name = "ID_KE_HOACH_DTL")
    private Long idKeHoachDtl;
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
    @Column(name = "LOAI_HINH_KHO")
    private String loaiHinhKho;
    @Column(name = "TICH_LUONG_KHA_DUNG")
    private Double tichLuongKhaDung;
    @Column(name = "DS_PHIEU_NHAP_KHO")
    private String dsPhieuNhapKho;
    @Column(name = "SL_THUC_NHAP_DC")
    private BigDecimal slThucNhapDc;
    @Column(name = "SO_BB_NHAP_DAY_KHO")
    private String soBbNhapDayKho;
    @Column(name = "BB_NHAP_DAY_KHO_ID")
    private Long bbNhapDayKhoId;
    @Column(name = "HINH_THUC_BAO_QUAN")
    private String hinhThucBaoQuan;
    @Column(name = "PHUONG_THUC_BAO_QUAN")
    private String phuongThucBaoQuan;
    @Column(name = "DINH_MUC_DUOC_GIAO")
    private Double dinhMucDuocGiao;
    @Column(name = "DINH_MUC_TT")
    private Double dinhMucTT;
    @Column(name = "TONG_SO_KINH_PHI_DA_TH")
    private BigDecimal tongKinhPhiDaTh;
    @Column(name = "TONG_SO_KINH_PHI_DA_TH_BC")
    private String tongKinhPhiDaThBc;
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
    @Column(name = "NHAN_XET")
    private String nhanXet;
    @Access(value=AccessType.PROPERTY)
    @Column(name = "TRANG_THAI")
    private String trangThai;
    @Column(name = "NGUOI_TAO_ID")
    private Long nguoiTaoId;
    @Column(name = "NGUOI_SUA_ID")
    private Long nguoiSuaId;
    @Column(name = "NGAY_GDUYET")
    private LocalDate ngayGduyet;
    @Column(name = "NGUOI_GDUYET_ID")
    private Long nguoiGduyetId;
    @Column(name = "NGAY_PDUYET")
    private LocalDate ngayPduyet;
    @Column(name = "NGUOI_PDUYET_ID")
    private Long nguoiPduyetId;
    @Column(name = "LY_DO_TU_CHOI")
    private String lyDoTuChoi;
    @Column(name = "NGAY_P_DUYET_KT")
    private LocalDate ngayPDuyetKt;
    @Column(name = "NGUOI_P_DUYET_KT")
    private Long nguoiPDuyeKt;
    @Column(name = "LAN")
    private Long lan;
    @Column(name = "LOAI_HINH_BAO_QUAN")
    private String loaiHinhBaoQuan;
    @Column(name = "DON_VI_TINH")
    private String donViTinh;
    @Transient
    private List<FileDinhKem> fileDinhKems = new ArrayList<>();
    @Transient
    private String tenTrangThai;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "HDR_ID")
    private List<DcnbBBNTBQDtl> dcnbBBNTBQDtl = new ArrayList<>();

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
        this.tenTrangThai = TrangThaiAllEnum.getLabelById(this.trangThai);
    }
}
