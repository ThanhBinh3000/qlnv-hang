package com.tcdt.qlnvhang.table.dieuchuyennoibo;

import com.tcdt.qlnvhang.entities.BaseEntity;
import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.table.FileDinhKem;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Table(name = "DCNB_TH_KE_HOACH_DCTC_HDR")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class THKeHoachDieuChuyenTongCucHdr extends BaseEntity implements Serializable {
    public static final String TABLE_NAME = "DCNB_TH_KE_HOACH_DCTC_HDR";
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DCNB_TH_KH_DCTC_HDR_SEQ")
    @SequenceGenerator(sequenceName = "DCNB_TH_KH_DCTC_HDR_SEQ", allocationSize = 1, name = "DCNB_TH_KH_DCTC_HDR_SEQ")
    private Long id;

    @Column(name = "MA_TONG_HOP")
    private Long maTongHop;

    @Column(name = "NGAY_TONG_HOP")
    private LocalDate ngayTongHop;

    @Column(name = "NOI_DUNG")
    private String trichYeu;

    @Column(name = "NAM_KE_HOACH")
    private Integer namKeHoach;

    @Column(name = "LOAI_DC")
    private String loaiDieuChuyen;

    @Column(name = "TH_TU_NGAY")
    private LocalDate thTuNgay;

    @Column(name = "TH_DEN_NGAY")
    private LocalDate thDenNgay;

    @Column(name = "LOAI_HH")
    private String loaiHangHoa;

    @Column(name = "TEN_LOAI_HH")
    private String tenLoaiHangHoa;

    @Column(name = "CLOAI_HH")
    private String chungLoaiHangHoa;

    @Column(name = "TRANG_THAI")
    @Access(value=AccessType.PROPERTY)
    private String trangThai;

    @Column(name = "MA_DVI")
    private String maDVi;

    @Column(name = "TEN_DVI")
    private String tenDVi;

    @Column(name = "THOI_GIAN_TONG_HOP")
    private LocalDateTime thoiGianTongHop;

    @Column(name = "QDDC_ID")
    private Long qdDcId;

    @Column(name = "SO_QDDC")
    private String soQddc;

    @Column(name = "LOAI_HINH_NHAP_XUAT")
    private String loaiHinhNhapXuat;

    @Column(name = "TEN_LOAI_HINH_NHAP_XUAT")
    private String tenLoaiHinhNhapXuat;

    @Column(name = "KIEU_NHAP_XUAT")
    private String kieuNhapXuat;

    @Column(name = "TEN_KIEU_NHAP_XUAT")
    private String tenKieuNhapXuat;
    @Transient
    private List<FileDinhKem> canCu = new ArrayList<>();
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "HDR_ID")
    private List<THKeHoachDieuChuyenTongCucDtl> thKeHoachDieuChuyenTongCucDtls = new ArrayList<>();
    @Transient
    private String tenTrangThai;
    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
        this.tenTrangThai = TrangThaiAllEnum.getLabelById(this.trangThai);
    }
}
