package com.tcdt.qlnvhang.entities.quanlyphieunhapkholuongthuc;

import com.tcdt.qlnvhang.entities.BaseEntity;
import com.tcdt.qlnvhang.entities.quyetdinhpheduyetketqualuachonnhathauvatu.QdKqlcntGoiThauVt;
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
@Table(name = "QL_PHIEU_NHAP_KHO_LT")
public class QlPhieuNhapKhoLt  extends BaseEntity implements Serializable {
    private static final long serialVersionUID = -1880694858465293452L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "QL_PHIEU_NHAP_KHO_LT_SEQ")
    @SequenceGenerator(sequenceName = "QL_PHIEU_NHAP_KHO_LT_SEQ", allocationSize = 1, name = "QL_PHIEU_NHAP_KHO_LT_SEQ")
    @Column(name = "ID")
    private Long id;

    @Column(name = "PHIEU_KT_CL_ID")
    private Long phieuKtClId;

    @Column(name = "BB_NGHIEM_THU_KL_ID")
    private Long bbNghiemThuKlId;

    @Column(name = "QDGNVNX_ID")
    private Long qdgnvnxId; // HhQdGiaoNvuNhapxuatHdr

    @Column(name = "SO_PHIEU")
    private String soPhieu;

    @Column(name = "NGAY_LAP")
    private LocalDate ngayLap;

    @Column(name = "MA_NGAN_LO")
    private String maNganLo;

    @Column(name = "TEN_NGAN_LO")
    private String tenNganLo;

    @Column(name = "NGUOI_GIAO_HANG")
    private String nguoiGiaoHang;

    @Column(name = "DIA_CHI_GIAO_NHAN")
    private String diaChiGiaoNhan;

    @Column(name = "THOI_GIAN_GIAO_NHAN")
    private LocalDateTime thoiGianGiaoNhan;

    @Column(name = "TAI_KHOAN_NO")
    private String taiKhoanNo;

    @Column(name = "TAI_KHOAN_CO")
    private String taiKhoanCo;

    @Column(name = "LOAI_HINH_NHAP")
    private String loaiHinhNhap;

    @Column(name = "GHI_CHU")
    private String ghiChu;

    @Column(name = "LY_DO_TU_CHOI")
    private String lyDoTuChoi;

    @Column(name = "MA_DON_VI")
    private String maDonVi;

    @Column(name = "NGAY_TAO")
    private LocalDate ngayTao;

    @Column(name = "NGUOI_TAO_ID")
    private Long nguoiTaoId;

    @Column(name = "NGAY_SUA")
    private LocalDate ngaySua;

    @Column(name = "NGUOI_SUA_ID")
    private Long nguoiSuaId;

    @Column(name = "NGAY_GUI_DUYET")
    private LocalDate ngayGuiDuyet;

    @Column(name = "NGUOI_GUI_DUYET_ID")
    private Long nguoiGuiDuyetId;

    @Column(name = "NGAY_PHE_DUYET")
    private LocalDate ngayPheDuyet;

    @Column(name = "NGUOI_PHE_DUYET_ID")
    private Long nguoiPheDuyetId;

    @Column(name = "TRANG_THAI")
    private String trangThai;

    @Column(name = "CAP_DON_VI")
    private String capDonVi;

    @Column(name = "MA_DIEM_KHO")
    private String maDiemKho;

    @Column(name = "MA_NHA_KHO")
    private String maNhaKho;

    @Column(name = "MA_QHNS")
    private String maQhns;

    @Column(name = "NGAY_NHAP_KHO")
    private LocalDate ngayNhapKho;

    @Column(name = "SO_QD_NVU_NHANG")
    private Long soQdNvuNhang;

    @Column(name = "NGAY_QD_NVU_NHANG")
    private LocalDate ngayQdNvuNhang;

    @Transient
    private List<QlPhieuNhapKhoHangHoaLt> hangHoaList = new ArrayList<>();
}
