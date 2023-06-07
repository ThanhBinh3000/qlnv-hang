package com.tcdt.qlnvhang.table.dieuchuyennoibo;

import com.tcdt.qlnvhang.entities.BaseEntity;
import com.tcdt.qlnvhang.table.FileDinhKem;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = DcnbBbChuanBiKhoHdr.TABLE_NAME)
@Getter
@Setter
public class DcnbBbChuanBiKhoHdr extends BaseEntity implements Serializable, Cloneable{
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
    private Long idThuKho;
    private Long idKeToan;
    private Long idLanhDao;
    private Long idDiaDiemKho;
    private String maDiemKho;
    private String maNhaKho;
    private String maNganKho;
    private String maLoKho;
    private String loaiHinhKho;
    private String loaiVthh;
    private String cloaiVthh;
    private BigDecimal tichLuong;
    private Long idPhieuNhapKho;
    private String soPhieuNhapKho;
    private BigDecimal soLuongPhieuNhapKho;
    private String hthucKlot;
    private String pthucBquan;
    private BigDecimal dinhMucGiao;
    private BigDecimal dinhMucThucTe;
    private String nhanXet;
    private String trangThai;
    private String lyDoTuChoi;

    @Transient
    private List<FileDinhKem> fileDinhKems = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "HDR_ID")
    private List<DcnbBbChuanBiKhoDtl> children = new ArrayList<>();
}
