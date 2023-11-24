package com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhapkho.phieunhapkhotamgui;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.entities.TrangThaiBaseEntity;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.util.Contains;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = NhPhieuNhapKhoTamGui.TABLE_NAME)
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class NhPhieuNhapKhoTamGui extends TrangThaiBaseEntity implements Serializable {
    private static final long serialVersionUID = -4126804462700206222L;
    public static final String TABLE_NAME = "NH_PHIEU_NHAP_KHO_TAM_GUI";

    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PHIEU_NHAP_KHO_TAM_GUI_SEQ")
//    @SequenceGenerator(sequenceName = "PHIEU_NHAP_KHO_TAM_GUI_SEQ", allocationSize = 1, name = "PHIEU_NHAP_KHO_TAM_GUI_SEQ")
    @Column(name = "ID")
    private Long id;

    @Column(name = "ID_QD_GIAO_NV_NH")
    private Long idQdGiaoNvNh;

    @Column(name = "SO_QD_GIAO_NV_NH")
    private String soQdGiaoNvNh;

    @Column(name = "SO_PHIEU_NHAP_KHO_TAM_GUI")
    private String soPhieuNhapKhoTamGui;

    @Column(name = "NGAY_NHAP_KHO")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayNhapKho;

    @Column(name = "SO_HD")
    private String soHd;

    @Column(name = "NGAY_HD")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayHd;

    @Column(name = "SO_NO")
    private BigDecimal soNo;

    @Column(name = "SO_CO")
    private BigDecimal soCo;


    @Column(name = "NGUOI_GIAO_HANG")
    private String nguoiGiaoHang;

    @Column(name = "CMT_NGUOI_GIAO_HANG")
    private String cmtNguoiGiaoHang;

    @Column(name = "DON_VI_GIAO_HANG")
    private String donViGiaoHang;

    @Column(name = "DIA_CHI_NGUOI_GIAO")
    private String diaChiNguoiGiao;

    @Column(name = "KE_TOAN_TRUONG")
    private String keToanTruong;

    @Column(name = "THOI_GIAN_GIAO_NHAN")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_TIME_STR)
    private Date thoiGianGiaoNhan;

    @Column(name = "ID_DDIEM_GIAO_NV_NH")
    private Long idDdiemGiaoNvNh;

    @Column(name = "MA_DIEM_KHO")
    private String maDiemKho;

    @Transient
    private String tenDiemKho;

    @Column(name = "MA_NHA_KHO")
    private String maNhaKho;

    @Transient
    private String tenNhaKho;

    @Column(name = "MA_NGAN_KHO")
    private String maNganKho;

    @Transient
    private String tenNganKho;

    @Column(name = "MA_LO_KHO")
    private String maLoKho;

    @Transient
    private String tenLoKho;

    @Column(name = "SO_LUONG_DDIEM_GIAO_NV_NH")
    private BigDecimal soLuongDdiemGiaoNvNh;

    @Column(name = "LOAI_VTHH")
    private String loaiVthh;

    @Transient
    private String tenLoaiVthh;

    @Column(name = "CLOAI_VTHH")
    private String cloaiVthh;

    @Transient
    private String tenCloaiVthh;

    @Column(name = "MA_DVI")
    private String maDvi;

    @Transient
    private String tenDvi;

    @Column(name = "NAM")
    private Integer nam;

    @Column(name = "GHI_CHU")
    private String ghiChu;

    @Transient
    private List<NhPhieuNhapKhoTamGuiCt> children = new ArrayList<>();

    @Transient
    private List<FileDinhKem> fileDinhKems = new ArrayList<>();
    @Transient
    Long tongSoLuong;
    @Transient
    Long tongTien;
    @Transient
    String tongSoLuongBangChu;
    @Transient
    String tongTienBangChu;
    @Transient
    String dviTinh;
    @Transient
    String tenDviCha;
}
