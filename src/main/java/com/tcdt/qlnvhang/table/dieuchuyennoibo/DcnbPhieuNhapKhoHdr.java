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
@Table(name = DcnbPhieuNhapKhoHdr.TABLE_NAME)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DcnbPhieuNhapKhoHdr extends BaseEntity implements Serializable, Cloneable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "DCNB_PHIEU_NHAP_KHO_HDR";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = DcnbPhieuNhapKhoHdr.TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = DcnbPhieuNhapKhoHdr.TABLE_NAME
            + "_SEQ", allocationSize = 1, name = DcnbPhieuNhapKhoHdr.TABLE_NAME + "_SEQ")
    private Long id;
    private String loaiDc;
    private Integer nam;
    private String maDvi;
    private String tenDvi;
    private String maQhns;
    private String soPhieuNhapKho;
    private LocalDate ngayLap;
    private BigDecimal soNo;
    private BigDecimal soCo;
    @Column(name = "SO_BB_CB_KHO")
    private String soBbCbKho;
    @Column(name = "BB_CB_KHO_ID")
    private Long bBCbKhoId;
    private String soQdDcCuc;
    private Long qdDcCucId;
    private LocalDate ngayQdDcCuc;
    private Long idKeHoachDtl;
    private String maDiemKho;
    private String maNhaKho;
    private String maNganKho;
    private String maLoKho;
    private Boolean thayDoiThuKho;
    private String soPhieuKtraCluong;
    private Long idPhieuKtraCluong;
    private String loaiVthh;
    private String cloaiVthh;
    @Column(name = "TEN_LOAI_VTHH")
    private String tenLoaiVthh;
    @Column(name = "TEN_CLOAI_VTHH")
    private String tenCloaiVthh;
    @Column(name = "ID_NGUOI_LAP")
    private Long idNguoiLap;
    @Column(name = "TEN_NGUOI_LAP")
    private String tenNguoiLap;
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
    private String keToanTruong;

    private String hoVaTenNguoiGiao;
    private String cmndNguoiGiao;
    private String donViNguoiGiao;
    private String diaChi;
    private LocalDate tgianGiaoNhanHang;
    private String loaiHinhNx;
    private String kieuNx;
    private String bbNghiemThuBqld;
    private String soBangKeCh;
    private Long bangKeChId;
    private String soBangKeVt;
    private Long bangKeVtId;
    private BigDecimal soLuongQdDcCuc;
    @Column(name = "TONG_SO_LUONG")
    private BigDecimal tongSoLuong;
    @Column(name = "TONG_SO_LUONG_BC")
    private String tongSoLuongBc;
    @Column(name = "TONG_KINH_PHI")
    private BigDecimal tongKinhPhi;
    @Column(name = "TONG_KINH_PHI_BC")
    private String tongKinhPhiBc;
    private String ghiChu;
    @Access(value=AccessType.PROPERTY)
    private String trangThai;
    private String lyDoTuChoi;
    @Column(name = "NGUOI_GDUYET")
    private Long nguoiGDuyet;
    @Column(name = "NGAY_GDUYET")
    private LocalDate ngayGDuyet;
    @Column(name = "NGUOI_PDUYET")
    private Long nguoiPDuyet;
    @Column(name = "NGAY_PDUYET")
    private LocalDate ngayPDuyet;
    private String tenDiemKho;
    private String tenNhaKho;
    private String tenNganKho;
    private String tenLoKho;

    @Transient
    private List<FileDinhKem> chungTuDinhKem = new ArrayList<>();
    @Transient
    private List<FileDinhKem> fileDinhKems = new ArrayList<>();
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "HDR_ID")
    private List<DcnbPhieuNhapKhoDtl> children = new ArrayList<>();
    @Transient
    private String tenTrangThai;
    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
        this.tenTrangThai = TrangThaiAllEnum.getLabelById(this.trangThai);
    }
}
