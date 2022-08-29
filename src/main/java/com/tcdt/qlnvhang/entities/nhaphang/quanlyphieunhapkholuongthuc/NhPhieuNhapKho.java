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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PHIEU_NHAP_KHO_SEQ")
    @SequenceGenerator(sequenceName = "PHIEU_NHAP_KHO_SEQ", allocationSize = 1, name = "PHIEU_NHAP_KHO_SEQ")
    @Column(name = "ID")
    private Long id;

    @Column(name = "PHIEU_KT_CL_ID")
    private Long phieuKtClId;

    @Column(name = "QDGNVNX_ID")
    private Long qdgnvnxId; // HhQdGiaoNvuNhapxuatHdr

    @Column(name = "SO_PHIEU")
    private String soPhieu;

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

    @Column(name = "MA_DIEM_KHO")
    private String maDiemKho;

    @Column(name = "MA_NHA_KHO")
    private String maNhaKho;

    @Column(name = "MA_NGAN_KHO")
    private String maNganKho;

    @Column(name = "MA_NGAN_LO")
    private String maNganLo;

    @Column(name = "MA_DVI")
    private String maDvi;

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

    private String cloaiVthh;
    @Transient
    private String tenCloaiVthh;

    private String moTaHangHoa;

    @Transient
    private List<NhPhieuNhapKhoCt> hangHoaList = new ArrayList<>();

    @Transient
    private List<FileDinhKem> chungTus = new ArrayList<>();

    @Transient
    private List<NhPhieuNhapKhoCt1> chiTiet1s = new ArrayList<>();
}
