package com.tcdt.qlnvhang.entities.nhaphang.nhapkhac;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.entities.BaseEntity;
import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.util.Contains;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = HhNkBbNhapDayKhoHdr.TABLE_NAME)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HhNkBbNhapDayKhoHdr extends BaseEntity implements Serializable, Cloneable{
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "HHNK_BB_NHAP_DAY_KHO_HDR";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = HhNkBbNhapDayKhoHdr.TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = HhNkBbNhapDayKhoHdr.TABLE_NAME
            + "_SEQ", allocationSize = 1, name = HhNkBbNhapDayKhoHdr.TABLE_NAME + "_SEQ")
    private Long id;
    private Integer nam;
    private String maDvi;
    private String tenDvi;
    private String maQhns;
    private String soBb;
    private LocalDate ngayLap;
    private String soQdPdNk;
    private Long qdPdNkId;
    private LocalDate ngayQdPdNk;
    private String maDiemKho;
    private String maNhaKho;
    private String maNganKho;
    private String maLoKho;
    private String loaiVthh;
    private String cloaiVthh;
    private String tenDiemKho;
    private String tenNhaKho;
    private String tenNganKho;
    private String tenLoKho;
    private String tenLoaiVthh;
    private String tenCloaiVthh;
    private String donViTinh;
    private LocalDate ngayBdNhap;
    private LocalDate ngayKtNhap;
    private BigDecimal soLuongQd;
    private String ghiChu;
    private Long idThuKho;
    private Long idKyThuatVien;
    private Long idKeToan;
    private Long idLanhDao;

    private String tenThuKho;
    private String tenKyThuatVien;
    private String tenKeToan;
    private String tenLanhDao;
    @Access(value=AccessType.PROPERTY)
    private String trangThai;
    private String lyDoTuChoi;
    @Column(name = "NGUOI_GDUYET")
    private Long nguoiGDuyet;
    @Column(name = "NGAY_GDUYET")
    private LocalDate ngayGDuyet;
    @Column(name = "NGUOI_GDUYET_KTVBQ")
    private Long nguoiGDuyetKtvbq;
    @Column(name = "NGAY_GDUYET_KTVBQ")
    private LocalDate ngayGDuyetKtvbq;
    @Column(name = "NGUOI_GDUYET_KT")
    private Long nguoiGDuyetKt;
    @Column(name = "NGAY_GDUYET_KT")
    private LocalDate ngayGDuyetKt;
    @Column(name = "NGUOI_PDUYET")
    private Long nguoiPDuyet;
    @Column(name = "NGAY_PDUYET")
    private LocalDate ngayPDuyet;
    @Transient
    private List<FileDinhKem> fileDinhKems = new ArrayList<>();
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "HDR_ID")
    private List<HhNkBbNhapDayKhoDtl> children = new ArrayList<>();
    @Transient
    private String tenTrangThai;
    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
        this.tenTrangThai = TrangThaiAllEnum.getLabelById(this.trangThai);
    }
}
