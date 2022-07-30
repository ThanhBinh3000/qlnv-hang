package com.tcdt.qlnvhang.entities.nhaphang.vattu.phieunhapkhotamgui;

import com.tcdt.qlnvhang.entities.BaseEntity;
import com.tcdt.qlnvhang.entities.TrangThaiBaseEntity;
import com.tcdt.qlnvhang.table.FileDinhKem;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PHIEU_NHAP_KHO_TAM_GUI_SEQ")
    @SequenceGenerator(sequenceName = "PHIEU_NHAP_KHO_TAM_GUI_SEQ", allocationSize = 1, name = "PHIEU_NHAP_KHO_TAM_GUI_SEQ")
    @Column(name = "ID")
    private Long id;

    @Column(name = "QDGNVNX_ID")
    private Long qdgnvnxId;

    @Column(name = "SO_PHIEU")
    private String soPhieu;

    @Column(name = "NGAY_NHAP_KHO")
    private LocalDate ngayNhapKho;

    @Column(name = "NO")
    private BigDecimal no;

    @Column(name = "CO")
    private BigDecimal co;

    @Column(name = "NGUOI_GIAO_HANG")
    private String nguoiGiaoHang;

    @Column(name = "THOI_GIAN_GIAO_NHAN_HANG")
    private LocalDateTime thoiGianGiaoNhanHang;

    @Column(name = "NGAY_TAO_PHIEU")
    private LocalDate ngayTaoPhieu;

    @Column(name = "MA_DIEM_KHO")
    private String maDiemKho;

    @Column(name = "MA_NHA_KHO")
    private String maNhaKho;

    @Column(name = "MA_NGAN_KHO")
    private String maNganKho;

    @Column(name = "MA_NGAN_LO")
    private String maNganLo;

    @Column(name = "TONG_SO_LUONG")
    private BigDecimal tongSoLuong;

    @Column(name = "TONG_SO_TIEN")
    private BigDecimal tongSoTien;

    @Column(name = "LOAI_VTHH")
    private String loaiVthh;

    private Long nguoiGuiDuyetId;
    private LocalDate ngayGuiDuyet;
    private Long nguoiPduyetId;
    private LocalDate ngayPduyet;
    private String maDvi;
    private String capDvi;
    private Integer so;
    private Integer nam;

    @Transient
    private List<NhPhieuNhapKhoTamGuiCt> chiTiets = new ArrayList<>();

    @Transient
    private List<FileDinhKem> fileDinhKems = new ArrayList<>();
}
