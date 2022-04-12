package com.tcdt.qlnvhang.entities.quanlybienbannhapdaykholuongthuc;

import com.tcdt.qlnvhang.entities.BaseEntity;
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
@Table(name = "QL_BIEN_BAN_NHAP_DAY_KHO_LT")
public class QlBienBanNhapDayKhoLt extends BaseEntity implements Serializable {
    private static final long serialVersionUID = -5271141998400379431L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "QL_BIEN_BAN_NHAP_DAY_KHO_LT_SEQ")
    @SequenceGenerator(sequenceName = "QL_BIEN_BAN_NHAP_DAY_KHO_LT_SEQ", allocationSize = 1, name = "QL_BIEN_BAN_NHAP_DAY_KHO_LT_SEQ")
    @Column(name = "ID")
    private Long id;

    @Column(name = "SO_BIEN_BAN")
    private String soBienBan;

    @Column(name = "NGAY_LAP")
    private LocalDate ngayLap;

    @Column(name = "MA_DON_VI")
    private String maDonVi;

    @Column(name = "MA_DON_VI_LAP")
    private String maDonViLap;

    @Column(name = "MA_KHO_NGAN_LO")
    private String maKhoNganLo;

    @Column(name = "MA_HANG")
    private String maHang;

    @Column(name = "TEN_HANG")
    private String tenHang;

    @Column(name = "NGAY_BAT_DAU_NHAP")
    private LocalDate ngayBatDauNhap;

    @Column(name = "NGAY_KET_THUC_NHAP")
    private LocalDate ngayKetThucNhap;

    @Column(name = "THU_KHO")
    private String thuKho;

    @Column(name = "KY_THUAT_VIEN")
    private String kyThuatVien;

    @Column(name = "KE_TOAN")
    private String keToan;

    @Column(name = "THU_TRUONG")
    private String thuTruong;

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

    @Column(name = "CAP_DON_VI")
    private String capDonVi;

    @Transient
    private List<QlBienBanNdkCtLt> chiTiets = new ArrayList<>();
}
