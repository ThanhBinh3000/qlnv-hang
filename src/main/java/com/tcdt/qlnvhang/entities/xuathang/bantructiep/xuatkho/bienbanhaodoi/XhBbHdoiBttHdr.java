package com.tcdt.qlnvhang.entities.xuathang.bantructiep.xuatkho.bienbanhaodoi;


import com.tcdt.qlnvhang.table.FileDinhKem;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = XhBbHdoiBttHdr.TABLE_NAME)
@Data
public class XhBbHdoiBttHdr implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_BB_HDOI_BTT_HDR";

    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator =TABLE_NAME +"_SEQ")
//    @SequenceGenerator(sequenceName = TABLE_NAME +"_SEQ", allocationSize = 1, name = TABLE_NAME +"_SEQ")

    private Long id;

    private Integer namKh;

    private String maDvi;
    @Transient
    private String tenDvi;

    private String maQhns;

    private String soBbHaoDoi;

    private Long idQdNv;

    private String soQdNv;

    private LocalDate ngayQdNv;

    private Long idHd;

    private String soHd;

    private LocalDate ngayKyHd;

    private String maDiemKho;
    @Transient
    private String tenDiemKho;

    private String maNhaKho;
    @Transient
    private String tenNhaKho;

    private String maNganKho;
    @Transient
    private String tenNganKho;

    private String maLoKho;
    @Transient
    private String tenLoKho;

    private Long idBbTinhKho;

    private String soBbTinhKho;

    private BigDecimal tongSlNhap;

    private LocalDate ngayKthucNhap;

    private BigDecimal tongSlXuat;

    private LocalDate ngayBdauXuat;

    private LocalDate ngayKthucXuat;

    private BigDecimal slHaoThucTe;

    private String tiLeHaoThucTe;

    private BigDecimal slHaoVuotMuc;

    private String tiLeHaoVuotMuc;

    private BigDecimal slHaoThanhLy;

    private String tiLeHaoThanhLy;

    private BigDecimal slHaoDuoiDinhMuc;

    private String tiLeHaoDuoiDinhMuc;

    private String nguyenNhan;

    private String kienNghi;

    private String ghiChu;

    private Long idThuKho;
    @Transient
    private String tenThuKho;

    private Long idKtv;
    @Transient
    private String tenKtv;

    private Long idKeToan;
    @Transient
    private String tenKeToan;

    private String trangThai;
    @Transient
    private String tenTrangThai;

    private LocalDate ngayTao;

    private Long nguoiTaoId;

    private LocalDate ngaySua;

    private Long nguoiSuaId;

    private LocalDate ngayGuiDuyet;

    private Long nguoiGuiDuyetId;

    private LocalDate ngayPduyet;

    private Long nguoiPduyetId;
    @Transient
    private String tenNguoiPduyet;

    private String lyDoTuChoi;

    @Transient
    private List<XhBbHdoiBttDtl> children = new ArrayList<>();

    @Transient
    private List<FileDinhKem> fileDinhKems = new ArrayList<>();
}
