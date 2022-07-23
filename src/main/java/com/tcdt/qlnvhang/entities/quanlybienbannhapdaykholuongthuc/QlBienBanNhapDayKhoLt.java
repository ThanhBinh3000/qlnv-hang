package com.tcdt.qlnvhang.entities.quanlybienbannhapdaykholuongthuc;

import com.tcdt.qlnvhang.entities.BaseEntity;
import com.tcdt.qlnvhang.table.FileDinhKem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = QlBienBanNhapDayKhoLt.TABLE_NAME)
public class QlBienBanNhapDayKhoLt extends BaseEntity implements Serializable {
    private static final long serialVersionUID = -5271141998400379431L;
    public static final String TABLE_NAME = "NH_BB_NHAP_DAY_KHO_LT";
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BB_NHAP_DAY_KHO_LT_SEQ")
    @SequenceGenerator(sequenceName = "BB_NHAP_DAY_KHO_LT_SEQ", allocationSize = 1, name = "BB_NHAP_DAY_KHO_LT_SEQ")
    @Column(name = "ID")
    private Long id;

    @Column(name = "QDGNVNX_ID")
    private Long qdgnvnxId;

    @Column(name = "BB_NGHIEM_THU_ID")
    private Long bbNghiemThuId; // HhBbNghiemthuKlstHdr

    @Column(name = "SO_BIEN_BAN")
    private String soBienBan;

    @Column(name = "NGAY_NHAP_DAY_KHO")
    private LocalDate ngayNhapDayKho;

    @Column(name = "THU_TRUONG")
    private String thuTruong;

    @Column(name = "KE_TOAN")
    private String keToan;

    @Column(name = "KY_THUAT_VIEN")
    private String kyThuatVien;

    @Column(name = "THU_KHO")
    private String thuKho;

    @Column(name = "MA_VAT_TU")
    private String maVatTu; // Chủng loại hàng hóa

    @Column(name = "MA_VAT_TU_CHA")
    private String maVatTuCha; // Loại hàng

    @Column(name = "NGAY_BAT_DAU_NHAP")
    private LocalDate ngayBatDauNhap;

    @Column(name = "NGAY_KET_THUC_NHAP")
    private LocalDate ngayKetThucNhap;

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

    @Column(name = "TRANG_THAI")
    private String trangThai;

    @Column(name = "LY_DO_TU_CHOI")
    private String lyDoTuChoi;

    @Column(name = "NGAY_GUI_DUYET")
    private LocalDate ngayGuiDuyet;

    @Column(name = "NGUOI_GUI_DUYET_ID")
    private Long nguoiGuiDuyetId;

    @Column(name = "NGAY_PHE_DUYET")
    private LocalDate ngayPheDuyet;

    @Column(name = "NGUOI_PHE_DUYET_ID")
    private Long nguoiPheDuyetId;

    private Integer so;
    private Integer nam;

    @Transient
    private List<QlBienBanNdkCtLt> chiTiets = new ArrayList<>();

    @Transient
    private List<FileDinhKem> fileDinhKems = new ArrayList<>();
}
