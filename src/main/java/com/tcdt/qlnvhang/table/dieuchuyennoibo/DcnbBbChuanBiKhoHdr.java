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
@Table(name = DcnbBbChuanBiKhoHdr.TABLE_NAME)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DcnbBbChuanBiKhoHdr extends BaseEntity implements Serializable, Cloneable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "DCNB_BB_CHUAN_BI_KHO_HDR";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = DcnbBbChuanBiKhoHdr.TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = DcnbBbChuanBiKhoHdr.TABLE_NAME
            + "_SEQ", allocationSize = 1, name = DcnbBbChuanBiKhoHdr.TABLE_NAME + "_SEQ")
    private Long id;
    private String loaiDc;
    private Integer nam;
    private String maDvi;
    private String tenDvi;
    private String maQhns;
    private String soQdDcCuc;
    private Long qdDcCucId;
    private LocalDate ngayQdDcCuc;
    private BigDecimal soLuongQdDcCuc;
    private String soBban;
    private LocalDate ngayLap;
    private LocalDate ngayKetThucNt;
    private Long idKyThuatVien;
    private String tenKyThuatVien;
    private Long idThuKho;
    private String tenThuKho;
    private Long idKeToan;
    private String tenKeToan;
    private Long idLanhDao;
    private String tenLanhDao;
    private String maDiemKho;
    private String maNhaKho;
    private String maNganKho;
    private String maLoKho;
    private String tenDiemKho;
    private String tenNhaKho;
    private String tenNganKho;
    private String tenLoKho;
    private String loaiHinhKho;
    private String loaiVthh;
    private String cloaiVthh;
    private String tenLoaiVthh;
    private String tenCloaiVthh;
    private String donViTinh;
    private BigDecimal tichLuong;
    private BigDecimal duToanKphi;
    private Long idPhieuNhapKho;
    private String soPhieuNhapKho;
    private BigDecimal soLuongPhieuNhapKho;
    private String hthucKlot;
    private String pthucBquan;
    private BigDecimal dinhMucGiao;
    private BigDecimal dinhMucThucTe;
    private String nhanXet;
    @Access(value = AccessType.PROPERTY)
    private String trangThai;
    private String lyDoTuChoi;
    @Column(name = "NGUOI_GDUYET")
    private Long nguoiGDuyet;
    @Column(name = "NGAY_GDUYET")
    private LocalDate ngayGDuyet;
    @Column(name = "NGUOI_GDUYET_TK")
    private Long nguoiPDuyetTk;
    @Column(name = "NGAY_GDUYET_TK")
    private LocalDate ngayPDuyetTk;
    @Column(name = "NGUOI_PDUYET")
    private Long nguoiPDuyet;
    @Column(name = "NGAY_PDUYET")
    private LocalDate ngayPDuyet;
    @Transient
    private List<FileDinhKem> fileDinhKems = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "HDR_ID")
    private List<DcnbBbChuanBiKhoDtl> children = new ArrayList<>();
    @Transient
    private String tenTrangThai;
    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
        this.tenTrangThai = TrangThaiAllEnum.getLabelById(this.trangThai);
    }
}
