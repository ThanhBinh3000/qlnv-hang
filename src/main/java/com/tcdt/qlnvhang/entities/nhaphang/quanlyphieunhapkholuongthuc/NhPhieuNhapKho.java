package com.tcdt.qlnvhang.entities.nhaphang.quanlyphieunhapkholuongthuc;

import com.tcdt.qlnvhang.entities.BaseEntity;
import com.tcdt.qlnvhang.entities.TrangThaiBaseEntity;
import com.tcdt.qlnvhang.table.FileDinhKem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = NhPhieuNhapKho.TABLE_NAME)
public class NhPhieuNhapKho extends TrangThaiBaseEntity implements Serializable {
    private static final long serialVersionUID = -1880694858465293452L;
    public static final String TABLE_NAME = "NH_PHIEU_NHAP_KHO";
    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PHIEU_NHAP_KHO_SEQ")
//    @SequenceGenerator(sequenceName = "PHIEU_NHAP_KHO_SEQ", allocationSize = 1, name = "PHIEU_NHAP_KHO_SEQ")
    @Column(name = "ID")
    private Long id;

    @Column(name = "SO_PHIEU_KTRA_CL")
    private String soPhieuKtraCl;

    @Column(name = "SO_QD_GIAO_NV_NH")
    private String soQdGiaoNvNh; // HhQdGiaoNvuNhapxuatHdr

    @Column(name = "ID_QD_GIAO_NV_NH")
    private Long idQdGiaoNvNh; // HhQdGiaoNvuNhapxuatHdr

    @Column(name = "SO_PHIEU_NHAP_KHO")
    private String soPhieuNhapKho;

    @Column(name = "SO_HD")
    private String soHd;

    @Column(name = "NGAY_HD")
    private LocalDate ngayHd;

    @Column(name = "NGAY_NHAP_KHO")
    private LocalDate ngayNhapKho;

    @Column(name = "NGUOI_GIAO_HANG")
    private String nguoiGiaoHang;

    @Column(name = "THOI_GIAN_GIAO_NHAN")
    private LocalDateTime thoiGianGiaoNhan;

    @Column(name = "NGAY_TAO_PHIEU")
    private LocalDate ngayTaoPhieu;

    @Column(name = "TAI_KHOAN_NO")
    private BigDecimal taiKhoanNo;

    @Column(name = "TAI_KHOAN_CO")
    private BigDecimal taiKhoanCo;

    @Column(name = "LOAI_HINH_NHAP")
    private String loaiHinhNhap;

    @Column(name = "NGAY_TAO")
    private LocalDate ngayTao;

    @Column(name = "NGUOI_TAO_ID")
    private Long nguoiTaoId;

    @Column(name = "NGAY_SUA")
    private LocalDate ngaySua;

    @Column(name = "NGUOI_SUA_ID")
    private Long nguoiSuaId;

    @Column(name = "ID_DDIEM_GIAO_NV_NH")
    private Long idDdiemGiaoNvNh;

    @Column(name = "MA_DIEM_KHO")
    private String maDiemKho;

    @Column(name = "MA_NHA_KHO")
    private String maNhaKho;

    @Column(name = "MA_NGAN_KHO")
    private String maNganKho;

    @Column(name = "MA_LO_KHO")
    private String maLoKho;

    @Column(name = "MA_DVI")
    private String maDvi;

    @Transient
    private String tenDvi;

    @Column(name = "MA_QHNS")
    private String maQhns;

    @Column(name = "CAP_DVI")
    private String capDvi;

    @Column(name = "SO")
    private Integer so;

    @Column(name = "NAM")
    private Integer nam;

    // Vat tu
    @Column(name = "HO_SO_KY_THUAT_ID")
    private Long hoSoKyThuatId;

    @Column(name = "TONG_SO_LUONG")
    private BigDecimal tongSoLuong;

    @Column(name = "TONG_SO_TIEN")
    private BigDecimal tongSoTien;

    @Column(name = "LOAI_VTHH")
    private String loaiVthh;

    @Transient
    private String tenVthh;

    @Column(name = "CLOAI_VTHH")
    private String cloaiVthh;

    @Transient
    private String tenCloaiVthh;

    @Column(name = "MO_TA_HANG_HOA")
    private String moTaHangHoa;

    @Column(name = "SO_NO")
    private Integer soNo; // Số nợ

     @Column(name = "SO_CO")
    private Integer soCo; // Số có

    @Column(name = "CMT_NGUOI_GIAO_HANG")
    private String cmtNguoiGiaoHang;

    @Column(name = "DON_VI_GIAO_HANG")
    private String donViGiaoHang;

    @Column(name = "DIA_CHI")
    private String diaChi;

    @Column(name = "KE_TOAN_TRUONG")
    private String keToanTruong;

    @Column(name = "GHI_CHU")
    private String ghiChu;


    @Transient
    private List<NhPhieuNhapKhoCt> hangHoaList = new ArrayList<>();

    @Transient
    private List<FileDinhKem> fileDinhKems = new ArrayList<>();

    @Transient
    private List<NhPhieuNhapKhoCt1> chiTiet1s = new ArrayList<>();
}
