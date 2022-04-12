package com.tcdt.qlnvhang.entities.quanlybangkecanhangluongthuc;

import com.tcdt.qlnvhang.entities.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "QL_BANG_KE_CAN_HANG_LT")
public class QlBangKeCanHangLt extends BaseEntity implements Serializable {
    private static final long serialVersionUID = -628455991119242429L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "QL_BANG_KE_CAN_HANG_LT_SEQ")
    @SequenceGenerator(sequenceName = "QL_BANG_KE_CAN_HANG_LT_SEQ", allocationSize = 1, name = "QL_BANG_KE_CAN_HANG_LT_SEQ")
    @Column(name = "ID")
    private Long id;

    @Column(name = "SO_BANG_KE")
    private String soBangKe;

    @Column(name = "NGAY_LAP")
    private LocalDate ngayLap;

    @Column(name = "MA_DON_VI")
    private String maDonVi;

    @Column(name = "MA_DON_VI_LAP")
    private String maDonViLap;

    @Column(name = "QL_PHIEU_NHAP_KHO_LT_ID")
    private Long qlPhieuNhapKhoLtId;

    @Column(name = "MA_KHO_NGAN_LO")
    private String maKhoNganLo;

    @Column(name = "SO_KHO")
    private String soKho;

    @Column(name = "MA_HANG")
    private String maHang;

    @Column(name = "TEN_HANG")
    private String tenHang;

    @Column(name = "DON_VI_TINH")
    private String donViTinh;

    @Column(name = "TEN_NGUOI_GIAO_HANG")
    private String tenNguoiGiaoHang;

    @Column(name = "THOI_GIAN_GIAO_HANG")
    private LocalDateTime thoiGianGiaoHang;

    @Column(name = "DIA_CHI")
    private String diaChi;

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
    private List<QlBangKeChCtLt> chiTiets = new ArrayList<>();
}
