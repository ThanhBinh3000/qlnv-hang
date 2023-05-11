package com.tcdt.qlnvhang.entities.xuathang.bantructiep.hopdong;

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
@Table(name = XhHopDongBttHdr.TABLE_NAME)
@Data
public class XhHopDongBttHdr extends TrangThaiBaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_HOP_DONG_BTT_HDR";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = TABLE_NAME +"_SEQ")
    @SequenceGenerator(sequenceName =  TABLE_NAME+ "_SEQ", allocationSize = 1, name = TABLE_NAME+ "_SEQ")
    private Long id;

    private Integer namHd;

    private Long idQdKq;

    private String soQdKq;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    @Column(columnDefinition = "Date")
    private Date ngayKyQd;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    @Column(columnDefinition = "Date")
    private Date ngayMkho;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    @Column(columnDefinition = "Date")
    private Date ngayKyQdPd;

    private String soQdPd;

    private String maDviTsan;

    private String loaiHinhNx;

    private String kieuNx;

    private String soHd;

    private String tenHd;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    @Column(columnDefinition = "Date")
    private Date ngayHluc;

    private String ghiChuNgayHluc;

    private String loaiHdong;
    @Transient
    private String tenLoaiHdong;

    private String ghiChuLoaiHdong;

    private Integer tgianThienHd;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    @Column(columnDefinition = "Date")
    private Date tgianGnhanTu;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    @Column(columnDefinition = "Date")
    private Date tgianGnhanDen;

    private String ghiChuTgianGnhan;

    private String noiDungHdong;

    private String dkienHanTtoan;

    private String maDvi;
    @Transient
    private String tenDvi;

    private String diaChiDvi;

    private String mst;

    private String tenNguoiDdien;

    private String chucVu;

    private String sdt;

    private String fax;

    private String stk;

    private String moTai;

    private String ttinGiayUyQuyen;

    private Long idDviMua;

    private String tenDviMua;

    private String diaChiDviMua;

    private String mstDviMua;

    private String tenNguoiDdienDviMua;

    private String chucVuDviMua;

    private String sdtDviMua;

    private String faxDviMua;

    private String stkDviMua;

    private String moTaiDviMua;

    private String loaiVthh;
    @Transient
    private String tenLoaiVthh;

    private String cloaiVthh;
    @Transient
    private String tenCloaiVthh;

    private String moTaHangHoa;

    private String donViTinh;

    private BigDecimal soLuongBanTrucTiep;

    private BigDecimal thanhTien;

    private String ghiChu;

    private BigDecimal tongSlXuatBanQdKh;

    private BigDecimal tongSlBanttQdkhDakyHd;

    private BigDecimal tongSlBanttQdkhChuakyHd;

    private String trangThaiXh;
    @Transient
    private String tenTrangThaiXh;

    @Transient
    private List<String> listMaDviTsan = new ArrayList<>();

    @Transient
    private List<FileDinhKem> canCuPhapLy = new ArrayList<>();

    @Transient
    private List<FileDinhKem> fileDinhKems = new ArrayList<>();

    @Transient
    private List<FileDinhKem> filePhuLuc = new ArrayList<>();

    @Transient
    private List<XhHopDongBttDtl> children = new ArrayList<>();


    //    Phụ lục
    private Long idHd;

    private String soPhuLuc;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    @Column(columnDefinition = "Date")
    private Date ngayHlucPhuLuc;

    private String noiDungPhuLuc;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    @Column(columnDefinition = "Date")
    private Date ngayHlucSauDcTu;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    @Column(columnDefinition = "Date")
    private Date ngayHlucSauDcDen;

    private Integer tgianThienHdSauDc;

    private String noiDungDcKhac;

    private String ghiChuPhuLuc;

    private String trangThaiPhuLuc;
    @Transient
    private String tenTrangThaiPhuLuc;

    @Transient
    private List<XhHopDongBttHdr> phuLuc = new ArrayList<>();

    @Transient
    private List<XhHopDongBttDtl> phuLucDtl = new ArrayList<>();

    private BigDecimal donGiaBanTrucTiep;

    private String soQdNv;

    private Long idQdNv;

//    cấp chi cục
   @Transient
   private List<XhHopDongBttDvi> xhHopDongBttDviList = new ArrayList<>();
}
