package com.tcdt.qlnvhang.entities.xuathang.bantructiep.xuatkho.bienbantinhkho;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.entities.TrangThaiBaseEntity;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.util.Contains;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = XhBbTinhkBttHdr.TABLE_NAME)
@Data
public class XhBbTinhkBttHdr extends TrangThaiBaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_BB_TINHK_BTT_HDR";

    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator =TABLE_NAME +"_SEQ")
//    @SequenceGenerator(sequenceName = TABLE_NAME +"_SEQ", allocationSize = 1, name = TABLE_NAME +"_SEQ")

    @Column(name = "ID")
    private Long id;

    private Integer namKh;

    private String maDvi;
    @Transient
    private String tenDvi;

    private String maQhns;

    private String soBbTinhKho;

    private Long idQd;

    private String soQd;

    private Long idHd;

    private String soHd;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayKyHd;

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

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayBdauXuat;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayKthucXuat;

    private BigDecimal tongSlNhap;

    private BigDecimal tongSlXuat;

    private BigDecimal slConLai;

    private BigDecimal slThucTe;

    private BigDecimal slChenhLech;

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

    @Transient
    private List<XhBbTinhkBttDtl> children = new ArrayList<>();

    @Transient
    private FileDinhKem fileDinhKem;
}
