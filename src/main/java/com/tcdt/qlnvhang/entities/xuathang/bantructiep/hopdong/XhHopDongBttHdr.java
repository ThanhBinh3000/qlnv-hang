package com.tcdt.qlnvhang.entities.xuathang.bantructiep.hopdong;
import com.tcdt.qlnvhang.table.FileDinhKem;
import lombok.Data;
import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = XhHopDongBttHdr.TABLE_NAME)
@Data
public class XhHopDongBttHdr implements Serializable {

    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_HOP_DONG_BTT_HDR";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = TABLE_NAME +"_SEQ")
    @SequenceGenerator(sequenceName =  TABLE_NAME+ "_SEQ", allocationSize = 1, name = TABLE_NAME+ "_SEQ")

    private Long id;

    private Integer namHd;

    private Long idQdKq;

    private String soQdKq;

    private LocalDate ngayKyQd;

    private LocalDate ngayMkho;

    private LocalDate ngayKyQdPd;

    private String soQdPd;

    private String maDviTsan;

    private String loaiHinhNx;

    private String kieuNx;

    private String soHd;

    private String tenHd;

    private LocalDate ngayHluc;

    private String ghiChuNgayHluc;

    private String loaiHdong;
    @Transient
    private String tenLoaiHdong;

    private String ghiChuLoaiHdong;

    private Integer tgianThienHd;

    private LocalDate tgianGnhanTu;

    private LocalDate tgianGnhanDen;

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

    private String lyDoTuChoi;

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

    private LocalDate ngayHlucPhuLuc;

    private String noiDungPhuLuc;

    private LocalDate ngayHlucSauDcTu;

    private LocalDate ngayHlucSauDcDen;

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
