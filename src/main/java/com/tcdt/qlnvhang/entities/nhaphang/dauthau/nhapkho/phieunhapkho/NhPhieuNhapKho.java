package com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhapkho.phieunhapkho;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.entities.TrangThaiBaseEntity;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhapkho.bangkecanhang.NhBangKeCanHang;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.util.Contains;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
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

    @Column(name = "SO_BIEN_BAN_GUI_HANG")
    private String soBienBanGuiHang;

    @Column(name = "SO_QD_GIAO_NV_NH")
    private String soQdGiaoNvNh; // HhQdGiaoNvuNhapxuatHdr

    @Column(name = "ID_QD_GIAO_NV_NH")
    private Long idQdGiaoNvNh; // HhQdGiaoNvuNhapxuatHdr

    @Column(name = "SO_PHIEU_NHAP_KHO")
    private String soPhieuNhapKho;

    @Column(name = "SO_HD")
    private String soHd;

    @Column(name = "NGAY_HD")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayHd;

    @Column(name = "NGAY_NHAP_KHO")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayNhapKho;

    @Column(name = "THOI_GIAN_GIAO_NHAN")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_TIME_STR)
    private Date thoiGianGiaoNhan;

    @Column(name = "NGAY_TAO_PHIEU")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayTaoPhieu;

    @Column(name = "TAI_KHOAN_NO")
    private BigDecimal taiKhoanNo;

    @Column(name = "TAI_KHOAN_CO")
    private BigDecimal taiKhoanCo;

    @Column(name = "LOAI_HINH_NHAP")
    private String loaiHinhNhap;

    @Transient
    private String tenNguoiTao;

    @Column(name = "ID_DDIEM_GIAO_NV_NH")
    private Long idDdiemGiaoNvNh;

    @Column(name = "MA_DIEM_KHO")
    private String maDiemKho;

    @Transient
    private String tenDiemKho;

    @Column(name = "MA_NHA_KHO")
    private String maNhaKho;

    @Transient
    private String tenNhaKho;

    @Column(name = "MA_NGAN_KHO")
    private String maNganKho;

    @Transient
    private String tenNganKho;

    @Column(name = "MA_LO_KHO")
    private String maLoKho;

    @Transient
    private String tenLoKho;

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
    private String tenLoaiVthh;

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

    @Column(name = "NGUOI_GIAO_HANG")
    private String nguoiGiaoHang;

    @Column(name = "CMT_NGUOI_GIAO_HANG")
    private String cmtNguoiGiaoHang;

    @Column(name = "DON_VI_GIAO_HANG")
    private String donViGiaoHang;

    @Column(name = "DIA_CHI_NGUOI_GIAO")
    private String diaChiNguoiGiao;

    @Column(name = "KE_TOAN_TRUONG")
    private String keToanTruong;

    @Column(name = "GHI_CHU")
    private String ghiChu;

    @Transient
    private List<NhPhieuNhapKhoCt> hangHoaList = new ArrayList<>();

    @Transient
    private List<FileDinhKem> fileDinhKems = new ArrayList<>();
    @Transient
    private List<FileDinhKem> chungTuKemTheo = new ArrayList<>();

    @Transient
    private List<NhPhieuNhapKhoCt1> chiTiet1s = new ArrayList<>();

    @Transient
    private NhBangKeCanHang bangKeCanHang;

    @Transient
    private BigDecimal soLuongNhapKho;
}
