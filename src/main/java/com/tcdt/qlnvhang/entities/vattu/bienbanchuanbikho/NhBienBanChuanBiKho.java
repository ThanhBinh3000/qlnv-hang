package com.tcdt.qlnvhang.entities.vattu.bienbanchuanbikho;

import com.tcdt.qlnvhang.entities.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "NH_BIEN_BAN_CHUAN_BI_KHO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class NhBienBanChuanBiKho extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1117405412018192929L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BIEN_BAN_CHUAN_BI_KHO_SEQ")
    @SequenceGenerator(sequenceName = "BIEN_BAN_CHUAN_BI_KHO_SEQ", allocationSize = 1, name = "BIEN_BAN_CHUAN_BI_KHO_SEQ")
    @Column(name = "ID")
    private Long id;

    @Column(name = "QDGNVNX_ID")
    private Long qdgnvnxId;

    @Column(name = "SO_BIEN_BAN")
    private String soBienBan;

    @Column(name = "NGAY_NGHIEM_THU")
    private LocalDate ngayNghiemThu;

    @Column(name = "THU_TRUONG_DOI_VI")
    private String thuTruongDonVi;

    @Column(name = "KE_TOAN_DON_VI")
    private String keToanDonVi;

    @Column(name = "KY_THUAT_VIEN")
    private String kyThuatVien;

    @Column(name = "THU_KHO")
    private String thuKho;

    @Column(name = "MA_VAT_TU_CHA")
    private String maVatTuCha; // Loai hang

    @Column(name = "MA_VAT_TU")
    private String maVatTu; // Chủng loại hàng

    @Column(name = "LOAI_HINH_KHO")
    private String loaiHinhKho;

    @Column(name = "MA_DIEM_KHO")
    private String maDiemKho;

    @Column(name = "MA_NHA_KHO")
    private String maNhaKho;

    @Column(name = "MA_NGAN_KHO")
    private String maNganKho;

    @Column(name = "MA_NGAN_LO")
    private String maNganLo;

    @Column(name = "PT_BAO_QUAN")
    private String ptBaoQuan;

    @Column(name = "THUC_NHAP")
    private String thucNhap;

    @Column(name = "HT_BAO_QUAN")
    private String htBaoQuan;

    @Column(name = "KET_LUAN")
    private String ketLuan;

    @Column(name = "NGUOI_GUI_DUYET_ID")
    private Long nguoiGuiDuyetId;

    @Column(name = "NGAY_GUI_DUYET")
    private LocalDate ngayGuiDuyet;

    @Column(name = "NGUOI_PDUYET_ID")
    private Long nguoiPduyetId;

    @Column(name = "NGAY_PDUYET")
    private LocalDate ngayPduyet;

    @Column(name = "MA_DVI")
    private String maDvi;

    @Column(name = "CAP_DVI")
    private String capDvi;

    @Column(name = "TONG_SO")
    private BigDecimal tongSo;

    @Column(name = "SO")
    private Integer so;

    @Column(name = "NAM")
    private Integer nam;

    @Column(name = "TRANG_THAI")
    private String trangThai;

    @Column(name = "LY_DO_TU_CHOI")
    private String lyDoTuChoi;

    @Column(name = "LOAI_VTHH")
    private String loaiVthh;

    @Transient
    private List<NhBienBanChuanBiKhoCt> chiTiets = new ArrayList<>();
}
