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
@Table(name = DcnbQuyetDinhDcCHdr.TABLE_NAME)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DcnbQuyetDinhDcCHdr extends BaseEntity implements Serializable, Cloneable {

    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "DCNB_QUYET_DINH_DC_C_HDR";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = DcnbQuyetDinhDcCHdr.TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = DcnbQuyetDinhDcCHdr.TABLE_NAME
            + "_SEQ", allocationSize = 1, name = DcnbQuyetDinhDcCHdr.TABLE_NAME + "_SEQ")
    private Long id;
    private Long parentId;
    private String loaiDc;
    private String tenLoaiDc;
    private Integer nam;
    private String soQdinh;
    private LocalDate ngayKyQdinh;
    private LocalDate ngayDuyetTc;
    private Long nguoiDuyetTcId;
    private String trichYeu;
    private String maDvi;
    private String tenDvi;
    private String loaiQdinh;
    private String tenLoaiQdinh;
    private BigDecimal tongDuToanKp;
    private BigDecimal tongDuToanKpPd;
    private Long canCuQdTc;
    private String soCanCuQdTc;
    private Long dxuatId;
    private String soDxuat;
    private LocalDate ngayTrinhDuyetTc;
    @Column(name = "TYPE")
    private String type; // DC, NDC, NDCTS
    @Access(value = AccessType.PROPERTY)
    private String trangThai;
    private String lyDoTuChoi;
    private LocalDate ngayGduyet;
    private Long nguoiGduyetId;
    private LocalDate ngayPduyet;
    private Long nguoiPduyetId;
    @Column(name = "NGAY_DUYET_LDCC")
    private LocalDate ngayDuyetLdcc;
    @Column(name = "NGUOI_DUYET_LDCC_ID")
    private Long nguoiDuyetLdccId;
    private LocalDate ngayHieuLuc;
    @Access(value = AccessType.PROPERTY)
    private String trangThaiXuatDc;
    @Access(value = AccessType.PROPERTY)
    private String trangThaiNhanDc;

    @Transient
    private String tenTrangThai;
    @Transient
    private String tenTrangThaiXuatDc;
    @Transient
    private String tenTrangThaiNhanDc;
    @Transient
    private List<FileDinhKem> canCu = new ArrayList<>();
    @Transient
    private List<FileDinhKem> quyetDinh = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "HDR_ID")
    private List<DcnbQuyetDinhDcCDtl> danhSachQuyetDinh = new ArrayList<>();

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
        this.tenTrangThai = TrangThaiAllEnum.getLabelById(this.trangThai);
    }

    public void setTrangThaiXuatDc(String trangThaiXuatDc) {
        this.trangThaiXuatDc = trangThaiXuatDc;
        this.tenTrangThaiXuatDc = TrangThaiAllEnum.getLabelById(this.trangThaiXuatDc);
    }

    public void setTrangThaiNhanDc(String trangThaiNhanDc) {
        this.trangThaiNhanDc = trangThaiNhanDc;
        this.tenTrangThaiNhanDc = TrangThaiAllEnum.getLabelById(this.trangThaiNhanDc);
    }
}
