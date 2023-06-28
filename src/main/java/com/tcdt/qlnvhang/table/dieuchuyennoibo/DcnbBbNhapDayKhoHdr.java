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
@Table(name = DcnbBbNhapDayKhoHdr.TABLE_NAME)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DcnbBbNhapDayKhoHdr extends BaseEntity implements Serializable, Cloneable{
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "DCNB_BB_NHAP_DAY_KHO_HDR";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = DcnbBbNhapDayKhoHdr.TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = DcnbBbNhapDayKhoHdr.TABLE_NAME
            + "_SEQ", allocationSize = 1, name = DcnbBbNhapDayKhoHdr.TABLE_NAME + "_SEQ")
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
    private Long idKeHoachDtl;
    private String maDiemKho;
    private String maNhaKho;
    private String maNganKho;
    private String maLoKho;
    private String loaiVthh;
    private String cloaiVthh;
    private String dviTinh;
    private LocalDate ngayBdNhap;
    private LocalDate ngayKtNhap;
    private BigDecimal soLuongQdDcCuc;
    private Long idThuKho;
    private Long idKyThuatVien;
    private Long idKeToan;
    private Long idLanhDao;
    private String ghiChu;
    @Access(value=AccessType.PROPERTY)
    private String trangThai;
    private String lyDoTuChoi;
    @Transient
    private List<FileDinhKem> fileDinhKems = new ArrayList<>();
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "HDR_ID")
    private List<DcnbBbNhapDayKhoDtl> children = new ArrayList<>();
    @Transient
    private String tenTrangThai;
    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
        this.tenTrangThai = TrangThaiAllEnum.getLabelById(this.trangThai);
    }
}
