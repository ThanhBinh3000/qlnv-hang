package com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhapkho.bienbannhapdaykho;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.entities.TrangThaiBaseEntity;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.util.Contains;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = NhBbNhapDayKho.TABLE_NAME)
public class NhBbNhapDayKho extends TrangThaiBaseEntity implements Serializable {
    private static final long serialVersionUID = -5271141998400379431L;
    public static final String TABLE_NAME = "NH_BB_NHAP_DAY_KHO";
    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BB_NHAP_DAY_KHO_LT_SEQ")
//    @SequenceGenerator(sequenceName = "BB_NHAP_DAY_KHO_LT_SEQ", allocationSize = 1, name = "BB_NHAP_DAY_KHO_LT_SEQ")
    @Column(name = "ID")
    private Long id;

    @Column(name = "SO_BIEN_BAN_NHAP_DAY_KHO")
    private String soBienBanNhapDayKho;

    @Column(name = "SO_QD_GIAO_NV_NH")
    private String soQdGiaoNvNh; // HhQdGiaoNvuNhapxuatHdr

    @Column(name = "ID_QD_GIAO_NV_NH")
    private Long idQdGiaoNvNh; // HhQdGiaoNvuNhapxuatHdr

    @Column(name = "SO_HD")
    private String soHd;

    @Column(name = "NGAY_HD")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayHd;

    @Column(name = "ID_KE_TOAN")
    private Long idKeToan;

    @Transient
    private String tenKeToan;

    @Column(name = "ID_KY_THUAT_VIEN")
    private Long idKyThuatVien;

    @Transient
    private String tenKyThuatVien;

    @Column(name = "NGAY_BAT_DAU_NHAP")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayBatDauNhap;

    @Column(name = "NGAY_KET_THUC_NHAP")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayKetThucNhap;

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

    @Column(name = "SO_LUONG")
    private BigDecimal soLuong;
    private BigDecimal tongSoLuongNhap;

    @Column(name = "MA_DVI")
    private String maDvi;

    @Transient
    private String tenDvi;

    @Column(name = "NAM")
    private Integer nam;

    @Column(name = "GHI_CHU")
    private String ghiChu;
    private String maQhns;

    @Transient
    private List<NhBbNhapDayKhoCt> chiTiets = new ArrayList<>();

    @Transient
    private List<FileDinhKem> fileDinhKems = new ArrayList<>();
}
