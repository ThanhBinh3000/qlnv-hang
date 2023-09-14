package com.tcdt.qlnvhang.table.dieuchuyennoibo;

import com.tcdt.qlnvhang.entities.BaseEntity;
import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.table.FileDinhKem;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = DcnbBbGiaoNhanHdr.TABLE_NAME)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DcnbBbGiaoNhanHdr extends BaseEntity implements Serializable, Cloneable{
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "DCNB_BB_GIAO_NHAN_HDR";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = DcnbBbGiaoNhanHdr.TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = DcnbBbGiaoNhanHdr.TABLE_NAME
            + "_SEQ", allocationSize = 1, name = DcnbBbGiaoNhanHdr.TABLE_NAME + "_SEQ")
    private Long id;
    private String loaiDc;
    private Integer nam;
    private String maDvi;
    private String tenDvi;
    private String maQhns;
    private String soBb;
    private LocalDate ngayLap;
    private String soQdDcCuc;
    private Long qdDcCucId;
    private LocalDate ngayQdDcCuc;
    private String soBbKtNhapKho;
    private Long idBbKtNhapKho;
    @Column(name = "SO_BB_BGLM")
    private String soBienBanLayMau;
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
    private String dviTinh;
    private LocalDate ngayBdNhap;
    private LocalDate ngayKtNhap;
    private BigDecimal soLuongQdDcCuc;
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
    @Column(name = "KE_HOACH_DC_DTL_ID")
    private Long keHoachDcDtlId;
    @Transient
    private List<FileDinhKem> fileCanCu = new ArrayList<>();
    @Transient
    private List<FileDinhKem> fileDinhKems = new ArrayList<>();
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "HDR_ID")
    private List<DcnbBbGiaoNhanDtl> danhSachDaiDien = new ArrayList<>();
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "HDR_ID")
    private List<DcnbBbGiaoNhanTTDtl> danhSachBangKe = new ArrayList<>();
    @Transient
    private String tenTrangThai;
    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
        this.tenTrangThai = TrangThaiAllEnum.getLabelById(this.trangThai);
    }
}
