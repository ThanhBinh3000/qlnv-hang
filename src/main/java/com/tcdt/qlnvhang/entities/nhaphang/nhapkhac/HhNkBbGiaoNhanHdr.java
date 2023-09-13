package com.tcdt.qlnvhang.entities.nhaphang.nhapkhac;

import com.tcdt.qlnvhang.entities.BaseEntity;
import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.table.FileDinhKem;
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
@Table(name = HhNkBbGiaoNhanHdr.TABLE_NAME)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HhNkBbGiaoNhanHdr extends BaseEntity implements Serializable, Cloneable{
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "HHNK_BB_GIAO_NHAN_HDR";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = HhNkBbGiaoNhanHdr.TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = HhNkBbGiaoNhanHdr.TABLE_NAME
            + "_SEQ", allocationSize = 1, name = HhNkBbGiaoNhanHdr.TABLE_NAME + "_SEQ")
    private Long id;
    private Integer nam;
    private String maDvi;
    private String tenDvi;
    private String maQhns;
    private String soBb;
    private LocalDate ngayLap;
    private LocalDate ngayTaoBk;
    private Long qdPdNkId;
    private String soQdPdNk;
    private LocalDate ngayQdPdNk;
    private String soBbKtNhapKho;
    private Long idBbKtNhapKho;
    private String maDiemKho;
    private String maNhaKho;
    private String maNganKho;
    private String maLoKho;
    private String tenDiemKho;
    private String tenNhaKho;
    private String tenNganKho;
    private String tenLoKho;
    private String soHoSoKyThuat;
    private Long idHoSoKyThuat;
    private String loaiVthh;
    private String cloaiVthh;
    @Column(name = "TEN_LOAI_VTHH")
    private String tenLoaiVthh;
    @Column(name = "TEN_CLOAI_VTHH")
    private String tenCloaiVthh;
    private String donViTinh;
    private LocalDate ngayBdNhap;
    private LocalDate ngayKtNhap;
    private BigDecimal soLuongQd;
    private String ghiChu;
    private String ketLuan;
    private Long idCanBo;
    private String tenCanBo;
    private Long idLanhDao;
    private String tenLanhDao;
    @Column(name = "NGUOI_GDUYET")
    private Long nguoiGDuyet;
    @Column(name = "NGAY_GDUYET")
    private LocalDate ngayGDuyet;
    @Column(name = "NGUOI_PDUYET")
    private Long nguoiPDuyet;
    @Column(name = "NGAY_PDUYET")
    private LocalDate ngayPDuyet;
    @Access(value=AccessType.PROPERTY)
    private String trangThai;
    private String lyDoTuChoi;
    @Transient
    private List<FileDinhKem> fileCanCu = new ArrayList<>();
    @Transient
    private List<FileDinhKem> fileDinhKems = new ArrayList<>();
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "HDR_ID")
    private List<HhNkBbGiaoNhanDtl> danhSachDaiDien = new ArrayList<>();
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "HDR_ID")
    private List<HhNkBbGiaoNhanTTDtl> danhSachBangKe = new ArrayList<>();
    @Transient
    private String tenTrangThai;
    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
        this.tenTrangThai = TrangThaiAllEnum.getLabelById(this.trangThai);
    }
}
