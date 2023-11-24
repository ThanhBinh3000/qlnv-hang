package com.tcdt.qlnvhang.table.nhaphangtheoptt;

import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.HhQdPheduyetKhMttHdr;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.hopdong.hopdongphuluc.HopDongMttHdr;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "HH_QD_GIAO_NV_NHAP_HANG")
@Data
public class HhQdGiaoNvNhapHang implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "HH_QD_GIAO_NV_NHAP_HANG";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HH_QD_GIAO_NV_NHAP_HANG_SEQ")
    @SequenceGenerator(sequenceName = "HH_QD_GIAO_NV_NHAP_HANG_SEQ", allocationSize = 1, name = "HH_QD_GIAO_NV_NHAP_HANG_SEQ")
    private Long id;
    private Integer namNhap;
    private String soQd;
    @Temporal(TemporalType.DATE)
    private Date ngayQd;
    @Temporal(TemporalType.DATE)
    private Date ngayKyHd;
    private String maDvi;
    private String tenDvi;
    private String loaiQd;
    private Long idHd;
    private String soHd;
    private String tenHd;
    private Long idQdPdKh;
    private String soQdPdKh;
    private Long idQdPdKq;
    private String soQdPdKq;
    private String loaiVthh;
    @Transient
    private String tenLoaiVthh;
    @Transient
    private Integer slHd;
    @Transient
    private Integer slHdDaKy;
    private String cloaiVthh;
    @Transient
    private String tenCloaiVthh;
    private String moTaHangHoa;
    private String donViTinh;
    @Temporal(TemporalType.DATE)
    private Date tgianNkho;
    private String trichYeu;
    private String trangThai;
    private String trangThaiHd;
    @Transient
    private String tenTrangThai;
    @Transient
    private String tenTrangThaiHd;
    private BigDecimal soLuong;
    @Temporal(TemporalType.DATE)
    private Date ngayTao;
    private String nguoiTao;
    @Temporal(TemporalType.DATE)
    private Date ngaySua;
    private  String nguoiSua;
    private String ldoTuchoi;
    @Temporal(TemporalType.DATE)
    private Date ngayGduyet;
    private String nguoiGduyet;
    @Temporal(TemporalType.DATE)
    private Date ngayPduyet;
    private String nguoiPduyet;

    @Transient
    private List<HopDongMttHdr> hopDongMttHdrs = new ArrayList<>();

    @Transient
    private HhQdPheduyetKhMttHdr hhQdPheduyetKhMttHdr;

    @Transient
    private List<HhQdGiaoNvNhangDtl> hhQdGiaoNvNhangDtlList= new ArrayList<>();

    @Transient
    private List<HhPhieuNhapKhoHdr> hhPhieuNhapKhoHdrList = new ArrayList<>();

    @Transient
    private List<HhBcanKeHangHdr> hhBcanKeHangHdrList = new ArrayList<>();

    @Transient
    private List<HhBienBanDayKhoHdr> hhBienBanDayKhoHdrList = new ArrayList<>();

    @Transient
    private List<FileDinhKem> fileDinhKems = new ArrayList<>();

    @Transient
    private FileDinhKem fileDinhKem;


//    preview
    @Transient
    private Date ngayMkho;
    @Transient
    private String ngayMkhoStr;
    @Transient
    private String tgianNkhoStr;
    @Transient
    private String tongThanhTien;
    @Transient
    private Integer ngay;
    @Transient
    private Integer thang;
    @Transient
    private Integer nam;
    @Transient
    private List<HhQdGiaoNvNhDdiem> children = new ArrayList<>();
    @Transient
    private List<FileDinhKem> listCanCuPhapLy = new ArrayList<>();
}
