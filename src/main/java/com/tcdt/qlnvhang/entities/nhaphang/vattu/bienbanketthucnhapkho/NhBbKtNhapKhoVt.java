package com.tcdt.qlnvhang.entities.nhaphang.vattu.bienbanketthucnhapkho;

import com.tcdt.qlnvhang.entities.BaseEntity;
import com.tcdt.qlnvhang.entities.TrangThaiBaseEntity;
import com.tcdt.qlnvhang.table.FileDinhKem;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = NhBbKtNhapKhoVt.TABLE_NAME)
@EqualsAndHashCode(callSuper = false)
public class NhBbKtNhapKhoVt extends TrangThaiBaseEntity implements Serializable {
    
    public static final String TABLE_NAME = "NH_BB_KT_NHAP_KHO_VT";
    private static final long serialVersionUID = -5670577801183342956L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BB_KT_NHAP_KHO_VT_SEQ")
    @SequenceGenerator(sequenceName = "BB_KT_NHAP_KHO_VT_SEQ", allocationSize = 1, name = "BB_KT_NHAP_KHO_VT_SEQ")
    @Column(name = "ID")
    private Long id;

    @Column(name = "QDGNVNX_ID")
    private Long qdgnvnxId;

    @Column(name = "BB_CHUAN_BI_KHO_ID")
    private Long bbChuanBiKhoId; // NH_BIEN_BAN_CHUAN_BI_KHO

    @Column(name = "MA_DVI")
    private String maDvi;

    @Column(name = "CAP_DVI")
    private String capDvi;

    @Column(name = "SO_BIEN_BAN")
    private String soBienBan;

    @Column(name = "NGAY_KET_THUC_KHO")
    private LocalDate ngayKetThucKho;

    @Column(name = "THU_TRUONG_DON_VI")
    private String thuTruongDonVi;

    @Column(name = "KE_TOAN_DON_VI")
    private String keToanDonVi;

    @Column(name = "KY_THUAT_VIEN")
    private String kyThuatVien;

    @Column(name = "THU_KHO")
    private String thuKho;

    @Column(name = "MA_VAT_TU_CHA")
    private String maVatTuCha;

    @Column(name = "MA_VAT_TU")
    private String maVatTu;

    @Column(name = "MA_DIEM_KHO")
    private String maDiemKho;

    @Column(name = "MA_NHA_KHO")
    private String maNhaKho;

    @Column(name = "MA_NGAN_KHO")
    private String maNganKho;

    @Column(name = "MA_NGAN_LO")
    private String maNganLo;

    @Column(name = "NGAY_BAT_DAU_NHAP")
    private LocalDate ngayBatDauNhap;

    @Column(name = "NGAY_KET_THUC_NHAP")
    private LocalDate ngayKetThucNhap;

    @Column(name = "NGUOI_GUI_DUYET_ID")
    private Long nguoiGuiDuyetId;


    @Column(name = "NGUOI_PDUYET_ID")
    private Long nguoiPduyetId;

    @Column(name = "SO")
    private Integer so;

    @Column(name = "NAM")
    private Integer nam;

    private String loaiVthh;
    @Transient
    private String tenVthh;

    private String cloaiVthh;
    @Transient
    private String tenCloaiVthh;

    private String moTaHangHoa;

    @Transient
    private List<NhBbKtNhapKhoVtCt> chiTiets = new ArrayList<>();

    @Transient
    private List<FileDinhKem> fileDinhKems = new ArrayList<>();
}
