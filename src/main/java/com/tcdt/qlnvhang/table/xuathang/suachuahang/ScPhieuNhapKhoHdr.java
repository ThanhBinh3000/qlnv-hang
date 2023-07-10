package com.tcdt.qlnvhang.table.xuathang.suachuahang;

import com.tcdt.qlnvhang.entities.BaseEntity;
import com.tcdt.qlnvhang.table.FileDinhKem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = ScPhieuNhapKhoHdr.TABLE_NAME)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScPhieuNhapKhoHdr extends BaseEntity implements Serializable {
    public static final String TABLE_NAME = "SC_PHIEU_NHAP_KHO_HDR";
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = ScPhieuNhapKhoHdr.TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = ScPhieuNhapKhoHdr.TABLE_NAME
            + "_SEQ", allocationSize = 1, name = ScPhieuNhapKhoHdr.TABLE_NAME + "_SEQ")
    private Long id;
    private Integer nam;
    private String maDvi;
    private String tenDvi;
    private String maQhns;
    private String soPhieuNhapKho;
    private LocalDate ngayLap;
    private LocalDate ngayNhapKho;
    private BigDecimal soNo;
    private BigDecimal soCo;
    private String soQdGiaoNvNhap;
    private Long qdGiaoNvNhapId;
    private LocalDate ngayKyQdGiaoNvNhap;
    private String maDiemKho;
    private String tenDiemKho;
    private String maNhaKho;
    private String tenNhaKho;
    private String maNganKho;
    private String tenNganKho;
    private String maLoKho;
    private String tenLoKho;
    @Column(name = "SO_PHIEU_KTRA_CL")
    private String soPhieuKtraCluong;
    @Column(name = "ID_PHIEU_KTRA_CL")
    private Long idPhieuKtraCluong;
    @Column(name = "TEN_LOAI_HANG")
    private String tenLoaiHang;
    @Column(name = "TEN_CHUNG_LOAI_HANG")
    private String tenChungLoaiHang;
    @Column(name = "ID_NGUOI_LAP")
    private Long idNguoiLap;
    @Column(name = "TEN_NGUOI_LAP")
    private String tenNguoiLap;
    @Column(name = "ID_LANH_DAO")
    private Long idLanhDao;
    @Column(name = "TEN_LANH_DAO")
    private String tenLanhDao;
    @Column(name = "ID_THU_KHO")
    private Long idThuKho;
    @Column(name = "TEN_THU_KHO")
    private String tenThuKho;
    @Column(name = "ID_KY_THUAT_VIEN")
    private Long idKyThuatVien;
    @Column(name = "TEN_KY_THUAT_VIEN")
    private String tenKyThuatVien;
    @Column(name = "ID_KE_TOAN_TRUONG")
    private Long idKeToanTruong;
    @Column(name = "TEN_KE_TOAN_TRUONG")
    private String keToanTruong;
    @Column(name = "HO_TEN_NGUOI_GIAO")
    private String hoVaTenNguoiGiao;
    @Column(name = "CMND_NGUOI_GIAO")
    private String cmndNguoiGiao;
    @Column(name = "DVI_NGUOI_GIAO")
    private String donViNguoiGiao;
    private String diaChi;
    @Column(name = "THOI_GIAN_GIAO_NHAN_HANG")
    private LocalDate tgianGiaoNhanHang;
    private Long bangKeNhapVtId;
    private String soBangKeNhapVt;
    @Column(name = "TONG_SO_LUONG")
    private BigDecimal tongSoLuong;
    @Column(name = "TONG_SO_LUONG_BC")
    private String tongSoLuongBc;
    @Column(name = "TONG_KINH_PHI")
    private BigDecimal tongKinhPhi;
    @Column(name = "TONG_KINH_PHI_BC")
    private String tongKinhPhiBc;
    private String ghiChu;
    private String trangThai;
    @Transient
    private List<FileDinhKem> fileDinhKems = new ArrayList<>();
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "HDR_ID")
    private List<ScPhieuNhapKhoDtl> children = new ArrayList<>();
}
