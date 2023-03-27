package com.tcdt.qlnvhang.entities.xuathang.bantructiep.nhiemvuxuat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.entities.TrangThaiBaseEntity;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.ktracluong.bienbanlaymau.XhBbLayMauBttHdr;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.ktracluong.phieuktracluong.XhPhieuKtraCluongBttHdr;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.xuatkho.bangkecanhang.XhBkeCanHangBttHdr;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.xuatkho.bienbanhaodoi.XhBbHdoiBttHdr;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.xuatkho.bienbantinhkho.XhBbTinhkBttHdr;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.xuatkho.phieuxuatkho.XhPhieuXkhoBtt;
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
@Table(name = "XH_QD_NV_XH_BTT_HDR")
@Data
public class XhQdNvXhBttHdr extends TrangThaiBaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_QD_NV_XH_BTT_HDR";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "XH_QD_NV_XH_BTT_HDR_SEQ")
    @SequenceGenerator(sequenceName = "XH_QD_NV_XH_BTT_HDR_SEQ", allocationSize = 1, name = "XH_QD_NV_XH_BTT_HDR_SEQ")
    private Long id;

    private Integer namKh;

    private String soQd;

    private String maDvi;
    @Transient
    private String tenDvi;

    private Long idHd;

    private String soHd;

    @Temporal(TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayHd;

    private Long idQdPdKh;

    private String soQdPd;

    private String maDviTsan;

    private String tenTtcn;

    private String loaiVthh;
    @Transient
    private String tenLoaiVthh;

    private String cloaiVthh;
    @Transient
    private String tenCloaiVthh;

    private String moTaHangHoa;

    private BigDecimal soLuong;

    private BigDecimal donGia;

    private String donViTinh;

    @Temporal(TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date tgianGnhan;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    @Column(columnDefinition = "Date")
    private Date tgianGnhanTu;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    @Column(columnDefinition = "Date")
    private Date tgianGnhanDen;

    private String trichYeu;

    private String trangThaiXh;
    @Transient
    private String tenTrangThaiXh;

    private String phanLoai;

    private Long idQdKqCg;

    @Transient
    private List<XhQdNvXhBttDtl> children = new ArrayList<>();

    @Transient
    private List<FileDinhKem> fileDinhKems = new ArrayList<>();

    @Transient
    private FileDinhKem fileDinhKem;

    @Transient
    private List<String> listMaDviTsan = new ArrayList<>();

    @Transient
    private List<XhBbLayMauBttHdr> xhBbLayMauBttHdrList = new ArrayList<>();

    @Transient
    private List<XhPhieuKtraCluongBttHdr> xhPhieuKtraCluongBttHdrList = new ArrayList<>();

    @Transient
    private List<XhPhieuXkhoBtt> xhPhieuXkhoBttList = new ArrayList<>();

    @Transient
    private List<XhBkeCanHangBttHdr> xhBkeCanHangBttHdrList = new ArrayList<>();

    @Transient
    private List<XhBbTinhkBttHdr> xhBbTinhkBttHdrList = new ArrayList<>();

    @Transient
    private List<XhBbHdoiBttHdr> xhBbHdoiBttHdrList = new ArrayList<>();

}
